package com.beiwo.qnejqaz.entity;

import java.io.Serializable;

public class Product implements Serializable {
    public String explains;
    public String id;
    public String productName;
    public String logoUrl;
    private String interestLowText;
    private int successCount;
    public String borrowingHighText;
    public String borrowingLowText;
    public String interestTimeText;

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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
}