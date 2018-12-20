package com.beiwo.qnejqaz.entity.request;

import java.io.Serializable;

/**
 * Created by xhb on 2018/5/1.
 * 账单详情
 */

public class XAccountInfo implements Serializable {

    /**
     * id : 1992
     * userId : 04e361bdbe1344039aa4a4e9a7e75c10
     * recordId : null
     * billId : 309eb4508fe341bbae215945afec3ca7
     * repayTime : 2018-09-23 00:00:00
     * type : 1
     * status : 1
     * title :
     * returnDay : 147
     * amount : 1200
     * overdueTotal : null
     * lastOverdue : false
     */

    //主键
    private long id;
    //用户Id
    private String userId;
    //记录Id （信用卡对应 信用卡Id，网贷对应-账单记录Id）
    private String recordId;
    //账单Id(卡Id，网贷对应-账单记录Id）
    private String billId;
    //	还款时间
    private String repayTime;
    //账单类型 1-网贷 2-信用卡
    private int type;
    //账单状态 1-待还 2-已还 3-逾期
    private int status;
    //账单标题
    private String title;
    //距离还款日
    private int returnDay;
    //还款金额
    private double amount;
    //逾期总数
    private int overdueTotal;
    //	最近逾期 true：是，false：否
    private boolean lastOverdue = false;

    //出账日期
    public String billDate;

    //距离出账单日
    public int outBillDay;

    public boolean isShow = false;

    public boolean isAnalog = false;


    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getOverdueTotal() {
        return overdueTotal;
    }

    public void setOverdueTotal(int overdueTotal) {
        this.overdueTotal = overdueTotal;
    }

    public boolean isLastOverdue() {
        return lastOverdue;
    }

    public void setLastOverdue(boolean lastOverdue) {
        this.lastOverdue = lastOverdue;
    }
}