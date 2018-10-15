package com.beiwo.klyjaz.entity;


import java.util.HashMap;
import java.util.List;

public class DebtCalendar {

    private double returnedAmount;
    private double stayReturnedAmount;
    private double payableAmount;
    private List<DetailBean> detail;

    private HashMap<String, Integer> returnHash;
    private HashMap<String, Integer> unReturnHash;

    public double getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(double returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public double getStayReturnedAmount() {
        return stayReturnedAmount;
    }

    public void setStayReturnedAmount(double stayReturnedAmount) {
        this.stayReturnedAmount = stayReturnedAmount;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public HashMap<String, Integer> getReturnHash() {
        return returnHash;
    }

    public void setReturnHash(HashMap<String, Integer> returnHash) {
        this.returnHash = returnHash;
    }

    public HashMap<String, Integer> getUnReturnHash() {
        return unReturnHash;
    }

    public void setUnReturnHash(HashMap<String, Integer> unReturnHash) {
        this.unReturnHash = unReturnHash;
    }

    public static class DetailBean {

        private String id;
        private String recordId;
        private int termNo;
        private String termRepayDate;
        private double termPayableAmount;
        private int status;
        private String gmtCreate;
        private String gmtModify;
        private String userId;
        private String channelId;
        private String channelName;
        private String projectName;
        private int repayType;
        private double capital;
        private double interest;
        private double payableAmount;
        private double returnedAmount;
        private double stayReturnedAmount;
        private double rate;
        private String startDate;
        private int termType;
        private int term;
        private String remark;
        private int returnDay;
        private String logo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public int getTermNo() {
            return termNo;
        }

        public void setTermNo(int termNo) {
            this.termNo = termNo;
        }

        public String getTermRepayDate() {
            return termRepayDate;
        }

        public void setTermRepayDate(String termRepayDate) {
            this.termRepayDate = termRepayDate;
        }

        public double getTermPayableAmount() {
            return termPayableAmount;
        }

        public void setTermPayableAmount(double termPayableAmount) {
            this.termPayableAmount = termPayableAmount;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public int getRepayType() {
            return repayType;
        }

        public void setRepayType(int repayType) {
            this.repayType = repayType;
        }

        public double getCapital() {
            return capital;
        }

        public void setCapital(double capital) {
            this.capital = capital;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }

        public double getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(double payableAmount) {
            this.payableAmount = payableAmount;
        }

        public double getReturnedAmount() {
            return returnedAmount;
        }

        public void setReturnedAmount(double returnedAmount) {
            this.returnedAmount = returnedAmount;
        }

        public double getStayReturnedAmount() {
            return stayReturnedAmount;
        }

        public void setStayReturnedAmount(double stayReturnedAmount) {
            this.stayReturnedAmount = stayReturnedAmount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }


        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public int getTermType() {
            return termType;
        }

        public void setTermType(int termType) {
            this.termType = termType;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(int returnDay) {
            this.returnDay = returnDay;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
