package com.beihui.market.helper;


import android.content.Context;
import android.content.SharedPreferences;

import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;
import com.google.gson.Gson;

public class UserHelper {

    private static UserHelper sInstance;

    private Profile profile;

    private UserHelper(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserHelper", Context.MODE_PRIVATE);
        String cached = sp.getString("profile", null);
        if (cached != null) {
            String profileStr = cached;
            profile = new Gson().fromJson(profileStr, Profile.class);
        }
    }

    public static UserHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UserHelper.class) {
                if (sInstance == null) {
                    sInstance = new UserHelper(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public Profile getProfile() {
        return profile;
    }

    public void update(UserProfile profile) {

    }

    public void update(UserProfileAbstract profileAbstract) {

    }

    static class Profile {
        String id;
        String account;
        String userName;
        String headPortrait;
        String profession;
        String msgIsRead;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getMsgIsRead() {
            return msgIsRead;
        }

        public void setMsgIsRead(String msgIsRead) {
            this.msgIsRead = msgIsRead;
        }
    }
}
