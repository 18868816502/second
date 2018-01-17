package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.DebtCalendar;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtCalendarComponent;
import com.beihui.market.injection.module.DebtCalendarModule;
import com.beihui.market.ui.activity.DebtDetailActivity;
import com.beihui.market.ui.adapter.DebtCalendarRVAdapter;
import com.beihui.market.ui.contract.DebtCalendarContract;
import com.beihui.market.ui.presenter.DebtCalendarPresenter;
import com.beihui.market.ui.rvdecoration.CalendarDebtItemDeco;
import com.beihui.market.ui.rvdecoration.CalendarDebtStickyHeaderItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.beihui.market.util.CommonUtils.keep2digits;

public class DebtCalCalendarFragment extends BaseComponentFragment implements DebtCalendarContract.View {

    @BindView(R.id.calendar)
    NCalendar nCalendar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.debt_amount)
    TextView debtAmount;
    @BindView(R.id.paid_amount)
    TextView paidAmount;
    @BindView(R.id.unpaid_amount)
    TextView unpaidAmount;

    @Inject
    DebtCalendarPresenter presenter;

    public interface DateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }

    private DateSelectedListener listener;
    private DebtCalendarRVAdapter adapter;

    private String selectedDate;


    public static DebtCalCalendarFragment newInstance(DateSelectedListener listener, String selectedDate) {
        DebtCalCalendarFragment fragment = new DebtCalCalendarFragment();
        fragment.listener = listener;
        fragment.selectedDate = selectedDate;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_cal_calendar;
    }

    @Override
    public void configViews() {
        nCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                int year = dateTime.year().get();
                int month = dateTime.monthOfYear().get() - 1;
                int day = dateTime.dayOfMonth().get();
                if (listener != null) {
                    listener.onDateSelected(year, month, day);
                }
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.set(year, month, day);
                presenter.loadCalendarDebt(calendar.getTime());
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
                return adapter.getItem(position).getTermRepayDate();
            }
        });
        recyclerView.addItemDecoration(new CalendarDebtStickyHeaderItemDeco(getContext()) {
            @Override
            public String getHeaderName(int pos) {
                return adapter.getItem(pos).getTermRepayDate();
            }
        });
    }

    @Override
    public void initDatas() {
        if (selectedDate != null) {
            nCalendar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nCalendar.setDate(selectedDate);
                    nCalendar.invalidate();
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
    public void showCalendarDebt(List<DebtCalendar.DetailBean> list) {
        if (isAdded()) {
            adapter.notifyDebtChanged(list);
        }
    }

    @Override
    public void showDebtAbstractInfo(double debtAmount, double paidAmount, double unpaidAmount) {
        this.debtAmount.setText(keep2digits(debtAmount));
        this.paidAmount.setText(keep2digits(paidAmount));
        this.unpaidAmount.setText(keep2digits(unpaidAmount));
    }

    @Override
    public void showMonthsDebtAmount(List<String> date, Map<String, Float> debts) {
        //no implements here
    }

    @Override
    public void navigateDebtDetail(String id) {
        Intent intent = new Intent(getContext(), DebtDetailActivity.class);
        intent.putExtra("debt_id", id);
        startActivityForResult(intent, 1);
    }

    @Override
    public void showCalendarDebtTag(Map<String, Integer> debtHint) {
        List<String> dates = new ArrayList<>();
        List<Integer> tags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : debtHint.entrySet()) {
            dates.add(entry.getKey());
            tags.add(entry.getValue());
        }
        nCalendar.setPoint(dates, tags);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.refreshCurDate();
    }
}
