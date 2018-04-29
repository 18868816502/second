package com.beihui.market.helper;

import android.content.Context;
import android.os.Environment;

import com.beihui.market.App;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by xhb on 2017/8/1.
 * 数据缓存清除类
 */

public class DataCleanManager {

    public static Context sContext = App.getInstance();

    private DataCleanManager() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 清除内部缓存
     * /data/data/com.xxx.xxx/cache
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanInternalCache() {
        return deleteFilesInDir(sContext.getCacheDir());
    }

    //获取内部缓存大小
    public static String getInternalCache() throws Exception {
        return getSize(sContext.getCacheDir());
    }
    public static long getInternalCacheSize() throws Exception {
        return getFolderSize(sContext.getCacheDir());
    }

    /**
     * 清除内部文件
     * /data/data/com.xxx.xxx/files
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanInternalFiles() {
        return deleteFilesInDir(sContext.getFilesDir());
    }

    //获取内部文件大小
    public static String getInternalFiles() throws Exception {
        return getSize(sContext.getFilesDir());
    }
    public static long getInternalFilesSize() throws Exception {
        return getFolderSize(sContext.getFilesDir());
    }

    /**
     * 清除内部数据库
     * /data/data/com.xxx.xxx/databases
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanInternalDbs() {
        return deleteFilesInDir(sContext.getFilesDir().getParent() + File.separator + "databases");
    }

    //获取内部数据库大小
    public static String getInternalDbs() throws Exception {
        return getSize(new File(sContext.getFilesDir().getParent() + File.separator + "databases"));
    }
    public static long getInternalDbsSize() throws Exception {
        return getFolderSize(new File(sContext.getFilesDir().getParent() + File.separator + "databases"));
    }


    /**
     * 清除内部SP
     * /data/data/com.xxx.xxx/shared_prefs
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanInternalSP() {
        return deleteFilesInDir(sContext.getFilesDir().getParent() + File.separator + "shared_prefs");
    }

    //获取内部SP大小
    public static String getInternalSP() throws Exception {
        return getSize(new File(sContext.getFilesDir().getParent() + File.separator + "shared_prefs"));
    }
    public static long getInternalSPSize() throws Exception {
        return getFolderSize(new File(sContext.getFilesDir().getParent() + File.separator + "shared_prefs"));
    }

    /**
     * 清除外部缓存
     * /storage/emulated/0/android/data/com.xxx.xxx/cache
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanExternalCache() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && deleteFilesInDir(sContext.getExternalCacheDir());
    }

    //获取外部缓存大小
    public static String getExternalCache() throws Exception {
        return getSize(sContext.getExternalCacheDir());
    }
    public static long getExternalCacheSize() throws Exception {
        return getFolderSize(sContext.getExternalCacheDir());
    }

    /**
     * 根据名称清除数据库
     * /data/data/com.xxx.xxx/databases/dbName
     * @param dbName 数据库名称
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanInternalDbByName(final String dbName) {
        return sContext.deleteDatabase(dbName);
    }

    /**
     * 清除自定义目录下的文件
     * @param dirPath 目录路径
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanCustomCache(final String dirPath) {
        return deleteFilesInDir(dirPath);
    }

    /**
     * 清除自定义目录下的文件
     * @param dir 目录
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanCustomCache(final File dir) {
        return deleteFilesInDir(dir);
    }

    public static boolean deleteFilesInDir(final String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    private static boolean deleteFilesInDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    private static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取缓存目录格式化
     */
    public static String getSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 获取文件
     * Context.getExternalFilesDir()-->SDCard/Android/data/你的应用的包名/files/目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir()-->SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {

        double megaByte = size / 1024 / 1024;

        double gigaByte = megaByte / 1024;

        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}
