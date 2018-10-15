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
        bankNameMap.put("3ABC#中国农业银行", R.mipmap.bank_logo_nonghang);
        bankNameMap.put("3PSBC#中国邮政储蓄银行", R.mipmap.bank_logo_youzheng);
        bankNameMap.put("2CIB#兴业银行", R.mipmap.bank_logo_xingye);
        bankNameMap.put("2CMBC#中国民生银行", R.mipmap.bank_logo_minsheng);
        bankNameMap.put("2CCB#中国建设银行", R.mipmap.bank_logo_jianhang);
        bankNameMap.put("2SPDB#浦发银行", R.mipmap.bank_logo_pufa);
        bankNameMap.put("2COMM#中国交通银行", R.mipmap.bank_logo_jiaohang);
        bankNameMap.put("2EGBANK#恒丰银行", R.mipmap.bank_logo_hengfeng);
        bankNameMap.put("2BOHAIB#渤海银行", R.mipmap.bank_logo_bohai);
        bankNameMap.put("4SPABANK#平安银行", R.mipmap.bank_logo_pingan);
        bankNameMap.put("4CEB#中国光大银行", R.mipmap.bank_logo_guangda);
        bankNameMap.put("1BOC#中国银行", R.mipmap.bank_logo_zhongguo);
        bankNameMap.put("1CITIC#中信银行", R.mipmap.bank_logo_zhongxin);
        bankNameMap.put("1GDB#广发银行", R.mipmap.bank_logo_guangfa);
        bankNameMap.put("1HXBANK#华夏银行", R.mipmap.bank_logo_huaxia);
        bankNameMap.put("1ICBC#中国工商银行", R.mipmap.bank_logo_gonghang);
        bankNameMap.put("1BJCN#北京银行", R.mipmap.bank_logo_beijing);
        bankNameMap.put("1CMB#招商银行", R.mipmap.bank_logo_zhaoshang);
        bankNameMap.put("1CZBANK#浙商银行", R.mipmap.bank_logo_zheshang);
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
                result.put("bg", Integer.valueOf(code.substring(0, 1)));
                return result;
            }
        }
        return null;
    }
}