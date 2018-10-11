package com.beihui.market.social.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.entity
 * @descripe
 * @time 2018/9/19 10:13
 */
public class SocialTopicBean implements Serializable{


    /**
     * forumActive : null
     * forum : [{"userId":"19e86f82c2b24f958ae03c6c010b842e","userName":"137****9292","userHeadUrl":"http://www.baidu.com","forumId":"33e39652a85f4a708c7bfd19c2fe3488","title":"测试","content":"测试数据。不要关心","picUrl":["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png","http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/573ee976fa7148b2ab6ba16edfd48c94.png"],"gmtCreate":null,"createText":null,"praiseCount":3,"commentCount":null,"isPraise":0}]
     * topic : null
     * daynamicCount : null
     */

    private String forumActive;
    private String topic;
    private String daynamicCount;
    private List<ForumBean> forum;

    public String getForumActive() {
        return forumActive;
    }

    public void setForumActive(String forumActive) {
        this.forumActive = forumActive;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
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

    public static class ForumBean implements Serializable{
        /**
         * userId : 19e86f82c2b24f958ae03c6c010b842e
         * userName : 137****9292
         * userHeadUrl : http://www.baidu.com
         * forumId : 33e39652a85f4a708c7bfd19c2fe3488
         * title : 测试
         * content : 测试数据。不要关心
         * picUrl : ["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png","http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/573ee976fa7148b2ab6ba16edfd48c94.png"]
         * gmtCreate : null
         * createText : null
         * praiseCount : 3
         * commentCount : null
         * isPraise : 0
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
        private String commentCount;
        private int isPraise;
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
    }
}
