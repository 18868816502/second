package com.beiwo.klyjaz.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.CalendarDebt;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerDebtCalendarComponent;
import com.beiwo.klyjaz.injection.module.DebtCalendarModule;
import com.beiwo.klyjaz.ui.activity.CreditCardDebtDetailActivity;
import com.beiwo.klyjaz.ui.activity.LoanDebtDetailActivity;
import com.beiwo.klyjaz.ui.adapter.DebtCalendarRVAdapter;
import com.beiwo.klyjaz.ui.contract.DebtCalendarContract;
import com.beiwo.klyjaz.ui.presenter.DebtCalendarPresenter;
import com.beiwo.klyjaz.ui.rvdecoration.CalendarDebtItemDeco;
import com.beiwo.klyjaz.ui.rvdecoration.CalendarDebtStickyHeaderItemDeco;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.beiwo.klyjaz.view.chart.DebtDataRender;
import com.beiwo.klyjaz.view.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.beiwo.klyjaz.util.CommonUtils.keep2digits;

/**
 * 账单模块 还款日历 图形化Fragment
 */
public class DebtCalChartFragment extends BaseComponentFragment implements DebtCalendarContract.View {

    private final SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    private final SimpleDateFormat fDateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    private DecimalFormat decimalFormat = new DecimalFormat();

    @BindView(R.id.start_day_container)
    View startDayContainer;
    @BindView(R.id.start_day)
    TextView startDay;
    @BindView(R.id.end_day_container)
    View endDayContainer;
    @BindView(R.id.end_day)
    TextView endDay;
    @BindView(R.id.line_chart)
    LineChart lineChart;
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

    private DebtCalendarRVAdapter adapter;

    private List<String> xLabels = new ArrayList<>();

    private int lastSelectedMonth = -1;

    public static DebtCalChartFragment newInstance() {
        return new DebtCalChartFragment();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新当月账单列表
        if (lastSelectedMonth != -1) {
            presenter.clickMonth(lastSelectedMonth);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_cal_chart;
    }

    @Override
    public void configViews() {
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMaximumFractionDigits(2);

        lineChart.setNoDataText("");
        lineChart.getDescription().setText("");
        lineChart.getLegend().setEnabled(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            private Highlight lastH;

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                presenter.clickMonth((int) e.getX());
                lastH = h;
            }

            @Override
            public void onNothingSelected() {
                if (lastH != null) {
                    lineChart.highlightValue(lastH);
                }
            }
        });
        lineChart.setRenderer(new DebtDataRender(getContext(), lineChart, lineChart.getAnimator(), lineChart.getViewPortHandler()));

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                index = index >= 0 ? index : 0;
                if (index < xLabels.size()) {
                    return xLabels.get(index);
                }
                return "";
            }
        });
        xAxis.setTextColor(getResources().getColor(R.color.black_2));

        YAxis leftAXis = lineChart.getAxisLeft();
        leftAXis.setDrawAxisLine(false);
        leftAXis.setDrawGridLines(false);
        leftAXis.setDrawLabels(false);


        YAxis rightAXis = lineChart.getAxisRight();
        rightAXis.setDrawAxisLine(false);
        rightAXis.setDrawGridLines(false);
        rightAXis.setDrawLabels(false);


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.start_day_container) {
                    showTimerPicker(true, (Date) startDay.getTag());
                } else {
                    showTimerPicker(false, (Date) endDay.getTag());
                }
            }
        };
        startDayContainer.setOnClickListener(clickListener);
        endDayContainer.setOnClickListener(clickListener);

        adapter = new DebtCalendarRVAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickDebt(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date now = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.MONTH, -1);
        Date yearLater = calendar.getTime();

        selectedDateRange(now, yearLater);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtCalendarComponent.builder()
                .appComponent(appComponent)
                .debtCalendarModule(new DebtCalendarModule(this))
                .build()
                .inject(this);
    }

    private void selectedDateRange(Date startDay, Date endDay) {
        this.startDay.setTag(startDay);
        this.endDay.setTag(endDay);
        this.startDay.setText(yearDateFormat.format(startDay));
        this.endDay.setText(yearDateFormat.format(endDay));

        presenter.fetchCalendarTrend(startDay, endDay);
    }

    @Override
    public void setPresenter(DebtCalendarContract.Presenter presenter) {
        //
    }

    @Override
    public void showCalendarAbstract(Map<String, Integer> calendarAbstract) {
        //
    }

    @Override
    public void showCalendarTrend(List<String> dateList, Map<String, Float> calendarTrend) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        List<Entry> xys = new ArrayList<>();
        xLabels.clear();
        for (int i = 0; i < dateList.size(); i++) {
            String cur = dateList.get(i);
            try {
                calendar.setTime(dateFormat.parse(cur));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            xLabels.add((calendar.get(Calendar.MONTH) + 1) + "月");
            if (calendarTrend.containsKey(cur)) {
                xys.add(new Entry(i, calendarTrend.get(cur)));
            } else {
                xys.add(new Entry(i, 0));
            }
        }
        LineDataSet dataSet = new LineDataSet(xys, "debtSet");
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        dataSet.disableDashedLine();
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(colorPrimary);
        dataSet.setCircleColor(colorPrimary);
        dataSet.setHighLightColor(colorPrimary);
        dataSet.setValueTextColor(colorPrimary);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setValueTextSize(getResources().getDisplayMetrics().density * 9);
        LineData data = new LineData(dataSet);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return decimalFormat.format(value);
            }
        });
        lineChart.setData(data);
        lineChart.getXAxis().setLabelCount(xys.size(), true);
        //如果当前只有一个数据，则设置横坐标标签居中，否则无法对应纵坐标位置
        //暂时性处理，之后修改成自定义Render
        if (dateList.size() == 1) {
            lineChart.getXAxis().setCenterAxisLabels(true);
        } else {
            lineChart.getXAxis().setCenterAxisLabels(false);
        }
        lineChart.invalidate();

        if (dateList.size() > 0) {
            lineChart.highlightValue(lineChart.getHighlightByTouchPoint(0, 0));
        }
    }

    @Override
    public void showCalendarDebtSumInfo(double debtAmount, double paidAmount, double unpaidAmount) {
        this.debtAmount.setText(keep2digits(debtAmount));
        this.paidAmount.setText(keep2digits(paidAmount));
        this.unpaidAmount.setText(keep2digits(unpaidAmount));
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
        if (FastClickUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), LoanDebtDetailActivity.class);
        intent.putExtra("debt_id", id);
        startActivityForResult(intent, 1);
    }

    @Override
    public void navigateCreditCardDebtDetail(String id, String billId, String logo, String bankName, String cardNum, boolean byHand) {
        if (FastClickUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), CreditCardDebtDetailActivity.class);
        intent.putExtra("debt_id", id);
        intent.putExtra("bill_id", billId);
        intent.putExtra("logo", logo);
        intent.putExtra("bank_name", bankName);
        intent.putExtra("card_num", cardNum);
        intent.putExtra("by_hand", byHand);//是否是手动记账
        startActivityForResult(intent, 1);
    }

    private void showTimerPicker(final boolean start, Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                if (start) {
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, 1);
                    calendar.add(Calendar.MONTH, -1);
                    selectedDateRange(date, calendar.getTime());
                } else {
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, 1);
                    selectedDateRange(calendar.getTime(), date);
                }
            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .setCancelText("取消")
                .setCancelColor(Color.parseColor("#5591ff"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#5591ff"))
                .setTitleText("选择日期")
                .setTitleColor(getResources().getColor(R.color.black_1))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setLabel("年", "月", null, null, null, null)
                .isCenterLabel(false)
                .setDate(calendar)
                .build();
        pickerView.show();
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
