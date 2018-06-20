package com.beihui.market.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/20.
 * @version 4.0.0
 * 首页账单列表bean
 */

public class TabAccountNewBean implements Serializable{


    /**
     * time : 2018-05-06
     * status : 3
     * returnDay : -6
     * list : [{"id":41432,"userId":"FD6F7AED9D184FF6B7F12BAD405FCED7","recordId":"2455941edec64daabcb00712c6b70b6e","billId":"020032ae69b3439c85c0f2051834d867","repayTime":"2018-05-13 00:00:00","type":2,"status":3,"title":"招商银行  8636","returnDay":-6,"amount":1604.12,"overdueTotal":1,"lastOverdue":true,"outBillDay":0,"billDate":"2018-04-25 00:00:00","logoUrl":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/credit_card_bank_logo/b60c3737fe414d31889ceebe9c75417e.png","month":4,"totalTerm":null,"term":null}]
     */

    public int total;
    public String time;
    public int status;
    public int returnDay;
    public List<TabAccountNewInfoBean> list = new ArrayList<>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }



    public static class TabAccountNewInfoBean implements Serializable {


        //前端配置需要
        public int headerTotal;
        public String headerTime;
        public Integer headerStatus = null;
        public Integer headerReturnDay = null;

        //是否是示例数据
        public boolean isAnalog = false;

        public int count;


        /**
         * id : 41432
         * userId : FD6F7AED9D184FF6B7F12BAD405FCED7
         * recordId : 2455941edec64daabcb00712c6b70b6e
         * billId : 020032ae69b3439c85c0f2051834d867
         * repayTime : 2018-05-13 00:00:00
         * type : 2
         * status : 3
         * title : 招商银行  8636
         * returnDay : -6
         * amount : 1604.12
         * overdueTotal : 1
         * lastOverdue : true
         * outBillDay : 0
         * billDate : 2018-04-25 00:00:00
         * logoUrl : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/credit_card_bank_logo/b60c3737fe414d31889ceebe9c75417e.png
         * month : 4
         * totalTerm : null
         * term : null
         */

        public int id;
        public String userId;
        public String recordId;
        public String billId;
        public String repayTime;
        public int type;
        public int status;
        public String title;
        public int returnDay;
        public double amount;
        public int overdueTotal;
        public boolean lastOverdue;
        public int outBillDay;
        public String billDate;
        public String logoUrl;
        public int month;
        public int totalTerm;
        public int term;
        public String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public int getOutBillDay() {
            return outBillDay;
        }

        public void setOutBillDay(int outBillDay) {
            this.outBillDay = outBillDay;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
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

        public int getTotalTerm() {
            return totalTerm;
        }

        public void setTotalTerm(int totalTerm) {
            this.totalTerm = totalTerm;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }
    }
}
