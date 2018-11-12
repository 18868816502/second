package com.beiwo.klyjaz.entity;

import java.io.Serializable;


public class AnalysisOverviewBean implements Serializable {
    /**
     * repayAmount : 40851.27
     * unpayAmount : 3891.52
     * overAmount : 1604.12
     */

    private double repayAmount;
    private double unpayAmount;
    private double overAmount;

    public double getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(double repayAmount) {
        this.repayAmount = repayAmount;
    }

    public double getUnpayAmount() {
        return unpayAmount;
    }

    public void setUnpayAmount(double unpayAmount) {
        this.unpayAmount = unpayAmount;
    }

    public double getOverAmount() {
        return overAmount;
    }

    public void setOverAmount(double overAmount) {
        this.overAmount = overAmount;
    }
}