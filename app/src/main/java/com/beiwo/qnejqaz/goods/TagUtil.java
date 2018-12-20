package com.beiwo.qnejqaz.goods;

import java.util.HashMap;
import java.util.Map;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/13
 */
public class TagUtil {
    private static Map<String, String> mapWithoutNum() {
        Map<String, String> map = new HashMap<>();
        map.put("QuickLoan", "放款快");
        map.put("HighLoan", "额度高");
        map.put("LowThreshold", "门槛低");
        map.put("ConvenientProcedure", "手续方便");
        map.put("NoMortgageRequired", "无需抵押");
        map.put("NoCredit", "不上征信");
        map.put("AuditTimely", "审批及时");
        map.put("HeightPass", "过审高");
        map.put("GoodExperience", "用户体验好");
        map.put("LargePlatform", "大平台");
        return map;
    }


    private static Map<String, String> mapValue2Key() {
        Map<String, String> map = new HashMap<>();
        map.put("放款快", "QuickLoan");
        map.put("额度高", "HighLoan");
        map.put("门槛低", "LowThreshold");
        map.put("手续方便", "ConvenientProcedure");
        map.put("无需抵押", "NoMortgageRequired");
        map.put("不上征信", "NoCredit");
        map.put("审批及时", "AuditTimely");
        map.put("过审高", "HeightPass");
        map.put("用户体验好", "GoodExperience");
        map.put("大平台", "LargePlatform");
        return map;
    }

    private static Map<String, String> mapWithNum() {
        Map<String, String> map = new HashMap<>();
        map.put("QuickLoan", "放款快(%d)");
        map.put("HighLoan", "额度高(%d)");
        map.put("LowThreshold", "门槛低(%d)");
        map.put("ConvenientProcedure", "手续方便(%d)");
        map.put("NoMortgageRequired", "无需抵押(%d)");
        map.put("NoCredit", "不上征信(%d)");
        map.put("AuditTimely", "审批及时(%d)");
        map.put("HeightPass", "过审高(%d)");
        map.put("GoodExperience", "用户体验好(%d)");
        map.put("LargePlatform", "大平台(%d)");
        return map;
    }

    public static String getKeyValue(String key, boolean withNum) {
        if (withNum) {
            return mapWithNum().get(key);
        } else {
            return mapWithoutNum().get(key);
        }
    }

    public static String getValByKey(String value) {
        return mapValue2Key().get(value);
    }
}