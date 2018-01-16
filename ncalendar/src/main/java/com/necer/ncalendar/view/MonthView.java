package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public class MonthView extends CalendarView {

    private List<String> lunarList;
    private int mRowNum;
    private OnClickMonthViewListener mOnClickMonthViewListener;

    private List<Rect> usedRect = new ArrayList<>();

    private int padding;

    public MonthView(Context context, DateTime dateTime, OnClickMonthViewListener onClickMonthViewListener) {
        super(context);
        this.mInitialDateTime = dateTime;

        //0周日，1周一
        Utils.NCalendar nCalendar2 = Utils.getMonthCalendar2(dateTime, Attrs.firstDayOfWeek);
        mOnClickMonthViewListener = onClickMonthViewListener;

        lunarList = nCalendar2.lunarList;
        dateTimes = nCalendar2.dateTimeList;

        mRowNum = dateTimes.size() / 7;

        for (int i = 0; i < 42; ++i) {
            usedRect.add(new Rect());
        }

        padding = (int) Utils.dp2px(context, 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        //绘制高度
        mHeight = getDrawHeight();
        mRectList.clear();
        int index = 0;
        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = usedRect.get(index);
                index++;
                rect.set(j * mWidth / 7, i * mHeight / mRowNum, j * mWidth / 7 + mWidth / 7, i * mHeight / mRowNum + mHeight / mRowNum);

                mRectList.add(rect);
                DateTime dateTime = dateTimes.get(i * 7 + j);
                Paint.FontMetricsInt fontMetrics = mSolarPaint.getFontMetricsInt();

                int baseline;//让6行的第一行和5行的第一行在同一直线上，处理选中第一行的滑动
                if (mRowNum == 5) {
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                } else {
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2 + (mHeight / 5 - mHeight / 6) / 2;
                }

                //当月和上下月的颜色不同
                if (Utils.isEqualsMonth(dateTime, mInitialDateTime)) {
                    if (mSelectDateTime != null && dateTime.equals(mSelectDateTime)) {
                        mSolarPaint.setColor(mSelectCircleColor);
                        int centerY = mRowNum == 5 ? rect.centerY() : (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2);
                        canvas.drawCircle(rect.centerX(), centerY, mSelectCircleRadius, mSolarPaint);
                        mSolarPaint.setColor(Color.WHITE);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSolarPaint);
                    } else {
                        mSolarPaint.setColor(mSolarTextColor);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSolarPaint);
                    }
                    //绘制圆点
                    drawPoint(canvas, rect, dateTime, baseline);
                } else {
                    mSolarPaint.setColor(mHintColor);
                    canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSolarPaint);
                }
            }
        }
    }

    public int getMonthHeight() {
        return Attrs.monthCalendarHeight;
    }

    public int getDrawHeight() {
        return (int) (getMonthHeight() - Utils.dp2px(getContext(), 10));
    }


    private void drawLunar(Canvas canvas, Rect rect, int baseline, int color, int i, int j) {
        if (isShowLunar) {
            mLunarPaint.setColor(color);
            String lunar = lunarList.get(i * 7 + j);
            canvas.drawText(lunar, rect.centerX(), baseline + getMonthHeight() / 20, mLunarPaint);
        }
    }

    public void drawPoint(Canvas canvas, Rect rect, DateTime dateTime, int baseline) {
        String dateStr = dateTime.toLocalDate().toString();
        if (pointList != null && pointList.contains(dateStr)) {
            int index = pointList.indexOf(dateStr);
            if (tags.get(index) == 0) {
                //待还
                mTagPaint.setColor(Color.parseColor("#ff395e"));
            } else {
                mTagPaint.setColor(Color.parseColor("#5591ff"));
            }
            canvas.drawCircle(rect.centerX(), baseline + mSelectCircleRadius / 2 + mPointSize + padding, mPointSize, mTagPaint);
        }
    }


    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    DateTime selectDateTime = dateTimes.get(i);
                    if (Utils.isLastMonth(selectDateTime, mInitialDateTime)) {
                        mOnClickMonthViewListener.onClickLastMonth(selectDateTime);
                    } else if (Utils.isNextMonth(selectDateTime, mInitialDateTime)) {
                        mOnClickMonthViewListener.onClickNextMonth(selectDateTime);
                    } else {
                        mOnClickMonthViewListener.onClickCurrentMonth(selectDateTime);
                    }
                    break;
                }
            }
            return true;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public int getRowNum() {
        return mRowNum;
    }

    public int getSelectRowIndex() {
        if (mSelectDateTime == null) {
            return 0;
        }
        int indexOf = dateTimes.indexOf(mSelectDateTime);
        return indexOf / 7;
    }


}
