package com.beiwo.qnejqaz.entity;

import java.io.Serializable;
import java.util.List;

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
public class CommentsTotal implements Serializable {
    private String praiseCutId;
    private String manageId;
    private String productId;
    private int status;
    private int commentCount;//总评论数
    private int goodCommentCount;//好评论数
    private float goodCommentRate;//好评率
    private int minCommentCount;//中评论数
    private int badCommentCount;//差评论数
    private List<Labels> labelList;//标签

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

    public int getMinCommentCount() {
        return minCommentCount;
    }

    public void setMinCommentCount(int minCommentCount) {
        this.minCommentCount = minCommentCount;
    }

    public int getBadCommentCount() {
        return badCommentCount;
    }

    public void setBadCommentCount(int badCommentCount) {
        this.badCommentCount = badCommentCount;
    }

    public List<Labels> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<Labels> labelList) {
        this.labelList = labelList;
    }
}