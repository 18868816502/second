package com.beiwo.qnejqaz.view.calendar.datepager;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.beiwo.qnejqaz.view.calendar.CalendarView;
import com.beiwo.qnejqaz.view.calendar.dateadapter.WeekAdapter;
import com.beiwo.qnejqaz.view.calendar.dateview.DateView;
import com.beiwo.qnejqaz.view.calendar.dateview.WeekView;
import com.beiwo.qnejqaz.view.calendar.utils.DateListHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekPager extends ViewPager {

    private CalendarView calendarView;
    private WeekAdapter adapter;

    private boolean notifyFakeSelected = true;
    private boolean notifySelected = true;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    private Date selectedDate;

    private int lastPosition;

    public WeekPager(Context context) {
        this(context, null);
    }

    public WeekPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new WeekAdapter(DateListHelper.getDateWeeksRange(), this, new WeekView.WeeklyDateSelectedListener() {
            @Override
            public void onWeeklyDateSelected(Date date) {
                selectedDate = date;
                calendarView.notifyDateSelected(date, false);
            }
        });
        setAdapter(adapter);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Date date = DateListHelper.calculateWeeks(position);

                if (selectedDate != null) {
                    if (notifySelected) {
                        calendar.setTime(selectedDate);
                        calendar.add(Calendar.DAY_OF_YEAR, 7 * (position - lastPosition));
                        selectedDate = calendar.getTime();
                        adapter.getCurrentWeekView().selectDate(selectedDate);
                        adapter.cleatNoPrimaryItemSelected();
                        calendarView.notifyDateSelected(selectedDate, false);
                    }
                } else if (notifyFakeSelected) {
                    calendarView.notifyFakeDateSelected(date, false);
                }

                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setCalendarView(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void notifyDateSelected(Date date) {
        selectedDate = date;

        notifySelected = false;
        int weeks = DateListHelper.weeksFromStartDate(date);
        if (weeks != getCurrentItem()) {
            setCurrentItem(weeks);
        }
        adapter.getCurrentWeekView().selectDate(date);
        notifySelected = true;
    }

    public void notifyFakeDateSelected(Date fakeSelectedDate) {
        notifyFakeSelected = false;
        notifySelected = false;
        int weeks = DateListHelper.weeksFromStartDate(fakeSelectedDate);
        if (weeks != getCurrentItem()) {
            setCurrentItem(weeks);
        }
        adapter.getCurrentWeekView().fakeSelect(fakeSelectedDate);
        notifyFakeSelected = true;
        notifySelected = true;
    }

    public void setTags(List<DateView.Tag> tags) {
        adapter.getCurrentWeekView().setTags(tags);
    }

    public void attachSelectedDate(Date date) {
        notifyDateSelected(date);
    }
}
