package com.beiwo.klbill;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author:
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/6
 */

public class Dispose3 {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    public static String getPrefString(Context context, String key,
                                       final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static void setPrefString(Context context, final String key,
                                     final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    public static boolean getPrefBoolean(Context context, final String key,
                                         final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(
                key);
    }

    public static void setPrefBoolean(Context context, final String key,
                                      final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    public static void setPrefInt(Context context, final String key,
                                  final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    public static int getPrefInt(Context context, final String key,
                                 final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    public static void setPrefFloat(Context context, final String key,
                                    final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).commit();
    }

    public static float getPrefFloat(Context context, final String key,
                                     final float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    public static void setSettingLong(Context context, final String key,
                                      final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).commit();
    }

    public static long getPrefLong(Context context, final String key,
                                   final long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    public static void clearPreference(Context context,
                                       final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取临时城市数组
     *
     * @param c
     * @return
     */
    public static List<City> getTmpCities(Cursor c) {
        List<City> list = new ArrayList<City>();
        if (c == null || c.getCount() == 0)
            return list;
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(""));
            String postID = c
                    .getString(c.getColumnIndex(""));
            long refreshTime = c.getLong(c
                    .getColumnIndex(""));
            int isLocation = c.getInt(c
                    .getColumnIndex(""));
            long pubTime = c.getLong(c.getColumnIndex(""));
            String weatherInfoStr = c.getString(c.getColumnIndex(""));
            City item = new City(name, postID, refreshTime, isLocation, pubTime, weatherInfoStr);
            // L.i("liweiping", "TmpCity  " + item.toString());
            if (!list.contains(item))// 如果不存在再添加
                list.add(item);
        }
        c.close();
        return list;
    }

    /**
     * 获取热门城市数组
     *
     * @param c
     * @return
     */
    public static List<City> getHotCities(Cursor c) {
        List<City> list = new ArrayList<City>();
        if (c == null || c.getCount() == 0)
            return list;
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(""));
            String postID = c
                    .getString(c.getColumnIndex(""));
            City item = new City(name, postID);
            list.add(item);
        }
        c.close();
        return list;
    }

    /**
     * 获取所有城市数组
     *
     * @param c
     * @return
     */
    public static List<City> getAllCities(Cursor c) {
        List<City> list = new ArrayList<City>();
        if (c == null || c.getCount() == 0)
            return list;
        while (c.moveToNext()) {
            String province = c.getString(c
                    .getColumnIndex(""));
            String city = c.getString(c.getColumnIndex(CityConstants.CITY));
            String name = c.getString(c.getColumnIndex(CityConstants.NAME));
            String pinyin = c.getString(c.getColumnIndex(CityConstants.PINYIN));
            String py = c.getString(c.getColumnIndex(CityConstants.PY));
            String phoneCode = c.getString(c
                    .getColumnIndex(CityConstants.PHONE_CODE));
            String areaCode = c.getString(c
                    .getColumnIndex(CityConstants.AREA_CODE));
            String postID = c
                    .getString(c.getColumnIndex(CityConstants.POST_ID));
            City item = new City(province, city, name, pinyin, py, phoneCode,
                    areaCode, postID);
            list.add(item);
        }
        c.close();
        return list;
    }

    public static String getDBFilePath(Context context) {
        return "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + context.getPackageName() + File.separator
                + "databases" + File.separator + CityProvider.CITY_DB_NAME;
    }

    public static String getDBDirPath(Context context) {
        return "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + context.getPackageName() + File.separator
                + "databases";
    }

    public static void copyDB(Context context) {
        Dispose1.i("liweiping", "copyDB begin....");
        // 如果不是第一次运行程序，直接返回
        if (!Dispose3.getPrefBoolean(context, "isFirstRun", true))
            return;
        File dbDir = new File(getDBDirPath(context));
        if (!dbDir.exists())
            dbDir.mkdir();
        try {
            File dbFile = new File(dbDir, CityProvider.CITY_DB_NAME);
            InputStream is = context.getAssets()
                    .open(CityProvider.CITY_DB_NAME);
            FileOutputStream fos = new FileOutputStream(dbFile);
            byte[] buffer = new byte[is.available()];// 本地文件读写可用此方法
            is.read(buffer);
            fos.write(buffer);
            // int len = -1;
            // byte[] buffer = new byte[1024 * 8];
            // while ((len = is.read(buffer)) != -1) {
            // fos.write(buffer, 0, len);
            fos.close();
            is.close();
            Dispose1.i("liweiping", "copyDB finish....");
            CityProvider.createTmpCityTable(context);
            Dispose3.setPrefBoolean(context, "isFirstRun", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个自定义风格的Dialog
     *
     * @param activity   上下文对象
     * @param style      风格
     * @param customView 自定义view
     * @return dialog
     */
    public static Dialog getCustomeDialog(Activity activity, int style,
                                          View customView) {
        Dialog dialog = new Dialog(activity, style);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customView);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        window.setAttributes(lp);
        return dialog;
    }

    public static Dialog getCustomeDialog(Activity activity, int style,
                                          int customView) {
        Dialog dialog = new Dialog(activity, style);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customView);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;
        window.setAttributes(lp);
        return dialog;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 反射方法获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 20;
        try {
            Class<?> _class = Class.forName("com.android.internal.R$dimen");
            Object object = _class.newInstance();
            Field field = _class.getField("status_bar_height");
            int restult = Integer.parseInt(field.get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(
                    restult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(getActivity(), "StatusBarHeight = " + statusBarHeight,
        // Toast.LENGTH_SHORT).show();
        return statusBarHeight;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inPreferredConfig = Bitmap.Config.RGB_565;
//		opt.inPurgeable = true;
//		opt.inInputShareable = true;
        opt.inSampleSize = 2;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
    private static final String TAG = "FrostedGlass";
    //private volatile static FrostedGlassUtil mFrostedGlassUtil;

    /*public static FrostedGlassUtil getInstance() {
        if (mFrostedGlassUtil == null) {
            synchronized (FrostedGlassUtil.class) {
                if (mFrostedGlassUtil == null) {
                    mFrostedGlassUtil = new FrostedGlassUtil();
                }
            }
        }
        return mFrostedGlassUtil;
    }

    public native void boxBlur(Bitmap srcBitmap, int radius);

    public native void stackBlur(Bitmap srcBitmap, int radius);

    public native void oilPaint(Bitmap srcBitmap, int radius);

    public native void colorWaterPaint(Bitmap srcBitmap, int radius);*/

    public synchronized Bitmap convertToBlur(Bitmap bmp, int radius) {
        //stackBlur(bmp, radius);
        return bmp;
    }

    static {
        // load frosted glass lib
        System.loadLibrary("frostedGlass");
    }


}
