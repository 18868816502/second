package com.beihui.market.entity;


public class UserProfile {

    private String account;
    private String userName;
    private String headPortrait;
    private String profession;

    //绑定手机号 为空表示未绑定
    public String bingPhone;
    //绑定微信号 为空表示未绑定
    public String wxUnionId;
    //版本号
    public String version;

    public String getBingPhone() {
        return bingPhone;
    }

    public void setBingPhone(String bingPhone) {
        this.bingPhone = bingPhone;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
