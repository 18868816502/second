package com.beihui.market.entity;


import java.util.List;

public class LoanProductDetail {

    private Base base;
    private List<Explain> explain;

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public List<Explain> getExplain() {
        return explain;
    }

    public void setExplain(List<Explain> explain) {
        this.explain = explain;
    }

    public static class Base {
        private String dueTimeText;
        private int quickCommend;
        private String feature;
        private String interestLow;
        private String logo;
        private int successCount;
        private String borrowing;
        private String id;
        private int productSign;
        private String interestTimeTypeText;
        private String productName;
        private String url;
        private String orientCareerText;
        private String successCountPointText;
        private String repayMethodText;
        private String fastestLoanTimeText;
        private String mortgageMethodText;

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

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getInterestLow() {
            return interestLow;
        }

        public void setInterestLow(String interestLow) {
            this.interestLow = interestLow;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public String getBorrowing() {
            return borrowing;
        }

        public void setBorrowing(String borrowing) {
            this.borrowing = borrowing;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getProductSign() {
            return productSign;
        }

        public void setProductSign(int productSign) {
            this.productSign = productSign;
        }

        public String getInterestTimeTypeText() {
            return interestTimeTypeText;
        }

        public void setInterestTimeTypeText(String interestTimeTypeText) {
            this.interestTimeTypeText = interestTimeTypeText;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOrientCareerText() {
            return orientCareerText;
        }

        public void setOrientCareerText(String orientCareerText) {
            this.orientCareerText = orientCareerText;
        }

        public String getSuccessCountPointText() {
            return successCountPointText;
        }

        public void setSuccessCountPointText(String successCountPointText) {
            this.successCountPointText = successCountPointText;
        }

        public String getRepayMethodText() {
            return repayMethodText;
        }

        public void setRepayMethodText(String repayMethodText) {
            this.repayMethodText = repayMethodText;
        }

        public String getFastestLoanTimeText() {
            return fastestLoanTimeText;
        }

        public void setFastestLoanTimeText(String fastestLoanTimeText) {
            this.fastestLoanTimeText = fastestLoanTimeText;
        }

        public String getMortgageMethodText() {
            return mortgageMethodText;
        }

        public void setMortgageMethodText(String mortgageMethodText) {
            this.mortgageMethodText = mortgageMethodText;
        }
    }

    public static class Explain {
        private String explainName;
        private String explainContent;
        private int explainType;

        public String getExplainName() {
            return explainName;
        }

        public void setExplainName(String explainName) {
            this.explainName = explainName;
        }

        public String getExplainContent() {
            return explainContent;
        }

        public void setExplainContent(String explainContent) {
            this.explainContent = explainContent;
        }

        public int getExplainType() {
            return explainType;
        }

        public void setExplainType(int explainType) {
            this.explainType = explainType;
        }
    }
}