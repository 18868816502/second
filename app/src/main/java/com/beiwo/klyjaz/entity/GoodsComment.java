package com.beiwo.klyjaz.entity;

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
public class GoodsComment implements Serializable {
    private String praiseId;
    private String cutId;
    private String userName;
    private int type;
    private Object loanStatus;
    private int dummyUser;
    private int status;
    private String headUrl;
    private String imageUrl;//评论图片 逗号分隔
    private String content;
    private String showText;
    private List<Labels> labelList;

    public String getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(String praiseId) {
        this.praiseId = praiseId;
    }

    public String getCutId() {
        return cutId;
    }

    public void setCutId(String cutId) {
        this.cutId = cutId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(Object loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(int dummyUser) {
        this.dummyUser = dummyUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public List<Labels> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<Labels> labelList) {
        this.labelList = labelList;
    }
}