package com.beiwo.klyjaz.api.interceptor;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/11/1
 */

public class Digest {
    public static String md5(String value) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(value.getBytes("utf-8"));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                if ((b & 0xff) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xff));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return "";
    }

    /*十六进制字符串转字节数组*/
    public static byte[] hexStr2Bytes(String hexStr) {
        if (TextUtils.isEmpty(hexStr)) return null;
        byte[] result = new byte[hexStr.length() / 2];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < result.length; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    /*字节数组转字符串*/
    public static String bytes2HexStr(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;
        StringBuilder sb = new StringBuilder();
        for (Byte b : bytes) {
            String hv = Integer.toHexString(b & 0xff);
            if (hv.length() < 2) sb.append(0);
            sb.append(hv);
        }
        return sb.toString();
    }

    public static String sha512(String value) {
        String result = "";
        if (value != null && value.length() > 0) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-512");
                digest.update(value.getBytes());
                byte[] bytes = digest.digest();
                StringBuffer sb = new StringBuffer();
                for (byte b : bytes) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) sb.append("0");
                    sb.append(hex);
                }
                result = sb.toString();
            } catch (Exception e) {
            }
        }
        return result;
    }
}