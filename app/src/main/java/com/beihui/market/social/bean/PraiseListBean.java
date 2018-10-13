package com.beihui.market.social.bean;

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
     * userId : 9b8eb142ec554c2b8ccd70498b7f3c38
     * praiseType : 0
     * forumTitle : 动态标题
     * commentContent : 评论回复内容
     * imageList : [{"imgUrl":"http://www.baidu.com"}]
     * userName : 昵称
     * headPortrait : http://www.baidu.com
     * createText : 刚刚
     */

    private String userId;
    private String praiseType;
    private String commentType;
    private String forumTitle;
    private String commentContent;
    private String userName;
    private String headPortrait;
    private String createText;
    private List<ImageListBean> imageList;

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

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
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

    public String getCreateText() {
        return createText;
    }

    public void setCreateText(String createText) {
        this.createText = createText;
    }

    public List<ImageListBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageListBean> imageList) {
        this.imageList = imageList;
    }

    public static class ImageListBean {
        /**
         * imgUrl : http://www.baidu.com
         */

        private String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
