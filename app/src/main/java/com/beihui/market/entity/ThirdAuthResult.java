package com.beihui.market.entity;


import java.util.List;

public class ThirdAuthResult {

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

        private String id;
        private String dueTimeText;
        private int quickCommend;
        private String borrowingHighText;
        private String feature;
        private String interestLowText;
        private int successCount;
        private int productSign;
        private String logoUrl;
        private String productName;
        private String interestTimeText;
        private int status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDueTimeText() {
            return dueTimeText;
        }

        public void setDueTimeText(String dueTimeText) {
            this.dueTimeText = dueTimeText;
        }

        public int getQuickCommend() {
            return quickCommend;
        }

        public void setQuickCommend(int quickCommend) {
            this.quickCommend = quickCommend;
        }

        public String getBorrowingHighText() {
            return borrowingHighText;
        }

        public void setBorrowingHighText(String borrowingHighText) {
            this.borrowingHighText = borrowingHighText;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getInterestLowText() {
            return interestLowText;
        }

        public void setInterestLowText(String interestLowText) {
            this.interestLowText = interestLowText;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public int getProductSign() {
            return productSign;
        }

        public void setProductSign(int productSign) {
            this.productSign = productSign;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getInterestTimeText() {
            return interestTimeText;
        }

        public void setInterestTimeText(String interestTimeText) {
            this.interestTimeText = interestTimeText;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
