package com.beiwo.klyjaz.util;

import android.text.TextUtils;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.scdkaz.util
 * @descripe
 * @time 2018/9/29 15:37
 */
public class Tools {

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     * @param mobileNums
     * @return 是否为手机号
     */
    public static boolean checkPhone(String mobileNums) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums)) {
            return false;
        } else {
            return mobileNums.matches(telRegex);
        }
    }

}
