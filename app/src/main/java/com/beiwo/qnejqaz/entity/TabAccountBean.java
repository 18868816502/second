package com.beiwo.qnejqaz.entity;

import java.io.Serializable;
import java.util.List;



public class TabAccountBean implements Serializable{


    private List<OverdueListBean> overdueList;
    private List<OverdueListBean> unpayList;

    public List<OverdueListBean> getOverdueList() {
        return overdueList;
    }

    public void setOverdueList(List<OverdueListBean> overdueList) {
        this.overdueList = overdueList;
    }

    public List<OverdueListBean> getUnpayList() {
        return unpayList;
    }

    public void setUnpayList(List<OverdueListBean> unpayList) {
        this.unpayList = unpayList;
    }

    public static class OverdueListBean implements Serializable {
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

        private long id;
        private String userId;
        private String recordId;
        private String billId;
        private String repayTime;
        private int type = 0;
        private int status;
        private String title;
        private int returnDay;
        private double amount;
        private int overdueTotal;
        private boolean lastOverdue;
        private int outBillDay;
        private String billDate;
        private String logoUrl;
        private int month;
        private int totalTerm;
        private int term;

        public boolean isShow = false;

        public boolean isAnalog = false;

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