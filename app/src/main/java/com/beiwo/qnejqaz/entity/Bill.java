package com.beiwo.qnejqaz.entity;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/19
 */

public class Bill implements Serializable {

    /**
     * userId : 6471cfde22314a7881b8bd967285439d
     * recordId : 44c4bc2d107a4e22bdc491235d26d48c
     * billId : da285af4a2c24ff2ba2f7280ab7e185f
     * repayTime : 2018-02-09 00:00:00
     * type : 1
     * status : 1
     * title : 51借呗
     * returnDay : -159
     * amount : 120.0
     * logoUrl : http://xxx.com
     * month : 1
     * term : 1
     * totalTerm : 2
     * moxieCode : CMB
     */

    private String userId;
    private String recordId;
    private String billId;
    private String repayTime;
    private int type;
    private int status;
    private String title;
    private int returnDay;
    private double amount;
    private String logoUrl;
    private int month;
    private int term;
    private int totalTerm;
    private String moxieCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getTotalTerm() {
        return totalTerm;
    }

    public void setTotalTerm(int totalTerm) {
        this.totalTerm = totalTerm;
    }

    public String getMoxieCode() {
        return moxieCode;
    }

    public void setMoxieCode(String moxieCode) {
        this.moxieCode = moxieCode;
    }
}
