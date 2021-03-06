package com.beiwo.qnejqaz.umeng;


public class Events {
    /********************************注册，认证*********************************************/
    /**
     * 进入登录界面
     */
    public static final String ENTER_LOGIN = "login_enter";
    /**
     * 登录界面-登录
     */
    public static final String LOGIN_LOGIN = "login_login";
    /**
     * 登录界面-注册
     */
    public static final String LOGIN_REGISTER = "login_register";
    /**
     * 登录界面-取消
     */
    public static final String LOGIN_CANCEL = "login_cancel";
    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "login_success";
    /**
     * 登录失败
     */
    public static final String LOGIN_FAILED = "login_failed";

    /**
     * 进入注册页面
     */
    public static final String ENTER_REGISTER = "register_enter";
    /**
     * 注册页面获取验证码
     */
    public static final String REGISTER_GET_VERIFY = "register_get_verification";
    /**
     * 注册页面获取验证码成功
     */
    public static final String REGISTER_GET_VERIFY_SUCCESS = "register_get_verification_success";
    /**
     * 注册页面获取验证码失败
     */
    public static final String REGISTER_GET_VERIFY_FAILED = "register_get_verification_failed";
    /**
     * 注册页面进入下一步
     */
    public static final String REGISTER_NEXT_STEP = "register_next_step";
    /**
     * 注册验证码成功
     */
    public static final String REGISTER_VERIFICATION_SUCCESS = "register_verification_success";
    /**
     * 注册验证码失败
     */
    public static final String REGISTER_VERIFICATION_FAILED = "register_verification_failed";
    /**
     * 注册成功
     */
    public static final String REGISTER_SUCCESS = "register_success";
    /**
     * 注册失败
     */
    public static final String REGISTER_FAILED = "register_failed";


    /********************************首页*********************************************/
    /**
     * 进入首页Tab
     */
    public static final String ENTER_HOME_PAGE = "home_enter";
    /**
     * banner-点击
     */
    public static final String CLICK_BANNER = "click_banner";
    /**
     * 首页广告弹窗出现
     */
    public static final String RESUME_AD_DIALOG = "resume_ad_dialog";
    /**
     * 首页广告弹窗-点击
     */
    public static final String CLICK_AD_DIALOG = "click_ad_dialog";
    /**
     * 首页-换一换-点击
     */
    public static final String CLICK_REFRESH_HOT = "click_refresh_hot";
    /**
     * 首页热门产品-点击
     */
    public static final String CLICK_HOT_PRODUCT = "click_hot_product";
    /**
     * 首页-一键借款-点击
     */
    public static final String CLICK_ONE_KEY_LOAN = "click_one_key_loan";
    /**
     * 首页-精选产品-点击
     */
    public static final String CLICK_CHOICE_PRODUCT = "click_choice_product";
    /**
     * 精选产品-进入
     */
    public static final String RESUME_CHOICE_PRODUCT = "resume_choice_product";
    /**
     * 首页点击测身价
     */
    public static final String HOME_CLICK_TEST = "home_click_test";
    /**
     * 借款攻略-点击
     */
    public static final String CLICK_HOT_NEWS = "click_hot_news";
    /**
     * 相关推荐-一键借款进入
     */
    public static final String RESUME_RELEVANT_PRODUCT_FROM_ONE_KEY_LOAN = "resume_relevant_prodcut_from_one_key_loan";

    /********************************借款*********************************************/
    /**
     * tab-借款-点击
     */
    public static final String CLICK_TAB_LOAN = "click_tab_loan";
    /**
     * 精选好借-进入
     */
    public static final String RESUME_PERSONAL_PRODUCT = "resume_personal_product";
    /**
     * 智能推荐-进入
     */
    public static final String RESUME_SMART_PRODUCT = "resume_smart_product";
    /**
     * 分组-点击
     */
    public static final String CLICK_GROUP = "click_group";
    /**
     * 产品详情页-进入
     */
    public static final String ENTER_LOAN_DETAIL_PAGE = "loanDetail_enter";
    /**
     * 产品详情页-我要借款-点击
     */
    public static final String LOAN_DETAIL_CLICK_LOAN = "loanDetail_click_loan";
    /**
     * 产品详情页-收藏-点击
     */
    public static final String CLICK_LOAN_COLLECT = "click_loan_collect";
    /**
     * 产品详情页-取消收藏-点击
     */
    public static final String CLICK_LOAN_DELETE_COLLECTION = "click_loan_delete_collection";
    /**
     * 产品详情页-分享
     */
    public static final String LOAN_DETAIL_CLICK_SHARE = "loanDetail_click_share";
    /**
     * 产品详情页-授权-进入
     */
    public static final String LOAN_ENTER_AUTHORIZE = "loan_enter_authorize";
    /**
     * 产品详情页-授权-确认-点击
     */
    public static final String LOAN_AUTHORIZE_CONFIRM = "loan_authorization_confirm";
    /**
     * 相关推荐-进入
     */
    public static final String RESUME_RECOMMEND_PRODUCT = "resume_recommend_product";
    /**
     * 产品列表Tab-借款金额
     */
    public static final String LOAN_CLICK_AMOUNT_FILTER = "loan_click_amount_filter";
    /**
     * 产品列表Tab-借款时间
     */
    public static final String LOAN_CLICK_TIME_FILTER = "loan_click_time_filter";
    /**
     * 产品列表Tab-排序
     */
    public static final String LOAN_CLICK_SORT = "loan_click_sort";
    /**
     * 相关推荐-产品详情页进入
     */
    public static final String RESUME_RELEVANT_PRODUCT_FROM_DETAIL = "resume_relevant_product_from_detail";
    /**
     * 相关推荐-产品点击
     */
    public static final String CLICK_RELEVANT_PRODUCT_ITEM = "click_relevant_product_item";

    /********************************资讯*********************************************/
    /**
     * 进入资讯Tab
     */
    public static final String ENTER_NEWS_PAGE = "news_enter";
    /**
     * 进入资讯详情页
     */
    public static final String ENTER_NEWS_DETAIL = "newsDetail_enter";
    /**
     * 资讯详情页-分享
     */
    public static final String NEWS_DETAIL_SHARE = "newsDetail_click_share";

    /********************************我的*********************************************/
    /**
     * 进入我的Tab
     */
    public static final String ENTER_MINE_PAGE = "mine_enter";
    /**
     * 在线客服-点击
     */
    public static final String CLICK_CONTACT_KEFU = "click_contact_kefu";
    /**
     * 消息-点击
     */
    public static final String MINE_CLICK_MESSAGE = "mine_click_message";
    /**
     * 我的借款-点击
     */
    public static final String CLICK_MY_PRODUCT = "click_my_product";
    /**
     * 我的借款产品-点击
     */
    public static final String CLICK_MY_PRODUCT_ITEM = "click_my_product_item";
    /**
     * 我的收藏-点击
     */
    public static final String CLICK_MY_COLLECTION = "click_my_collection";
    /**
     * 我的收藏产品-点击
     */
    public static final String CLICK_MY_COLLECTION_ITEM = "click_my_collection_item";
    /**
     * 邀请好友-点击
     */
    public static final String MINE_CLICK_INVITATION = "mine_click_invitation";
    /**
     * 微信公众号-点击
     */
    public static final String CLICK_WECHAT = "click_wechat";
    /**
     * 微信公众号-去微信-点击
     */
    public static final String CLICK_WECHAT_GO = "click_wechat_go";
    /**
     * 设置-安全退出-点击
     */
    public static final String SETTING_EXIT = "setting_exit";
    /**
     * 安全退出-确认-点击
     */
    public static final String EXIT_CONFIRM = "exit_confirm";
    /**
     * 安全退出-取消-点击
     */
    public static final String EXIT_DISMISS = "exit_dismiss";
    /**
     * 我的Tab-帮助反馈
     */
    public static final String MINE_CLICK_HELP_FEEDBACK = "mine_click_help_feedback";
    /**
     * 我的Tab-设置
     */
    public static final String MINE_CLICK_SETTING = "mine_click_setting";

    /**
     * 邀请好友-邀请
     */
    public static final String INVITATION_INVITE = "invitation_invite";
    /**
     * 设置-修改密码-点击
     */
    public static final String SETTING_CHANGE_PASSWORD = "setting_change_password";
    /**
     * 修改密码-确认
     */
    public static final String CHANGE_PASSWORD_CONFIRM = "changePassword_confirm";
    /**
     * 修改密码-成功
     */
    public static final String CHANGE_PASSWORD_SUCCESS = "change_psd_success";
    /**
     * 修改密码-失败
     */
    public static final String CHANGE_PASSWORD_FAILED = "change_psd_failed";
}