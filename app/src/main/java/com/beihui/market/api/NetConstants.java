package com.beihui.market.api;


import com.beihui.market.BuildConfig;

import java.util.List;

public class NetConstants {

    public static final String DOMAIN = BuildConfig.DOMAIN;

    public static final String BASE_PATH = "/s1";

    public static final String PRODUCT_PATH = "/s3";

    public static final String SECRET_KEY = "0bca3e8e2baa42218040c5dbf6978f315e104e5c";

    public static final String ACCESS_KEY = "699b9305418757ef9a26e5a32ca9dbfb";

    /**********H5 static field********/
    public static final String H5_DOMAIN = BuildConfig.H5_DOMAIN;

    public static final String H5_HELPER = H5_DOMAIN + BuildConfig.PATH_HELPER_CENTER + "?isApp=1";

    public static final String H5_TEST = H5_DOMAIN + "/test.html?isApp=1";

    public static final String H5_NEWS_DETAIL = H5_DOMAIN + "/newsDetail.html";

    /**
     * TODO 发现页 链接
     */
//    public static final String H5_FIND_WEVVIEW_DETAIL = H5_DOMAIN + "/findH5.html";
    public static final String H5_FIND_WEVVIEW_DETAIL = H5_DOMAIN + "/findH5-v3.html";

    public static final String H5_LOAN_DETAIL = H5_DOMAIN + "/productDetail.html";

    public static final String H5_INVITATION = H5_DOMAIN + "/regist_h5.html";

    public static final String H5_ABOUT_US = H5_DOMAIN + BuildConfig.PATH_ABOUT_US + "?isApp=1";

    public static final String H5_USER_AGREEMENT = H5_DOMAIN + BuildConfig.PATH_USER_AGREEMENT + "?isApp=1";

    public static final String H5_INTERNAL_MESSAGE = H5_DOMAIN + "/letterDetail.html";

    public static final String H5_ONE_KEY_LOAN = H5_DOMAIN + "/oneKeyRegistration.html";

    public static final String H5_DEBT_ANALYZE = H5_DOMAIN + "/debtAnalysis.html";

    public static final String H5_CREDIT_CARD_CENTER = H5_DOMAIN + "/creditIndex.html";

    public static final String H5_REWARD_POINTS = H5_DOMAIN + "/integral.html";

    /**
     * 用户协议H5
     */
    public static final String H5_USER_REGISTRATION_PROTOCOL = H5_DOMAIN + "/registrationProtocol.html";


    /**
     * 账单导入成功
     */
    public static final String H5_LEADING_IN_RESULT_SUCCESS = H5_DOMAIN + "/export/success.html";
    /**
     * 账单导入失败
     */
    public static final String H5_LEADING_IN_RESULT_FAILED = H5_DOMAIN + "/export/fail.html";
    /**
     * 账单导入帮助页面
     */
    public static final String H5_HELP = H5_DOMAIN + "/export/help.html";
    /**
     * 办卡进度查询
     */
    public static final String H5_CREDIT_CARD_PROGRESS = "http://www.huishuaka.com/5/coop/jinduchaxun.html?ichannelid=";


    /**
     * @author xhb
     * @date 20180420
     * @param userId 用户ID isAppId 区分web前端还是app (区分平台)
     * @desc 资讯模块 详情页面
     */
    public static String generateNewsUrl(String id, String userId) {
        return H5_NEWS_DETAIL + "?id=" + id + "&isApp=1&userId=" + userId;
    }

    /**
     * @author xhb
     * @date 20180420
     * @param userId 用户ID isAppId 区分web前端还是app (区分平台)
     * @desc 发现模块
     *
     * 生成发现页链接
     */
    public static String generateNewsWebViewUrl(String userId, String channelId, String version) {
        return H5_FIND_WEVVIEW_DETAIL + "?isApp=1&userId=" + userId + "&packageId=" + channelId + "&version=" + version;
    }

    public static String generateProductUrl(String id) {
        return H5_LOAN_DETAIL + "?id=" + id + "&isApp=1";
    }

    public static String generateInvitationUrl(String userId) {
        return H5_INVITATION + "?id=" + userId;
    }

    public static String generateInternalMessageUrl(String id) {
        return H5_INTERNAL_MESSAGE + "?id=" + id;
    }

    public static String generateTestUrl(String id) {
        return H5_TEST + "?isApp=1&id=" + id;
    }

    public static String generateCreditCardUrl(String id) {
        return H5_CREDIT_CARD_CENTER + "?isApp=1&userId=" + id;
    }

    public static String generateRewardPointsUrl(String id) {
        return H5_REWARD_POINTS + "?isApp=1&userId=" + id;
    }

    public static String generateDebtAnalyzeUrl(String id) {
        return H5_DEBT_ANALYZE + "?isApp=1&userId=" + id;
    }


    public static String generateOneKeyLoanUrl(List<String> ids, String userId) {
        StringBuilder sb = new StringBuilder();
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                sb.append(id).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return H5_ONE_KEY_LOAN + "?isApp=1&pids=" + sb.toString() + "&userId=" + userId;
    }
}
