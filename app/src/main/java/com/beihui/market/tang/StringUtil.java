package com.beihui.market.tang;

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
}
