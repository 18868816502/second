package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
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
import com.beihui.market.view.chart.DebtDataRender;
import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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

import static com.beihui.market.util.CommonUtils.keep2digits;

public class DebtCalChartFragment extends BaseComponentFragment implements DebtCalendarContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);

    @BindView(R.id.start_day)
    TextView startDay;
    @BindView(R.id.end_day)
    TextView endDay;
    @BindView(R.id.line_chart)
    LineChart lineChart;
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

    private DebtCalendarRVAdapter adapter;

    private List<String> xLabels = new ArrayList<>();

    public static DebtCalChartFragment newInstance() {
        return new DebtCalChartFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_cal_chart;
    }

    @Override
    public void configViews() {
        lineChart.setNoDataText("");
        lineChart.getDescription().setText("");
        lineChart.getLegend().setEnabled(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setHighlightPerTapEnabled(true);
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
                if (v.getId() == R.id.start_day) {
                    showTimerPicker(true);
                } else {
                    showTimerPicker(false);
                }
            }
        };
        startDay.setOnClickListener(clickListener);
        endDay.setOnClickListener(clickListener);

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
        this.startDay.setText(dateFormat.format(startDay));
        this.endDay.setText(dateFormat.format(endDay));

        presenter.loadMonthDebt(startDay, endDay);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        List<Entry> xys = new ArrayList<>();
        xLabels.clear();
        for (int i = 0; i < date.size(); i++) {
            String cur = date.get(i);
            try {
                calendar.setTime(dateFormat.parse(cur));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            xLabels.add((calendar.get(Calendar.MONTH) + 1) + "月");
            if (debts.containsKey(cur)) {
                xys.add(new Entry(i, debts.get(cur)));
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
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.getXAxis().setLabelCount(xys.size(), true);
        //如果当前只有一个数据，则设置横坐标标签居中，否则无法对应纵坐标位置
        //暂时性处理，之后修改成自定义Render
        if (date.size() == 1) {
            lineChart.getXAxis().setCenterAxisLabels(true);
        } else {
            lineChart.getXAxis().setCenterAxisLabels(false);
        }
        lineChart.invalidate();

        if (date.size() > 0) {
            lineChart.highlightValue(lineChart.getHighlightByTouchPoint(0, 0));
        }
    }

    @Override
    public void navigateDebtDetail(String id) {
        Intent intent = new Intent(getContext(), DebtDetailActivity.class);
        intent.putExtra("debt_id", id);
        startActivityForResult(intent, 1);
    }

    @Override
    public void showCalendarDebtTag(Map<String, Integer> debtHint) {
        //no implements here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.refreshCurMonth();
    }

    private void showTimerPicker(final boolean start) {
        Calendar startDate = Calendar.getInstance(Locale.CHINA);
        Calendar endDate = Calendar.getInstance(Locale.CHINA);
        if (start) {
            Date anchor = (Date) endDay.getTag();
            endDate.setTime(anchor);
            startDate.setTime(anchor);
            startDate.add(Calendar.YEAR, -1);
            startDate.add(Calendar.MONTH, 1);
        } else {
            Date anchor = (Date) startDay.getTag();
            startDate.setTime(anchor);
            endDate.setTime(anchor);
            endDate.add(Calendar.YEAR, 1);
            endDate.add(Calendar.MONTH, -1);
        }
        TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (start) {
                    selectedDateRange(date, (Date) endDay.getTag());
                } else {
                    selectedDateRange((Date) startDay.getTag(), date);
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
                .setRangDate(startDate, endDate)
                .build();
        pickerView.show();
    }
}