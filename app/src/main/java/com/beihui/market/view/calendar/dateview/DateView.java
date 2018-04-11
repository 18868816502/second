package com.beihui.market.view.calendar.dateview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.beihui.market.view.calendar.CalendarView.Attrs;


public abstract class DateView extends View {

    protected Paint solarPaint;
    protected Paint tagPaint;

    protected int dateTextSize;
    protected int dateTextColor;
    protected int selectedDateCircleRadius;
    protected int selectedDateCircleColor;
    protected int selectedDateTextColor;
    protected int currentDateTextColor;
    protected int otherDateTextColor;

    protected int tagPositiveColor;
    protected int tagNegativeColor;
    protected int tagPointRadius;
    protected int tagPointBottomPadding;


    /**
     * 选中的日期
     */
    protected Date selectedDate;
    /**
     * 当前日期
     */
    protected Date currentDate;
    /**
     * 模拟选中的日期
     */
    protected Date fakeSelectedDate;

    protected List<Date> dateList = new ArrayList<>();

    protected List<Tag> tags = new ArrayList<>();
    private List<String> tagDateList = new ArrayList<>();

    private GestureDetector gestureDetector;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return onSingleTap(e);
            }
        });

        currentDate = new Date();

        dateTextSize = Attrs.dateTextSize;
        dateTextColor = Attrs.dateTextColor;
        selectedDateCircleRadius = (int) (dateTextSize * 1.15);
        selectedDateCircleColor = Attrs.selectedDateCircleColor;
        selectedDateTextColor = Attrs.selectedDateTextColor;
        currentDateTextColor = Attrs.currentDateTextColor;
        otherDateTextColor = Attrs.otherDateTextColor;
        tagPositiveColor = Attrs.tagPositiveColor;
        tagNegativeColor = Attrs.tagNegativeColor;
        tagPointRadius = (int) (context.getResources().getDisplayMetrics().density * 2);
        tagPointBottomPadding = (int) (context.getResources().getDisplayMetrics().density * 2);

        solarPaint = new Paint();
        solarPaint.setAntiAlias(true);
        solarPaint.setTextAlign(Paint.Align.CENTER);
        solarPaint.setTextSize(Attrs.dateTextSize);

        tagPaint = new Paint();
        tagPaint.setAntiAlias(true);
        tagPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * 用户点击
     *
     * @param event motion up event
     * @return true if consumed
     */
    protected abstract boolean onSingleTap(MotionEvent event);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    protected void drawTags(Canvas canvas, RectF rectF, Date date) {
        String dateStr = dateFormat.format(date);
        if (tagDateList.contains(dateStr)) {
            Tag tag = tags.get(tagDateList.indexOf(dateStr));
            if (tag.tag > 0) {
                tagPaint.setColor(tagPositiveColor);
            } else {
                tagPaint.setColor(tagNegativeColor);
            }
            canvas.drawCircle(rectF.centerX(), rectF.bottom - tagPointBottomPadding, tagPointRadius, tagPaint);
        }
    }

    public void setDateList(List<Date> dateList) {
        if (dateList == null || dateList.size() == 0) {
            throw new IllegalArgumentException("Date List must be provided.");
        }
        this.dateList.clear();
        this.dateList.addAll(dateList);
    }

    /**
     * 选中一个日期
     *
     * @param date 选中的日期
     */
    public void selectDate(Date date) {
        selectedDate = date;
        postInvalidate();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * 清除选中的日期
     */
    public void clearSelected() {
        selectedDate = null;
        postInvalidate();
    }

    /**
     * 模拟选中一个日期，但不特殊绘制，用于滑动或者判断当前月份的坐标
     *
     * @param date 选中的日期
     */
    public void fakeSelect(Date date) {
        fakeSelectedDate = date;
        postInvalidate();
    }

    public Date getFakeSelectedDate() {
        return fakeSelectedDate;
    }

    public void setTags(List<Tag> tags) {
        this.tags.clear();
        this.tagDateList.clear();
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); ++i) {
                Tag tag = tags.get(i);
                this.tags.add(tag);
                this.tagDateList.add(tag.dateStr);
            }
        }
        postInvalidate();
    }

    public void clearTags() {
        tags.clear();
        tagDateList.clear();
        postInvalidate();
    }


    public static class Tag {
        private String dateStr;
        private Integer tag;

        public Tag(String dateStr, Integer tag) {
            this.dateStr = dateStr;
            this.tag = tag;
        }
    }
}
