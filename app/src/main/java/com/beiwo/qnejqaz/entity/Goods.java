package com.beiwo.qnejqaz.entity;

import java.io.Serializable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/11
 */
public class Goods implements Serializable {
    private String praiseCutId;//好评口子ID
    private String manageId;//口子产品ID
    private String productId;//产品ID
    private int status;
    private int commentCount;
    private int goodCommentCount;
    private float goodCommentRate;
    private String gmtOnline;
    private String gmtCreate;
    private String gmtModify;
    private String name;
    private String logo;
    private String term;
    private String minQuota;
    private String maxQuota;

    public String getPraiseCutId() {
        return praiseCutId;
    }

    public void setPraiseCutId(String praiseCutId) {
        this.praiseCutId = praiseCutId;
    }

    public String getManageId() {
        return manageId;
    }

    public void setManageId(String manageId) {
        this.manageId = manageId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getGoodCommentCount() {
        return goodCommentCount;
    }

    public void setGoodCommentCount(int goodCommentCount) {
        this.goodCommentCount = goodCommentCount;
    }

    public float getGoodCommentRate() {
        return goodCommentRate;
    }

    public void setGoodCommentRate(float goodCommentRate) {
        this.goodCommentRate = goodCommentRate;
    }

    public String getGmtOnline() {
        return gmtOnline;
    }

    public void setGmtOnline(String gmtOnline) {
        this.gmtOnline = gmtOnline;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getMinQuota() {
        return minQuota;
    }

    public void setMinQuota(String minQuota) {
        this.minQuota = minQuota;
    }

    public String getMaxQuota() {
        return maxQuota;
    }

    public void setMaxQuota(String maxQuota) {
        this.maxQuota = maxQuota;
    }
}