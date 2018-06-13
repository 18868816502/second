package com.beihui.market.view.multiChildHistogram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.util.Px2DpUtils;

import java.math.BigDecimal;

/**
 * Created by admin on 2018/5/22.
 */

public class SingleChartView extends View {

    private Paint textPaint;
    private Paint textTopPaint;


    public int redColor = Color.parseColor("#ff0000");
    public int grayColor = Color.parseColor("#9F9FAC");

    //需要绘制的颜色
    public int canvasColor = redColor;
    private Path path;


    public int line = 0;

    public SingleChartView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SingleChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SingleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(5);
        linePaint.setColor(Color.parseColor("#ff5240"));

        lineEffectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineEffectPaint.setStyle(Paint.Style.FILL);
        lineEffectPaint.setStrokeWidth(5);
        lineEffectPaint.setColor(Color.parseColor("#F2C9C9"));
        PathEffect pathEffect = new DashPathEffect(new float[]{20, 15}, 0);
        lineEffectPaint.setPathEffect(pathEffect);

        chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setStyle(Paint.Style.FILL);
        chartPaint.setColor(canvasColor);

        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setStyle(Paint.Style.STROKE);
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(canvasColor);


        //绘制上方数量
        textTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //左侧文字颜色
        textTopPaint.setColor(Color.parseColor("#B3B3B3"));
        //左侧字体为16px
        textTopPaint.setTextSize(Px2DpUtils.dp2px(mContext, 12));
        //设置文字左对齐
        textTopPaint.setTextAlign(Paint.Align.CENTER);

        path = new Path();

    }

    public Context mContext;

    // 轴线画笔
    private Paint linePaint;
    private Paint lineEffectPaint;
    private Paint chartPaint;
    private Paint selectPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画底部线
//        canvas.drawLine(0, getHeight()-DpUtils.dip2px(mContext, 30), getWidth(), getHeight()-DpUtils.dip2px(mContext, 30), linePaint);


//        canvas.drawText(bottomText, getWidth()/2, getHeight()-DpUtils.dip2px(mContext, 10), textPaint);

        int chartHeight = (int) (getHeight() - (getHeight() - DpUtils.dip2px(mContext, 40)) * chartHeidht);

        //画左折线
        if (leftHeight != null) {
            int leftChartHeight = (int) (getHeight() - (getHeight() - DpUtils.dip2px(mContext, 40)) * leftHeight);
            if (line == 2) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
                canvas.drawLine(-getWidth() / 2, leftChartHeight - DpUtils.dip2px(mContext, 6), getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 6), lineEffectPaint);
            } else {
                canvas.drawLine(-getWidth() / 2, leftChartHeight - DpUtils.dip2px(mContext, 6), getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 6), linePaint);
            }
        }
        //画右折线
        if (rightHeight != null) {
            int rightChartHeight = (int) (getHeight() - (getHeight() - DpUtils.dip2px(mContext, 40)) * rightHeight);
            if (line != 0) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
                canvas.drawLine(getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 6), 3 * getWidth() / 2, rightChartHeight - DpUtils.dip2px(mContext, 6), lineEffectPaint);
            } else {
                canvas.drawLine(getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 6), 3 * getWidth() / 2, rightChartHeight - DpUtils.dip2px(mContext, 6), linePaint);
            }
        }
        //画圆
        canvas.drawCircle(getWidth()/2, chartHeight -DpUtils.dip2px(mContext, 6), DpUtils.dip2px(mContext, 4), chartPaint);


        //画柱状图
//        RectF histogramPaintRect = new RectF(getWidth()/2 -DpUtils.dip2px(mContext, 4), chartHeight, getWidth()/2+DpUtils.dip2px(mContext, 4), getHeight()+DpUtils.dip2px(mContext, 2));
//        canvas.drawRoundRect(histogramPaintRect, 8, 8, chartPaint);

        if (isSelect) {
            //绘制Bitmap选中的图标
//            canvas.drawLine(getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 10), getWidth() / 2, chartHeight, linePaint);
//
//            path.moveTo(getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 9));
//            path.lineTo(getWidth() / 2 - DpUtils.dip2px(mContext, 3), chartHeight - DpUtils.dip2px(mContext, 15));
//            path.lineTo(getWidth() / 2 + DpUtils.dip2px(mContext, 3), chartHeight - DpUtils.dip2px(mContext, 15));
//            canvas.drawPath(path, linePaint);


            canvas.drawCircle(getWidth()/2, chartHeight -DpUtils.dip2px(mContext, 6), DpUtils.dip2px(mContext, 6), selectPaint);

            //上侧文字颜色
            textTopPaint.setColor(Color.parseColor("#000000"));
            canvas.drawText(number, getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 23), textTopPaint);
        } else {
            //上侧文字颜色
            textTopPaint.setColor(Color.parseColor("#B3B3B3"));
            canvas.drawText(number, getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 13), textTopPaint);
        }
    }

    //柱形图高度
    public double chartHeidht = 0f;
    public Double leftHeight = null;
    public Double rightHeight = null;
    //数量
    public String number =  " ";

    //选中状态
    public boolean isSelect = false;


    public void notifyChartData(double chartHeidht, String number,  boolean isSelect){
        this.chartHeidht = chartHeidht;
        this.number = number;
        this.isSelect = isSelect;
        canvasColor = redColor;
        this.postInvalidate();
    }

    public void notifyChartData(double chartHeidht, String number,  boolean isSelect, int gray){
        this.chartHeidht = chartHeidht;
        this.number = number;
        this.isSelect = isSelect;
        canvasColor = this.grayColor;
        this.postInvalidate();
    }

    public void notifyChartColor(double chartHeidht, Double leftHeight,Double rightHeight, String number, boolean isSelect) {
        this.chartHeidht = chartHeidht;


        this.leftHeight = leftHeight;
        this.rightHeight = rightHeight;
        this.number = number;
        this.isSelect = isSelect;
        canvasColor = redColor;

        this.postInvalidate();
    }

    public void notifyChartColor(double chartHeidht, Double leftHeight,Double rightHeight, String number,  boolean isSelect, int line) {
        this.chartHeidht = chartHeidht;

        this.leftHeight = leftHeight;
        this.rightHeight = rightHeight;
        this.number = number;
        this.isSelect = isSelect;
        canvasColor = redColor;

        this.line = line;

        this.postInvalidate();
    }
}
