package com.beiwo.klyjaz.social.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.entity
 * @descripe
 * @time 2018/9/19 10:13
 */
public class SocialTopicBean implements Serializable {
    /**
     * forumActive : null
     * forum : [{"userId":"19e86f82c2b24f958ae03c6c010b842e","userName":"137****9292","userHeadUrl":"http://www.baidu.com","forumId":"33e39652a85f4a708c7bfd19c2fe3488","title":"测试","content":"测试数据。不要关心","picUrl":["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png","http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/573ee976fa7148b2ab6ba16edfd48c94.png"],"gmtCreate":null,"createText":null,"praiseCount":3,"commentCount":null,"isPraise":0}]
     * topic : null
     * daynamicCount : 54
     */

    private ForumBean forumActive;
    private ForumBean topic;
    private String daynamicCount;
    private List<ForumBean> forum;

    public ForumBean getForumActive() {
        return forumActive;
    }

    public void setForumActive(ForumBean forumActive) {
        this.forumActive = forumActive;
    }

    public ForumBean getTopic() {
        return topic;
    }

    public void setTopic(ForumBean topic) {
        this.topic = topic;
    }

    public String getDaynamicCount() {
        return daynamicCount;
    }

    public void setDaynamicCount(String daynamicCount) {
        this.daynamicCount = daynamicCount;
    }

    public List<ForumBean> getForum() {
        return forum;
    }

    public void setForum(List<ForumBean> forum) {
        this.forum = forum;
    }

    public static class ForumBean implements Serializable, MultiItemEntity {
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
        private String commentCount;
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

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
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

        @Override
        public int getItemType() {
            if (!TextUtils.isEmpty(forumId)) return TYPE_ARTICLE;
            else if (!TextUtils.isEmpty(topicId)) return TYPE_TOPIC;
            else if (!TextUtils.isEmpty(activeUrl) && !TextUtils.isEmpty(activeName))
                return TYPE_EVENT;
            else return TYPE_GOODS;
        }
    }
}