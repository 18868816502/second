package com.beihui.market.entity;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/24
 */

public class CreditBill implements Serializable {

    /**
     * id : 2fbcf762e1cc484a86adf3d58df40a28
     * userId : e0cba5a98c0e4dc9a4b4e4a292b3805c
     * cardId : bc439c5bb3a4457eb0d5fd5b158bcdc1
     * billDate : 2017-04-11 00:00:00
     * billMonth : 2017-03-12 00:00:00
     * paymentDueDate : 2017-05-05 00:00:00
     * currencyType : 1
     * lastBalance : 849.5
     * lastPayment : 849.5
     * interest : 0
     * newBalance : 849.5
     * minPayment : 849.5
     * billSource : 2
     * status : 2
     * gmtCreate : 2018-04-11 17:03:46
     * gmtModify : 2018-04-11 17:04:31
     * outBillDay : null
     * returnDay : 0
     * detailList : null
     * startTime : 2017-03-12 00:00:00
     * endTime : 2017-04-11 00:00:00
     * presentFlag: false
     */

    private String id;
    private String userId;
    private String cardId;
    private String billDate;
    private String billMonth;
    private String paymentDueDate;
    private int currencyType;
    private double lastBalance;
    private double lastPayment;
    private int interest;
    private double newBalance;
    private double minPayment;
    private int billSource;
    private int status;
    private String gmtCreate;
    private String gmtModify;
    private Object outBillDay;
    private int returnDay;
    private Object detailList;
    private String startTime;
    private String endTime;
    private boolean presentFlag;

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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public double getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(double lastBalance) {
        this.lastBalance = lastBalance;
    }

    public double getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(double lastPayment) {
        this.lastPayment = lastPayment;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    public double getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(double minPayment) {
        this.minPayment = minPayment;
    }

    public int getBillSource() {
        return billSource;
    }

    public void setBillSource(int billSource) {
        this.billSource = billSource;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Object getOutBillDay() {
        return outBillDay;
    }

    public void setOutBillDay(Object outBillDay) {
        this.outBillDay = outBillDay;
    }

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }

    public Object getDetailList() {
        return detailList;
    }

    public void setDetailList(Object detailList) {
        this.detailList = detailList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isPresentFlag() {
        return presentFlag;
    }

    public void setPresentFlag(boolean presentFlag) {
        this.presentFlag = presentFlag;
    }
}
