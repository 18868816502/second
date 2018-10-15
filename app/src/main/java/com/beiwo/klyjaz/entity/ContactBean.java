package com.beiwo.klyjaz.entity;

/**
 * Copyright: dondo (C)2018
 * FileName: ContactBean
 * Author: jiang
 * Create on: 2018/8/13 11:28
 * Description:
 */
public class ContactBean {

    private String displayName;
    private String phoneNum;
    private String photo;
    private int contactId;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
