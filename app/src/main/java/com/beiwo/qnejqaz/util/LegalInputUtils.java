package com.beiwo.qnejqaz.util;


import android.text.TextUtils;

public class LegalInputUtils {
    /**
     * 输入的手机号和密码是否符合规范
     */
    public static boolean isPhoneAndPwdLegal(String phone, String password) {
        return validatePhone(phone) && validatePassword(password);
    }

    public static boolean validatePassword(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 6 || str.length() > 16) {
            return false;
        }
        String regex = "^[0-9A-Za-z]{6,16}$";
        return str.matches(regex);
    }

    /**
     * 手机号码的显示规范
     */
    public static String formatMobile(String mobile) {
        if (mobile.length() == 11) {
            StringBuilder builder = new StringBuilder(mobile);
            StringBuilder temp = new StringBuilder(" ");
            builder.replace(3, 7, temp.append(builder.substring(3, 7)).append(" ").toString());
            return builder.toString();
        }
        return mobile;
    }

    /**
     * 验证手机号
     *
     * @author xhb 判断是一位纯数字 首位为1
     */
    public static boolean validatePhone(String phone) {
        /*if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return false;
        }
        return phone.matches("^(1)\\d{10}$");*/
        return !(TextUtils.isEmpty(phone) || phone.length() != 11) && phone.matches("^(1)\\d{10}$");
    }
}