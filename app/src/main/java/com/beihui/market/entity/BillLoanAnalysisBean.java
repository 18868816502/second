package com.beihui.market.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/22.
 */

public class BillLoanAnalysisBean implements Serializable {


    private String startTime;
    private String endTime;
    private int overAmount = 0;
    private int returnAmount = 0;
    private double unpayAmount = 0;

    /**
     * 自定义
     */
    public String timeTitleTop;
    public String timeTitleBottom;

    private List<ListBean> list = new ArrayList<>();

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

    public int getOverAmount() {
        return overAmount;
    }

    public void setOverAmount(int overAmount) {
        this.overAmount = overAmount;
    }

    public int getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(int returnAmount) {
        this.returnAmount = returnAmount;
    }

    public double getUnpayAmount() {
        return unpayAmount;
    }

    public void setUnpayAmount(double unpayAmount) {
        this.unpayAmount = unpayAmount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * id : 58404
         * userId : FD6F7AED9D184FF6B7F12BAD405FCED7
         * recordId : 1d804a6e0fe045c4adf48484257be934
         * billId : 42add8aac1cf4420959847a9f5220892
         * repayTime : 2018-05-27 00:00:00
         * type : 2
         * status : 1
         * amount : 23.5
         * logoUrl : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/credit_card_bank_logo/b60c3737fe414d31889ceebe9c75417e.png
         * month : 5
         * totalTerm : null
         * term : null
         * title : 招商银行  4933
         */

        private int id;
        private String userId;
        private String recordId;
        private String billId;
        private String repayTime;
        private int type;
        private int status;
        private double amount;
        private String logoUrl;
        private int month;
        private int totalTerm;
        private int term;
        private String title;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
