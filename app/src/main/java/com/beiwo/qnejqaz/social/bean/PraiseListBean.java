package com.beiwo.qnejqaz.social.bean;

import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beihui.market.social.bean
 * @descripe
 * @time 2018/10/13 11:54
 */
public class PraiseListBean {


    /**
     * id : 5decebbf6dc146cfb780c1051d21e4ee
     * userId : 7308f00aada545cc894c4e43aecd0cfd
     * praiseType : 0
     * forumReplayId : 1571e3ffd46245f1874382acb38938a3
     * gmtCreate : 2018-10-15 18:00:29
     * gmtModify : 2018-10-15 18:00:29
     * forumTitle : 今天你们借了多少
     * commentContent : null
     * imageList : [{"id":null,"imgUrl":"http://static2.kaolabill.com/Forum/e239d0f1f7554f4eaf59b9a87471cd4e.png","imgType":null,"forumTopicId":null,"sortKey":0,"gmtCreate":null,"gmtModify":null}]
     * userName : Tyrael
     * headPortrait : https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI6hasiaL1x3SfjlEZ6CDI6kzVmLiac0bDB0u6s1QMOlhAq3c6P0Gz0kRiaaEOvouLbCFckwcuNDjO3w/132
     * forumId : 1571e3ffd46245f1874382acb38938a3
     * createText : 1天前
     */

    private String id;
    private String userId;
    private String praiseType;
    private String forumReplayId;
    private String gmtCreate;
    private String gmtModify;
    private String forumTitle;
    private String commentContent;
    private String userName;
    private String headPortrait;
    private String forumId;
    private String createText;
    private String commentType;
    private List<ImageListBean> imageList;

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

    public String getPraiseType() {
        return praiseType;
    }

    public void setPraiseType(String praiseType) {
        this.praiseType = praiseType;
    }

    public String getForumReplayId() {
        return forumReplayId;
    }

    public void setForumReplayId(String forumReplayId) {
        this.forumReplayId = forumReplayId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getCreateText() {
        return createText;
    }

    public void setCreateText(String createText) {
        this.createText = createText;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public List<ImageListBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageListBean> imageList) {
        this.imageList = imageList;
    }

    public static class ImageListBean {
        /**
         * id : null
         * imgUrl : http://static2.kaolabill.com/Forum/e239d0f1f7554f4eaf59b9a87471cd4e.png
         * imgType : null
         * forumTopicId : null
         * sortKey : 0
         * gmtCreate : null
         * gmtModify : null
         */

        private Object id;
        private String imgUrl;
        private Object imgType;
        private Object forumTopicId;
        private int sortKey;
        private Object gmtCreate;
        private Object gmtModify;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getImgType() {
            return imgType;
        }

        public void setImgType(Object imgType) {
            this.imgType = imgType;
        }

        public Object getForumTopicId() {
            return forumTopicId;
        }

        public void setForumTopicId(Object forumTopicId) {
            this.forumTopicId = forumTopicId;
        }

        public int getSortKey() {
            return sortKey;
        }

        public void setSortKey(int sortKey) {
            this.sortKey = sortKey;
        }

        public Object getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(Object gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public Object getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(Object gmtModify) {
            this.gmtModify = gmtModify;
        }
    }
}
