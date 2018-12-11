package com.beiwo.klyjaz.tang.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.beiwo.klyjaz.R;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/10
 */
public class RateView extends View {
    private int starRadius;
    private int starWidth;
    private int starHeight;
    private int starGap;
    private final int DEFAULT_RADIUS = 25;
    private Path starPath = new Path();
    private Paint starPaint = new Paint();
    private int progress = 0;

    public RateView(Context context) {
        this(context, null);
    }

    public RateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        starPaint.setAntiAlias(true);
        starPaint.setColor(Color.RED);
        starPaint.setStrokeWidth(1.5f);
        starGap = 8;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        starWidth = (w - getPaddingLeft() - getPaddingRight() - 4 * starGap) / 5;
        starHeight = h;
        starRadius = Math.min(DEFAULT_RADIUS, Math.min(starWidth, starHeight) / 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackgroud(canvas);
        drawNormal(canvas);
        drawProgress(canvas);
    }

    private void drawBackgroud(Canvas canvas) {
        //nothing
        setBackgroundResource(R.color.c_26df56);
    }

    private void drawNormal(Canvas canvas) {
        starPaint.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            PointF point = getPoint(i);//圆心
            //0->2->4->1->3->0
            starPath.moveTo(point.x, point.y - starRadius);//0
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 2 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 2 * 72 / 180)));//0 -> 2
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 4 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 4 * 72 / 180)));//2 -> 4
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 1 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 1 * 72 / 180)));//4 -> 1
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 3 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 3 * 72 / 180)));//1 -> 3
            starPath.close();//3 -> 0
            canvas.drawPath(starPath, starPaint);
        }
    }

    private void drawProgress(Canvas canvas) {
        starPaint.setColor(Color.RED);
        for (int i = 3; i < 5; i++) {
            PointF point = getPoint(i);//圆心
            //0->2->4->1->3->0
            starPath.moveTo(point.x, point.y - starRadius);//0
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 2 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 2 * 72 / 180)));//0 -> 2
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 4 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 4 * 72 / 180)));//2 -> 4
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 1 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 1 * 72 / 180)));//4 -> 1
            starPath.lineTo((float) (point.x + starRadius * Math.sin(Math.PI * 3 * 72 / 180)), (float) (point.y - starRadius * Math.cos(Math.PI * 3 * 72 / 180)));//1 -> 3
            starPath.close();//3 -> 0
            canvas.drawPath(starPath, starPaint);
        }
    }

    private PointF getPoint(int position) {
        PointF point = new PointF();
        point.x = getPaddingLeft() + starRadius * (position * 2 + 1) + starGap * position;
        point.y = getPaddingTop() + starRadius;
        return point;
    }

    public void setProgress(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        this.progress = progress;
        Rect rect = new Rect();
        invalidate(rect);
    }
}