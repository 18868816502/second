package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.DebtCalCalendarFragment;
import com.beihui.market.ui.fragment.DebtCalChartFragment;
import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

public class DebtCalendarActivity extends BaseComponentActivity {

    private final SimpleDateFormat titleFormat = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.date)
    TextView dateStrView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.show_switch)
    ImageView showSwitch;

    private Date selectedDate;

    private DebtCalCalendarFragment.OnCalendarChangedListener dateSelectedListener = new DebtCalCalendarFragment.OnCalendarChangedListener() {
        @Override
        public void onMonthChanged(Date date) {
            dateStrView.setText(titleFormat.format(date));
        }

        @Override
        public void onDateSelected(Date date) {
            selectedDate = date;
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

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    private void switchContent(boolean isCalendar) {
        dateStrView.setVisibility(isCalendar ? View.VISIBLE : View.GONE);
        title.setVisibility(isCalendar ? View.GONE : View.VISIBLE);

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
