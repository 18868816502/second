package com.beiwo.klyjaz.entity;

import java.io.Serializable;


public class AnalysisChartBean implements Serializable {

    /**
     * amount : 0
     * time : 2018-01
     */

    //数量
    private Double amount;

    //底部文字
    private String time;

    //数据展示
    public String showAmount;

    //是否选中
    public boolean isSelect = false;


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}