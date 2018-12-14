package com.beiwo.klyjaz.tang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static final String FORMAT_Y_M_D = "yyyy-MM-dd";
    public static final String FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";

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
                if (day > 10) {//超过10天显示日期
                    return time.substring(0, 10);
                } else return day + "天前";
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

    public static boolean isEmail(String target) {
        String emailPattern = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(target);
        return m.matches();
    }

    public static boolean isPhone(String target) {
        String phonePattern = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
        Pattern p = Pattern.compile(phonePattern);
        Matcher m = p.matcher(target);
        return m.matches();
    }

    public static long time2NowSecond(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S);
            Date date = dateFormat.parse(time);
            long dateTime = date.getTime();
            long nowTime = System.currentTimeMillis();
            long second = (nowTime - dateTime) / 1000;//秒
            return second;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static long timeGapSecond(String last, String previous) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S);
            Date lastDate = dateFormat.parse(last);
            Date previousDate = dateFormat.parse(previous);
            long lastTime = lastDate.getTime();
            long preTime = previousDate.getTime();
            long second = Math.abs(lastTime - preTime) / 1000;//秒
            return second;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String stamp2Str(long stamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S);
            return dateFormat.format(stamp);
        } catch (Exception e) {
            return "";
        }
    }

    public static String stamp2Str(long stamp, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(stamp);
        } catch (Exception e) {
            return "";
        }
    }

    public static long str2Stamp(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S);
            Date date = dateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
        }
        return 0;
    }

    public static String date2Now(int distanceDay) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y_M_D);
            Date today = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distanceDay);
            Date distanceDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
            return dateFormat.format(distanceDate);
        } catch (Exception e) {
        }
        return "";
    }

    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) ((time / 60) % 60);//分
        int h = (int) (time / 3600);//时
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}