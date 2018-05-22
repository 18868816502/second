package com.beihui.market.view.barcharts;

/**
 * Created by opq on 2017/8/8.
 * 柱状图传入的bean
 */

public class LinChartData {

    //选中的柱状图
    private boolean isSelect;

    //单个柱状图的高度
    private float chartHeight;

    //单个柱状图的名称
    private String name;

    //单个柱状图的数量
    private String number;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public float getChartHeight() {
        return chartHeight;
    }

    public void setChartHeight(float chartHeight) {
        this.chartHeight = chartHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
