package com.beihui.market.view.barcharts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;


import com.beihui.market.R;

import java.util.List;


/**
 * Created by opq on 2017/8/8.
 * 重写一个LinearLayout， 遍历添加LinChartView
 */

public class LinChartLayout extends LinearLayout {

    /**
     * 列表的数据源
     */
    private List<LinChartData> mData;
    private TypedArray mTypedArray;

    //单个柱状图的宽（配置类为linChartViewHeight）
    private float linChartViewHeight;

    /**
     * 构造方法
     */
    public LinChartLayout(Context context) {
        this(context, null);
    }

    public LinChartLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LinChartView);

        linChartViewHeight =  mTypedArray.getDimension(R.styleable.LinChartView_linChartViewHeight, LinChartConfig.linChartViewHeight);
        this.setOrientation(VERTICAL);
        setView();
    }

    /**
     * 设置布局
     */
    public void setView() {
        if (mData != null && !mData.isEmpty()) {
            //最长文字的个数
            int text_max_length = 0;
            //最长数量的个数
            int number_max_length = 0;
            //最长的柱形图的高度
            float value_max = 0f;
            for (LinChartData data : mData) {
                //获取最长文字的个数
                if (text_max_length <= data.getName().length()) {
                    text_max_length = data.getName().length();
                }

                //获取最高的柱状图的高度
                if (value_max <= data.getChartHeight()) {
                    value_max = data.getChartHeight();
                }

                //获取最长数量的个数
                if (number_max_length <= data.getNumber().length()) {
                    number_max_length = data.getNumber().length();
                }
            }

            //获取单个字符的高和宽
            int[] wh = getTextWH();
            int[] nh = getNumberWH();

            //文字区域的宽
            int textAreW = text_max_length * wh[0];

            //数量区域的宽
            int numberAreW = (number_max_length + 1) * nh[0];

            //设置容器的宽和高
            LayoutParams layoutParams = new LayoutParams(LinChartConfig.linChartViewWidth, (int) linChartViewHeight);

            //设置居中
            layoutParams.gravity = Gravity.CENTER;

            //遍历添加LinChartView
            for (LinChartData data : mData) {
                LinChartView chartView = new LinChartView(getContext());
                chartView.setData(textAreW,  numberAreW, value_max, data);
                this.addView(chartView, layoutParams);
            }
        }
    }

    /**
     * 获取单个字符的高和宽
     */
    private int[] getTextWH() {
        int[] wh = new int[2];
        //一个矩形
        Rect rect = new Rect();
        String text = "我";
        Paint paint = new Paint();
        //设置文字大小
        paint.setTextSize(LinChartConfig.leftTextSize);
        paint.getTextBounds(text, 0, text.length(), rect);
        wh[0] = rect.width();
        wh[1] = rect.height();
        return wh;
    }

    /**
     * 获取单个数字的高和宽
     */
    private int[] getNumberWH() {
        int[] wh = new int[2];
        //一个矩形
        Rect rect = new Rect();
        String text = "0";
        Paint paint = new Paint();
        //设置文字大小
        paint.setTextSize(LinChartConfig.leftTextSize);
        paint.getTextBounds(text, 0, text.length(), rect);
        wh[0] = rect.width();
        wh[1] = rect.height();
        return wh;
    }

    /**
     * 设置数据
     */
    public void setData(List<LinChartData> d, int chartViewHeight) {
        this.mData = d;
        this.linChartViewHeight = chartViewHeight;
        setView();
    }

}
