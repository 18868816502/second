package com.beiwo.qnejqaz.social.bean;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.social.bean
 * @descripe
 * @time 2018/10/10 13:11
 */
public class PraiseBean {
    /**
     * fansCount : 0
     * praiseCount : 0
     * commentCount : 0
     */

    private int fansCount;
    private int praiseCount;
    private int commentCount;

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
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
}