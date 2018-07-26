package com.beihui.market.tang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/25
 */

public class StringUtil {
    public static boolean isFloat(String value) {
        String regex_float = "^([0-9][0-9]*)+(\\.[0-9]{0,2})?$";
        Pattern p = Pattern.compile(regex_float);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    private static final String FORMAT_Y_M_D = "yyyy-MM-dd";
    private static final String FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";

    public static String time2Str(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S);
            Date date = dateFormat.parse(time);
            long dateTime = date.getTime();
            long nowTime = System.currentTimeMillis();
            long second = (nowTime - dateTime) / 1000;//秒
            long minite = second / 60;//分
            long hour = minite / 60;//时
            long day = hour / 24;//天
            if (day >= 1) {
                return day + "天前";
            } else if (hour >= 1) {
                return hour + "小时前";
            } else if (minite >= 1) {
                return minite + "分钟前";
            } else if (second >= 1) {
                return minite + "秒前";
            } else {
                return "刚刚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
