package com.beihui.market.api;

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
import com.beihui.market.entity.request.XAccountInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static com.beihui.market.api.NetConstants.BASE_PATH;
import static com.beihui.market.api.NetConstants.PRODUCT_PATH;

/**
 * @author xhb
 * 请求接口
 */
public interface ApiService {

    /**
     * @author xhb
     * 获取头信息 以及未还总额 已还总额
     * 获取账单信息摘要
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/unRepayBill")
    Observable<ResultEntity<DebtAbstract>> queryTabAccountHeaderInfo(@Field("userId") String userId, @Field("billType") int billType);

    /**
     * @author xhb
     * 获取头信息 以及未还总额 已还总额
     * 获取账单信息摘要
     * @param userId 用户Id
     * @param billStatus 账单状态 1-待还 2-已还 3-逾期
     * @param firstScreen 是不是首屏 true:是 false：不是
     * @param pageSize 一页记录数
     * @param pageNo 页码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/bill/index")
    Observable<ResultEntity<List<XAccountInfo>>> queryTabAccountListInfo(@Field("userId") String userId, @Field("billStatus") int billStatus, @Field("firstScreen") boolean firstScreen, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/bill/index")
    Observable<ResultEntity<List<XAccountInfo>>> queryTabAccountListInfo(@Field("userId") String userId, @Field("firstScreen") boolean firstScreen);


    /**
     * 更新还款状态
     * @param userId 用户ID
     * @param liabilitiesDetailId 	网贷分期账单Id 每一期的ID
     * @param repayAmount 还款金额 默认全额
     * @param status 	状态 1-待还 2-已还
     */

    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/newUpdateRepayStatus")
    Observable<ResultEntity> updateDebtStatus(@Field("userId") String userId, @Field("liabilitiesDetailId") String liabilitiesDetailId, @Field("repayAmount") double repayAmount, @Field("status") int status);

    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/newUpdateRepayStatus")
    Observable<ResultEntity> updateDebtStatus(@Field("userId") String userId, @Field("liabilitiesDetailId") String liabilitiesDetailId, @Field("status") int status);

    /**
     * 获取网贷账单详情
     * @param userId 用户ID
     * @param liabilitiesId 	账单Id
     * @param billId 网贷分期账单Id 每一期的ID
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/newQueryloan")
    Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(@Field("userId") String userId, @Field("liabilitiesId") String liabilitiesId, @Field("billId") String billId);

    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/newQueryloan")
    Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(@Field("userId") String userId, @Field("liabilitiesId") String liabilitiesId);

    /**
     * 获取信用卡账单详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/bill")
    Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(@Field("userId") String userId, @Field("recordId") String recordId, @Field("billId") String billId);

    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/bill")
    Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(@Field("userId") String userId, @Field("recordId") String recordId);


    /****************************************************************************** 分割线 **************************************************************************************/


    /**
     * 密码登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> login(@Field("account") String account, @Field("pwd") String pwd, @Field("packageId") String packageId, @Field("platform") int platform);

    /**
     * 密码登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> loginByCode(@Field("account") String account, @Field("loginType") String loginType, @Field("verifyCode") String verifyCode, @Field("packageId") String packageId, @Field("platform") int platform);

    /**
     * 免密码登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> loginNoPwd(@Field("account") String account, @Field("packageId") String packageId, @Field("platform") int platform);

    /**
     * 微信登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/wx/login")
    Observable<ResultEntity<UserProfileAbstract>> loginWithWechat(@Field("openId") String openId);

    /**
     * 请求验证码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/sms/sendSms")
    Observable<ResultEntity<Phone>> requestSms(@Field("phone") String phone, @Field("type") String type, @Field("packageId") String packageId);

    /**
     * 验证验证码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/verificationCodeVerify")
    Observable<ResultEntity> verifyCode(@Field("account") String account, @Field("verificationCode") String verificationCode,
                                        @Field("verificationCodeType") String verificationCodeType);

    /**
     * 校验微信绑定验证码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/wx/newAuthVerify")
    Observable<ResultEntity<UserProfileAbstract>> verifyWeChatBindCode(@Field("account") String account, @Field("wxOpenId") String wxOpenId, @Field("wxName") String wxName, @Field("wxImage") String wxImage,
                                                                       @Field("verificationCodeType") String type, @Field("verificationCode") String code, @Field("packageId") String packageId, @Field("platform") String platform);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/register")
    Observable<ResultEntity<UserProfileAbstract>> register(@Field("platform") int platform, @Field("account") String account, @Field("pwd") String pwd,
                                                           @Field("wxOpenId") String exOpenId, @Field("wxName") String wxName, @Field("wxImage") String wxImage,
                                                           @Field("inviteCode") String inviteCode, @Field("packageId") String packageId);

    /**
     * 更新密码，重置或者修改
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/updatePwd")
    Observable<ResultEntity> updatePwd(@Field("userId") String id, @Field("account") String account, @Field("pwdType") int pwdType,
                                       @Field("pwd") String pwd, @Field("originPwd") String originPwd,
                                       @Field("pwd2") String pwd2);

    /**
     * 用户个人中心信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/personalCenter")
    Observable<ResultEntity<UserProfile>> userProfile(@Field("userId") String id);


    @FormUrlEncoded
    @POST(BASE_PATH + "/attach/uploadUserHeadPortrait")
    Observable<ResultEntity<Avatar>> updateUserAvatar(@Field("userId") String userId, @Field("fileName") String fileName,
                                                      @Field("fileBase64") String fileBase64);

    /**
     * 修改用户名
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/updateNickName")
    Observable<ResultEntity> updateUsername(@Field("userId") String id, @Field("userName") String userName);

    /**
     * 获取职业列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/showProfession")
    Observable<ResultEntity<ArrayList<Profession>>> queryProfession(@Field("userId") String id);

    /**
     * 修改职业
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/updateProfession")
    Observable<ResultEntity> updateProfession(@Field("userId") String id, @Field("professionType") int professionType);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/logout")
    Observable<ResultEntity> logout(@Field("userId") String id);

    /**
     * 消息中心-公告
     */
    @POST(BASE_PATH + "/notice/home")
    Observable<ResultEntity<NoticeAbstract>> noticeHome();

    /**
     * 公告列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/notice/list")
    Observable<ResultEntity<Notice>> noticeList(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 公告详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/notice/details")
    Observable<ResultEntity<NoticeDetail>> noticeDetail(@Field("id") String id);

    /**
     * 消息中心-系统消息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/systemMessage/home")
    Observable<ResultEntity<SysMsgAbstract>> sysMsgHome(@Field("userId") String userId);

    /**
     * 系统消息列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/systemMessage/list")
    Observable<ResultEntity<SysMsg>> sysMsgList(@Field("userId") String userId, @Field("pageNo") int pageNum,
                                                @Field("pageSize") int pageSize);

    /**
     * 系统消息详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/systemMessage/details")
    Observable<ResultEntity<SysMsgDetail>> sysMsgDetail(@Field("id") String id);

    /**
     * 资讯列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/information/list")
    Observable<ResultEntity<News>> queryNews(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 站内信
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/pushInfo/queryMessage")
    Observable<ResultEntity<List<Message>>> queryMessages(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 启动页广告，banner，弹窗广告
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/supernatant/querySupernatant")
    Observable<ResultEntity<List<AdBanner>>> querySupernatant(@Field("port") int port, @Field("supernatantType") int supernatantType,
                                                              @Field("packageId") String packageId);

    /**
     * 头条滚动信息
     */
    @POST(BASE_PATH + "/supernatant/queryBorrowingScroll")
    Observable<ResultEntity<List<String>>> queryBorrowingScroll();

    /**
     * 获取首页热门资讯
     */
    @GET(BASE_PATH + "/information/hotList")
    Observable<ResultEntity<List<HotNews>>> queryHotNews();

    /**
     * 查看首页热门产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/dynamicList")
    Observable<ResultEntity<HotLoanProduct>> queryHotProduct(@Field("pageNo") int pageNo, @Field("platform") String platform);

    /**
     * 查询一键借款资质
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/queryButton")
    Observable<ResultEntity<List<String>>> queryOneKeyLoanQuality(@Field("userId") String userId, @Field("pids") String pids);

    /**
     * 查询精选产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/selectedList")
    Observable<ResultEntity<LoanProduct>> queryChoiceProduct(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize, @Field("type") int sortType,
                                                             @Field("platform") String platform);

    /**
     * 查询首页推荐信用卡
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/creditCard/cardGroupList")
    Observable<ResultEntity<CreditCard>> queryRecommendedCreditCards(@Field("userId") String userId, @Field("flag") String flag);

    /**
     * 查询信用卡详情，用作PV，UV
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/creditCard/byId")
    Observable<ResultEntity<CreditCard.Row>> queryCreditCardDetail(@Field("userId") String userId, @Field("cardId") String cardId);

    /**
     * 查询产品提示语
     */
    @POST(PRODUCT_PATH + "/product/queryBorrowingPrompt")
    Observable<ResultEntity<List<String>>> queryLoanHint();

    /**
     * 查询个性推荐产品分组
     */
    @POST(PRODUCT_PATH + "/product/groupList")
    Observable<ResultEntity<List<LoanGroup>>> queryLoanGroup();

    /**
     * 查询个性推荐产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/groupProductList")
    Observable<ResultEntity<LoanProduct>> queryPersonalProducts(@Field("groupId") String groupId, @Field("userId") String userId,
                                                                @Field("pageNo") int pageNo, @Field("pageSize") int pageSize,
                                                                @Field("platform") String platform);

    /**
     * 查询智能推荐产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/list")
    Observable<ResultEntity<LoanProduct>> queryLoanProduct(@Field("amount") double amount, @Field("dueTime") String dueTime,
                                                           @Field("sortType") int sortType, @Field("pageNo") int pageNum,
                                                           @Field("pageSize") int pageSize, @Field("platform") String platform);

    /**
     * 查询贷款产品详情
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/details")
    Observable<ResultEntity<LoanProductDetail>> queryLoanProductDetail(@Field("id") String productId, @Field("userId") String userId);

    /**
     * 查询授权产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/queryJurisdiction")
    Observable<ResultEntity<List<ThirdAuthorization>>> queryThirdAuthorization(@Field("userId") String userId, @Field("pids") String pids);

    /**
     * 授权第三方产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/clickJurisdiction")
    Observable<ResultEntity> authorize(@Field("userId") String userId, @Field("pids") String pids);

    /**
     * 查询第三方产品授权结果
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/queryOneLoan")
    Observable<ResultEntity<ThirdAuthResult>> authorizationResult(@Field("userId") String userId, @Field("pids") String pids);


    /**
     * 查询相关推荐产品
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/queryRelevant")
    Observable<ResultEntity<LoanProduct>> queryRecommendProduct(@Field("borrowingHigh") int amount, @Field("platform") String platform);

    /**
     * 添加或者删除产品，资讯收藏
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/editUserCollection")
    Observable<ResultEntity> addOrDeleteCollection(@Field("userId") String userId, @Field("productId") String productId, @Field("status") int status);


    /**
     * 查询收藏的产品
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/queryUserCollection")
    Observable<ResultEntity<LoanProduct>> queryProductionCollection(@Field("userId") String userId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 查询我的产品
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/queryUserLoan")
    Observable<ResultEntity<MyProduct>> queryMyProduct(@Field("userId") String userId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 获取我的账单
     * @author xhb
     * /accounting/myBillList 换成 /accounting/newMyBillList
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/newMyBillList")
    Observable<ResultEntity<LoanBill>> fetchLoanBill(@Field("userId") String userId, @Field("billType") int billType, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 查询邀请详细
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/invite")
    Observable<ResultEntity<Invitation>> queryInvitation(@Field("userId") String userId);

    /**
     * 查询版本更新
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/version/queryVersion")
    Observable<ResultEntity<AppUpdate>> queryAppUpdate(@Field("clientType") String clientType, @Field("packageId") String packageId);

    /**
     * 用户反馈
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/insertFeedback")
    Observable<ResultEntity> submitFeedback(@Field("userId") String userId, @Field("content") String content, @Field("image") String image, @Field("imageName") String imageName);

    /*****************************************************记账*******************************************************/
    /**
     * 读取记账首页宣传图
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/attach/queryPropagandaMap")
    Observable<ResultEntity<String>> queryPropaganda();

    /**
     * 获取账单信息摘要
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/queryBaseLoan")
    Observable<ResultEntity<DebtAbstract>> fetchDebtAbstractInfo(@Field("userId") String userId, @Field("billType") int billType);


    /**
     * 查询全部记账信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/allLoan")
    Observable<ResultEntity<AllDebt>> queryAllDebt(@Field("userId") String userId, @Field("status") int status, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 查询借款记账渠道
     */
    @POST(BASE_PATH + "/accounting/loanChannels")
    Observable<ResultEntity<LinkedHashMap<String, List<DebtChannel>>>> queryLoanChannel();

    /**
     * 查询网贷平台渠道
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/channels/show")
    Observable<ResultEntity<List<DebtChannel>>> fetchDebtSourceChannel(@Field("appShow") int appShow);

    /**
     * 确认记账，保存账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/sureRepayPlan")
    Observable<ResultEntity<PayPlan>> saveDebt(@FieldMap Map<String, Object> params);

    /**
     * 确认记账，不返回还款计划，直接插入数据
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/netLoadBill")
    Observable<ResultEntity> saveDebtImmediately(@FieldMap Map<String, Object> params);

    /**
     * 获取银行列表
     */
    @GET(BASE_PATH + "/accounting/bankList")
    Observable<ResultEntity<List<CreditCardBank>>> fetchCreditCardBankList();

    /**
     * 手动添加信用卡账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/collection/create/task/aisinButlerMeansCollection")
    Observable<ResultEntity> saveCreditCardDebt(@Field("userId") String userId, @Field("cardNums") String cardNums, @Field("bankId") long bankId,
                                                @Field("realName") String realName, @Field("billDay") int billDay, @Field("dueDay") int dueDay, @Field("amount") double amount);

    /**
     * 更新信用卡账单信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/updateCard")
    Observable<ResultEntity> updateCreditCardDebt(@Field("userId") String userId, @Field("cardId") String cardId,
                                                  @Field("billDay") int billDay, @Field("dueDay") int dueDay, @Field("amount") double amount);





    /**
     * 获取信用卡月份账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/billList")
    Observable<ResultEntity<List<CreditCardDebtBill>>> fetchCreditCardDebtBill(@Field("userId") String userId, @Field("recordId") String recordId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 获取信用卡月份账单详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/bill/detailList")
    Observable<ResultEntity<List<BillDetail>>> fetchCreditCardBillDetail(@Field("userId") String userId, @Field("billId") String billId);

    /**
     * 更新月份账单金额
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/updateBill")
    Observable<ResultEntity> updateMonthBillAmount(@Field("userId") String userId, @Field("cardId") String cardId, @Field("billId") String billId, @Field("amount") double amount);

    /**
     * 更新信用卡账单还款状态
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/updateBill")
    Observable<ResultEntity> updateCreditCardDebtBillStatus(@Field("userId") String userId, @Field("cardId") String cardId, @Field("billId") String billId, @Field("status") int status);

    /**
     * 删除信用卡账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/update")
    Observable<ResultEntity> deleteCreditCardDebt(@Field("userId") String userId, @Field("recordId") String recordId, @Field("status") int status);

    /**
     * 账单还款提醒设置
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/redmine")
    Observable<ResultEntity> updateDebtRemindStatus(@Field("userId") String userId, @Field("channelId") String channelId, @Field("cardId") String cardId, @Field("day") int day);

    /**
     * 删除借款
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/deleteLoan")
    Observable<ResultEntity> deleteDebt(@Field("userId") String userId, @Field("liabilitiesId") String liabilitiesId);

    /**
     * 获取首页账单信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/indexBillList")
    Observable<ResultEntity<List<AccountBill>>> fetchAccountBills(@Field("userId") String userId);

    /**
     * 更新账单是否在首页显示
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/hide")
    Observable<ResultEntity> updateDebtVisibility(@Field("userId") String userId, @Field("recordId") String recordId, @Field("type") int type, @Field("hide") int hide);

    /**
     * 查询日历模式账单记录月份摘要
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/calendar/tagging")
    Observable<ResultEntity<CalendarAbstract>> fetchCalendarAbstract(@Field("userId") String userId, @Field("begin") String beginDate, @Field("end") String endDate);

    /**
     * 查询日历模式账单记录月份趋势
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/calendar/trend")
    Observable<ResultEntity<Map<String, Float>>> fetchCalendarTrend(@Field("userId") String userId, @Field("begin") String beginDate, @Field("end") String endDate);

    /**
     * 查询日历模式账单记录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/repayment/billList")
    Observable<ResultEntity<CalendarDebt>> fetchCalendarDebt(@Field("userId") String userId, @Field("begin") String beginDate, @Field("end") String endDate);


    /**
     * 查询底部栏图标
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/bottom/list")
    Observable<ResultEntity<List<TabImage>>> queryBottomImage(@Field("version") String version, @Field("platform") String platform);

    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/sum")
    Observable<ResultEntity<Integer>> queryTotalRewardPoints(@Field("userId") String userId);

    /**
     * 查询积分任务信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/taskType")
    Observable<ResultEntity<List<RewardPoint>>> queryRewardPoint(@Field("userId") String userId, @Field("taskName") String taskName);

    /**
     * 标记积分任务已读
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/isRead")
    Observable<ResultEntity> sendRewardPointRead(@Field("recordId") String recordId, @Field("isRead") int isRead);

    /**
     * 添加一个积分任务
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/add")
    Observable<ResultEntity> addRewardPoint(@Field("userId") String userId, @Field("taskId") String taskId);

    /**
     * 查询菜单，按钮是否显示
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/appconfig/btnmenu/show")
    Observable<ResultEntity<Boolean>> queryMenuVisible(@Field("flag") String flag);

    /**
     * 获取网银导入地址
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/collection/create/task/nutH5BankCollection")
    Observable<ResultEntity<EBank>> fetchEBankUrl(@Field("userId") String userId);

    /**
     * 获取用户的邮箱列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/emailList")
    Observable<ResultEntity<List<UsedEmail>>> fetchUsedEmail(@Field("userId") String userId);

    /**
     * 获取坚果爬虫的银行列表
     */
    @POST(BASE_PATH + "/dataDictionary/emailConfig")
    Observable<ResultEntity<List<NutEmail>>> fetchNutEmail();

    /**
     * 查询信用卡账单采集结果
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/collection/task/status/nutSdkEmailCollection")
    Observable<ResultEntity<Boolean>> pollLeadInResult(@Field("userId") String userId, @Field("email") String email);


    /****************************************************个推账号用户绑定*****************************************************/

    /**
     * 绑定个推用账号
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/userLinkGetui")
    Observable<ResultEntity> bindClientId(@Field("userId") String userId, @Field("clientId") String clientId);

    /**********************************************************************************************************/


    /****************************************************数据统计*****************************************************/

    /**
     * 点击第三方产品外链
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/productSkip")
    Observable<ResultEntity> onProductClicked(@Field("userId") String userId, @Field("id") String id);

    /**
     * 点击广告，包括启动页，弹窗，banner
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/supernatant/loadSupernatant")
    Observable<ResultEntity> onAdClicked(@Field("id") String id, @Field("userId") String userId, @Field("supernatantType") int supernatantType);

    /**
     * 统计站内信点击次数
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/pushInfo/details")
    Observable<ResultEntity> onInternalMessageClicked(@Field("id") String id);

    /**
     * 数据统计
     *
     * @param type   点击类型
     * @param userId 用户id
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/dataDictionary/countUv")
    Observable<ResultEntity> onCountUv(@Field("type") String type, @Field("userId") String userId);

    /**
     * 个推消息点击次数统计
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/dataDictionary/message")
    Observable<ResultEntity> onPushClicked(@Field("userId") String userId, @Field("messageId") String message);


    /**
     * 网贷账单/信用卡备注更新
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/message/count")
    Observable<ResultEntity<String>> queryMessage(@Field("userId") String userId);

    /**
     * 查询消息数
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/accounting/updateRemark")
    Observable<ResultEntity> updateLoanOrCreditCardRemark(@Field("userId") String userId, @Field("remark") String remark, @Field("recordId") String recordId, @Field("type") String type);


    /**********************************************************************************************************/
}
