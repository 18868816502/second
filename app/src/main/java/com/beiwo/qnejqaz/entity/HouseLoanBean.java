package com.beiwo.qnejqaz.entity;

public class HouseLoanBean {

    /**
     * 内容
     */
    private String content;
    /**
     * 数值年限
     */
    private int value;

    /**
     * 利率
     */
    private double rate;

    /**
     * 是否选中
     */
    private boolean isSelect;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
