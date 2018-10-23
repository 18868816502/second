package com.beiwo.klyjaz.api;


import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需要检查
 */
public class NetConstants {
    /**
     * 分组贷超产品列表 产品分组Id 秒批到账
     */
    //public static final String SECOND_PRODUCT = "599c7594aaa7453c8d8b52c35b865adf";
    public static final String SECOND_PRODUCT = "b1c82ecf234241a6bded788a585383da";
    public static final String SECOND_PRODUCT_CHECKING1 = "0f7e2d4f8674487591571166318459e2";
    public static final String SECOND_PRODUCT_CHECKING2 = "2735e029dbf04585baadb750e5faa42f";

    public static final String DOMAIN = BuildConfig.DOMAIN;

    public static final String VERSION_NAME = BuildConfig.VERSION_NAME;
    public static final String APP_NAME = App.getInstance().getString(R.string.app_name);
    public static final String COMPANY_NAME = apname2CompName(APP_NAME);
    public static final String SHORT_COMPANY_NAME = shortComp(APP_NAME);

    private static String apname2CompName(String appname) {
        Map<String, String> map = new HashMap<>();
        map.put("考拉有借", "杭州优材科技有限公司");
        map.put("鱼米记账", "杭州贝捷金融信息服务有限公司");
        if (map.containsKey(appname)) return map.get(appname);
        return "杭州贝沃科技有限公司";
    }

    private static String shortComp(String appname) {
        Map<String, String> map = new HashMap<>();
        map.put("考拉有借", "优材科技");
        map.put("鱼米记账", "贝捷金融");
        if (map.containsKey(appname)) return map.get(appname);
        return "贝沃科技";
    }

    public static final String BASE_PATH = "/s1";
    public static final String BASE_PATH_S_FOUR = "/s4";
    public static final String PRODUCT_PATH = "/s3";

    /*网络请求Request的两个字段*/
    public static final String SECRET_KEY = "0bca3e8e2baa42218040c5dbf6978f315e104e5c";
    public static final String ACCESS_KEY = "699b9305418757ef9a26e5a32ca9dbfb";

    /**********H5 static field********/
    public static final String H5_DOMAIN = BuildConfig.H5_DOMAIN;
    public static final String H5_DOMAIN_NEW = BuildConfig.H5_DOMAIN_NEW;
    public static final String H5_HELPER = H5_DOMAIN + BuildConfig.PATH_HELPER_CENTER + "?isApp=1";
    public static final String H5_TEST = H5_DOMAIN + "/test.html?isApp=1";
    public static final String H5_NEWS_DETAIL = H5_DOMAIN + "/newsDetail.html";
    public static String H5_FIND_WEVVIEW_DETAIL = H5_DOMAIN + "/findH5-v4.html";
    public static final String H5_FIND_WEVVIEW_DETAIL_COPY = H5_DOMAIN + "/findH5-v4.html";
    /*活动的URL*/
    public static final String H5_ACTIVITY_WEVVIEW_DETAIL_COPY = H5_DOMAIN + "/activity-h5.html";
    public static final String H5_LOAN_DETAIL = H5_DOMAIN + "/productDetail.html";
    public static final String H5_INVITATION = H5_DOMAIN + "/regist_h5.html";
    public static final String H5_INVITATION_ACTIVITY = H5_DOMAIN_NEW + "/activity/page/activity-contact-invite.html";
    public static final String H5_INVITE = H5_DOMAIN + "/invite-friends.html";
    public static final String H5_INVITATION_CONTACTS = H5_DOMAIN_NEW + "/activity/page/activity-invite-friends.html";
    public static final String H5_MISSION = H5_DOMAIN_NEW + "/activity/page/activity-invite-task.html";
    public static final String H5_CODE = H5_DOMAIN_NEW + "/activity/page/activity-input-invite.html";
    public static final String H5_GUIDE_INVITE = H5_DOMAIN_NEW + "/activity/page/activity-guide-task.html";
    public static final String H5_ABOUT_US = H5_DOMAIN + BuildConfig.PATH_ABOUT_US + "?isApp=1" + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME + "&companyName=" + COMPANY_NAME + "&appName=" + APP_NAME + "&logoName=klyj-icon";
    public static final String H5_USER_AGREEMENT = H5_DOMAIN + BuildConfig.PATH_USER_AGREEMENT + "?isApp=1" + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    public static final String H5_INTERNAL_MESSAGE = H5_DOMAIN + "/letterDetail.html";
    public static final String H5_ONE_KEY_LOAN = H5_DOMAIN + "/oneKeyRegistration.html";
    public static final String H5_DEBT_ANALYZE = H5_DOMAIN + "/debtAnalysis.html";
    public static final String H5_CREDIT_CARD_CENTER = H5_DOMAIN + "/creditIndex.html";
    public static final String H5_REWARD_POINTS = H5_DOMAIN + "/integral.html";
    /*用户协议H5*/
    public static final String H5_USER_REGISTRATION_PROTOCOL = H5_DOMAIN + "/majiabao/regist-v2.html" + "?isApp=1&packageId=" + App.sChannelId + "&version=" + VERSION_NAME + "&appName=" + APP_NAME + "&companyName=" + COMPANY_NAME;
    public static final String H5_USER_SECRET_PROTOCOL = H5_DOMAIN_NEW + "/activity/page/privacy-agreement.html" + "?shortCompanyName=" + SHORT_COMPANY_NAME + "&companyName=" + COMPANY_NAME + "&appName=" + APP_NAME;
    public static final String H5_USER_AUTH_PROTOCOL = H5_DOMAIN_NEW + "/activity/page/authorization-protocol.html" + "?shortCompanyName=" + SHORT_COMPANY_NAME + "&companyName=" + COMPANY_NAME + "&appName=" + APP_NAME;
    public static final String H5_USER_PROTOCOL = H5_DOMAIN_NEW + "/activity/page/user-protocol.html" + "?shortCompanyName=" + SHORT_COMPANY_NAME + "&companyName=" + COMPANY_NAME + "&appName=" + APP_NAME;

    /*魔蝎协议*/
    public static final String H5_USER_MOXIE_PROTOCOL = H5_DOMAIN + "/majiabao/moxie-protocol.html" + "?isApp=1&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    /*账单导入成功*/
    public static final String H5_LEADING_IN_RESULT_SUCCESS = H5_DOMAIN + "/export/success.html";
    /*账单导入失败*/
    public static final String H5_LEADING_IN_RESULT_FAILED = H5_DOMAIN + "/export/fail.html";
    /*账单导入帮助页面*/
    public static final String H5_HELP = H5_DOMAIN + "/help-v2.html" + "?isApp=1&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    /*办卡进度查询*/
    public static final String H5_CREDIT_CARD_PROGRESS = "http://www.huishuaka.com/5/coop/jinduchaxun.html?ichannelid=";

    /**
     * @param userId 用户ID isAppId 区分web前端还是app (区分平台)
     * @author xhb
     * @date 20180420
     * @desc 资讯模块 详情页面
     */
    public static String generateNewsUrl(String id, String userId) {
        return H5_NEWS_DETAIL + "?id=" + id + "&isApp=1&userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    /**
     * @param userId 用户ID isAppId 区分web前端还是app (区分平台)
     * @author xhb
     * @date 20180420
     * @desc 发现模块
     * <p>
     * 生成发现页链接
     */
    public static String generateNewsWebViewUrl(String userId, String channelId, String version) {
        return H5_FIND_WEVVIEW_DETAIL + "?isApp=1&userId=" + userId + "&packageId=" + channelId + "&version=" + version;
    }

    /*借款协议*/
    public static String generateLoanProtocol(String usrId) {
        return H5_DOMAIN_NEW + "/activity/page/loan-protocol.html" + "?shortCompanyName=" + SHORT_COMPANY_NAME + "&companyName=" + COMPANY_NAME + "&appName=" + APP_NAME + sufPublicParam(usrId);
    }

    /*创建活动的URL*/
    public static String generateActivityWebViewUrl(String userId, String channelId, String version) {
        return H5_ACTIVITY_WEVVIEW_DETAIL_COPY + "?isApp=1&userId=" + userId + "&packageId=" + channelId + "&version=" + version;
    }

    public static String generateProductUrl(String id) {
        return H5_LOAN_DETAIL + "?id=" + id + "&isApp=1" + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String generateInvitationUrl(String userId) {
        return H5_INVITATION + "?id=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String invitationActivityUrl(String userId) {
        return H5_INVITATION_ACTIVITY + "?id=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String invitationUrl(String userId) {
        //return H5_INVITE + "?userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
        return H5_INVITATION_CONTACTS + "?userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String missionUrl(String userId) {
        return H5_MISSION + "?userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String codeUrl(String userId, String phone) {
        return H5_CODE + "?userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME + "&phone=" + phone;
    }

    public static String guideInvite() {
        return H5_GUIDE_INVITE;
    }

    public static String generateInternalMessageUrl(String id) {
        return H5_INTERNAL_MESSAGE + "?id=" + id + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String generateTestUrl(String id) {
        return H5_TEST + "?isApp=1&id=" + id + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String generateCreditCardUrl(String id) {
        return H5_CREDIT_CARD_CENTER + "?isApp=1&userId=" + id + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String generateRewardPointsUrl(String id) {
        return H5_REWARD_POINTS + "?isApp=1&userId=" + id + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String generateDebtAnalyzeUrl(String id) {
        return H5_DEBT_ANALYZE + "?isApp=1&userId=" + id + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String generateOneKeyLoanUrl(List<String> ids, String userId) {
        StringBuilder sb = new StringBuilder();
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                sb.append(id).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return H5_ONE_KEY_LOAN + "?isApp=1&pids=" + sb.toString() + "&userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + VERSION_NAME;
    }

    public static String sufPublicParam(String userId) {
        return "&version=" + BuildConfig.VERSION_NAME + "&packageId=" + App.sChannelId + "&userId=" + userId;
    }
}