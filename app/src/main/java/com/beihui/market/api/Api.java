package com.beihui.market.api;


import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.api.interceptor.AccessHeadInterceptor;
import com.beihui.market.api.interceptor.PackageIdAndVersionNameInterceptor;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.AllDebt;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.entity.Avatar;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CalendarAbstract;
import com.beihui.market.entity.CalendarDebt;
import com.beihui.market.entity.CreditCard;
import com.beihui.market.entity.CreditCardBank;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.EBank;
import com.beihui.market.entity.FastDebtDetail;
import com.beihui.market.entity.HotLoanProduct;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.Invitation;
import com.beihui.market.entity.LoanBill;
import com.beihui.market.entity.LoanGroup;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.entity.Message;
import com.beihui.market.entity.MyProduct;
import com.beihui.market.entity.News;
import com.beihui.market.entity.Notice;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.NoticeDetail;
import com.beihui.market.entity.NutEmail;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.entity.Phone;
import com.beihui.market.entity.Profession;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.entity.SysMsgDetail;
import com.beihui.market.entity.TabImage;
import com.beihui.market.entity.ThirdAuthResult;
import com.beihui.market.entity.ThirdAuthorization;
import com.beihui.market.entity.UsedEmail;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.entity.request.XAccountInfo;
import com.beihui.market.util.LogUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {

    private static final String PLATFORM = "Android";

    private static Api sInstance;
    private ApiService service;

    public static Api getInstance() {
        if (sInstance == null) {
            synchronized (Api.class) {
                if (sInstance == null) {
                    sInstance = new Api(createHttpClient());
                }
            }
        }
        return sInstance;
    }

    /**
     * 自定义拦截器 通过拦截器获取日志
     */
    private static HttpLoggingInterceptor  interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印日志
            Log.e("OkHttp--->message: ", message);
        }
    });

    private static OkHttpClient createHttpClient() {
        File cacheFile = new File(App.getInstance().getCacheDir().getAbsolutePath(), "MarketCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(new PackageIdAndVersionNameInterceptor())
                .addInterceptor(new AccessHeadInterceptor());

        //设置拦截日志
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

    private Api(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    /**
     * 获取账单信息摘要
     * @version 3.0.0
     * @param userId 用户id
     */
    public Observable<ResultEntity<DebtAbstract>> queryTabAccountHeaderInfo(String userId, int billType) {
        return service.queryTabAccountHeaderInfo(userId, billType);
    }

    /**
     * 获取账单信息列表
     * @version 3.0.0
     * @param userId 用户Id
     * @param billStatus 账单状态 1-待还 2-已还 3-逾期
     * @param firstScreen 是不是首屏 true:是 false：不是
     * @param pageSize 一页记录数
     * @param pageNo 页码
     */
    public Observable<ResultEntity<List<XAccountInfo>>> queryTabAccountListInfo(String userId, int billStatus, boolean firstScreen, int pageNo, int pageSize) {
        return service.queryTabAccountListInfo(userId, billStatus, firstScreen, pageNo, pageSize);
    }
    /**
     * 获取账单信息列表
     * @version 3.0.0
     */
    public Observable<ResultEntity<List<XAccountInfo>>> queryTabAccountListInfo(String userId, boolean firstScreen) {
        return service.queryTabAccountListInfo(userId, firstScreen);
    }

    /**
     * 更新还款状态
     *
     * @param userId              用户id
     * @param liabilitiesDetailId 还款项目计划id
     * @param repayAmount         还款金额 默认全额
     * @param status              1-待还 2-已还
     */
    public Observable<ResultEntity> updateDebtStatus(String userId, String liabilitiesDetailId, double repayAmount, int status) {
        return service.updateDebtStatus(userId, liabilitiesDetailId, repayAmount, status);
    }

    public Observable<ResultEntity> updateDebtStatus(String userId, String liabilitiesDetailId, int status) {
        return service.updateDebtStatus(userId, liabilitiesDetailId, status);
    }

    /**
     * 获取信用卡账单详情
     *
     * @param userId   用户id
     * @param recordId 信用卡账单id
     */
    public Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(String userId, String recordId, String billId) {
        return service.fetchCreditCardDebtDetail(userId, recordId, billId);
    }
    public Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(String userId, String recordId) {
        return service.fetchCreditCardDebtDetail(userId, recordId);
    }


    /**
     * 网贷账单/信用卡备注更新
     * @param userId 用户id
     * @param remark 备注
     * @param recordId 网贷账单Id/信用卡Id
     * @param type 	类型 1-网贷 2-信用卡
     */
    public Observable<ResultEntity> updateLoanOrCreditCardRemark(String userId, String remark, String recordId, int type) {
        return service.updateLoanOrCreditCardRemark(userId, remark, recordId, type+"");
    }

    /**
     * 查询消息数
     */
    public Observable<ResultEntity<String>> queryMessage(String userId) {
        return service.queryMessage(userId);
    }

    /****************************************************************************** 分割线 **************************************************************************************/





    /**
     * 密码登录
     *
     * @param account 用户账号
     * @param pwd     用户密码
     */
    public Observable<ResultEntity<UserProfileAbstract>> login(String account, String pwd) {
        //注销channelID
//        return service.login(account, generatePwd(pwd, account), getChannelId(), 1);
        return service.login(account, generatePwd(pwd, account),  1);
    }

    /**
     * 验证码登录
     *
     * @param account 用户账号
     *                2 代表的是验证码登录
     */
    public Observable<ResultEntity<UserProfileAbstract>> loginByCode(String account, String verifyCode) {
        //注销channelID
//        return service.loginByCode(account, "2", verifyCode, getChannelId(), 1);
        return service.loginByCode(account, "2", verifyCode, 1);
    }

    /**
     * 登录
     *
     * @param account 用户账号
     */
    public Observable<ResultEntity<UserProfileAbstract>> loginNoPwd(String account) {
        //注销channelID
//        return service.loginNoPwd(account, getChannelId(), 1);
        return service.loginNoPwd(account, 1);
    }

    /**
     * 微信登录
     *
     * @param openId 微信openId
     */
    public Observable<ResultEntity<UserProfileAbstract>> loginWithWechat(String openId) {
        return service.loginWithWechat(openId);
    }

    /**
     * 请求注册验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRegisterSms(String phone) {
        //注销channelID
//        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_REGISTER, getChannelId());
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 请求重置密码验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRestPwdSms(String phone) {
        //注销channelID
//        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_RESET_PWD, getChannelId());
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 请求微信绑定验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestWeChatBindPwdSms(String phone) {
        //注销channelID
//        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_WE_CHAT_BIND, getChannelId());
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_WE_CHAT_BIND);
    }

    /**
     * 请求验证码 用于登录
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestPhoneLogin(String phone) {
        //注销channelID
//        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_LOGIN, getChannelId());
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_LOGIN);
    }

    /**
     * 验证注册验证码
     *
     * @param phone 请求的手机号
     * @param code  请求的验证码
     */
    public Observable<ResultEntity> verifyRegisterCode(String phone, String code) {
        return service.verifyCode(phone, code, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 验证重置密码验证码
     *
     * @param phone 请求的手机号
     * @param code  请求的验证码
     */
    public Observable<ResultEntity> verifyResetPwdCode(String phone, String code) {
        return service.verifyCode(phone, code, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 验证微信绑定验证码
     *
     * @param account  请求的手机号
     * @param wxOpenId 微信openId
     * @param code     请求的验证码
     */
    public Observable<ResultEntity<UserProfileAbstract>> verifyWeChatBindCode(String account, String wxOpenId, String wxName, String wxImage, String code) {
        //注销channelID
//        return service.verifyWeChatBindCode(account, wxOpenId, wxName, wxImage, RequestConstants.VERIFICATION_TYPE_WE_CHAT_BIND, code, getChannelId(), "1");
        return service.verifyWeChatBindCode(account, wxOpenId, wxName, wxImage, RequestConstants.VERIFICATION_TYPE_WE_CHAT_BIND, code, "1");
    }

    /**
     * 注册
     *
     * @param phone      注册手机号
     * @param pwd        用户密码
     * @param wxOpenId   微信openId
     * @param wxName     微信用户名
     * @param wxImage    微信头像
     * @param inviteCode 邀请码，可空
     */
    public Observable<ResultEntity<UserProfileAbstract>> register(String phone, String pwd, String wxOpenId, String wxName, String wxImage, String inviteCode) {
        //注销channelID
//        return service.register(RequestConstants.PLATFORM, phone, generatePwd(pwd, phone), wxOpenId, wxName, wxImage, inviteCode, getChannelId());
        return service.register(RequestConstants.PLATFORM, phone, generatePwd(pwd, phone), wxOpenId, wxName, wxImage, inviteCode);
    }

    /**
     * 重置密码
     *
     * @param account 用户手机号
     * @param pwd     新密码
     */
    public Observable<ResultEntity> resetPwd(String account, String pwd) {
        return service.updatePwd(null, account, RequestConstants.UPDATE_PWD_TYPE_RESET, generatePwd(pwd, account), null, null);
    }

    /**
     * 修改密码
     *
     * @param account   用户手机号
     * @param pwd       新密码
     * @param originPwd 原密码
     */
    public Observable<ResultEntity> updatePwd(String id, String account, String pwd, String originPwd) {
        String generatedPwd = generatePwd(pwd, account);
        return service.updatePwd(id, account, RequestConstants.UPDATE_PWD_TYPE_CHANGE,
                generatedPwd, generatePwd(originPwd, account), generatedPwd);
    }

    /**
     * 查询用户个人信息
     *
     * @param id 用户id
     */
    public Observable<ResultEntity<UserProfile>> queryUserProfile(String id) {
        return service.userProfile(id);
    }

    /**
     * 更新用户头像
     *
     * @param userId   用户id
     * @param fileName 文件名
     * @param avatar   头像
     */
    public Observable<ResultEntity<Avatar>> updateUserAvatar(String userId, String fileName, byte[] avatar) {
        String avatarBase64 = Base64.encodeToString(avatar, Base64.DEFAULT);
        return service.updateUserAvatar(userId, fileName, avatarBase64);
    }

    /**
     * 修改用户名
     *
     * @param id       用户id
     * @param username 新用户名
     */
    public Observable<ResultEntity> updateUsername(String id, String username) {
        return service.updateUsername(id, username);
    }

    /**
     * 获取职业列表
     *
     * @param id 用户id
     */
    public Observable<ResultEntity<ArrayList<Profession>>> queryProfession(String id) {
        return service.queryProfession(id);
    }

    /**
     * 更新用户职业
     *
     * @param id             用户id
     * @param professionType 职业类型
     */
    public Observable<ResultEntity> updateUserProfession(String id, int professionType) {
        return service.updateProfession(id, professionType);
    }

    /**
     * 退出登录
     *
     * @param id 用户id
     */
    public Observable<ResultEntity> logout(String id) {
        return service.logout(id);
    }


    /**
     * 消息中心-公告
     */
    public Observable<ResultEntity<NoticeAbstract>> queryNoticeHome() {
        return service.noticeHome();
    }

    /**
     * 查询公告列表
     *
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<Notice>> queryNoticeList(int pageNum, int pageSize) {
        return service.noticeList(pageNum, pageSize);
    }

    /**
     * 查询公告详情
     *
     * @param id 公告id
     */
    public Observable<ResultEntity<NoticeDetail>> queryNoticeDetail(String id) {
        return service.noticeDetail(id);
    }

    /**
     * 消息中心-系统消息
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<SysMsgAbstract>> querySysMsgHome(String userId) {
        return service.sysMsgHome(userId);
    }

    /**
     * 查询系统消息列表
     *
     * @param userId   用户id
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<SysMsg>> querySysMsgList(String userId, int pageNum, int pageSize) {
        return service.sysMsgList(userId, pageNum, pageSize);
    }

    /**
     * 查询系统消息详情
     *
     * @param id 消息id
     */
    public Observable<ResultEntity<SysMsgDetail>> querySysMsgDetail(String id) {
        return service.sysMsgDetail(id);
    }

    /**
     * 资讯列表
     *
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<News>> queryNews(int pageNum, int pageSize) {
        return service.queryNews(pageNum, pageSize);
    }

    /**
     * 站内信
     */
    public Observable<ResultEntity<List<Message>>> queryMessage(int pageNum, int pageSize) {
        return service.queryMessages(pageNum, pageSize);
    }

    /**
     * 查询启动页广告，banner，弹窗广告
     *
     * @param supernatantType 查询类型
     */
    public Observable<ResultEntity<List<AdBanner>>> querySupernatant(int supernatantType) {
        String channelId = "unknown";
        try {
            channelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //注销channelID
//        return service.querySupernatant(RequestConstants.PLATFORM, supernatantType, channelId);
        return service.querySupernatant(RequestConstants.PLATFORM, supernatantType);
    }

    /**
     * 查询头条消息
     */
    public Observable<ResultEntity<List<String>>> queryBorrowingScroll() {
        return service.queryBorrowingScroll();
    }

    /**
     * 查询首页热门资讯
     */
    public Observable<ResultEntity<List<HotNews>>> queryHotNews() {
        return service.queryHotNews();
    }

    /**
     * 查询首页热门产品
     *
     * @param pageNo 查询页数
     */
    public Observable<ResultEntity<HotLoanProduct>> queryHotProduct(int pageNo) {
        return service.queryHotProduct(pageNo, PLATFORM);
    }

    /**
     * 查询一键借款的资质
     *
     * @param userId 用户id
     * @param ids    需要查询的产品id
     */
    public Observable<ResultEntity<List<String>>> queryOneKeyLoanQuality(String userId, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            sb.append(id).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return service.queryOneKeyLoanQuality(userId, sb.toString());
    }

    /**
     * 查询首页精选产品
     *
     * @param pageNo   查询页数
     * @param pageSize 查询每页大小
     * @param sortType 排序方式
     */
    public Observable<ResultEntity<LoanProduct>> queryChoiceProduct(int pageNo, int pageSize, int sortType) {
        return service.queryChoiceProduct(pageNo, pageSize, sortType, PLATFORM);
    }

    /**
     * 查询首页全部推荐信用卡
     *
     * @param userId 用户id
     * @param flag   查询分组，normal-一般 hot-热门卡片 selected-精选 index-首页 renqi-人气推荐
     *               固定查询renqi
     */
    public Observable<ResultEntity<CreditCard>> queryCreditCards(String userId, String flag) {
        return service.queryRecommendedCreditCards(userId, flag);
    }

    /**
     * 查询信用卡详情
     *
     * @param userId 用户id
     * @param cardId 信用卡id
     */
    public Observable<ResultEntity<CreditCard.Row>> queryCreditCardDetail(String userId, String cardId) {
        return service.queryCreditCardDetail(userId, cardId);
    }


    /**
     * 查询个人推荐产品提示语
     */
    public Observable<ResultEntity<List<String>>> queryLoanHint() {
        return service.queryLoanHint();
    }

    /**
     * 查询个性推荐产品分组
     */
    public Observable<ResultEntity<List<LoanGroup>>> queryLoanGroup() {
        return service.queryLoanGroup();
    }

    /**
     * 查询个性推荐产品
     *
     * @param groupId  个人推荐group id
     * @param pageNo   查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<LoanProduct>> queryPersonalProducts(String groupId, String userId, int pageNo, int pageSize) {
        return service.queryPersonalProducts(groupId, userId, pageNo, pageSize, PLATFORM);
    }

    /**
     * 查询智能推荐产品
     *
     * @param amount   目标金额
     * @param dueTime  借款期限
     * @param sortType 排序规则
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<LoanProduct>> queryLoanProduct(double amount, int dueTime, int sortType, int pageNum, int pageSize) {
        return service.queryLoanProduct(amount, dueTime + "", sortType, pageNum, pageSize, PLATFORM);
    }

    /**
     * 查询贷款产品详情
     *
     * @param id     产品id
     * @param userId 用户id
     */
    public Observable<ResultEntity<LoanProductDetail>> queryLoanProductDetail(String id, String userId) {
        return service.queryLoanProductDetail(id, userId);
    }

    /**
     * 查询授权产品信息
     *
     * @param userId 用户id
     * @param pids   产品id
     */
    public Observable<ResultEntity<List<ThirdAuthorization>>> queryThirdAuthorization(String userId, String pids) {
        return service.queryThirdAuthorization(userId, pids);
    }

    /**
     * 确认授权第三方产品
     *
     * @param userId 用户id
     * @param pids   产品id
     */
    public Observable<ResultEntity> authorize(String userId, String pids) {
        return service.authorize(userId, pids);
    }

    /**
     * 查询第三方产品授权结果
     *
     * @param userId 用户id
     * @param pids   产品id
     */
    public Observable<ResultEntity<ThirdAuthResult>> authorizationResult(String userId, String pids) {
        return service.authorizationResult(userId, pids);
    }

    /**
     * 查询相关推荐产品
     *
     * @param amount 相关金额
     */
    public Observable<ResultEntity<LoanProduct>> queryRecommendProduct(int amount) {
        return service.queryRecommendProduct(amount, PLATFORM);
    }

    /**
     * 添加或者删除收藏（产品，资讯）
     *
     * @param userId    用户id
     * @param productId 产品id
     * @param status    0.删除 1.添加
     */
    public Observable<ResultEntity> addOrDeleteCollection(String userId, String productId, int status) {
        return service.addOrDeleteCollection(userId, productId, status);
    }

    /**
     * 查询产品收藏
     *
     * @param userId   用户id
     * @param pageNo   查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<LoanProduct>> queryProductionCollection(String userId, int pageNo, int pageSize) {
        return service.queryProductionCollection(userId, pageNo, pageSize);
    }

    /**
     * 查询我的产品
     *
     * @param userId   用户id
     * @param pageNo   查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<MyProduct>> queryMyProduct(String userId, int pageNo, int pageSize) {
        return service.queryMyProduct(userId, pageNo, pageSize);
    }

    /**
     * 获取我的账单
     *
     * @param userId   用户id
     * @param billType 账单类型，1-网贷账单，2-信用卡
     * @param pageNo   查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<LoanBill>> fetchMyLoanBill(String userId, int billType, int pageNo, int pageSize) {
        return service.fetchLoanBill(userId, billType, pageNo, pageSize);
    }

    /**
     * 查询邀请详细
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<Invitation>> queryInvitation(String userId) {
        return service.queryInvitation(userId);
    }

    /**
     * 查询版本更新
     */
    public Observable<ResultEntity<AppUpdate>> queryUpdate() {
        String channelId = "unknown";
        try {
            channelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //注销channelID
//        return service.queryAppUpdate(RequestConstants.PLATFORM + "", channelId);
        return service.queryAppUpdate(RequestConstants.PLATFORM + "");
    }

    /**
     * 提交用户反馈
     *
     * @param userId  用户id
     * @param content 反馈内容
     */
    public Observable<ResultEntity> submitFeedback(String userId, String content, byte[] image, String imageName) {
        String imageBase64 = null;
        if (image != null && image.length > 0) {
            imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
        }
        return service.submitFeedback(userId, content, imageBase64, imageName);
    }

    /**
     *  确认记账 快捷记账的api
     */
    public Observable<ResultEntity> saveFastDebt(Map<String, Object> params) {
        return service.saveFastDebt(params);
    }

    /**
     * 更新信用卡账单还款状态
     *
     * @param userId 用户id
     * @param billId	分期账单Id
     * @param recordId 账单Id
     * @param status 还款状态，状态 0-删除, 1-待还 2-已还
     * @param amount 还款金额
     */
    public Observable<ResultEntity> updateFastDebtBillStatus(String userId, String billId, String recordId, int status, Double amount) {
        if (amount == null ) {
            return service.updateFastDebtBillStatus(userId, billId, recordId, status);
        } else {
            return service.updateFastDebtBillStatus(userId, billId, recordId, status, amount);
        }
    }


    /**
     * 更新信用卡账单还款状态
     *
     * @param userId 用户id
     * @param billId	分期账单Id
     * @param recordId 账单Id
     */
    public Observable<ResultEntity<FastDebtDetail>> queryFastDebtBillDetail(String userId, String billId, String recordId) {
        return service.updateFastDebtBillStatus(userId, billId, recordId);
    }


    /*****************************************************记账*******************************************************/
    /**
     * 查询记账首页宣传图片
     */
    public Observable<ResultEntity<String>> queryPropaganda() {
        return service.queryPropaganda();
    }

    /**
     * 获取账单信息摘要
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<DebtAbstract>> fetchDebtAbstractInfo(String userId, int billType) {
        return service.fetchDebtAbstractInfo(userId, billType);
    }


    /**
     * 查询全部借款
     *
     * @param userId   用户id
     * @param status   借款状态 1-待还 2-已还 3-全部
     * @param pageNo   查询开始页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<AllDebt>> queryAllDebt(String userId, int status, int pageNo, int pageSize) {
        return service.queryAllDebt(userId, status, pageNo, pageSize);
    }

    /**
     * 查询记账渠道
     */
    public Observable<ResultEntity<LinkedHashMap<String, List<DebtChannel>>>> queryLoanChannel() {
        return service.queryLoanChannel();
    }

    /**
     * 查询网贷平台渠道
     */
    public Observable<ResultEntity<List<DebtChannel>>> fetchDebtSourceChannel() {
        return service.fetchDebtSourceChannel(1);
    }

    /**
     * 确认记账，保存账单
     */
    public Observable<ResultEntity<PayPlan>> saveDebt(Map<String, Object> params) {
        return service.saveDebt(params);
    }

    /**
     * 确认记账，不返回还款计划，直接插入数据
     */
    public Observable<ResultEntity> saveDebtImmediately(Map<String, Object> params) {
        return service.saveDebtImmediately(params);
    }


    /**
     * 获取银行列表
     */
    public Observable<ResultEntity<List<CreditCardBank>>> fetchCreditCardBankList() {
        return service.fetchCreditCardBankList();
    }

    /**
     * 手动添加信用卡账单
     */
    public Observable<ResultEntity> saveCreditCardDebt(String userId, String cardNums, long bankId, String realName, int billDay, int dueDay, double amount) {
        return service.saveCreditCardDebt(userId, cardNums, bankId, realName, billDay, dueDay, amount);
    }

    /**
     * 更新信用卡账单信息
     *
     * @param userId  用户id
     * @param cardId  信用卡id
     * @param billDay 账单日
     * @param dueDay  还款日
     * @param amount  账单金额
     */
    public Observable<ResultEntity> updateCreditCardDebt(String userId, String cardId, int billDay, int dueDay, double amount) {
        return service.updateCreditCardDebt(userId, cardId, billDay, dueDay, amount);
    }

    /**
     * 获取网贷账单详情
     *
     * @param userId        用户id
     * @param liabilitiesId 网贷账单id
     */
    public Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(String userId, String liabilitiesId) {
        return service.fetchLoanDebtDetail(userId, liabilitiesId);
    }

    public Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(String userId, String liabilitiesId, String billId) {
        if (billId == null) {
            return service.fetchLoanDebtDetail(userId, liabilitiesId);
        } else {
            return service.fetchLoanDebtDetail(userId, liabilitiesId, billId);
        }
    }



    /**
     * 获取信用卡详情月份账单
     *
     * @param userId   用户id
     * @param recordId 信用卡账单id
     * @param pageNo   当前查询页
     * @param pageSize 当前页大小
     */
    public Observable<ResultEntity<List<CreditCardDebtBill>>> fetchCreditCardDebtBill(String userId, String recordId, int pageNo, int pageSize) {
        return service.fetchCreditCardDebtBill(userId, recordId, pageNo, pageSize);
    }

    /**
     * 获取信用卡月份账单详情
     *
     * @param userId 用户id
     * @param billId 月份账单id
     */
    public Observable<ResultEntity<List<BillDetail>>> fetchCreditCardBillDetail(String userId, String billId) {
        return service.fetchCreditCardBillDetail(userId, billId);
    }

    /**
     * 更新月份账单金额
     *
     * @param userId 用户id
     * @param billId 月份账单id
     * @param cardId 信用卡账单id
     * @param amount 账单金额
     */
    public Observable<ResultEntity> updateMonthBillAmount(String userId, String billId, String cardId, double amount) {
        return service.updateMonthBillAmount(userId, cardId, billId, amount);
    }

    /**
     * 更新信用卡账单还款状态
     *
     * @param userId 用户id
     * @param cardId 信用卡id
     * @param billId 账单id
     * @param status 还款状态，1-待还，2-已还
     */
    public Observable<ResultEntity> updateCreditCardBillStatus(String userId, String cardId, String billId, int status) {
        return service.updateCreditCardDebtBillStatus(userId, cardId, billId, status);
    }

    /**
     * 更新账单提醒状态
     *
     * @param userId    用户id
     * @param channelId 渠道id，网贷账单需要
     * @param cardId    信用卡id，信用卡账单需要
     * @param day       -1-不提醒，1-提前1天，2-提前2天
     */
    public Observable<ResultEntity> updateRemindStatus(String userId, String channelId, String cardId, int day) {
        return service.updateDebtRemindStatus(userId, channelId, cardId, day);
    }

    /**
     * 删除信用卡账单
     *
     * @param userId   用户id
     * @param recordId 账单id
     */
    public Observable<ResultEntity> deleteCreditCardDebt(String userId, String recordId) {
        return service.deleteCreditCardDebt(userId, recordId, 0);
    }

    /**
     * 删除借款
     *
     * @param userId        用户id
     * @param liabilitiesId 借款项目id
     */
    public Observable<ResultEntity> deleteDebt(String userId, String liabilitiesId) {
        return service.deleteDebt(userId, liabilitiesId);
    }

    /**
     * 获取首页账单信息
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<List<AccountBill>>> fetchAccountBills(String userId) {
        return service.fetchAccountBills(userId);
    }

    /**
     * 更新账单是否在首页显示
     *
     * @param userId   用户id
     * @param recordId 账单记录id
     * @param type     账单类型 1-网贷，2-信用卡
     * @param hide     是否隐藏 0-隐藏，1-不隐藏
     */
    public Observable<ResultEntity> updateDebtVisibility(String userId, String recordId, int type, int hide) {
        return service.updateDebtVisibility(userId, recordId, type, hide);
    }

    /**
     * 查询日历模式账单记录月份摘要
     *
     * @param userId    用户id
     * @param beginDate 查询开始日期
     * @param endDate   查询结束日期
     */
    public Observable<ResultEntity<CalendarAbstract>> fetchCalendarAbstract(String userId, String beginDate, String endDate) {
        return service.fetchCalendarAbstract(userId, beginDate, endDate);
    }

    /**
     * 查询日历模式账单记录月份趋势
     *
     * @param userId    用户id
     * @param beginDate 查询开始日期
     * @param endDate   查询结束日期
     */
    public Observable<ResultEntity<Map<String, Float>>> fetchCalendarTrend(String userId, String beginDate, String endDate) {
        return service.fetchCalendarTrend(userId, beginDate, endDate);
    }

    /**
     * 查询日历模式账单记录
     *
     * @param userId    用户id
     * @param beginDate 查询开始日期
     * @param endDate   查询结束日期
     */
    public Observable<ResultEntity<CalendarDebt>> fetchCalendarDebt(String userId, String beginDate, String endDate) {
        return service.fetchCalendarDebt(userId, beginDate, endDate);
    }





    /**
     * 查询底部栏图标
     */
    public Observable<ResultEntity<List<TabImage>>> queryBottomImage() {
        String version = "";
        String packageId = "";
        try {
            version = App.getInstance().getPackageManager().getPackageInfo(App.getInstance().getPackageName(), 0).versionName;
            packageId =  App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //注销packageId
//        return service.queryBottomImage(version, "1", packageId);
        return service.queryBottomImage(version, "1");
    }

    /**
     * 查询积分总额
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<Integer>> queryTotalRewardPoints(String userId) {
        return service.queryTotalRewardPoints(userId);
    }

    /**
     * 查询积分任务信息
     *
     * @param userId   用户id
     * @param taskName 任务名称
     */
    public Observable<ResultEntity<List<RewardPoint>>> queryRewardPoints(String userId, String taskName) {
        return service.queryRewardPoint(userId, taskName);
    }

    /**
     * 标记积分任务已读
     *
     * @param recordId 任务id
     */
    public Observable<ResultEntity> sendReadPointRead(String recordId) {
        return service.sendRewardPointRead(recordId, 1);
    }

    /**
     * 添加积分任务
     *
     * @param userId 用户id
     * @param taskId 积分任务id
     */
    public Observable<ResultEntity> addRewardPoint(String userId, String taskId) {
        return service.addRewardPoint(userId, taskId);
    }

    /**
     * 获取网银导入地址
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<EBank>> fetchEBankUrl(String userId) {
        return service.fetchEBankUrl(userId);
    }

    /**
     * 获取用户的邮箱列表
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<List<UsedEmail>>> fetchUsedEmail(String userId) {
        return service.fetchUsedEmail(userId);
    }

    /**
     * 获取坚果邮箱列表
     */
    public Observable<ResultEntity<List<NutEmail>>> fetchNutEmail() {
        return service.fetchNutEmail();
    }

    /**
     * 查询信用卡账单采集结果
     *
     * @param userId 用户id
     * @param email  采集邮箱
     */
    public Observable<ResultEntity<Boolean>> pollLeadInResult(String userId, String email) {
        return service.pollLeadInResult(userId, email);
    }

    /*****+******************************************************按钮显示***********************************************+*****+*****/
    /**
     * 查询菜单，按钮是否显示
     *
     * @param flag 需要确定状态的菜单，按钮
     */
    public Observable<ResultEntity<Boolean>> queryMenuVisible(String flag) {
        return service.queryMenuVisible(flag);
    }

    /*****+******************************************************个推绑定***********************************************+*****+*****/

    /**
     * 绑定个推用户账号
     *
     * @param userId   用户id
     * @param clientId 本机个推id
     */
    public Observable<ResultEntity> bindClientId(String userId, String clientId) {
        return service.bindClientId(userId, clientId);
    }

    /**
     * 个推消息点击次数统计
     *
     * @param userId    用户id
     * @param messageId 消息id
     */
    public Observable<ResultEntity> sendPushClicked(String userId, String messageId) {
        return service.onPushClicked(userId, messageId);
    }


    /*****+******************************************************数据统计***********************************************+*****+*****/

    /**
     * 点击第三方产品外链
     *
     * @param userId 用户Id
     * @param id     产品id
     */
    public Observable<ResultEntity> onProductClicked(String userId, String id) {
        return service.onProductClicked(userId, id);
    }

    /**
     * 点击广告，包括启动页，弹窗，banner
     *
     * @param id     广告id
     * @param userId 用户id，可为空
     * @param type   广告类型
     */
    public Observable<ResultEntity> onAdClicked(String id, String userId, int type) {
        return service.onAdClicked(id, userId, type);
    }

    /**
     * 统计站内信点击次数
     *
     * @param id 站内信id
     */
    public Observable<ResultEntity> onInternalMessageClicked(String id) {
        return service.onInternalMessageClicked(id);
    }

    /**
     * 数据统计，各类pv/uv
     *
     * @param id     事件id
     * @param userId 用户Id
     */
    public Observable<ResultEntity> onCountUv(String id, String userId) {
        return service.onCountUv(id, userId);
    }

    /**************+***************************************************+******************************************************/

    private String getChannelId() {
        String channelId = "unknown";
        try {
            channelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelId;
    }

    /*****generate method*****/
    //加密密码
    private String generatePwd(String pwd, String account) {
        String sha = new String(Hex.encodeHex(DigestUtils.sha512(pwd)));
        String md5 = new String(Hex.encodeHex(DigestUtils.md5(sha + account)));
        return md5.toUpperCase();
    }
}
