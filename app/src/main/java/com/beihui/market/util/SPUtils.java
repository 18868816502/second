package com.beihui.market.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static final String TAG = "Info";

    public static long getLastAdShowTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getLong("lastAdShowTime", 0);
    }

    public static void setLastAdShowTime(Context context, long showTime) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("lastAdShowTime", showTime);
        editor.apply();
    }

    public static String getLastInstalledVersion(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastInstalledVersion", null);
    }

    public static void setLastInstalledVersion(Context context, String version) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastInstalledVersion", version);
        editor.apply();
    }

    public static String getLastNoticeId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastNoticeId", null);
    }

    public static void setLastNoticeId(Context context, String lastNoticeId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastNoticeId", lastNoticeId);
        editor.apply();
    }

    public static boolean getNoticeClosed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("noticeClose", false);
    }

    public static void setNoticeClosed(Context context, boolean closed) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("noticeClose", closed);
        editor.apply();
    }

    public static boolean getCheckPermission(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("checkPermission", false);
    }

    public static void setCheckPermission(Context context, boolean check) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("checkPermission", check);
        editor.apply();
    }

    public static boolean getWechatSurpriseClicked(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("wechatSurpriseClicked", false);
    }

    public static void setWechatSurpriseClicked(Context context, boolean clicked) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("wechatSurpriseClicked", clicked);
        editor.apply();
    }

    public static void setCacheUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cacheUserId", userId);
        editor.apply();
    }

    public static String getCacheUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("cacheUserId", null);
    }

    public static boolean getTabAccountGuideShowed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("tabAccountGuideShowed", false);
    }

    public static void setTabAccountGuideShowed(Context context, boolean showed) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("tabAccountGuideShowed", showed);
        editor.apply();
    }

    public static boolean getConfirmPlanGuideShowed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("confirmPlanGuideShowed", false);
    }

    public static void setConfirmPlanGuideShowed(Context context, boolean showed){
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("confirmPlanGuideShowed", showed);
        editor.apply();
    }
}
