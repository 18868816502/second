package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public class WeekView extends CalendarView {

    private OnClickWeekViewListener mOnClickWeekViewListener;
    private List<String> lunarList;

    private List<Rect> usedRect = new ArrayList<>();

    private int padding;

    public WeekView(Context context, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
        super(context);

        this.mInitialDateTime = dateTime;
        Utils.NCalendar weekCalendar2 = Utils.getWeekCalendar2(dateTime, Attrs.firstDayOfWeek);

        dateTimes = weekCalendar2.dateTimeList;
        lunarList = weekCalendar2.lunarList;
        mOnClickWeekViewListener = onClickWeekViewListener;

        for (int i = 0; i < 7; ++i) {
            usedRect.add(new Rect());
        }

        padding = (int) Utils.dp2px(context, 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        //为了与月日历保持一致，往上压缩一下,5倍的关系
        mHeight = (int) (getHeight() - Utils.dp2px(getContext(), 2));
        mRectList.clear();

        int index = 0;
        for (int i = 0; i < 7; i++) {
            Rect rect = usedRect.get(index);
            rect.set(i * mWidth / 7, 0, i * mWidth / 7 + mWidth / 7, mHeight);
            index++;

            mRectList.add(rect);
            DateTime dateTime = dateTimes.get(i);
            Paint.FontMetricsInt fontMetrics = mSolarPaint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;

            if (mSelectDateTime != null && dateTime.equals(mSelectDateTime)) {
                mSolarPaint.setColor(mSelectCircleColor);
                canvas.drawCircle(rect.centerX(), rect.centerY(), mSelectCircleRadius, mSolarPaint);
                mSolarPaint.setColor(Color.WHITE);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSolarPaint);
            } else if (Utils.isToday(dateTime)) {
                mSolarPaint.setColor(mSelectCircleColor);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSolarPaint);
            } else {
                mSolarPaint.setColor(mSolarTextColor);
                canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mSolarPaint);
            }
            //绘制圆点
            drawPoint(canvas, rect, dateTime, baseline);
        }
    }

    private void drawLunar(Canvas canvas, Rect rect, int baseline, int i) {
        if (isShowLunar) {
            mLunarPaint.setColor(mLunarTextColor);
            String lunar = lunarList.get(i);
            canvas.drawText(lunar, rect.centerX(), baseline + getHeight() / 4, mLunarPaint);
        }
    }

    public void drawPoint(Canvas canvas, Rect rect, DateTime dateTime, int baseline) {
        if (pointList != null && pointList.contains(dateTime.toLocalDate().toString())) {
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
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
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
                    mOnClickWeekViewListener.onClickCurrentWeek(selectDateTime);
                    break;
                }
            }
            return true;
        }
    });


    public boolean contains(DateTime dateTime) {
        return dateTimes.contains(dateTime);
    }
}
