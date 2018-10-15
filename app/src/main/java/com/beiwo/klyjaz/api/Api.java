package com.beiwo.klyjaz.api;


import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.api.interceptor.AccessHeadInterceptor;
import com.beiwo.klyjaz.entity.AccountFlowIconBean;
import com.beiwo.klyjaz.entity.AdBanner;
import com.beiwo.klyjaz.entity.AllDebt;
import com.beiwo.klyjaz.entity.AnalysisChartBean;
import com.beiwo.klyjaz.entity.AppUpdate;
import com.beiwo.klyjaz.entity.Audit;
import com.beiwo.klyjaz.entity.Avatar;
import com.beiwo.klyjaz.entity.BillDetail;
import com.beiwo.klyjaz.entity.BillLoanAnalysisBean;
import com.beiwo.klyjaz.entity.BillState;
import com.beiwo.klyjaz.entity.BillSummaryBean;
import com.beiwo.klyjaz.entity.BlackList;
import com.beiwo.klyjaz.entity.CalendarAbstract;
import com.beiwo.klyjaz.entity.CalendarDebt;
import com.beiwo.klyjaz.entity.CreateAccountReturnIDsBean;
import com.beiwo.klyjaz.entity.CreditBill;
import com.beiwo.klyjaz.entity.CreditCard;
import com.beiwo.klyjaz.entity.CreditCardBank;
import com.beiwo.klyjaz.entity.CreditCardBean;
import com.beiwo.klyjaz.entity.CreditCardDebtBill;
import com.beiwo.klyjaz.entity.CreditCardDebtDetail;
import com.beiwo.klyjaz.entity.DebeDetailRecord;
import com.beiwo.klyjaz.entity.DebtAbstract;
import com.beiwo.klyjaz.entity.DebtChannel;
import com.beiwo.klyjaz.entity.DebtDetail;
import com.beiwo.klyjaz.entity.DetailHead;
import com.beiwo.klyjaz.entity.DetailList;
import com.beiwo.klyjaz.entity.EBank;
import com.beiwo.klyjaz.entity.EventBean;
import com.beiwo.klyjaz.entity.FastDebtDetail;
import com.beiwo.klyjaz.entity.GroupProductBean;
import com.beiwo.klyjaz.entity.HomeData;
import com.beiwo.klyjaz.entity.HotLoanProduct;
import com.beiwo.klyjaz.entity.HotNews;
import com.beiwo.klyjaz.entity.Invitation;
import com.beiwo.klyjaz.entity.LastNoticeBean;
import com.beiwo.klyjaz.entity.LoanAccountIconBean;
import com.beiwo.klyjaz.entity.LoanBill;
import com.beiwo.klyjaz.entity.LoanGroup;
import com.beiwo.klyjaz.entity.LoanProduct;
import com.beiwo.klyjaz.entity.LoanProductDetail;
import com.beiwo.klyjaz.entity.Message;
import com.beiwo.klyjaz.entity.MyProduct;
import com.beiwo.klyjaz.entity.News;
import com.beiwo.klyjaz.entity.Notice;
import com.beiwo.klyjaz.entity.NoticeAbstract;
import com.beiwo.klyjaz.entity.NoticeDetail;
import com.beiwo.klyjaz.entity.NutEmail;
import com.beiwo.klyjaz.entity.PayAccount;
import com.beiwo.klyjaz.entity.Phone;
import com.beiwo.klyjaz.entity.Profession;
import com.beiwo.klyjaz.entity.PurseBalance;
import com.beiwo.klyjaz.entity.RemindBean;
import com.beiwo.klyjaz.entity.RewardPoint;
import com.beiwo.klyjaz.entity.SysMsg;
import com.beiwo.klyjaz.entity.SysMsgAbstract;
import com.beiwo.klyjaz.entity.SysMsgDetail;
import com.beiwo.klyjaz.entity.TabAccountNewBean;
import com.beiwo.klyjaz.entity.TabImageBean;
import com.beiwo.klyjaz.entity.ThirdAuthResult;
import com.beiwo.klyjaz.entity.ThirdAuthorization;
import com.beiwo.klyjaz.entity.Ticket;
import com.beiwo.klyjaz.entity.UsedEmail;
import com.beiwo.klyjaz.entity.UserTopicBean;
import com.beiwo.klyjaz.entity.UserInfoBean;
import com.beiwo.klyjaz.entity.UserProfile;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.entity.Withdraw;
import com.beiwo.klyjaz.entity.WithdrawRecord;
import com.beiwo.klyjaz.entity.request.RequestConstants;
import com.beiwo.klyjaz.jjd.bean.BankCard;
import com.beiwo.klyjaz.jjd.bean.BankName;
import com.beiwo.klyjaz.jjd.bean.CashOrder;
import com.beiwo.klyjaz.jjd.bean.CashUserInfo;
import com.beiwo.klyjaz.loan.Product;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.DraftEditForumBean;
import com.beiwo.klyjaz.social.bean.DraftsBean;
import com.beiwo.klyjaz.social.bean.PraiseBean;
import com.beiwo.klyjaz.social.bean.PraiseListBean;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;

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
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;

import static com.beiwo.klyjaz.api.NetConstants.SECOND_PRODUCT;

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

    /*自定义拦截器 通过拦截器获取日志*/
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            if (BuildConfig.API_ENV)
                Log.i("OkHttp--->message: ", message);//打印日志
        }
    });

    private static OkHttpClient createHttpClient() {
        File cacheFile = new File(App.getInstance().getCacheDir().getAbsolutePath(), "MarketCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(new AccessHeadInterceptor());

        builder.addNetworkInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY));
        return builder.build();
    }

    private Api(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(RsaGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    /*获取网贷记账图标*/
    public Observable<ResultEntity<List<LoanAccountIconBean>>> queryLoanAccountIcon(String userId) {
        return service.queryLoanAccountIcon(userId);
    }

    /*获取网贷记账图标*/
    public Observable<ResultEntity<List<LoanAccountIconBean>>> queryLoanAccountIcon(String userId, String searchContent) {
        return service.queryLoanAccountIcon(userId, searchContent);
    }

    /*创建通用账单*/
    public Observable<ResultEntity<CreateAccountReturnIDsBean>> createNormalAccount(Map<String, Object> params) {
        return service.createNormalAccount(params);
    }

    /*创建通用账单*/
    public Observable<ResultEntity<CreateAccountReturnIDsBean>> createLoanAccount(Map<String, Object> params) {
        return service.createLoanAccount(params);
    }

    /*获取通用记账图标列表*/
    public Observable<ResultEntity<List<AccountFlowIconBean>>> queryIconList(String userId, String type) {
        return service.queryIconList("1", userId, type);
    }

    /*删除通用记账图标*/
    public Observable<ResultEntity> deleteLoanAccountIcon(String tallyId) {
        return service.deleteLoanAccountIcon(tallyId);
    }

    /*获取通用记账图标列表*/
    public Observable<ResultEntity<List<AccountFlowIconBean>>> queryCustomIconList() {
        return service.queryCustomIconList("1", "LCustom");
    }

    /*保存自定义图标*/
    public Observable<ResultEntity> saveCustomIcon(String userId, String iconName, String iconId, String type) {
        return service.saveCustomIcon(userId, iconName, iconId, type);
    }

    /*魔蝎银行列表*/
    public Observable<ResultEntity<List<CreditCardBean>>> queryBankList() {
        return service.queryBankList();
    }

    /*魔蝎银行列表*/
    public Observable<ResultEntity<List<TabAccountNewBean>>> queryTabAccountList(String userId, int collectType, int pageNo, int pageSize) {
        return service.queryTabAccountList(userId, collectType, pageNo, pageSize);
    }

    /*魔蝎银行列表*/
    public Observable<ResultEntity<List<TabAccountNewBean>>> queryTabAccountList(String userId, int collectType) {
        return service.queryTabAccountList(userId, collectType);
    }

    /*分组贷超产品列表*/
    public Observable<ResultEntity<List<GroupProductBean>>> queryGroupProductList() {
        return service.queryGroupProductList(SECOND_PRODUCT);
    }

    /*进入贷超产品详情*/
    public Observable<ResultEntity<String>> queryGroupProductSkip(String userId, String productId) {
        return service.queryGroupProductSkip(userId, productId);
    }

    /*贷超产品页面跳转（原生)*/
    public Observable<ResultEntity> proSkip(@Field("productId") String productId) {
        return service.proSkip(productId);
    }

    /*绑定手机号*/
    public Observable<ResultEntity> updateBindPhone(String account, String phone, String verifyCode) {
        return service.updateBindPhone(account, phone, verifyCode, "7");
    }

    /*绑定微信号*/
    public Observable<ResultEntity> bindWXChat(String userId, String unionId) {
        return service.bindWXChat(userId, unionId);
    }

    /*解绑微信号*/
    public Observable<ResultEntity> unBindWXChat(String userId) {
        return service.unBindWXChat(userId);
    }

    /*修改网贷备注*/
    public Observable<ResultEntity> updateLoanDebtBillRemark(String userId, String recordId, String remark) {
        return service.updateLoanDebtBillRemark(userId, recordId, remark);
    }

    /*修改通用备注*/
    public Observable<ResultEntity> updateFastDebtBillRemark(String userId, String recordId, String remark) {
        return service.updateFastDebtBillRemark(userId, recordId, remark);
    }

    /*修改信用卡备注*/
    public Observable<ResultEntity> updateCreditCardBillRemark(String userId, String recordId, String remark) {
        return service.updateCreditCardBillRemark(userId, recordId, remark);
    }

    /*获取账单信息摘要*/
    public Observable<ResultEntity<DebtAbstract>> queryTabAccountHeaderInfo(String userId, int billType) {
        return service.queryTabAccountHeaderInfo(userId, billType);
    }

    /*更新还款状态*/
    public Observable<ResultEntity> updateDebtStatus(String userId, String liabilitiesDetailId, double repayAmount, int status) {
        return service.updateDebtStatus(userId, liabilitiesDetailId, repayAmount, status);
    }

    public Observable<ResultEntity> updateDebtStatus(String userId, String liabilitiesDetailId, int status) {
        return service.updateDebtStatus(userId, liabilitiesDetailId, status);
    }

    /*更新账单状态*/
    public Observable<ResultEntity<BillState>> updateStatus(String userId, String billId, int status, int type) {
        if (type == 0) {
            return service.updateNormalStatus(userId, billId, status);
        } else {
            return service.updateStatus(userId, billId, status);
        }
    }

    /*获取信用卡账单详情*/
    public Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(String userId, String recordId, String billId) {
        return service.fetchCreditCardDebtDetail(userId, recordId, billId);
    }

    /*查询消息数*/
    public Observable<ResultEntity<String>> queryMessage(String userId) {
        return service.queryMessage(userId);
    }

    /*删除借款*/
    public Observable<ResultEntity> deleteFastDebt(String userId, String recordId) {
        return service.deleteFastDebt(userId, recordId);
    }

    /* 网贷分析 柱状图*/
    public Observable<ResultEntity<List<AnalysisChartBean>>> queryAnalysisOverviewChart(String userId, int type, String startTime, String endTime) {
        return service.queryAnalysisOverviewChart(userId, type, startTime, endTime);
    }

    /*网贷分析 列表数据*/
    public Observable<ResultEntity<BillLoanAnalysisBean>> queryAnalysisOverviewList(String userId, int type, String time) {
        return service.queryAnalysisOverviewList(userId, type, time);
    }

    /*获取最新公告*/
    public Observable<ResultEntity<LastNoticeBean>> getNewNotice() {
        return service.getNewNotice();
    }

    /*查询网贷账单的还款记录*/
    public Observable<ResultEntity<List<DebeDetailRecord>>> getDebeDetailRecord(String userId, String billId) {
        return service.getDebeDetailRecord(userId, billId);
    }

    /*查询快捷账单的还款记录*/
    public Observable<ResultEntity<List<DebeDetailRecord>>> getFastDetailRecord(String userId, String billId) {
        return service.getFastDetailRecord(userId, billId);
    }

    /*密码登录*/
    public Observable<ResultEntity<UserProfileAbstract>> login(String account, String pwd) {
        return service.login(account, generatePwd(pwd, account), 1);
    }

    /**
     * 验证码登录
     *
     * @param account 用户账号
     *                2 代表的是验证码登录
     */
    public Observable<ResultEntity<UserProfileAbstract>> loginByCode(String account, String verifyCode) {
        return service.loginByCode(account, "2", verifyCode, 1);
    }

    /**
     * 微信登录
     *
     * @param openId 微信openId
     */
    public Observable<ResultEntity<UserProfileAbstract>> loginWithWechat(String openId) {
        return service.loginWithWechat(openId, 1);
    }

    /**
     * 请求注册验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRegisterSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 请求重置密码验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRestPwdSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 请求微信绑定验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestWeChatBindPwdSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_WE_CHAT_BIND);
    }

    /**
     * 请求验证码 用于登录
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestPhoneLogin(String phone) {
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
        return service.queryAppUpdate(RequestConstants.PLATFORM + "");
    }

    /*提交用户反馈*/
    public Observable<ResultEntity> submitFeedback(String userId, String content, String contactWay, byte[] image, String imageName) {
        String imageBase64 = null;
        if (image != null && image.length > 0) {
            imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
        }
        if (TextUtils.isEmpty(contactWay)) {
            return service.submitFeedback(userId, content, imageBase64, imageName);
        } else {
            return service.submitFeedback(userId, content, contactWay, imageBase64, imageName);
        }
    }

    /*确认记账 快捷记账的api*/
    public Observable<ResultEntity> saveFastDebt(Map<String, Object> params) {
        return service.saveFastDebt(params);
    }

    /*更新信用卡账单还款状态*/
    public Observable<ResultEntity> updateFastDebtBillStatus(String userId, String billId, String recordId, int status, Double amount) {
        if (amount == null) {
            return service.updateFastDebtBillStatus(userId, billId, recordId, status);
        } else {
            return service.updateFastDebtBillStatus(userId, billId, recordId, status, amount);
        }
    }


    /*更新信用卡账单还款状态*/
    public Observable<ResultEntity<FastDebtDetail>> queryFastDebtBillDetail(String userId, String billId, String recordId) {
        return service.updateFastDebtBillStatus(userId, billId, recordId);
    }

    /*查询全部借款*/
    public Observable<ResultEntity<AllDebt>> queryAllDebt(String userId, int status, int pageNo, int pageSize) {
        return service.queryAllDebt(userId, status, pageNo, pageSize);
    }

    /*查询记账渠道*/
    public Observable<ResultEntity<LinkedHashMap<String, List<DebtChannel>>>> queryLoanChannel() {
        return service.queryLoanChannel();
    }

    /*确认记账，不返回还款计划，直接插入数据*/
    public Observable<ResultEntity> saveDebtImmediately(Map<String, Object> params) {
        return service.saveDebtImmediately(params);
    }


    /*获取银行列表*/
    public Observable<ResultEntity<List<CreditCardBank>>> fetchCreditCardBankList() {
        return service.fetchCreditCardBankList();
    }

    /*手动添加信用卡账单*/
    public Observable<ResultEntity> saveCreditCardDebt(String userId, String cardNums, long bankId, String realName, int billDay, int dueDay, double amount) {
        return service.saveCreditCardDebt(userId, cardNums, bankId, realName, billDay, dueDay, amount);
    }

    /*更新信用卡账单信息*/
    public Observable<ResultEntity> updateCreditCardDebt(String userId, String cardId, int billDay, int dueDay, double amount) {
        return service.updateCreditCardDebt(userId, cardId, billDay, dueDay, amount);
    }

    /*获取网贷账单详情*/
    public Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(String userId, String liabilitiesId, String billId) {
        if (billId == null) {
            return service.fetchLoanDebtDetail(userId, liabilitiesId);
        } else {
            return service.fetchLoanDebtDetail(userId, liabilitiesId, billId);
        }
    }

    /* 获取信用卡详情月份账单*/
    public Observable<ResultEntity<List<CreditCardDebtBill>>> fetchCreditCardDebtBill(String userId, String recordId, int pageNo, int pageSize) {
        return service.fetchCreditCardDebtBill(userId, recordId, pageNo, pageSize);
    }

    /*获取信用卡月份账单详情*/
    public Observable<ResultEntity<List<BillDetail>>> fetchCreditCardBillDetail(String userId, String billId) {
        return service.fetchCreditCardBillDetail(userId, billId);
    }

    /*更新月份账单金额*/
    public Observable<ResultEntity> updateMonthBillAmount(String userId, String billId, String cardId, double amount) {
        return service.updateMonthBillAmount(userId, cardId, billId, amount);
    }

    /*更新信用卡账单还款状态*/
    public Observable<ResultEntity> updateCreditCardBillStatus(String userId, String cardId, String billId, int status) {
        return service.updateCreditCardDebtBillStatus(userId, billId, status);
    }

    /*更新信用卡账单还款状态*/
    public Observable<ResultEntity> updateCreditCardBillStatus(String userId, String billId, int status) {
        return service.updateCreditCardDebtBillStatus(userId, billId, status);
    }

    /*更新账单提醒状态*/
    public Observable<ResultEntity> updateRemindStatus(String userId, String type, String recordId, int day) {
        return service.updateDebtRemindStatus(userId, type, recordId, day);
    }

    /*删除信用卡账单*/
    public Observable<ResultEntity> deleteCreditCardDebt(String userId, String recordId) {
        return service.deleteCreditCardDebt(userId, recordId, 0);
    }

    /*删除借款*/
    public Observable<ResultEntity> deleteDebt(String userId, String liabilitiesId) {
        return service.deleteDebt(userId, liabilitiesId);
    }

    /*更新账单是否在首页显示*/
    public Observable<ResultEntity> updateDebtVisibility(String userId, String recordId, int type, int hide) {
        return service.updateDebtVisibility(userId, recordId, type, hide);
    }

    /*查询日历模式账单记录月份摘要*/
    public Observable<ResultEntity<CalendarAbstract>> fetchCalendarAbstract(String userId, String beginDate, String endDate) {
        return service.fetchCalendarAbstract(userId, beginDate, endDate);
    }

    /*查询日历模式账单记录月份趋势*/
    public Observable<ResultEntity<Map<String, Float>>> fetchCalendarTrend(String userId, String beginDate, String endDate) {
        return service.fetchCalendarTrend(userId, beginDate, endDate);
    }

    /*查询日历模式账单记录*/
    public Observable<ResultEntity<CalendarDebt>> fetchCalendarDebt(String userId, String beginDate, String endDate) {
        return service.fetchCalendarDebt(userId, beginDate, endDate);
    }

    /*查询底部栏图*/
    public Observable<ResultEntity<TabImageBean>> queryBottomImage() {
        return service.queryBottomImage(1);
    }

    /*查询积分总额*/
    public Observable<ResultEntity<Integer>> queryTotalRewardPoints(String userId) {
        return service.queryTotalRewardPoints(userId);
    }

    /*查询积分任务信息*/
    public Observable<ResultEntity<List<RewardPoint>>> queryRewardPoints(String userId, String taskName) {
        return service.queryRewardPoint(userId, taskName);
    }

    /*标记积分任务已读*/
    public Observable<ResultEntity> sendReadPointRead(String recordId) {
        return service.sendRewardPointRead(recordId, 1);
    }

    /*添加积分任务*/
    public Observable<ResultEntity> addRewardPoint(String userId, String taskId) {
        return service.addRewardPoint(userId, taskId);
    }

    /*获取网银导入地址*/
    public Observable<ResultEntity<EBank>> fetchEBankUrl(String userId) {
        return service.fetchEBankUrl(userId);
    }

    /*获取用户的邮箱列表*/
    public Observable<ResultEntity<List<UsedEmail>>> fetchUsedEmail(String userId) {
        return service.fetchUsedEmail(userId);
    }

    /*获取坚果邮箱列表*/
    public Observable<ResultEntity<List<NutEmail>>> fetchNutEmail() {
        return service.fetchNutEmail();
    }

    /*查询信用卡账单采集结果*/
    public Observable<ResultEntity<Boolean>> pollLeadInResult(String userId, String email) {
        return service.pollLeadInResult(userId, email);
    }

    /*查询菜单，按钮是否显示*/
    public Observable<ResultEntity<Boolean>> queryMenuVisible(String flag) {
        return service.queryMenuVisible(flag);
    }

    /*绑定个推用户账号*/
    public Observable<ResultEntity> bindClientId(String userId, String clientId) {
        return service.bindClientId(userId, clientId);
    }

    /*个推消息点击次数统计*/
    public Observable<ResultEntity> sendPushClicked(String userId, String messageId) {
        return service.onPushClicked(userId, messageId);
    }

    /*点击第三方产品外链*/
    public Observable<ResultEntity> onProductClicked(String userId, String id) {
        return service.onProductClicked(userId, id);
    }

    /*点击广告，包括启动页，弹窗，banner*/
    public Observable<ResultEntity> onAdClicked(String id, String userId, int type) {
        return service.onAdClicked(id, userId, type);
    }

    /*统计站内信点击次数*/
    public Observable<ResultEntity> onInternalMessageClicked(String id) {
        return service.onInternalMessageClicked(id);
    }

    /*数据统计，各类pv/uv*/
    public Observable<ResultEntity> onCountUv(String type, String userId) {
        return service.onCountUv(type, userId);
    }

    /*账单汇总*/
    public Observable<ResultEntity<BillSummaryBean>> onBillSummary(String id, String pageNo) {
        return service.onBillSummary(id, pageNo);
    }

    /*消息标记为已读*/
    public Observable<ResultEntity> onReadAll(String id) {
        return service.onReadAll(id);
    }

    /*清空消息*/
    public Observable<ResultEntity> onDeleteMessageAll(String id) {
        return service.onDeleteMessageAll(id);
    }

    /*提醒查询*/
    public Observable<ResultEntity<RemindBean>> onRemindInfo(String id) {
        return service.onRemindInfo(id);
    }

    /* 提醒设置*/
    public Observable<ResultEntity> onRemindSetting(String id, String geTui, String sms, String wechat, String day) {
        return service.onRemindSetting(id, geTui, sms, wechat, day);
    }

    /*v4.2.0首页数据*/
    public Observable<ResultEntity<HomeData>> home(String userId, int pageNo) {
        return service.home(userId, pageNo);
    }

    /*v4.2.0活动入口*/
    public Observable<ResultEntity<EventBean>> homeEvent(String location, int port) {
        return service.homeEvent(location, port);
    }

    /*v4.2.3活动*/
    public Observable<ResultEntity<List<EventBean>>> isShowOwnerActive(String location, int port) {
        return service.isShowOwnerActive(location, port);
    }

    /*获取网贷配置图标*/
    public Observable<ResultEntity<List<LoanAccountIconBean>>> netIcon() {
        return service.netIcon();
    }

    /*账单详情头部----网贷*/
    public Observable<ResultEntity<DetailHead>> detailHead(String userId, String recordId) {
        return service.detailHead(userId, recordId);
    }

    /*账单详情头部----通用*/
    public Observable<ResultEntity<DetailHead>> commonDetailHead(String userId, String recordId) {
        return service.commonDetailHead(userId, recordId);
    }

    /*账单详情列表----网贷*/
    public Observable<ResultEntity<DetailList>> detailList(String userId, String recordId, int pageNo, int pageSize) {
        return service.detailList(userId, recordId, pageNo, pageSize);
    }

    /*账单详情列表----通用*/
    public Observable<ResultEntity<DetailList>> commonDetailList(String userId, String recordId, int pageNo, int pageSize) {
        return service.commonDetailList(userId, recordId, pageNo, pageSize);
    }

    /*结清全部----网贷*/
    public Observable<ResultEntity> closeAll(String userId, String recordId) {
        return service.closeAll(userId, recordId);
    }

    /*结清全部----通用*/
    public Observable<ResultEntity> commonCloseAll(String userId, String recordId) {
        return service.commonCloseAll(userId, recordId);
    }

    /*账单列表----信用卡*/
    public Observable<ResultEntity<List<CreditBill>>> creditList(String userId, String recordId) {
        return service.creditList(userId, recordId, 1, 100);
    }

    /*账单详情列表*/
    public Observable<ResultEntity<List<BillDetail>>> billDetail(String userId, String billId) {
        return service.billDetail(userId, billId);
    }

    /*更新信用卡状态 删除*/
    public Observable<ResultEntity> deleteCredit(String userId, String recordId, int status) {
        return service.deleteCredit(userId, recordId, status);
    }

    /*钱包*/
    public Observable<ResultEntity<PurseBalance>> purseBalance(String userId) {
        return service.purseBalance(userId);
    }

    /*提现记录列表*/
    public Observable<ResultEntity<WithdrawRecord>> withdrawRecord(Map<String, Object> map) {
        return service.withdrawRecord(map);
    }

    /*获取支付宝账户*/
    public Observable<ResultEntity<List<PayAccount>>> payAccount(String userId) {
        return service.payAccount(userId);
    }

    /*保存支付宝账户*/
    public Observable<ResultEntity<PayAccount>> saveAlpAccount(Map<String, Object> map) {
        return service.saveAlpAccount(map);
    }

    /*提现*/
    public Observable<ResultEntity<Withdraw>> withdraw(Map<String, Object> map) {
        return service.withdraw(map);
    }

    /*发票列表*/
    public Observable<ResultEntity<List<Ticket>>> tickets(String userId) {
        return service.tickets(userId);
    }

    /*保存/编辑发票*/
    public Observable<ResultEntity<Ticket>> saveTicket(Map<String, Object> map) {
        return service.saveTicket(map);
    }

    /*删除发票*/
    public Observable<ResultEntity> deleteTicket(String userId, long invoiceId) {
        return service.deleteTicket(userId, invoiceId);
    }

    /*信用查询*/
    public Observable<ResultEntity<BlackList>> queryCredit(Map<String, Object> map) {
        return service.queryCredit(map);
    }

    /*重复-获取短信验证码*/
    public Observable<ResultEntity<Phone>> requestSms(String phone, String type) {
        return service.requestSms(phone, type);
    }

    /*获取短信内容*/
    public Observable<ResultEntity<String>> getInviteMsg(String userId) {
        return service.getInviteMsg(userId);
    }

    /*获取短信内容*/
    public Observable<ResultEntity> shareUser(String userId) {
        return service.shareUser(userId);
    }

    /*分享朋友圈回调*/
    public Observable<ResultEntity> uploadImg(String userId, String phone, String imgType, String activeName, byte[] image) {
        String imageBase64 = "";
        if (image != null && image.length > 0) {
            imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);

        }
        return service.uploadImg(userId, phone, imgType, activeName, imageBase64);
    }

    /*查询用户个人信息*/
    public Observable<ResultEntity<UserInfoBean>> queryUserInfo(String userId) {
        return service.queryUserInfo(userId);
    }

    /*查询用户发表的文章*/
    public Observable<ResultEntity<List<UserTopicBean>>> queryUserTopicInfo(String userId, int pageNo, int pageSize) {
        return service.queryUserTopicInfo(userId, pageNo, pageSize);
    }

    /*generate method*/
    //加密密码
    private String generatePwd(String pwd, String account) {
        String sha = new String(Hex.encodeHex(DigestUtils.sha512(pwd)));
        String md5 = new String(Hex.encodeHex(DigestUtils.md5(sha + account)));
        return md5.toUpperCase();
    }


    public Observable<ResultEntity<Audit>> audit() {
        return service.audit();
    }

    /************************************社区************************************/
    /*查询推荐话题*/
    public Observable<ResultEntity<SocialTopicBean>> queryRecommendTopic(String userId, int pageNo, int pageSize) {
//        return service.queryRecommendTopic(pageNo, pageSize);
        return service.queryRecommendTopic(userId, pageNo, pageSize);
    }

    /*查询推荐话题*/
    public Observable<ResultEntity<SocialTopicBean>> queryRecommendTopic(Map<String, Object> mMap) {
        return service.queryRecommendTopic(mMap);
    }

//    /*图片上传*/
//    public Observable<ResultEntity<String>> uploadFourmImg(String base64) {
//        String avatarBase64 = Base64.encodeToString(avatar, Base64.DEFAULT);
//        return service.uploadFourmImg(base64);
//    }

    /*图片上传*/
    public Observable<ResultEntity<String>> uploadFourmImg(byte[] bytes) {
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return service.uploadFourmImg(base64);
    }

    /**
     * 发布话题
     *
     * @param userId
     * @param imgKey
     * @param forumTitle
     * @param forumContent
     * @param status
     * @param topicId
     * @return
     */
    public Observable<ResultEntity> publicForumInfo(String userId, String imgKey, String forumTitle,
                                                    String forumContent, String status, String topicId, String forumId) {
        return service.publicForumInfo(userId, imgKey, forumTitle, forumContent, status, topicId, forumId);

    }

    /**
     * 获取草稿箱动态编辑
     *
     * @param forumId
     * @return
     */
    public Observable<ResultEntity<DraftEditForumBean>> fetchEditForum(String forumId) {
        return service.fetchEditForum(forumId);
    }


    /*产品列表查询*/
    public Observable<ResultEntity<List<Product>>> products(Map<String, Object> map) {
        return service.products(map);
    }

    /**
     * 查询评论回复列表
     *
     * @param forumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Observable<ResultEntity<List<CommentReplyBean>>> queryCommentList(String forumId, int pageNo, int pageSize) {
        return service.queryCommentList(forumId, pageNo + "", pageSize + "");
    }

    /**
     * 评论回复
     *
     * @param userId
     * @param commentType
     * @param commentContent
     * @param forumId
     * @param toUserId
     * @param selfId
     * @return
     */
    public Observable<ResultEntity> fetchReplyForumInfo(String userId, String commentType, String commentContent, String forumId, String toUserId, String selfId) {
        return service.fetchReplyForumInfo(userId, commentType, commentContent, forumId, toUserId, selfId);
    }

    /**
     * 评论回复
     *
     * @param mMap
     * @return
     */
    public Observable<ResultEntity> fetchReplyForumInfo(Map<String, Object> mMap) {
        return service.fetchReplyForumInfo(mMap);
    }

    /**
     * 提交举报信息
     *
     * @param userId
     * @param linkId
     * @param reportType
     * @param reportContent
     * @return
     */
    public Observable<ResultEntity> fetchSaveReport(String userId, String linkId, String reportType, String reportContent) {
        return service.fetchSaveReport(userId, linkId, reportType, reportContent);
    }

    /**
     * 删除动态
     *
     * @param forumId
     * @return
     */
    public Observable<ResultEntity> fetchCancelForum(String forumId) {
        return service.fetchCancelForum(forumId);
    }

    /**
     * 删除评论回复
     *
     * @param replyId
     * @return
     */
    public Observable<ResultEntity> fetchCancelReply(String replyId) {
        return service.fetchCancelReply(replyId);
    }

    /**
     * 社区评论回复点赞
     *
     * @param praiseType
     * @param forumReplyId
     * @param userId
     * @return
     */
    public Observable<ResultEntity<PraiseBean>> fetchClickPraise(int praiseType, String forumReplyId, String userId) {
        return service.fetchClickPraise(praiseType, forumReplyId, userId);
    }

    /**
     * 社区评论回复取消点赞
     *
     * @param praiseType
     * @param forumReplyId
     * @param userId
     * @return
     */
    public Observable<ResultEntity> fetchCancelPraise(int praiseType, String forumReplyId, String userId) {
        return service.fetchCancelPraise(praiseType, forumReplyId, userId);
    }


    /**
     * 保存用户信息
     *
     * @param mMap
     * @return
     */
    public Observable<ResultEntity> fetchSaveUserInfo(Map<String, Object> mMap) {
        return service.fetchSaveUserInfo(mMap);
    }


    /**
     * 查询草稿箱
     *
     * @param mMap
     * @return
     */
    public Observable<ResultEntity<List<DraftsBean>>> queryCenterForum(Map<String, Object> mMap) {
        return service.queryCenterForum(mMap);
    }

    /**
     * 查询待审核
     *
     * @param mMap
     * @return
     */
    public Observable<ResultEntity<List<DraftsBean>>> queryCenterForumAudit(Map<String, Object> mMap) {
        return service.queryCenterForumAudit(mMap);
    }

    /*** 查询点赞列表 */
    public Observable<ResultEntity<List<PraiseListBean>>> queryPraiseList(Map<String, Object> mMap) {
        return service.queryPraiseList(mMap);
    }

    /*** 查询评论列表 */
    public Observable<ResultEntity<List<PraiseListBean>>> queryCommentList(Map<String, Object> mMap) {
        return service.queryCommentList(mMap);
    }


    /*用户认证信息查询*/
    public Observable<ResultEntity<CashUserInfo>> userAuth(String userId) {
        return service.userAuth(userId);
    }

    /*用户订单状态检查*/
    public Observable<ResultEntity<CashOrder>> cashOrder(String userId) {
        return service.cashOrder(userId);
    }

    /*银行卡列表*/
    public Observable<ResultEntity<List<BankCard>>> cardList(String userId) {
        return service.cardList(userId);
    }

    /*卡号获取银行名称*/
    public Observable<ResultEntity<BankName>> bankName(String cardNo) {
        return service.bankName(cardNo);
    }

    /*保存更新银行卡*/
    public Observable<ResultEntity> saveCard(Map<String, Object> map) {
        return service.saveCard(map);
    }

    /*查询银行卡(重新编辑)*/
    public Observable<ResultEntity<BankCard>> queryCard(String userId, String cardId) {
        return service.queryCard(userId, cardId);
    }

    /*保存借款订单*/
    public Observable<ResultEntity<CashOrder>> saveCashOrder(Map<String, Object> map) {
        return service.saveCashOrder(map);
    }

    /*实名认证*/
    public Observable<ResultEntity> fetchVertifyIDCard(String userId, String userName, String idCard) {
        return service.fetchVertifyIDCard(userId, userName, idCard);
    }

    /*保存联系人*/
    public Observable<ResultEntity> fetchSaveContact(String userId, String userContact, String userRelate, String mobileNum) {
        return service.fetchSaveContact(userId, userContact, userRelate, mobileNum);
    }
}