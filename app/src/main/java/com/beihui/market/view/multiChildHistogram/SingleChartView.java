package com.beihui.market.view.multiChildHistogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by admin on 2018/5/22.
 */

public class SingleChartView extends View {
    public SingleChartView(Context context) {
        super(context);
        init();
    }

    public SingleChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        coordinateAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinateAxisPaint.setStyle(Paint.Style.FILL);
        coordinateAxisPaint.setStrokeWidth(2);
        coordinateAxisPaint.setColor(Color.parseColor("#ff0000"));
    }

    // 轴线画笔
    private Paint coordinateAxisPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画底部线
        canvas.drawLine(0, getHeight()-20, getWidth(), getHeight()-2, coordinateAxisPaint);

        //画底部文字
        canvas.drawText("1周", 20, getHeight()-8, coordinateAxisPaint);

        //画柱状图
        Rect histogramPaintRect = new Rect(getWidth()/2 -10, getHeight()/2, getWidth()/2+10, getHeight());
        canvas.drawRect(histogramPaintRect, coordinateAxisPaint);

        //绘制Bitmap选中的图标


        //绘制上方数量

    }
}
