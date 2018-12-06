package com.beiwo.klyjaz.social.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

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
 * @date: 2018/12/6
 */
public class ForumBean implements Serializable, MultiItemEntity {
    //文章
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
    private int isPraise;
    private List<String> picUrl;
    //话题
    private List<String> topicUserHeadUrl;
    private String topicTitle;
    private String topicContent;
    private int onLookCount;
    private int location;
    private String topicId;
    //活动
    private String imgUrl;
    private String activeUrl;
    private String activeName;
    //下款推荐
    private int loadProductNum;
    //额外定义的类型
    public static final int TYPE_ARTICLE = 0;
    public static final int TYPE_EVENT = 1;
    public static final int TYPE_GOODS = 2;
    public static final int TYPE_TOPIC = 3;
    private int itemType;

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

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }

    //话题
    public List<String> getTopicUserHeadUrl() {
        return topicUserHeadUrl;
    }

    public void setTopicUserHeadUrl(List<String> topicUserHeadUrl) {
        this.topicUserHeadUrl = topicUserHeadUrl;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public int getOnLookCount() {
        return onLookCount;
    }

    public void setOnLookCount(int onLookCount) {
        this.onLookCount = onLookCount;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    //活动
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getActiveUrl() {
        return activeUrl;
    }

    public void setActiveUrl(String activeUrl) {
        this.activeUrl = activeUrl;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    //下款推荐
    public int getLoadProductNum() {
        return loadProductNum;
    }

    public void setLoadProductNum(int loadProductNum) {
        this.loadProductNum = loadProductNum;
    }

    @Override
    public int getItemType() {
        if (!TextUtils.isEmpty(forumId)) return TYPE_ARTICLE;
        else if (!TextUtils.isEmpty(topicId)) return TYPE_TOPIC;
        else if (!TextUtils.isEmpty(activeUrl) && !TextUtils.isEmpty(activeName))
            return TYPE_EVENT;
        else if (loadProductNum > 0) return TYPE_GOODS;
        else return -1;
    }
}
