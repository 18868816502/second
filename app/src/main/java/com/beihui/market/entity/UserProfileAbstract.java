package com.beihui.market.entity;


public class UserProfileAbstract {
    private String id;
    private String userName;
    private String msgIsRead;
    private String headPortrait;
    private String account = "11111111111";
    private boolean isNewUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsgIsRead() {
        return msgIsRead;
    }

    public void setMsgIsRead(String msgIsRead) {
        this.msgIsRead = msgIsRead;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }
}
