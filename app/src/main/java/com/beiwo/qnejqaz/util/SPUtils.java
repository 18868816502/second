package com.beiwo.qnejqaz.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.helper.DataHelper;

public class SPUtils {
    private static final String TAG = "Info";
    private static final Context context = App.getInstance();

    public static long getLastAdShowTime() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getLong("lastAdShowTime", 0);
    }

    public static void setLastActName(String name) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("last_act_name", name);
        editor.apply();
    }

    public static String getLastActName() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("last_act_name", "");
    }

    public static void setLastAdShowTime(long showTime) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("lastAdShowTime", showTime);
        editor.apply();
    }

    public static String getLastInstalledVersion() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastInstalledVersion", null);
    }

    public static void setLastInstalledVersion(String version) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastInstalledVersion", version);
        editor.apply();
    }

    public static String getLastNoticeId() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastNoticeId", null);
    }

    public static void setLastNoticeId(String lastNoticeId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastNoticeId", lastNoticeId);
        editor.apply();
    }

    public static boolean getFirstInstall() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean(DataHelper.ID_FIRST_INSTALL, true);
    }

    public static void setFirstInstall(boolean first) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DataHelper.ID_FIRST_INSTALL, first);
        editor.apply();
    }


    public static boolean getNoticeClosed() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("noticeClose", false);
    }

    public static void setNoticeClosed(boolean closed) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("noticeClose", closed);
        editor.apply();
    }

    public static boolean getCheckPermission() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("checkPermission", false);
    }

    public static void setCheckPermission(boolean check) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("checkPermission", check);
        editor.apply();
    }

    public static boolean getWechatSurpriseClicked() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("wechatSurpriseClicked", false);
    }

    public static void setWechatSurpriseClicked(boolean clicked) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("wechatSurpriseClicked", clicked);
        editor.apply();
    }

    public static void setCacheUserId(String userId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cacheUserId", userId);
        editor.apply();
    }

    public static String getCacheUserId() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("cacheUserId", null);
    }

    public static boolean getTabAccountGuideShowed() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("tabAccountGuideShowed", false);
    }

    public static void setTabAccountGuideShowed(boolean showed) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("tabAccountGuideShowed", showed);
        editor.apply();
    }

    public static String getPushBindClientId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("pushBindClientId", null);
    }

    public static void setPushBindClientId(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pushBindClientId", id);
        editor.apply();
    }

    public static String getPushBindUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("pushBindUserId", null);
    }

    public static void setPushBindUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pushBindUserId", userId);
        editor.apply();
    }

    public static void setValue(String value) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(value, value);
        editor.apply();
    }

    public static String getValue(String key) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void setValue(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setShowMainAddBanner(boolean isShow) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("showMainAdBanner", isShow);
        editor.apply();
    }

    public static boolean getShowMainAddBanner() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("showMainAdBanner", false);
    }

    public static boolean getNumVisible() {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("loanNumVisible", true);
    }

    public static void putNumVisible(boolean visible) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loanNumVisible", visible);
        editor.apply();
    }

    public static int getVertifyState(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getInt(phone, 1);
    }

    public static void setVertifyState(Context context, int state, String phone) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(phone, state);
        editor.commit();
    }

    public static String getPhone(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString(userId, "");
    }

    public static void setPhone(Context context, String userId, String phone) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userId, phone);
        editor.commit();
    }
}