package com.beiwo.klyjaz.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.beiwo.klyjaz.R;


public class RoundProgressBar extends View {

    private Paint ringPaint;
    private Paint arcPaint;
    private Paint textPaint;

    private int progress = 0;

    private float strokeWidth;

    private RectF rectF = new RectF();
    private Rect textRect = new Rect();
    private RectF ovalRectF = new RectF();

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ringPaint = new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setColor(Color.parseColor("#f3f3f3"));

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeJoin(Paint.Join.BEVEL);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setDither(true);
        arcPaint.setColor(getResources().getColor(R.color.colorPrimary));

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.black_1));
        textPaint.setTextSize(getResources().getDisplayMetrics().density * 20f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        strokeWidth = getMeasuredWidth() / 15.0f;
        ringPaint.setStrokeWidth(strokeWidth);
        arcPaint.setStrokeWidth(strokeWidth);

        ovalRectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2,
                getMeasuredWidth() - strokeWidth / 2, getMeasuredHeight() - strokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f - strokeWidth / 2.0f, ringPaint);

        String text = ((int) (progress / 100.0 * 100)) + "%";
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, rectF.width() / 2 - textRect.width() / 2, rectF.height() / 2 + textRect.height() / 2, textPaint);

        canvas.save();
        canvas.rotate(-90, rectF.centerX(), rectF.centerY());
        canvas.drawArc(ovalRectF, 0, progress / 100.0f * 360, false, arcPaint);
        canvas.restore();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }
}