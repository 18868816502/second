package com.beihui.market.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.beihui.market.App;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtils {

    private static DecimalFormat decimalFormat = new DecimalFormat();
    private static DecimalFormat rateDecimalFormat = new DecimalFormat();
    private static DecimalFormat amountFormat = new DecimalFormat();

    static {
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(3);

        rateDecimalFormat.setMaximumFractionDigits(3);

        amountFormat.setGroupingUsed(false);
        amountFormat.setMaximumFractionDigits(2);
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     * @return 加上逗号的数字
     */
    public static String addComma(String str) {
        // 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将[789,456,] 中最后一个[,]去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * @param @param  context
     * @param @return
     * @return String
     * @throws
     * @Title: getVersionName
     * @Description: 获取当前应用的版本号
     */
    public static String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = App.getInstance().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(App.getInstance().getPackageName(),
                    0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static int getVersionCode() {
        PackageInfo packInfo = null;
        try {
            PackageManager packageManager = App.getInstance().getPackageManager();
            packInfo = packageManager.getPackageInfo(App.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo != null ? packInfo.versionCode : 0;
    }

    // 安装下载后的apk文件
    public static void instanll(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static int getScreenMaxWidth(Context context, int paramInt) {
        Object localObject = new DisplayMetrics();
        try {
            DisplayMetrics localDisplayMetrics =
                    context.getApplicationContext().getResources().getDisplayMetrics();
            localObject = localDisplayMetrics;
            return ((DisplayMetrics) localObject).widthPixels - dip2px(context, paramInt);
        } catch (Exception localException) {
            while (true) {
                localException.printStackTrace();
                ((DisplayMetrics) localObject).widthPixels = 640;
            }
        }
    }


    public static int getScreenMaxHeight(Context paramContext, int paramInt) {
        Object localObject = new DisplayMetrics();
        try {
            DisplayMetrics localDisplayMetrics =
                    paramContext.getApplicationContext().getResources().getDisplayMetrics();
            localObject = localDisplayMetrics;
            return ((DisplayMetrics) localObject).heightPixels - dip2px(paramContext, paramInt);
        } catch (Exception localException) {
            while (true) {
                localException.printStackTrace();
                ((DisplayMetrics) localObject).heightPixels = 960;
            }
        }
    }


    /**
     * 讲电话号码中间变成*
     *
     * @param pNumber
     * @return
     */
    public static String changeTel(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return pNumber;
    }

    /**
     * 讲电话号码中间变成*
     *
     * @param pNumber
     * @return
     */
    public static String formatTel(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else if (i == 2) {
                    sb.append(c).append(" ");
                } else if (i == 7) {
                    sb.append(" ").append(c);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return pNumber;
    }

    public static boolean matchPhone(String text) {
        if (Pattern.compile("^1(3|4|5|7|8)\\d{9}$").matcher(text).matches()) {
            return true;
        }
        return false;
    }


    public static String getNetworkOperatorName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperatorName();

    }


    public static int getPixColor(Bitmap src) {
        int pixelColor;
        pixelColor = src.getPixel(5, 5);
        return pixelColor;
    }


    /**
     * 获取bitmap图片的大小
     *
     * @param bitmap
     * @return
     */
    public static long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    /**
     * 获取bitmap图片的大小后转换成kb
     *
     * @param bitmap
     * @return
     */
    public static String convertFileSize(Bitmap bitmap) {
        long kb = 1024;
        float f = (float) getBitmapsize(bitmap) / kb;
        return String.format(f > 100 ? "%.0f" : "%.1f", f);
    }


    /**
     * 安卓获取状态栏(Status Bar)高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    /**
     * 判断是否有虚拟按键
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    public static void hideBottomUIMenu(Activity context) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }


    /**
     * 将Edittext光标定位到最后一位
     *
     * @param editText
     */
    public static void setEditTextCursorLocation(EditText editText) {
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    /**
     * 两个String数组拼接
     *
     * @param a
     * @param b
     * @return
     */
    public static String[] concat(String[] a, String[] b) {
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    /**
     * 通过包名判断有没有安装某个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 将.0改为整数
     *
     * @param sMoney
     * @return
     */
    public static String formatMoney(String sMoney) {
        String formatString;

        if (!TextUtils.isEmpty(sMoney)) {
            float money = Float.valueOf(sMoney);
            if (money == 0) {
                formatString = "0";
            } else {
                if (money % (int) money == 0) {
                    formatString = (int) money + "";
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    formatString = decimalFormat.format(money);//format 返回的是字符串
                }
            }

        } else {
            formatString = "0";
        }
        return formatString;

    }


    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }


    public static String phone2Username(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(8, 11);
    }

    public static String getFormatNumber(long number) {
        if (number >= 100000000) {
            String numStr = ((int) ((number / 100000000.0) * 10)) / 10.0 + "";
            if (numStr.charAt(numStr.length() - 1) == '0') {
                numStr = numStr.substring(0, numStr.length() - 2);
            }
            return numStr + "亿";
        } else if (number >= 1000000) {
            String numStr = ((int) ((number / 10000.0) * 10)) / 10.0 + "";
            if (numStr.charAt(numStr.length() - 1) == '0') {
                numStr = numStr.substring(0, numStr.length() - 2);
            }
            return numStr + "万";
        } else {
            return number + "";
        }
    }

    public static int convertStringAmount2Int(String amount) {
        int value = 0;
        if (amount.contains("万")) {
            try {
                value = Integer.valueOf(amount.substring(0, amount.length() - 2)) * 10000;
            } catch (NumberFormatException e) {
            }
        } else {
            try {
                value = Integer.valueOf(amount.substring(0, amount.length() - 1));
            } catch (NumberFormatException e) {
            }
        }
        return value;
    }

    public static String keep2digits(double input) {
        return decimalFormat.format(input);
    }

    public static String keep2digitsWithoutZero(double input) {
        String str = decimalFormat.format(input);
        if (str.contains(".00")) {
            return str.substring(0, str.indexOf(".00"));
        }
        return str;
    }

    public static String keepWithoutZero(double input) {
        String str = decimalFormat.format(input);
        if (str.contains(".0")) {
            return str.substring(0, str.indexOf(".0"));
        }
        if (str.contains(".00")) {
            return str.substring(0, str.indexOf(".00"));
        }
        return str;
    }

    public static String convertInterestRate(double rate) {
        return rateDecimalFormat.format(rate);
    }

    public static String convertAmount(double amount) {
        return amountFormat.format(amount);
    }

    public static String getChaneseNum(int num) {
        if (num == 0) {
            return "每月";
        } else if (num == 1) {
            return "每月";
        } else if (num == 2) {
            return "每2月";
        } else if (num == 3) {
            return "每3月";
        } else if (num == 4) {
            return "每4月";
        } else if (num == 5) {
            return "每5月";
        } else if (num == 6) {
            return "每6月";
        } else if (num == 7) {
            return "每7月";
        } else if (num == 8) {
            return "每8月";
        } else if (num == 9) {
            return "每9月";
        } else if (num == 10) {
            return "每10月";
        } else if (num == 11) {
            return "每11月";
        } else {
            return "每年";
        }
    }

    public static String getDay(int num) {
        if (num == 0) {
            return "提前一天";
        } else if (num == 1) {
            return "提前二天";
        } else if (num == 2) {
            return "提前三天";
        } else if (num == 3) {
            return "提前四天";
        } else if (num == 4) {
            return "提前五天";
        } else if (num == 5) {
            return "提前六天";
        } else if (num == 6) {
            return "提前七天";
        } else if (num == 7) {
            return "提前八天";
        } else if (num == 8) {
            return "提前九天";
        } else if (num == 9) {
            return "提前十天";
        } else {
            return "";
        }
    }

}
