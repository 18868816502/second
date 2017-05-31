/**
 * @Title: FileUtil.java
 * @Package com.bluemor.reddotface.utils
 * @Description: TODO
 * @author caojiabing(曹家兵) caojiabing@foxmail.com
 * @date 2014-11-18 下午1:50:06
 * @version V1.0
 */
package com.beihui.market.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @ClassName: FileUtil
 * @Description: TODO
 * @author caojiabing(曹家兵) caojiabing@foxmail.com
 * @date 2015-3-4 下午12:55:02
 */
public class FileUtil {
    // 缓存根目录
    private static File cacheDir;
    // 发布的缓存图片目录－拍照，图库
    private static File publishDir;
    private static File userAvatarDir;
    private static File userBgAvatar;

    public static void initFileCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    Environment.getExternalStorageDirectory(),
                    "ShanDaiXia");
        else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        // 初始化发布混存目录
        publishDir = new File(cacheDir, "publish");

        if (!publishDir.exists())
            publishDir.mkdirs();
        // 初始化用户图像混存目录
        userAvatarDir = new File(cacheDir, "user_avatar");

        if (!userAvatarDir.exists())
            userAvatarDir.mkdirs();
        // 初始化用户背景混存目录
        userBgAvatar = new File(cacheDir, "user_bg");

        if (!userBgAvatar.exists())
            userBgAvatar.mkdirs();
    }

    public static File getFile(String filename) {
        File f = new File(publishDir, filename);
        return f;
    }

    //图像
    public static File getAvatarFile(String filename) {
        File f = new File(userAvatarDir, filename);
        return f;
    }

    public static File getPhotoFile(String path) {
        File f = new File(publishDir, path);
        return f;
    }

    /**
     * 发布完成删除图片
     *
     * @Title: deleteUploadFile
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public static void deleteUploadFile() {
        if (publishDir == null || !publishDir.exists()
                || !publishDir.isDirectory())
            return;

        for (File file : publishDir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件
            }
        }
        // image.delete();// 删除目录本身
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        if (publishDir == null || !publishDir.exists()
                || !publishDir.isDirectory())
            return;

        for (File file : publishDir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
        if (userAvatarDir == null || !userAvatarDir.exists()
                || !userAvatarDir.isDirectory())
            return;

        for (File file : userAvatarDir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
        if (userBgAvatar == null || !userBgAvatar.exists()
                || !userBgAvatar.isDirectory())
            return;

        for (File file : userBgAvatar.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * 计算缓存文件的大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getCacheSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getCacheSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    private static long SIZE_KB = 1024;
    private static long SIZE_MB = 1024 * 1024;
    private static long SIZE_GB = 1024 * 1024 * 1024;

    public static String displayCacheSize() throws Exception {
        DecimalFormat df = new DecimalFormat(".00");
        String result = "0 kb";
        long size = getCacheSize(cacheDir);
        if (size >= SIZE_GB) {
            result = df.format(size * 1.0f / SIZE_GB) + " GB";
        } else if (size >= SIZE_MB) {
            result = df.format(size * 1.0f / SIZE_MB) + " MB";
        } else if (size >= SIZE_KB) {
            result = df.format(size * 1.0f / SIZE_KB) + " KB";
        } else if (size > 0) {
            result = "1 KB";
        } else {
            result = "0 KB";
        }
        return result;
    }


    @SuppressWarnings("unused")
    private void clear() {
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

    public static void saveBitmap(File file, Bitmap bitmap) throws IOException {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(
                file));
    }

    public static void saveLog(String name, String filecontent) {
        try {
            File file = getPhotoFile(name + ".txt");
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(filecontent.getBytes());
            outStream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(
                        new File(targetDir).getAbsolutePath() + File.separator
                                + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }


    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建根目录
     *
     * @param path
     *            目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path
     *            文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }


    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     * @author guhaizhou@126.com
     * @param path 文件路径
     * @return
     * @throws Exception
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, 2);
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件). <br/>
     * @author guhaizhou@126.com
     * @param base64Code 编码后的字串
     * @param savePath  文件保存路径
     * @throws Exception
     * @since JDK 1.6
     */
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, 2);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



}
