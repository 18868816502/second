package com.beihui.market.jjd;

import android.text.TextUtils;


import com.beihui.market.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/17
 */

public class BankInfoUtil {
    private static final Map<String, Integer> bankNameMap = new HashMap<>();

    public static Map<String, Integer> bankNameMap() {
        bankNameMap.put("ABC#中国农业银行", R.mipmap.bank_logo_nonghang);
        bankNameMap.put("BOC#中国银行", R.mipmap.bank_logo_zhongguo);
        bankNameMap.put("CITIC#中信银行", R.mipmap.bank_logo_zhongxin);
        bankNameMap.put("CEB#中国光大银行", R.mipmap.bank_logo_guangda);
        bankNameMap.put("CIB#兴业银行", R.mipmap.bank_logo_xingye);
        bankNameMap.put("GDB#广发银行", R.mipmap.bank_logo_guangfa);
        bankNameMap.put("HXBANK#华夏银行", R.mipmap.bank_logo_huaxia);
        bankNameMap.put("ICBC#中国工商银行", R.mipmap.bank_logo_gonghang);
        bankNameMap.put("CMBC#中国民生银行", R.mipmap.bank_logo_minsheng);
        bankNameMap.put("CCB#中国建设银行", R.mipmap.bank_logo_jianhang);
        bankNameMap.put("PSBC#中国邮政储蓄银行", R.mipmap.bank_logo_youzheng);
        bankNameMap.put("SPABANK#平安银行", R.mipmap.bank_logo_pingan);
        bankNameMap.put("SPDB#浦发银行", R.mipmap.bank_logo_pufa);
        bankNameMap.put("BJCN#北京银行", R.mipmap.bank_logo_beijing);
        bankNameMap.put("CMB#招商银行", R.mipmap.bank_logo_zhaoshang);
        bankNameMap.put("COMM#中国交通银行", R.mipmap.bank_logo_jiaohang);
        bankNameMap.put("EGBANK#恒丰银行", R.mipmap.bank_logo_hengfeng);
        bankNameMap.put("BOHAIB#渤海银行", R.mipmap.bank_logo_bohai);
        bankNameMap.put("CZBANK#浙商银行", R.mipmap.bank_logo_zheshang);
        return bankNameMap;
    }

    public static Map<String, Integer> bankNameAndLogo(String bankName) {
        Map<String, Integer> bankNameMap = bankNameMap();
        Iterator<Map.Entry<String, Integer>> iterator = bankNameMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            Map<String, Integer> result = new HashMap<>();
            String[] codesAndNames = key.split("#");
            String code = codesAndNames[0];
            String name = codesAndNames[1];
            if (TextUtils.equals(name, bankName)) {
                result.put("bankLogo", entry.getValue());
                return result;
            }
        }
        return null;
    }
}