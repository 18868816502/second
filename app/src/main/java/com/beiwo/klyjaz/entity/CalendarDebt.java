package com.beiwo.klyjaz.entity;


import java.util.List;

public class CalendarDebt {

    private double returnedAmount;
    private double stayReturnedAmount;
    private double payableAmount;
    private List<DetailBean> detail;

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

    public static class DetailBean {

        private double amount;
        private int billSource;
        private int billType;
        private String userId;
        private int returnDay;
        private String recordId;
        private String billName;
        private String billId;
        private String topic;
        private String logo;
        private String repayDate;
        private String billCycle;
        private int status;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getBillSource() {
            return billSource;
        }

        public void setBillSource(int billSource) {
            this.billSource = billSource;
        }

        public int getBillType() {
            return billType;
        }

        public void setBillType(int billType) {
            this.billType = billType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(int returnDay) {
            this.returnDay = returnDay;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getBillName() {
            return billName;
        }

        public void setBillName(String billName) {
            this.billName = billName;
        }

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getBillCycle() {
            return billCycle;
        }

        public void setBillCycle(String billCycle) {
            this.billCycle = billCycle;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
