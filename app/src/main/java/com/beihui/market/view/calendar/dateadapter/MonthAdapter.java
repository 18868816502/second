package com.beihui.market.view.calendar.dateadapter;


import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.view.calendar.datepager.MonthPager;
import com.beihui.market.view.calendar.dateview.MonthView;
import com.beihui.market.view.calendar.utils.DateListHelper;

import java.util.LinkedList;

public class MonthAdapter extends PagerAdapter {
    /**
     * cached MonthView
     */
    private LinkedList<MonthView> monthViewList = new LinkedList<>();
    /**
     * MonthView that has been added
     */
    private SparseArray<MonthView> monthViewSparseArray = new SparseArray<>();

    private int pagerCount;
    private MonthPager monthPager;
    private MonthView.MonthlyDateSelectedListener monthlyDateSelectedListener;

    public MonthAdapter(int pagerCount, MonthPager monthPager, MonthView.MonthlyDateSelectedListener monthlyDateSelectedListener) {
        this.pagerCount = pagerCount;
        this.monthPager = monthPager;
        this.monthlyDateSelectedListener = monthlyDateSelectedListener;
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
        MonthView monthView;
        if (monthViewList.size() > 0) {
            monthView = monthViewList.pop();
        } else {
            monthView = new MonthView(container.getContext());
            monthView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        monthView.setMonthlyDateSelectedListener(monthlyDateSelectedListener);
        monthView.setDateList(DateListHelper.generateMonthDateList(position));
        monthView.fakeSelect(DateListHelper.calculateMonth(position));
        if (monthPager.getSelectedDate() != null) {
            monthView.selectDate(monthPager.getSelectedDate());
        }
        container.addView(monthView);

        monthViewSparseArray.put(position, monthView);
        return monthView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        MonthView monthView = (MonthView) object;
        container.removeView(monthView);
        monthViewList.add(monthView);

        monthViewSparseArray.remove(position);
        //清除tags
        monthView.clearTags();
    }

    public MonthView getCurrentMonthView() {
        return monthViewSparseArray.get(monthPager.getCurrentItem());
    }

    public void clearNoPrimaryItemSelected() {
        MonthView primary = getCurrentMonthView();
        for (int i = 0; i < monthViewSparseArray.size(); ++i) {
            MonthView monthView = monthViewSparseArray.valueAt(i);
            if (primary != monthView) {
                monthView.clearSelected();
            }
        }
    }
}
