package com.beiwo.klyjaz.view.calendar.dateview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.beiwo.klyjaz.view.calendar.utils.DateListHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekView extends DateView {
    /**
     * all rect in need
     */
    private List<RectF> dateAreas = new ArrayList<>();

    private int column = DateListHelper.COLUMN;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    public interface WeeklyDateSelectedListener {
        void onWeeklyDateSelected(Date date);
    }

    private WeeklyDateSelectedListener weeklyDateSelectedListener;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        for (int i = 0; i < column; ++i) {
            dateAreas.add(new RectF());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Date anchorDate = selectedDate != null ? selectedDate : fakeSelectedDate;
        for (int i = 0; i < column; ++i) {
            RectF rectf = dateAreas.get(i);
            float rectWidth = (float) width / column;
            rectf.set(i * rectWidth, 0, (i + 1) * rectWidth, height);

            Date date = dateList.get(i);
            Paint.FontMetricsInt fontMetrics = solarPaint.getFontMetricsInt();
            int baseline = (int) (rectf.bottom + rectf.top - fontMetrics.bottom - fontMetrics.top) / 2;
            calendar.setTime(date);
            String dateStr = calendar.get(Calendar.DAY_OF_MONTH) + "";

            if (selectedDate != null && DateListHelper.equalsDay(selectedDate, date)) {
                solarPaint.setColor(selectedDateCircleColor);
                canvas.drawCircle(rectf.centerX(), rectf.centerY(), selectedDateCircleRadius, solarPaint);
                solarPaint.setColor(Color.WHITE);
                canvas.drawText(dateStr, rectf.centerX(), baseline, solarPaint);
            } else if (DateListHelper.equalsDay(currentDate, date)) {
                solarPaint.setColor(selectedDateCircleColor);
                canvas.drawText(dateStr, rectf.centerX(), baseline, solarPaint);
            } else if (anchorDate != null && !DateListHelper.equalsMonth(anchorDate, date)) {
                solarPaint.setColor(otherDateTextColor);
                canvas.drawText(dateStr, rectf.centerX(), baseline, solarPaint);
            } else {
                solarPaint.setColor(dateTextColor);
                canvas.drawText(dateStr, rectf.centerX(), baseline, solarPaint);
            }
            //绘制tag
            drawTags(canvas, rectf, date);
        }
    }

    @Override
    protected boolean onSingleTap(MotionEvent event) {
        for (RectF rectf : dateAreas) {
            if (rectf.contains(event.getX(), event.getY())) {
                Date selected = dateList.get(dateAreas.indexOf(rectf));
                selectedDate = selected;
                postInvalidate();

                if (weeklyDateSelectedListener != null) {
                    weeklyDateSelectedListener.onWeeklyDateSelected(selected);
                }
                return true;
            }
        }
        return false;
    }

    public void setWeeklyDateSelectedListener(WeeklyDateSelectedListener weeklyDateSelectedListener) {
        this.weeklyDateSelectedListener = weeklyDateSelectedListener;
    }
}
