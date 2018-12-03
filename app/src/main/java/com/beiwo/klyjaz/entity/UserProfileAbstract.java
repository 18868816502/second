package com.beiwo.klyjaz.entity;


import com.google.gson.Gson;

public class UserProfileAbstract {
    private String id;
    private String userName;
    private String msgIsRead;
    private String headPortrait;
    private String account = "11111111111";
    private boolean newUser;

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
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}