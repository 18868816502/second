package com.beihui.market.entity;

import java.util.List;

/**
 * @name loanmarket
 * @class name：com.beihui.market.entity
 * @class describe
 * @author chenguoguo
 * @time 2018/9/12 11:57
 */
public class UserArticleBean {


    /**
     * userId : 19e86f82c2b24f958ae03c6c010b842e
     * userName : 136****1224
     * userHeadUrl : http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg
     * forumId : adasdasdada
     * title : 测试
     * content : 测试数据。不要关心
     * picUrl : []
     * gmtCreate : 2018-09-03 16:25:02
     * createText : 2天前
     * praiseCount : 0
     * commentCount : 4
     */

    private String userId;
    private String userName;
    private String userHeadUrl;
    private String forumId;
    private String title;
    private String content;
    private String gmtCreate;
    private String createText;
    private int praiseCount;
    private int commentCount;
    private List<String> picUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
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

    public String getCreateText() {
        return createText;
    }

    public void setCreateText(String createText) {
        this.createText = createText;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }
}
