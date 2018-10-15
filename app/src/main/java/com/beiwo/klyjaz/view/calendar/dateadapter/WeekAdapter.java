package com.beiwo.klyjaz.view.calendar.dateadapter;


import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.beiwo.klyjaz.view.calendar.datepager.WeekPager;
import com.beiwo.klyjaz.view.calendar.dateview.WeekView;
import com.beiwo.klyjaz.view.calendar.utils.DateListHelper;

import java.util.LinkedList;

public class WeekAdapter extends PagerAdapter {

    private LinkedList<WeekView> weekViewList = new LinkedList<>();
    private SparseArray<WeekView> weekViewSparseArray = new SparseArray<>();

    private int pagerCount;
    private WeekPager weekPager;
    private WeekView.WeeklyDateSelectedListener weeklyDateSelectedListener;

    public WeekAdapter(int pagerCount, WeekPager weekPager, WeekView.WeeklyDateSelectedListener weeklyDateSelectedListener) {
        this.pagerCount = pagerCount;
        this.weekPager = weekPager;
        this.weeklyDateSelectedListener = weeklyDateSelectedListener;
    }

    @Override
    public int getCount() {
        return pagerCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        WeekView weekView;
        if (weekViewList.size() > 0) {
            weekView = weekViewList.pop();
        } else {
            weekView = new WeekView(container.getContext());
            weekView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        weekView.setWeeklyDateSelectedListener(weeklyDateSelectedListener);
        weekView.setDateList(DateListHelper.generateWeekDateList(position));
        weekView.fakeSelect(DateListHelper.calculateWeeks(position));
        if (weekPager.getSelectedDate() != null) {
            weekView.selectDate(weekPager.getSelectedDate());
        }
        container.addView(weekView);

        weekViewSparseArray.put(position, weekView);
        return weekView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        WeekView weekView = (WeekView) object;
        container.removeView(weekView);
        weekViewList.add(weekView);

        weekViewSparseArray.remove(position);
        //清除tags
        weekView.clearTags();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        for (int i = 0; i < weekViewSparseArray.size(); ++i) {
            WeekView weekView = weekViewSparseArray.valueAt(i);
            if (weekView != object) {
                weekView.clearSelected();
            }
        }
    }

    public WeekView getCurrentWeekView() {
        return weekViewSparseArray.get(weekPager.getCurrentItem());
    }

    public void cleatNoPrimaryItemSelected() {
        WeekView primary = getCurrentWeekView();
        for (int i = 0; i < weekViewSparseArray.size(); ++i) {
            WeekView weekView = weekViewSparseArray.valueAt(i);
            if (weekView != primary) {
                weekView.clearSelected();
            }
        }
    }
}
