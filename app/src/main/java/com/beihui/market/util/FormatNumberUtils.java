package com.beihui.market.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * *      ┏┓　　　┏┓
 * *    ┏┛┻━━━┛┻┓
 * *    ┃　　　　　　　┃
 * *    ┃　　　━　　　┃
 * *    ┃　┳┛　┗┳　┃
 * *    ┃　　　　　　　┃
 * *    ┃　　　┻　　　┃
 * *    ┃　　　　　　　┃
 * *    ┗━┓　　　┏━┛
 * *       ┃　　　┃   神兽保佑
 * *       ┃　　　┃   代码无BUG！
 * *       ┃　　　┗━━━┓
 * *       ┃　　　　　　　┣┓
 * *       ┃　　　　　　　┏┛
 * *       ┗┓┓┏━┳┓┏┛━━━━━┛
 * *         ┃┫┫　┃┫┫
 * *         ┗┻┛　┗┻┛
 * *
 * Created by opq on 2017/8/28.
 * 数字格式化
 */

public class FormatNumberUtils {

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.66
     */
    public static String FormatNumberFor2(BigDecimal bigDecimal) {
        DecimalFormat format = new DecimalFormat(",###,##0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(bigDecimal);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.66
     */
    public static String FormatNumberFor2(Double number) {
        DecimalFormat format = new DecimalFormat(",###,##0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 特殊
     * 如 12,123,456.66
     */
    public static Float FormatNumberForTabFloat(Double number) {
        DecimalFormat format = new DecimalFormat("#######0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return Float.valueOf(format.format(number));
    }

    /**
     * 特殊
     * 如 12,123,456.66
     */
    public static String FormatNumberForTabDouble(Double number) {
        DecimalFormat format = new DecimalFormat("#######0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }


    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.66
     */
    public static String FormatNumberFor2(String number) {
        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.66
     */
    public static String FormatNumberFor2(Long number) {
        DecimalFormat format = new DecimalFormat(",###,##0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.66
     */
    public static String FormatNumberFor2(Integer number) {
        DecimalFormat format = new DecimalFormat(",###,##0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.66
     */
    public static String FormatNumberFor2(Float number) {
        DecimalFormat format = new DecimalFormat(",###,##0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor1(BigDecimal number) {
        DecimalFormat format = new DecimalFormat(",###,##0.0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }


    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor1(Double number) {
        DecimalFormat format = new DecimalFormat(",###,##0.0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor1(Long number) {
        DecimalFormat format = new DecimalFormat(",###,##0.0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor1(Integer number) {
        DecimalFormat format = new DecimalFormat(",###,##0.0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor1(Float number) {
        DecimalFormat format = new DecimalFormat(",###,##0.0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor0(BigDecimal bigDecimal) {
        DecimalFormat format = new DecimalFormat(",###,##0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(bigDecimal);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor0(Double number) {
        DecimalFormat format = new DecimalFormat(",###,##0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor0(Long number) {
        DecimalFormat format = new DecimalFormat(",###,##0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor0(Integer number) {
        DecimalFormat format = new DecimalFormat(",###,##0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 货币化数字 保留两位小数 不四舍五入 截取小数
     * 如 12,123,456.6
     */
    public static String FormatNumberFor0(Float number) {
        DecimalFormat format = new DecimalFormat(",###,##0");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(number);
    }

    /**
     * 以元的单位传进来
     * -1 小于,0 等于,1 大于
     */
    public static boolean isTenThousand(BigDecimal money) {
        if (money.compareTo(new BigDecimal(10000)) == -1 && money.compareTo(new BigDecimal(0)) > -1) {          //[0, 10000}
            return false;
        } else if (money.compareTo(new BigDecimal(-10000)) > 0 && money.compareTo(new BigDecimal(0)) == -1){    //{-10000, 0}
            return false;
        } else {
            return true;                                                                                        //{-∞, -10000] && [10000, +∞}
        }
    }

    /**
     * 以元的单位传进来
     * -1 小于,0 等于,1 大于
     */
    public static boolean isTenThousand(Long money) {
        if (money >= 10000) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 以元的单位传进来
     * -1 小于,0 等于,1 大于
     */
    public static boolean isTenThousand(Integer money) {
        if (money >= 10000) {
            return true;
        } else {
            return false;
        }
    }
}
