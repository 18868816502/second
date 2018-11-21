package com.beiwo.klyjaz.social.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.social.bean
 * @descripe
 * @time 2018/10/10 10:48
 */
public class CommentReplyBean implements Serializable {


    /**
     * userId : 0001a5adef07492ca562a17febbe1c7a
     * userName : null
     * userHeadUrl : null
     * praiseCount : 0
     * content : 这是语文
     * gmtCreate : 1535966126000
     * replyDtoList : [{"id":"2d9f761ca6ea4e60a655527745caee0b","userId":"ad67cbc40f5d4146bb98f49061843236","toUserId":"0001a5adef07492ca562a17febbe1c7a","userName":null,"toUserName":null,"content":"这是回复"},{"id":"aa5fe968afec11e8901400163e13c505","userId":"0000581aaebe411081e14a8bd0cd1c61","toUserId":"000058c81c5a4894b43f9409dbf3d284","userName":null,"toUserName":null,"content":"这是回复"},{"id":"ab01d1a3afec11e8901400163e13c505","userId":"0000581aaebe411081e14a8bd0cd1c61","toUserId":"000058c81c5a4894b43f9409dbf3d284","userName":null,"toUserName":null,"content":"这是回复"},{"id":"ab8e47e5afec11e8901400163e13c505","userId":"0000581aaebe411081e14a8bd0cd1c61","toUserId":"000058c81c5a4894b43f9409dbf3d284","userName":null,"toUserName":null,"content":"这是回复"},{"id":"ac0a6850afec11e8901400163e13c505","userId":"0000581aaebe411081e14a8bd0cd1c61","toUserId":"000058c81c5a4894b43f9409dbf3d284","userName":null,"toUserName":null,"content":"这是回复"}]
     * id : d85babf9cc4e417aa76ac3479c332ba1
     */

    private String userId;
    private String userName;
    private String userHeadUrl;
    private int praiseCount;
    private String content;
    private String gmtCreate;
    private String id;
    private List<ReplyDtoListBean> replyDtoList;

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

    public List<ReplyDtoListBean> getReplyDtoList() {
        return replyDtoList;
    }

    public void setReplyDtoList(List<ReplyDtoListBean> replyDtoList) {
        this.replyDtoList = replyDtoList;
    }

    public static class ReplyDtoListBean implements Serializable {
        /**
         * id : 2d9f761ca6ea4e60a655527745caee0b
         * userId : ad67cbc40f5d4146bb98f49061843236
         * toUserId : 0001a5adef07492ca562a17febbe1c7a
         * userName : null
         * toUserName : null
         * content : 这是回复
         */

        private String id;
        private String userId;
        private String toUserId;
        private String userName;
        private String toUserName;
        private String content;
        private String userHeadUrl;
        private int praiseCount;
        private String gmtCreate;
        private String selfId;
        private String replyId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getSelfId() {
            return selfId;
        }

        public void setSelfId(String selfId) {
            this.selfId = selfId;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }
    }
}
