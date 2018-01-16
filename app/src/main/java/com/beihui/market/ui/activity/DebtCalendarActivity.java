package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.DebtCalCalendarFragment;
import com.beihui.market.ui.fragment.DebtCalChartFragment;
import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;

public class DebtCalendarActivity extends BaseComponentActivity {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private final Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.show_switch)
    ImageView showSwitch;

    private String selectedDate;

    private DebtCalCalendarFragment.DateSelectedListener dateSelectedListener = new DebtCalCalendarFragment.DateSelectedListener() {
        @Override
        public void onDateSelected(int year, int month, int day) {
            calendar.set(year, month, day);
            selectedDate = dateFormat.format(calendar.getTime());
            date.setText(year + "年" + (month + 1) + "月");
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_calendar;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        showSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwitch.setSelected(!showSwitch.isSelected());
                switchContent(!showSwitch.isSelected());
            }
        });
        switchContent(true);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    private void switchContent(boolean isCalendar) {
        date.setVisibility(isCalendar ? View.VISIBLE : View.GONE);
        title.setVisibility(isCalendar ? View.GONE : View.VISIBLE);

//        String calendarTag = DebtCalCalendarFragment.class.getSimpleName();
//        String chartTag = DebtCalChartFragment.class.getSimpleName();
//        Fragment calendar = getSupportFragmentManager().findFragmentByTag(calendarTag);
//        Fragment chart = getSupportFragmentManager().findFragmentByTag(chartTag);

//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        if (isCalendar) {
//            if (chart != null) {
//                transaction.detach(chart);
//            }
//            if (calendar == null) {
//                calendar = DebtCalCalendarFragment.newInstance(dateSelectedListener, selectedDate);
//                transaction.add(R.id.content_container, calendar, calendarTag);
//            } else {
//                transaction.attach(calendar);
//            }
//        } else {
//            if (calendar != null) {
//                transaction.detach(calendar);
//            }
//            if (chart == null) {
//                chart = DebtCalChartFragment.newInstance();
//                transaction.add(R.id.content_container, chart, chartTag);
//            } else {
//                transaction.attach(chart);
//            }
//        }
//        transaction.commit();

        if (isCalendar) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_container, DebtCalCalendarFragment.newInstance(dateSelectedListener, selectedDate))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_container, new DebtCalChartFragment())
                    .commit();
        }
    }
}
