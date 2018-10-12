package com.beihui.market.jjd.bean;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/14
 */

public class CashUserInfo {
    private CashUser cashUser;
    private CashContact cashContact;

    public CashUser getCashUser() {
        return cashUser;
    }

    public void setCashUser(CashUser cashUser) {
        this.cashUser = cashUser;
    }

    public CashContact getCashContact() {
        return cashContact;
    }

    public void setCashContact(CashContact cashContact) {
        this.cashContact = cashContact;
    }

    private static class CashUser {
        private String id;
        private String userId;
        private String userName;
        private String idCard;
        private long gmtCreate;

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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }
    }

    private static class CashContact {
        private String id;
        private String userId;
        private String userContact;
        private String userRelate;
        private String mobileNum;
        private long gmtCreate;

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

        public String getUserContact() {
            return userContact;
        }

        public void setUserContact(String userContact) {
            this.userContact = userContact;
        }

        public String getUserRelate() {
            return userRelate;
        }

        public void setUserRelate(String userRelate) {
            this.userRelate = userRelate;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }
    }
}