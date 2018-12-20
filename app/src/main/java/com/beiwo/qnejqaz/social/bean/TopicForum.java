package com.beiwo.qnejqaz.social.bean;

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
 * @date: 2018/12/4
 */
public class TopicForum implements Serializable {
    /**
     * userId : 19e86f82c2b24f958ae03c6c010b842e
     * userName : 136****1224
     * userHeadUrl : http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg
     * forumId : 33e39652a85f4a708c7bfd19c2fe3488
     * title : 测试
     * content : 测试数据。不要关心
     * picUrl : ["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png","http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png"]
     * gmtCreate : 2018-09-03 16:25:02
     * createText : 2天前
     * praiseCount : 1
     * commentCount : 234
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