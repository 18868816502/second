package com.beihui.market.social.bean;

import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beihui.market.social.bean
 * @descripe
 * @time 2018/10/12 16:10
 */
public class DraftsBean {


    /**
     * userId : 19e86f82c2b24f958ae03c6c010b842e
     * forumId : 8d2e5368b03011e8901400163e13c505
     * title : 测试
     * content : 测试数据。不要关心
     * picUrl : ["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/41c341558c1c4be6b05dac93b7f11381.png"]
     * gmtCreate : null
     * isFollow : null
     * forumStatus : 2
     * onlineStatus : null
     * forumAuditContent : 拒绝
     */

    private String userId;
    private String forumId;
    private String title;
    private String content;
    private String gmtCreate;
    private String isFollow;
    private String forumStatus;
    private String onlineStatus;
    private String forumAuditContent;
    private List<String> picUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public String getForumStatus() {
        return forumStatus;
    }

    public void setForumStatus(String forumStatus) {
        this.forumStatus = forumStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getForumAuditContent() {
        return forumAuditContent;
    }

    public void setForumAuditContent(String forumAuditContent) {
        this.forumAuditContent = forumAuditContent;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }
}
