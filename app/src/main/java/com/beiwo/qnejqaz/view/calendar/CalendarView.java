package com.beiwo.qnejqaz.view.calendar;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.view.calendar.datepager.MonthPager;
import com.beiwo.qnejqaz.view.calendar.datepager.WeekPager;
import com.beiwo.qnejqaz.view.calendar.dateview.DateView;
import com.beiwo.qnejqaz.view.calendar.utils.DateListHelper;
import com.beiwo.qnejqaz.view.calendar.utils.DpUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarView extends ViewGroup implements NestedScrollingParent {

    public static final int STATE_MONTH = 1;
    public static final int STATE_WEEK = 2;

    private NestedScrollingParentHelper scrollingParentHelper;

    private MonthPager monthPager;
    private WeekPager weekPager;
    private View targetView;

    private int targetViewTop;
    private int monthPagerTop;

    private int monthPagerOffset;

    private Rect monthRect;
    private Rect weekRect;

    private ValueAnimator monthPagerAnimator;
    private ValueAnimator targetViewAnimator;

    //handle touch event
    private int downY;
    private int downX;
    private int lastY;
    private int touchSlop;
    private boolean isFirstScroll = true; //第一次手势滑动，因为第一次滑动的偏移量大于verticalY，会出现猛的一划，这里只对第一次滑动做处理

    private int calendarState = STATE_MONTH;

    public interface OnCalendarChangedListener {
        /**
         * 选中日期
         *
         * @param date 选中的日期
         */
        void onDateSelected(Date date);

        /**
         * 月份切换
         *
         * @param date 新的月份
         */
        void onMonthChange(Date date);
    }

    private OnCalendarChangedListener onCalendarChangedListener;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        Attrs.dateLineHeight = a.getDimensionPixelSize(R.styleable.CalendarView_dateLineHeight, (int) DpUtils.dp2px(context, 36));
        Attrs.dateTextSize = a.getDimensionPixelSize(R.styleable.CalendarView_dateTextSize, (int) DpUtils.dp2px(context, 13));
        Attrs.dateTextColor = a.getColor(R.styleable.CalendarView_dateTextColor, Color.BLACK);
        Attrs.selectedDateCircleColor = a.getColor(R.styleable.CalendarView_selectedDateCircleColor, Color.BLUE);
        Attrs.selectedDateTextColor = a.getColor(R.styleable.CalendarView_selectedDateTextColor, Color.WHITE);
        Attrs.currentDateTextColor = a.getColor(R.styleable.CalendarView_currentDateTextColor, Color.BLUE);
        Attrs.otherDateTextColor = a.getColor(R.styleable.CalendarView_otherDateTextColor, Color.GRAY);
        Attrs.tagPositiveColor = a.getColor(R.styleable.CalendarView_tagPositiveColor, Color.BLUE);
        Attrs.tagNegativeColor = a.getColor(R.styleable.CalendarView_tagNegativeColor, Color.RED);
        a.recycle();

        scrollingParentHelper = new NestedScrollingParentHelper(this);

        Calendar start = Calendar.getInstance(Locale.CHINA);
        start.setTime(new Date());
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.add(Calendar.YEAR, -50);

        Calendar end = Calendar.getInstance(Locale.CHINA);
        end.setTime(new Date());
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.add(Calendar.YEAR, 50);
        DateListHelper.initDateRange(start.getTime(), end.getTime());
        monthPager = new MonthPager(context);
        monthPager.setCalendarView(this);
        monthPager.setBackgroundColor(Color.WHITE);
        addView(monthPager, new LayoutParams(LayoutParams.MATCH_PARENT, Attrs.dateLineHeight * DateListHelper.ROW));

        weekPager = new WeekPager(context);
        weekPager.setCalendarView(this);
        weekPager.setBackgroundColor(Color.WHITE);
        weekPager.setOffscreenPageLimit(11);
        addView(weekPager, new LayoutParams(LayoutParams.MATCH_PARENT, Attrs.dateLineHeight));

        post(new Runnable() {
            @Override
            public void run() {
                weekPager.setVisibility(calendarState == STATE_MONTH ? INVISIBLE : VISIBLE);
                monthRect = new Rect(0, monthPager.getTop(), monthPager.getMeasuredWidth(), monthPager.getMeasuredHeight());
                weekRect = new Rect(0, weekPager.getTop(), weekPager.getMeasuredWidth(), weekPager.getMeasuredHeight());

                monthPager.setCurrentItem(DateListHelper.monthsFromStartDate(new Date()), false);
            }
        });

        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation == monthPagerAnimator) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    int top = monthPager.getTop();
                    int i = animatedValue - top;
                    monthPager.offsetTopAndBottom(i);
                } else {
                    int animatedValue = (int) animation.getAnimatedValue();
                    int top = targetView.getTop();
                    int i = animatedValue - top;
                    targetView.offsetTopAndBottom(i);
                }
            }
        };
        monthPagerAnimator = new ValueAnimator();
        monthPagerAnimator.addUpdateListener(updateListener);

        targetViewAnimator = new ValueAnimator();
        targetViewAnimator.addUpdateListener(updateListener);
        targetViewAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int top = targetView.getTop();
                if (top == monthPager.getMeasuredHeight()) {
                    calendarState = STATE_MONTH;
                    weekPager.setVisibility(INVISIBLE);
                } else {
                    calendarState = STATE_WEEK;
                    weekPager.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); ++i) {
            View childView = getChildAt(i);
            if (childView != monthPager && childView != weekPager) {
                targetView = childView;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (targetView != null) {
            targetView.measure(MeasureSpec.makeMeasureSpec(targetView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(targetView.getMeasuredHeight() - Attrs.dateLineHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (calendarState == STATE_MONTH) {
            monthPagerTop = monthPager.getTop();
            targetViewTop = targetView.getTop() == 0 ? monthPager.getMeasuredHeight() : targetView.getTop();
        } else {
            monthPagerTop = -getMonthPagerOffset();
            targetViewTop = targetView.getTop() == 0 ? weekPager.getMeasuredHeight() : targetView.getTop();
        }

        monthPager.layout(0, monthPagerTop, r, monthPagerTop + monthPager.getMeasuredHeight());
        targetView.layout(0, targetViewTop, r, targetViewTop + targetView.getMeasuredHeight());
        weekPager.layout(0, 0, r, weekPager.getMeasuredHeight());
    }

    private int getMonthPagerOffset() {
        return monthPager.getCurrentMonthView().getSelectedDateRowIndex() * Attrs.dateLineHeight;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes) {
        scrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        scrollingParentHelper.onStopNestedScroll(target);
        settingScroll();
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        scrollCalendarAndTarget(dy, true, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        //防止快速滑动
        targetViewTop = targetView.getTop();
        if (targetViewTop > weekPager.getMeasuredHeight()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @ViewCompat.ScrollAxis
    public int getNestedScrollAxes() {
        return scrollingParentHelper.getNestedScrollAxes();
    }

    private void scrollCalendarAndTarget(int dy, boolean isNestScroll, int[] consumed) {
        monthPagerTop = monthPager.getTop();
        targetViewTop = targetView.getTop();

        int weekHeight = weekPager.getMeasuredHeight();
        int monthHeight = monthPager.getMeasuredHeight();

        //4种情况
        if (dy > 0 && Math.abs(monthPagerTop) < monthPagerOffset) {
            //月日历和childView同时上滑
            int offset = getScrollOffset(dy, monthPagerOffset - Math.abs(monthPagerTop));
            monthPager.offsetTopAndBottom(-offset);
            targetView.offsetTopAndBottom(-offset);

            if (isNestScroll) {
                consumed[1] = offset;
            }
        } else if (dy > 0 && targetViewTop > weekHeight) {
            //月日历滑动到位置后，childView继续上滑，覆盖一部分月日历
            int offset = getScrollOffset(dy, targetViewTop - weekHeight);
            targetView.offsetTopAndBottom(-offset);

            if (isNestScroll) {
                consumed[1] = offset;
            }
        } else if (dy < 0 && monthPagerTop != 0 && !targetView.canScrollVertically(-1)) {
            //月日历和childView下滑
            int offset = getScrollOffset(Math.abs(dy), Math.abs(monthPagerTop));
            monthPager.offsetTopAndBottom(offset);
            targetView.offsetTopAndBottom(offset);

            if (isNestScroll) {
                consumed[1] = -offset;
            }
        } else if (dy < 0 && monthPagerTop == 0 && targetViewTop != monthHeight && !ViewCompat.canScrollVertically(targetView, -1)) {
            //月日历滑动到位置后，childView继续下滑
            int offset = getScrollOffset(Math.abs(dy), monthHeight - targetViewTop);
            targetView.offsetTopAndBottom(offset);

            if (isNestScroll) {
                consumed[1] = -offset;
            }
        }

        //childView滑动到周位置后，标记状态，同时周日显示
        if (targetViewTop == weekHeight) {
            calendarState = STATE_WEEK;
            weekPager.setVisibility(VISIBLE);
        }

        //周状态，下滑显示月日历，把周日历隐掉
        if (calendarState == STATE_WEEK && dy < 0 && !targetView.canScrollVertically(-1)) {
            weekPager.setVisibility(INVISIBLE);
        }

        //彻底滑到月日历，标记状态
        if (targetViewTop == monthHeight) {
            calendarState = STATE_MONTH;
        }
    }

    private void settingScroll() {
        //停止滑动的时候，距顶部的距离
        monthPagerTop = monthPager.getTop();
        targetViewTop = targetView.getTop();

        int monthHeight = monthPager.getMeasuredHeight();
        int weekHeight = weekPager.getMeasuredHeight();

        if (monthPagerTop == 0 && targetViewTop == monthHeight) {
            return;
        }
        if (monthPagerTop == -monthPagerOffset && targetViewTop == weekHeight) {
            return;
        }

        if (calendarState == STATE_MONTH) {
            if (monthHeight - targetViewTop < weekHeight) {
                startScrollAnimation(monthPagerTop, 0, targetViewTop, monthHeight);
            } else {
                startScrollAnimation(monthPagerTop, -monthPagerOffset, targetViewTop, weekHeight);
            }
        } else {
            if (targetViewTop < weekHeight * 2) {
                startScrollAnimation(monthPagerTop, -monthPagerOffset, targetViewTop, weekHeight);
            } else {
                startScrollAnimation(monthPagerTop, 0, targetViewTop, monthHeight);
            }
        }
    }

    private void startScrollAnimation(int startMonth, int endMonth, int startChild, int endChild) {
        monthPagerAnimator.setIntValues(startMonth, endMonth);
        monthPagerAnimator.setDuration(300);
        monthPagerAnimator.start();

        targetViewAnimator.setIntValues(startChild, endChild);
        targetViewAnimator.setDuration(300);
        targetViewAnimator.start();
    }

    private int getScrollOffset(int offset, int maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    private boolean isInCalendar(int x, int y) {
        if (calendarState == STATE_MONTH) {
            return monthRect.contains(x, y);
        } else {
            return weekRect.contains(x, y);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int absY = Math.abs(downY - y);
                if (absY > touchSlop && isInCalendar(downX, downY)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                int dy = lastY - y;
                if (isFirstScroll) {
                    if (dy > touchSlop) {
                        dy = dy - touchSlop;
                    } else if (dy < -touchSlop) {
                        dy = dy + touchSlop;
                    }
                    isFirstScroll = false;
                }
                scrollCalendarAndTarget(dy, false, null);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isFirstScroll = true;
                settingScroll();
                break;
        }
        return true;
    }

    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {
        this.onCalendarChangedListener = onCalendarChangedListener;
    }

    /**
     * 通知月份切换事件
     *
     * @param date 当前月份
     */
    public void notifyMonthChanged(Date date) {
        if (onCalendarChangedListener != null) {
            onCalendarChangedListener.onMonthChange(date);
        }
    }

    /**
     * 通知日期选中事件
     *
     * @param date       选中的日期
     * @param notifyWeek 是否同步周日历。是-同步周日历，否-同步月日历
     */
    public void notifyDateSelected(Date date, boolean notifyWeek) {
        if (notifyWeek) {
            weekPager.notifyDateSelected(date);
        } else {
            monthPager.notifyDateSelected(date);
        }
        monthPagerOffset = getMonthPagerOffset();

        if (onCalendarChangedListener != null) {
            onCalendarChangedListener.onDateSelected(date);
        }
    }

    /**
     * 通知模拟日期选中事件
     *
     * @param date       模拟选中的日期
     * @param notifyWeek 是否同步周日历。是-同步周日历，否-同步月日历
     */
    public void notifyFakeDateSelected(Date date, boolean notifyWeek) {
        if (notifyWeek) {
            weekPager.notifyFakeDateSelected(date);
        } else {
            monthPager.notifyFakeDateSelected(date);
        }
        monthPagerOffset = getMonthPagerOffset();
    }

    public void setTags(List<DateView.Tag> tags) {
        monthPager.setTags(tags);
        weekPager.setTags(tags);
    }

    public void attachSelectedDate(Date date) {
        monthPager.attachSelectedDate(date);
        weekPager.attachSelectedDate(date);

        monthPagerOffset = getMonthPagerOffset();

        if (onCalendarChangedListener != null) {
            onCalendarChangedListener.onDateSelected(date);
        }
    }


    public static class Attrs {
        static int dateLineHeight;
        public static int dateTextSize;
        public static int dateTextColor;
        public static int selectedDateCircleColor;
        public static int selectedDateTextColor;
        public static int currentDateTextColor;
        public static int otherDateTextColor;
        public static int tagPositiveColor;
        public static int tagNegativeColor;
    }
}
