package com.beiwo.qnejqaz.entity;

/**
 * @name loanmarket
 * @class name：com.beihui.market.entity
 * @class describe
 * @anthor chenguoguo
 * @time 2018/9/12 11:30
 */
public class UserInfoBean {


    /**
     * userName : Wzh
     * account : 18550424180
     * userId : 000058c81c5a4894b43f9409dbf3d284
     * headPortrait : https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIxAIXQL4JxsosvBgqWFibQPSWWRTkQ8j0fwdiaPcfOFsr67VVfEWicSLwyTlNU4r9GQhA9ibsgFibaNzw/132
     * introduce : null
     * sex : 0
     * forumCount : 0
     * followerCount : 0
     * fansCount : 0
     * praiseCount : 0
     */

    private String userName;
    private String account;
    private String userId;
    private String headPortrait;
    private String introduce;
    private int sex;
    private int forumCount;
    private int followerCount;
    private int fansCount;
    private int praiseCount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getForumCount() {
        return forumCount;
    }

    public void setForumCount(int forumCount) {
        this.forumCount = forumCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

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
}
