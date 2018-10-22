package com.beiwo.klyjaz.social.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.bean
 * @descripe
 * @time 2018/10/16 20:01
 */
public class ForumInfoBean implements Serializable {


    /**
     * forum : {"userId":"ad67cbc40f5d4146bb98f49061843236","userName":"137****9292","userHeadUrl":null,"forumId":"fa49649293e64852abcc3d378b8ba554","title":"UUKEY测试","content":"第二次测试有以硅","picUrl":["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/912c7ae809284c1b99779ab9f772a8a9.png","http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/912c7ae809284c1b99779ab9f772a8a9.png"],"gmtCreate":"2018-10-16 15:08:50","createText":"2小时前","praiseCount":null,"commentCount":null,"isPraise":0}
     * commentDtoList : [{"userId":"0001a5adef07492ca562a17febbe1c7a","userName":"134****0597","userHeadUrl":null,"praiseCount":0,"content":"这是uukey","gmtCreate":"2018-10-16 17:29:20","replyDtoList":[],"id":"1"}]
     */

    private ForumBean forum;
    private List<CommentDtoListBean> commentDtoList;

    public ForumBean getForum() {
        return forum;
    }

    public void setForum(ForumBean forum) {
        this.forum = forum;
    }

    public List<CommentDtoListBean> getCommentDtoList() {
        return commentDtoList;
    }

    public void setCommentDtoList(List<CommentDtoListBean> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }

    public static class ForumBean implements Serializable{
        /**
         * userId : ad67cbc40f5d4146bb98f49061843236
         * userName : 137****9292
         * userHeadUrl : null
         * forumId : fa49649293e64852abcc3d378b8ba554
         * title : UUKEY测试
         * content : 第二次测试有以硅
         * picUrl : ["http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/912c7ae809284c1b99779ab9f772a8a9.png","http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Forum/912c7ae809284c1b99779ab9f772a8a9.png"]
         * gmtCreate : 2018-10-16 15:08:50
         * createText : 2小时前
         * praiseCount : null
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
        private int commentCount;
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
    }

    public static class CommentDtoListBean {
        /**
         * userId : 0001a5adef07492ca562a17febbe1c7a
         * userName : 134****0597
         * userHeadUrl : null
         * praiseCount : 0
         * content : 这是uukey
         * gmtCreate : 2018-10-16 17:29:20
         * replyDtoList : []
         * id : 1
         */

        private String userId;
        private String userName;
        private String userHeadUrl;
        private int praiseCount;
        private String content;
        private String gmtCreate;
        private String id;
        private List<?> replyDtoList;

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

        public int getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(int praiseCount) {
            this.praiseCount = praiseCount;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<?> getReplyDtoList() {
            return replyDtoList;
        }

        public void setReplyDtoList(List<?> replyDtoList) {
            this.replyDtoList = replyDtoList;
        }
    }
}
