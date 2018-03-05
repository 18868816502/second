package com.beihui.market.view.calendar.datepager;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.beihui.market.view.calendar.CalendarView;
import com.beihui.market.view.calendar.dateadapter.MonthAdapter;
import com.beihui.market.view.calendar.dateview.DateView;
import com.beihui.market.view.calendar.dateview.MonthView;
import com.beihui.market.view.calendar.utils.DateListHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthPager extends ViewPager {

    private MonthAdapter adapter;
    private CalendarView calendarView;

    private boolean notifyFakeSelected = true;
    private boolean notifySelected = true;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private Date selectedDate;

    private int lastPosition;

    public MonthPager(Context context) {
        this(context, null);
    }

    public MonthPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        adapter = new MonthAdapter(DateListHelper.getDateMonthRange(), this, new MonthView.MonthlyDateSelectedListener() {
            @Override
            public void onMonthlyDateSelected(Date date, boolean isLast, boolean isNext) {
                selectedDate = date;
                notifySelected = false;
                if (isLast) {
                    setCurrentItem(getCurrentItem() - 1, true);
                    adapter.getCurrentMonthView().selectDate(date);
                } else if (isNext) {
                    setCurrentItem(getCurrentItem() + 1, true);
                    adapter.getCurrentMonthView().selectDate(date);
                }
                notifySelected = true;
                adapter.clearNoPrimaryItemSelected();
                calendarView.notifyDateSelected(date, true);
            }
        });
        setAdapter(adapter);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Date date = DateListHelper.calculateMonth(position);
                calendarView.notifyMonthChanged(date);

                if (selectedDate != null) {
                    if (notifySelected) {
                        calendar.setTime(selectedDate);
                        calendar.add(Calendar.MONTH, position - lastPosition);
                        selectedDate = calendar.getTime();
                        adapter.getCurrentMonthView().selectDate(selectedDate);
                        adapter.clearNoPrimaryItemSelected();
                        calendarView.notifyDateSelected(selectedDate, true);
                    }
                } else if (notifyFakeSelected) {
                    calendarView.notifyFakeDateSelected(date, true);
                }

                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public MonthView getCurrentMonthView() {
        return adapter.getCurrentMonthView();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setCalendarView(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public void notifyDateSelected(Date date) {
        selectedDate = date;

        notifySelected = false;
        int months = DateListHelper.monthsFromStartDate(date);
        if (months != getCurrentItem()) {
            setCurrentItem(months);
        }

        adapter.getCurrentMonthView().selectDate(selectedDate);
        notifySelected = true;
    }

    public void notifyFakeDateSelected(Date date) {
        notifyFakeSelected = false;
        notifySelected = false;
        int months = DateListHelper.monthsFromStartDate(date);
        if (months != getCurrentItem()) {
            setCurrentItem(months);
        }
        adapter.getCurrentMonthView().fakeSelect(date);
        notifyFakeSelected = true;
        notifySelected = true;
    }

    public void setTags(List<DateView.Tag> tags) {
        adapter.getCurrentMonthView().setTags(tags);
    }

    public void attachSelectedDate(Date date) {
        notifyDateSelected(date);
    }
}
