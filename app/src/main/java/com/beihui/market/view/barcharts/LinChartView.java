package com.beihui.market.view.barcharts;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by opq on 2017/8/8.
 * 重写一个View ， 绘制一个柱状图
 */

public class LinChartView extends View {

    //数据源
    private LinChartData mData;
    //mTextW文字宽度
    private int mTextW;
    //numberAreW是数量的宽度
    private int mNumberAreW;
    //mMaxV为最高柱状图的高度(mMaxV在LinChartLayout判断)
    private float mMaxV;
    //柱状图画笔
    private Paint arcPaint = null;
    //坐标画笔
    private Paint linePaint = null;


    /**
     * 构造函数
     */
    public LinChartView(Context context) {
        this(context, null);
    }

    public LinChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null) {
            return;
        }
        //hua底部文字
        drawBottomText(canvas, mData.getName());
        //画坐标线
        drawBottomLine(canvas);
        //画柱形图形
        drawBottomChart(canvas);
        //画选中的Bitmap图片
        drawSelectBitmap(canvas);
        //画上方数字
        drawTopText(canvas, mData.getNumber());

    }

    /**
     * 绘制底部文字说明 居中
     */
    private void drawBottomText(Canvas canvas, String text) {
        //控件的高度
        int y = getHeight();
        Paint textPaint = new Paint();
        //左侧文字颜色
        textPaint.setColor(LinChartConfig.leftTextColor);
        //左侧字体为16px
        textPaint.setTextSize(LinChartConfig.leftTextSize);
        //设置文字左对齐
        textPaint.setTextAlign(Paint.Align.CENTER);
        //注意第二个参数，左对齐，文字是从左开始写的，那么  x 就是对齐处的X坐标
        canvas.drawText(text, 0, y, textPaint);
    }

    /**
     * 画坐标线
     */
    private void drawBottomLine(Canvas canvas) {
        //坐标画笔
        this.linePaint = new Paint();
        //设置画笔的宽度
        this.linePaint.setStrokeWidth(LinChartConfig.coordinateLineStrokeWidth);
        //设置画笔颜色
        this.linePaint.setColor(LinChartConfig.coordinateLineColor);
        //去除锯齿
        this.linePaint.setAntiAlias(true);
        //画线
        canvas.drawLine(0, 0, getWidth(), getHeight(), linePaint);
    }

    /**
     * 绘制柱形图形
     */
    private void drawBottomChart(Canvas canvas) {
        //柱形图长度（也就是高）
        double chart_length = (LinChartConfig.linChartViewWidth - mTextW - mNumberAreW - LinChartConfig.coordinateLineStrokeWidth
                - LinChartConfig.coordinateLineMarginLeftText - LinChartConfig.coordinateLineMarginRightText) / mMaxV;
        //左
        int start_left = mTextW + LinChartConfig.coordinateLineStrokeWidth + LinChartConfig.coordinateLineMarginLeftText;
        //上
        int start_top = LinChartConfig.singleChartTopMargin;
        //减去距离右侧文字的偏移量
        int start_right;
        if (mValue <= 0) {
            start_right = start_left;
        } else  {
            if ((int) (chart_length * mValue) - LinChartConfig.coordinateLineMarginRightText < 0) {
                start_right = start_left + 1;
            } else {
                start_right = start_left + (int) (chart_length * mValue) - LinChartConfig.coordinateLineMarginRightText;
            }
        }
        //创建画笔
        this.arcPaint = new Paint();
        //设置画笔颜色
        if (mData.isSelect()) {
            this.arcPaint.setColor(LinChartConfig.singleChartSelectColor);
        } else {
            this.arcPaint.setColor(LinChartConfig.singleChartUnSelectColor);
        }
        //去除锯齿
        this.arcPaint.setAntiAlias(true);
        //绘制成圆角矩形
        RectF rectF = new RectF(start_left, start_top, start_right, getHeight()-LinChartConfig.singleChartBottomMargin);
        canvas.drawRoundRect(rectF, LinChartConfig.singleChartCorner, LinChartConfig.singleChartCorner, arcPaint);
    }

    /**
     * 画选中的图片
     */
    public void drawSelectBitmap(Canvas canvas) {


    }

    /**
     * 画上方的数字
     */
    private void drawTopText(Canvas canvas, String text) {
        //控件的高度
        int y = getHeight();
        Paint textPaint = new Paint();
        //左侧文字颜色
        textPaint.setColor(LinChartConfig.leftTextColor);
        //左侧字体为16px
        textPaint.setTextSize(LinChartConfig.leftTextSize);
        //设置文字左对齐
        textPaint.setTextAlign(Paint.Align.CENTER);
        //注意第二个参数，左对齐，文字是从左开始写的，那么  x 就是对齐处的X坐标
        canvas.drawText(text, 0, 0, textPaint);
    }


    /**
     * 返回指定笔和指定字符串的长度
     */
    public static float getFontLength(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    /**
     * 设置数据
     */
    public void setData(int textW, int numberAreW, float max_value, LinChartData data) {
        this.mNumberAreW = numberAreW;
        this.mTextW = textW;
        this.mMaxV = max_value;
        this.mData = data;
        this.postInvalidate();
    }
}
