package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.CalendarDebt;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtCalendarComponent;
import com.beihui.market.injection.module.DebtCalendarModule;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.ui.adapter.DebtCalendarRVAdapter;
import com.beihui.market.ui.contract.DebtCalendarContract;
import com.beihui.market.ui.presenter.DebtCalendarPresenter;
import com.beihui.market.ui.rvdecoration.CalendarDebtItemDeco;
import com.beihui.market.ui.rvdecoration.CalendarDebtStickyHeaderItemDeco;
import com.beihui.market.view.calendar.CalendarView;
import com.beihui.market.view.calendar.dateview.DateView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

/**
 * 账单模块 还款日历 日历Fragment
 */
public class DebtCalCalendarFragment extends BaseComponentFragment implements DebtCalendarContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    private final SimpleDateFormat fDateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    @BindView(R.id.calendar)
    CalendarView calendarView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_record)
    View noRecord;

    @BindView(R.id.debt_amount)
    TextView debtAmount;
    @BindView(R.id.paid_amount)
    TextView paidAmount;
    @BindView(R.id.unpaid_amount)
    TextView unpaidAmount;

    @Inject
    DebtCalendarPresenter presenter;

    public interface OnCalendarChangedListener {
        void onMonthChanged(Date date);

        void onDateSelected(Date date);
    }

    private OnCalendarChangedListener listener;
    private DebtCalendarRVAdapter adapter;

    //是否已经选中一个日期
    private boolean hasDateBeenSelected;
    //当前数据来源的日期，如果hasDateBeenSelected==true,则对应日，否则对应月
    private Date calendarDate;


    public static DebtCalCalendarFragment newInstance(OnCalendarChangedListener listener, Date lastSelectedDate) {
        DebtCalCalendarFragment fragment = new DebtCalCalendarFragment();
        fragment.listener = listener;
        fragment.calendarDate = lastSelectedDate;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果当前已经选中过一个日期，则唤起刷新
        if (calendarDate != null) {
            //刷新账单月份摘要
            presenter.fetchCalendarAbstract(calendarDate);
            //刷新列表
            presenter.fetchDebtList(calendarDate, !hasDateBeenSelected);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_cal_calendar;
    }

    @Override
    public void configViews() {
        calendarView.setOnCalendarChangedListener(new CalendarView.OnCalendarChangedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (listener != null) {
                    listener.onDateSelected(date);
                }
                hasDateBeenSelected = true;
                calendarDate = date;
                presenter.fetchDebtList(date, false);
            }

            @Override
            public void onMonthChange(Date date) {
                if (listener != null) {
                    listener.onMonthChanged(date);
                }
                //月份变化，加载月份摘要
                presenter.fetchCalendarAbstract(date);
                if (!hasDateBeenSelected) {
                    //如果已经选中过日期，则不在加载整月数据
                    presenter.fetchDebtList(date, true);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DebtCalendarRVAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickDebt(position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CalendarDebtItemDeco() {
            @Override
            public String getHeader(int position) {
                return generateDateString(adapter.getItem(position).getRepayDate());
            }
        });
        recyclerView.addItemDecoration(new CalendarDebtStickyHeaderItemDeco(getContext()) {
            @Override
            public String getHeaderName(int pos) {
                return generateDateString(adapter.getItem(pos).getRepayDate());
            }
        });
    }

    @Override
    public void initDatas() {
        if (calendarDate != null) {
            calendarView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calendarView.attachSelectedDate(calendarDate);
                }
            }, 50);
        }


    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtCalendarComponent.builder()
                .appComponent(appComponent)
                .debtCalendarModule(new DebtCalendarModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DebtCalendarContract.Presenter presenter) {
        //
    }

    @Override
    public void showCalendarAbstract(Map<String, Integer> calendarAbstract) {
        List<DateView.Tag> tags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : calendarAbstract.entrySet()) {
            tags.add(new DateView.Tag(entry.getKey(), entry.getValue()));
        }
        calendarView.setTags(tags);
    }

    @Override
    public void showCalendarTrend(List<String> dateList, Map<String, Float> calendarTrend) {

    }

    @Override
    public void showCalendarDebtSumInfo(double debtAmount, double paidAmount, double unpaidAmount) {
        this.debtAmount.setText(keep2digitsWithoutZero(debtAmount));
        this.paidAmount.setText(keep2digitsWithoutZero(paidAmount));
        this.unpaidAmount.setText(keep2digitsWithoutZero(unpaidAmount));
    }

    @Override
    public void showCalendarDebtList(List<CalendarDebt.DetailBean> list) {
        if (isAdded()) {
            adapter.notifyDebtChanged(list);

            if (list != null && list.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                noRecord.setVisibility(View.INVISIBLE);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                noRecord.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void navigateLoanDebtDetail(String id) {
        Intent intent = new Intent(getContext(), LoanDebtDetailActivity.class);
        intent.putExtra("debt_id", id);
        startActivityForResult(intent, 1);
    }

    @Override
    public void navigateCreditCardDebtDetail(String id, String logo, String bankName, String cardNum, boolean byHand) {
        Intent intent = new Intent(getContext(), CreditCardDebtDetailActivity.class);
        intent.putExtra("debt_id", id);
        intent.putExtra("logo", logo);
        intent.putExtra("bank_name", bankName);
        intent.putExtra("card_num", cardNum);
        intent.putExtra("by_hand", byHand);//是否是手动记账
        startActivityForResult(intent, 1);
    }


    private String generateDateString(String date) {
        try {
            return fDateFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
