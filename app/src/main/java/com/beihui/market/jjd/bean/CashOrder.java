package com.beihui.market.jjd.bean;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/14
 */

public class CashOrder {
    /**
     * id : 165608f3b0b2486781b7e2ba463e8f93
     * userId : ad67cbc40f5d4146bb98f49061843236
     * orderAmount : 1000
     * limitDay : 10
     * serviceCharge : 45
     * accountAmount : 900
     * returnTime : 2018-11-12
     * returnAmount : 1000
     * orderStatus : 1
     * gmtCreate : 2018-09-14 15:38:26
     * gmtModify : 2018-09-14 15:39:27
     * overDate : 2018-09-16 15:39:28
     * auditDate : 2018-09-16 15:39:28
     */

    private String id;
    private String userId;
    private float orderAmount;
    private int limitDay;
    private float serviceCharge;
    private float accountAmount;
    private String returnTime;
    private float returnAmount;
    private String orderStatus;
    private String gmtCreate;
    private String gmtModify;
    private String overDate;
    private String auditDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(float orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }

    public float getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(float serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public float getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(float accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public float getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(float returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getOverDate() {
        return overDate;
    }

    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }
}