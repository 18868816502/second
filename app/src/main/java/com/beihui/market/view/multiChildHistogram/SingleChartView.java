package com.beihui.market.view.multiChildHistogram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.util.Px2DpUtils;

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
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.parseColor("#000000"));

        chartPaint = new Paint();
        chartPaint.setStyle(Paint.Style.FILL);


        //绘制上方数量
        textTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //左侧文字颜色
        textTopPaint.setColor(Color.parseColor("#B3B3B3"));
        //左侧字体为16px
        textTopPaint.setTextSize(Px2DpUtils.dp2px(mContext, 12));
        //设置文字左对齐
        textTopPaint.setTextAlign(Paint.Align.CENTER);

    }

    public Context mContext;

    // 轴线画笔
    private Paint linePaint;
    private Paint chartPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画底部线
//        canvas.drawLine(0, getHeight()-DpUtils.dip2px(mContext, 30), getWidth(), getHeight()-DpUtils.dip2px(mContext, 30), linePaint);


//        canvas.drawText(bottomText, getWidth()/2, getHeight()-DpUtils.dip2px(mContext, 10), textPaint);


        //左侧文字颜色
        chartPaint.setColor(canvasColor);
        int chartHeight = (int) (getHeight() - (getHeight() - DpUtils.dip2px(mContext, 40)) * chartHeidht);
        //画柱状图
        RectF histogramPaintRect = new RectF(getWidth()/2 -DpUtils.dip2px(mContext, 4), chartHeight, getWidth()/2+DpUtils.dip2px(mContext, 4), getHeight()+DpUtils.dip2px(mContext, 2));
        canvas.drawRoundRect(histogramPaintRect, 8, 8, chartPaint);

        if (isSelect) {
            //绘制Bitmap选中的图标
            canvas.drawLine(getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 10), getWidth() / 2, chartHeight, linePaint);
            linePaint.setStyle(Paint.Style.FILL);
            Path path = new Path();
            path.moveTo(getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 9));
            path.lineTo(getWidth() / 2 - DpUtils.dip2px(mContext, 3), chartHeight - DpUtils.dip2px(mContext, 15));
            path.lineTo(getWidth() / 2 + DpUtils.dip2px(mContext, 3), chartHeight - DpUtils.dip2px(mContext, 15));
            canvas.drawPath(path, linePaint);


            //左侧文字颜色
            textTopPaint.setColor(Color.parseColor("#000000"));
            canvas.drawText(number, getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 23), textTopPaint);
        } else {
            //左侧文字颜色
            textTopPaint.setColor(Color.parseColor("#B3B3B3"));
            canvas.drawText(number, getWidth() / 2, chartHeight - DpUtils.dip2px(mContext, 8), textTopPaint);
        }
    }

    //柱形图高度
    public double chartHeidht = 0D;
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

    public void notifyChartColor(double chartHeidht, String number,  boolean isSelect) {
        this.chartHeidht = chartHeidht;
        this.number = number;
        this.isSelect = isSelect;
        canvasColor = redColor;
        this.postInvalidate();
    }

    public void notifyChartColor(double chartHeidht, String number,  boolean isSelect, int gray) {
        this.chartHeidht = chartHeidht;
        this.number = number;
        this.isSelect = isSelect;
        canvasColor = this.grayColor;
        this.postInvalidate();
    }
}
