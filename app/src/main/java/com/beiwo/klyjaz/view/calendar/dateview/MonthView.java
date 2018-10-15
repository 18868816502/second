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

public class MonthView extends DateView {

    /**
     * all rect in need
     */
    private List<RectF> dateAreas = new ArrayList<>();
    /**
     * drawn date row and column
     */
    private int row = DateListHelper.ROW;
    private int column = DateListHelper.COLUMN;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    public interface MonthlyDateSelectedListener {
        /**
         * 选中日期
         *
         * @param date   当前选中的日期
         * @param isLast 是否选中上月日期
         * @param isNext 是否选中下月日期
         */
        void onMonthlyDateSelected(Date date, boolean isLast, boolean isNext);
    }

    private MonthlyDateSelectedListener monthlyDateSelectedListener;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int size = row * column;
        for (int i = 0; i < size; ++i) {
            dateAreas.add(new RectF());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int index = 0;
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                RectF rectf = dateAreas.get(index);
                index++;
                float rectWidth = width / (float) column;
                float rectHeight = height / (float) row;
                rectf.set(j * rectWidth, i * rectHeight, (j + 1) * rectWidth, (i + 1) * rectHeight);

                Date date = dateList.get(i * column + j);
                Paint.FontMetricsInt fontMetrics = solarPaint.getFontMetricsInt();
                int baseline = (int) ((rectf.bottom + rectf.top - fontMetrics.bottom - fontMetrics.top) / 2);

                calendar.setTime(date);
                String monthStr = calendar.get(Calendar.DAY_OF_MONTH) + "";

                Date anchorDate = fakeSelectedDate;
                if (selectedDate != null && DateListHelper.equalsDay(date, selectedDate)) {
                    solarPaint.setColor(selectedDateCircleColor);
                    canvas.drawCircle(rectf.centerX(), rectf.centerY(), selectedDateCircleRadius, solarPaint);
                    solarPaint.setColor(Color.WHITE);
                    canvas.drawText(monthStr, rectf.centerX(), baseline, solarPaint);
                } else if (DateListHelper.equalsDay(date, currentDate)) {
                    solarPaint.setColor(selectedDateCircleColor);
                    canvas.drawText(monthStr, rectf.centerX(), baseline, solarPaint);
                } else if (anchorDate != null && !DateListHelper.equalsMonth(date, anchorDate)) {
                    solarPaint.setColor(otherDateTextColor);
                    canvas.drawText(monthStr, rectf.centerX(), baseline, solarPaint);
                } else {
                    solarPaint.setColor(dateTextColor);
                    canvas.drawText(monthStr, rectf.centerX(), baseline, solarPaint);
                }
                //绘制tag
                drawTags(canvas, rectf, date);
            }
        }
    }

    @Override
    protected boolean onSingleTap(MotionEvent event) {
        for (RectF rectf : dateAreas) {
            if (rectf.contains(event.getX(), event.getY())) {
                Date selected = dateList.get(dateAreas.indexOf(rectf));
                if (selectedDate != selected) {
                    selectedDate = selected;
                    postInvalidate();

                    if (monthlyDateSelectedListener != null) {
                        int cal = DateListHelper.compareMonth(selected, fakeSelectedDate);
                        monthlyDateSelectedListener.onMonthlyDateSelected(selected, cal < 0, cal > 0);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int getSelectedDateRowIndex() {
        if (selectedDate != null) {
            for (int i = 0; i < dateList.size(); ++i) {
                if (DateListHelper.equalsDay(selectedDate, dateList.get(i))) {
                    return i / 7;
                }
            }
        }
        if (fakeSelectedDate != null) {
            for (int i = 0; i < dateList.size(); ++i) {
                if (DateListHelper.equalsDay(fakeSelectedDate, dateList.get(i))) {
                    return i / 7;
                }
            }
        }
        return 0;
    }

    public void setMonthlyDateSelectedListener(MonthlyDateSelectedListener monthlyDateSelectedListener) {
        this.monthlyDateSelectedListener = monthlyDateSelectedListener;
    }
}
