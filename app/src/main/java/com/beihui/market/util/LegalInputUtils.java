package com.beihui.market.util;


public class LegalInputUtils {

    /**
     * 输入的手机号和密码是否符合规范
     *
     * @param phone    手机号
     * @param password 密码
     */
    public static boolean isPhoneAndPwdLegal(String phone, String password) {
        return phone.length() == 11 && containNumberAndChar(password);
    }

    public static boolean containNumberAndChar(String str) {
        if (str != null) {
            return true;
        } else {
            return false;
        }
    }
}
