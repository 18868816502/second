package com.beihui.market.entity;


import java.util.List;

public class LoanBill {

    private int total;
    private List<Row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public static class Row {

        private int billType;
        private String channelName;
        private String bankName;
        private String logo;
        private double amount;
        private String billName;
        private String cardNums;
        private int returnDay;
        private int outBillDay;
        private int billDay;
        private int dueDay;
        private String billId;
        private String recordId;
        private int status;
        private int billFlag;
        private String billDate;
        private String repayDate;
        private String createDate;
        private String updateDate;
        private int cardSource;
        private int hide;

        public int getBillType() {
            return billType;
        }

        public void setBillType(int billType) {
            this.billType = billType;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getBillName() {
            return billName;
        }

        public void setBillName(String billName) {
            this.billName = billName;
        }

        public String getCardNums() {
            return cardNums;
        }

        public void setCardNums(String cardNums) {
            this.cardNums = cardNums;
        }

        public int getReturnDay() {
            return returnDay;
        }

        public void setReturnDay(int returnDay) {
            this.returnDay = returnDay;
        }

        public int getOutBillDay() {
            return outBillDay;
        }

        public void setOutBillDay(int outBillDay) {
            this.outBillDay = outBillDay;
        }

        public int getBillDay() {
            return billDay;
        }

        public void setBillDay(int billDay) {
            this.billDay = billDay;
        }

        public int getDueDay() {
            return dueDay;
        }

        public void setDueDay(int dueDay) {
            this.dueDay = dueDay;
        }

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getBillFlag() {
            return billFlag;
        }

        public void setBillFlag(int billFlag) {
            this.billFlag = billFlag;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public int getCardSource() {
            return cardSource;
        }

        public void setCardSource(int cardSource) {
            this.cardSource = cardSource;
        }

        public int getHide() {
            return hide;
        }

        public void setHide(int hide) {
            this.hide = hide;
        }
    }
}
