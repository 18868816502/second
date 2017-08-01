package com.beihui.market.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static final String TAG = "Info";

    public static String getLastDialogAdId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastDialogAdId", null);
    }

    public static void setLastDialogAdId(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastDialogAdId", id);
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

}
