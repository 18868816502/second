package com.beihui.market.entity;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/22.
 */

public class AnalysisChartBean implements Serializable {


    /**
     * amount : 0
     * time : 2018-01
     */

    private int amount;
    private String time;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
