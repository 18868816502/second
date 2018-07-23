package com.beihui.market.entity;

import java.io.Serializable;

/**
 * Copyright: zhujia (C)2018
 * FileName: RemindBean
 * Author: jiang
 * Create on: 2018/7/23 11:57
 * Description: 提醒数据体
 */
public class RemindBean implements Serializable {

    private int geTui;
    private int sms;
    private int wechat;
    private int day;

    public int getGeTui() {
        return geTui;
    }

    public void setGeTui(int geTui) {
        this.geTui = geTui;
    }

    public int getSms() {
        return sms;
    }

    public void setSms(int sms) {
        this.sms = sms;
    }

    public int getWechat() {
        return wechat;
    }

    public void setWechat(int wechat) {
        this.wechat = wechat;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
