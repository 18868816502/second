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
     * forumActive : {"imgUrl":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Popup/d1ea78c79786419abaa39d53911d050b.png","activeUrl":"http://baidu.com","activeName":"测试活动"}
     * forum : [{"userId":"19e86f82c2b24f958ae03c6c010b842e","userName":"136****1224","userHeadUrl":"http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","forumId":"33e39652a85f4a708c7bfd19c2fe3488","title":"测试","content":"测试数据。不要关心","picUrl":["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png"],"gmtCreate":null,"createText":null,"praiseCount":1,"commentCount":null},{"userId":"19e86f82c2b24f958ae03c6c010b842e","userName":"136****1224","userHeadUrl":"http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","forumId":"8d2e5368b03011e8901400163e13c505","title":"测试","content":"测试数据。不要关心","picUrl":[],"gmtCreate":null,"createText":null,"praiseCount":0,"commentCount":null},{"userId":"19e86f82c2b24f958ae03c6c010b842e","userName":"136****1224","userHeadUrl":"http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","forumId":"8db9c6e4b03011e8901400163e13c505","title":"测试","content":"测试数据。不要关心","picUrl":[],"gmtCreate":null,"createText":null,"praiseCount":0,"commentCount":null},{"userId":"19e86f82c2b24f958ae03c6c010b842e","userName":"136****1224","userHeadUrl":"http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","forumId":"8e112010b03011e8901400163e13c505","title":"测试","content":"数据。不要关心","picUrl":[],"gmtCreate":null,"createText":null,"praiseCount":0,"commentCount":null}]
     * topic : {"userHeadUrl":["http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg"],"topicTitle":"adad","topicContent":"adadadadad","onLookCount":0,"location":5,"topicId":"927d8dd3aff011e8901400163e13c505"}
     */

    private ForumActiveBean forumActive;
    private TopicBean topic;
    private List<ForumBean> forum;

    public ForumActiveBean getForumActive() {
        return forumActive;
    }

    public void setForumActive(ForumActiveBean forumActive) {
        this.forumActive = forumActive;
    }

    public TopicBean getTopic() {
        return topic;
    }

    public void setTopic(TopicBean topic) {
        this.topic = topic;
    }

    public List<ForumBean> getForum() {
        return forum;
    }

    public void setForum(List<ForumBean> forum) {
        this.forum = forum;
    }

    public static class ForumActiveBean {
        /**
         * imgUrl : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Popup/d1ea78c79786419abaa39d53911d050b.png
         * activeUrl : http://baidu.com
         * activeName : 测试活动
         */

        private String imgUrl;
        private String activeUrl;
        private String activeName;

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
    }

    public static class TopicBean {
        /**
         * userHeadUrl : ["http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg","http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg"]
         * topicTitle : adad
         * topicContent : adadadadad
         * onLookCount : 0
         * location : 5
         * topicId : 927d8dd3aff011e8901400163e13c505
         */

        private String topicTitle;
        private String topicContent;
        private int onLookCount;
        private int location;
        private String topicId;
        private List<String> userHeadUrl;

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

        public List<String> getUserHeadUrl() {
            return userHeadUrl;
        }

        public void setUserHeadUrl(List<String> userHeadUrl) {
            this.userHeadUrl = userHeadUrl;
        }
    }

    public static class ForumBean implements Serializable {
        /**
         * userId : 19e86f82c2b24f958ae03c6c010b842e
         * userName : 136****1224
         * userHeadUrl : http://axgj.oss-cn-hangzhou.aliyuncs.com/UserHead/83d7ba29c26842fe9992027daa7e268b.jpg
         * forumId : 33e39652a85f4a708c7bfd19c2fe3488
         * title : 测试
         * content : 测试数据。不要关心
         * picUrl : ["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/5176ecaaecbb4beaab8b931eef342ca7.png"]
         * gmtCreate : null
         * createText : null
         * praiseCount : 1
         * commentCount : null
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

        public List<String> getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(List<String> picUrl) {
            this.picUrl = picUrl;
        }
    }
}
