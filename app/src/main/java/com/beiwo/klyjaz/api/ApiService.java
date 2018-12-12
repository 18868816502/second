package com.beiwo.klyjaz.api;

import com.beiwo.klyjaz.entity.AccountBill;
import com.beiwo.klyjaz.entity.AccountFlowIconBean;
import com.beiwo.klyjaz.entity.AdBanner;
import com.beiwo.klyjaz.entity.AllDebt;
import com.beiwo.klyjaz.entity.AnalysisChartBean;
import com.beiwo.klyjaz.entity.AnalysisOverviewBean;
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
import com.beiwo.klyjaz.entity.Comments;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.entity.CreateAccountReturnIDsBean;
import com.beiwo.klyjaz.entity.CreditBill;
import com.beiwo.klyjaz.entity.CreditCard;
import com.beiwo.klyjaz.entity.CreditCardBank;
import com.beiwo.klyjaz.entity.CreditCardBean;
import com.beiwo.klyjaz.entity.CreditCardDebtBill;
import com.beiwo.klyjaz.entity.CreditCardDebtDetail;
import com.beiwo.klyjaz.entity.DebeDetailRecord;
import com.beiwo.klyjaz.entity.DebtAbstract;
import com.beiwo.klyjaz.entity.DebtDetail;
import com.beiwo.klyjaz.entity.DetailHead;
import com.beiwo.klyjaz.entity.DetailList;
import com.beiwo.klyjaz.entity.EBank;
import com.beiwo.klyjaz.entity.EventBean;
import com.beiwo.klyjaz.entity.FastDebtDetail;
import com.beiwo.klyjaz.entity.Goods;
import com.beiwo.klyjaz.entity.GoodsInfo;
import com.beiwo.klyjaz.entity.HotTop;
import com.beiwo.klyjaz.entity.Product;
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
import com.beiwo.klyjaz.entity.PayPlan;
import com.beiwo.klyjaz.entity.Phone;
import com.beiwo.klyjaz.entity.Profession;
import com.beiwo.klyjaz.entity.PurseBalance;
import com.beiwo.klyjaz.entity.RemindBean;
import com.beiwo.klyjaz.entity.RewardPoint;
import com.beiwo.klyjaz.entity.SysMsg;
import com.beiwo.klyjaz.entity.SysMsgAbstract;
import com.beiwo.klyjaz.entity.SysMsgDetail;
import com.beiwo.klyjaz.entity.TabAccountBean;
import com.beiwo.klyjaz.entity.TabAccountNewBean;
import com.beiwo.klyjaz.entity.TabImageBean;
import com.beiwo.klyjaz.entity.ThirdAuthResult;
import com.beiwo.klyjaz.entity.ThirdAuthorization;
import com.beiwo.klyjaz.entity.Ticket;
import com.beiwo.klyjaz.entity.UploadImg;
import com.beiwo.klyjaz.entity.UsedEmail;
import com.beiwo.klyjaz.entity.UserInfoBean;
import com.beiwo.klyjaz.entity.UserProfile;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.entity.UserTopicBean;
import com.beiwo.klyjaz.entity.Withdraw;
import com.beiwo.klyjaz.entity.WithdrawRecord;
import com.beiwo.klyjaz.entity.request.XAccountInfo;
import com.beiwo.klyjaz.jjd.bean.BankCard;
import com.beiwo.klyjaz.jjd.bean.BankName;
import com.beiwo.klyjaz.jjd.bean.CashOrder;
import com.beiwo.klyjaz.jjd.bean.CashUserInfo;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.DraftEditForumBean;
import com.beiwo.klyjaz.social.bean.DraftsBean;
import com.beiwo.klyjaz.social.bean.ForumBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.bean.IndexForum;
import com.beiwo.klyjaz.social.bean.PraiseBean;
import com.beiwo.klyjaz.social.bean.PraiseListBean;
import com.beiwo.klyjaz.social.bean.SocialMessageBean;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.social.bean.TopicDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static com.beiwo.klyjaz.api.NetConstants.BASE_PATH;
import static com.beiwo.klyjaz.api.NetConstants.BASE_PATH_S_FOUR;
import static com.beiwo.klyjaz.api.NetConstants.PRODUCT_PATH;

/**
 * @author xhb
 * 请求接口
 */
public interface ApiService {

    /*************************************************新接口*****************************************/
    /**
     * @author xhb
     * 创建网贷账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/save")
    Observable<ResultEntity<CreateAccountReturnIDsBean>> createLoanAccount(@FieldMap Map<String, Object> params);

    /**
     * @author xhb
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/getLnetLoan")
    Observable<ResultEntity<List<LoanAccountIconBean>>> queryLoanAccountIcon(@Field("userId") String userId);

    /**
     * @author xhb
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/delCustomIcon")
    Observable<ResultEntity> deleteLoanAccountIcon(@Field("tallyId") String tallyId);

    /**
     * @author xhb
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/getLnetLoan")
    Observable<ResultEntity<List<LoanAccountIconBean>>> queryLoanAccountIcon(@Field("userId") String userId, @Field("searchContent") String searchContent);

    /**
     * @author xhb
     * 创建通用账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/save")
    Observable<ResultEntity<CreateAccountReturnIDsBean>> createNormalAccount(@FieldMap Map<String, Object> params);

    /**
     * @author xhb
     * 获取通用记账图标列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/listIcon")
    Observable<ResultEntity<List<AccountFlowIconBean>>> queryIconList(@Field("appShow") String appShow, @Field("userId") String userId, @Field("type") String type);

    /**
     * @author xhb
     * 获取通用记账图标列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/getCommonIcon")
    Observable<ResultEntity<List<AccountFlowIconBean>>> queryCustomIconList(@Field("appShow") String appShow, @Field("type") String type);

    /**
     * @author xhb
     * 获取通用记账图标列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/saveCustomIcon")
    Observable<ResultEntity> saveCustomIcon(@Field("userId") String userId, @Field("iconName") String iconName, @Field("iconId") String iconId, @Field("type") String type);

    /**
     * @author xhb
     * 魔蝎银行列表
     */
    @POST(BASE_PATH_S_FOUR + "/creditcard/moxie/bankList")
    Observable<ResultEntity<List<CreditCardBean>>> queryBankList();

    /**
     * @author xhb
     * 获取首页账单列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/index/v400")
    Observable<ResultEntity<List<TabAccountNewBean>>> queryTabAccountList(@Field("userId") String userId, @Field("collectType") int iconNcollectTypeame, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * @author xhb
     * 获取首页账单列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/index/v400")
    Observable<ResultEntity<List<TabAccountNewBean>>> queryTabAccountList(@Field("userId") String userId, @Field("collectType") int iconNcollectTypeame);

    /*分组贷超产品列表(爆款推荐/今日推荐)*/
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/groupProduct/list")
    Observable<ResultEntity<List<Product>>> groupProducts(@Field("groupId") String groupId);

    /**
     * @author xhb
     * 进入贷超产品详情
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/skip")
    Observable<ResultEntity<String>> queryGroupProductSkip(@Field("userId") String userId, @Field("productId") String productId);

    /*贷超产品页面跳转（原生)*/
    @FormUrlEncoded
    @POST("/s3/product/productSkipForNative")
    Observable<ResultEntity> proSkip(@Field("productId") String productId);

    /**
     * 查询快捷账单还款详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/loadBill")
    Observable<ResultEntity<FastDebtDetail>> updateFastDebtBillStatus(@Field("userId") String userId, @Field("billId") String billId, @Field("recordId") String recordId);

    /**
     * 修改快捷账单备注
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/updateRemark")
    Observable<ResultEntity> updateFastDebtBillRemark(@Field("userId") String userId, @Field("recordId") String recordId, @Field("remark") String remark);

    /**
     * 获取网贷账单详情
     *
     * @param userId        用户ID
     * @param liabilitiesId 账单Id
     * @param billId        网贷分期账单Id 每一期的ID
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/loadBill")
    Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(@Field("userId") String userId, @Field("recordId") String liabilitiesId, @Field("billId") String billId);

    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/loadBill")
    Observable<ResultEntity<DebtDetail>> fetchLoanDebtDetail(@Field("userId") String userId, @Field("recordId") String liabilitiesId);

    /**
     * 修改网贷账单备注
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/updateRemark")
    Observable<ResultEntity> updateLoanDebtBillRemark(@Field("userId") String userId, @Field("recordId") String recordId, @Field("remark") String remark);

    /**
     * 修改网贷账单备注
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/creditcard/updateRemark")
    Observable<ResultEntity> updateCreditCardBillRemark(@Field("userId") String userId, @Field("cardId") String recordId, @Field("remark") String remark);

    /**
     * 删除借款
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/deleteBill")
    Observable<ResultEntity> deleteDebt(@Field("userId") String userId, @Field("recordId") String liabilitiesId);

    /**
     * 删除快捷记账账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/deleteBill")
    Observable<ResultEntity> deleteFastDebt(@Field("userId") String userId, @Field("recordId") String recordId);

    /**
     * 绑定手机号
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/updateBindPhone")
    Observable<ResultEntity> updateBindPhone(@Field("account") String account, @Field("phone") String phone, @Field("verifyCode") String verifyCode, @Field("type") String type);

    /**
     * 绑定微信
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/wx/bindWx")
    Observable<ResultEntity> bindWXChat(@Field("userId") String userId, @Field("unionId") String unionId);

    /**
     * 解绑微信
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/wx/uniteWx")
    Observable<ResultEntity> unBindWXChat(@Field("userId") String userId);

    /*************************************************新接口*****************************************/


    /**
     * @author xhb
     * 获取头信息 以及未还总额 已还总额
     * 获取账单信息摘要
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/unRepayBill")
    Observable<ResultEntity<DebtAbstract>> queryTabAccountHeaderInfo(@Field("userId") String userId, @Field("billType") int billType);

    /**
     * @param userId      用户Id
     * @param billStatus  账单状态 1-待还 2-已还 3-逾期
     * @param firstScreen 是不是首屏 true:是 false：不是
     * @param pageSize    一页记录数
     * @param pageNo      页码
     * @author xhb
     * 获取头信息 以及未还总额 已还总额
     * 获取账单信息摘要
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/bill/index")
    Observable<ResultEntity<List<XAccountInfo>>> queryTabAccountListInfo(@Field("userId") String userId, @Field("billStatus") int billStatus, @Field("firstScreen") boolean firstScreen, @Field("billType") int billType, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/bill/index")
    Observable<ResultEntity<List<XAccountInfo>>> queryTabAccountListInfo(@Field("userId") String userId, @Field("firstScreen") boolean firstScreen, @Field("billType") int billType);

    /**
     * 更新还款状态
     *
     * @param userId              用户ID
     * @param liabilitiesDetailId 网贷分期账单Id 每一期的ID
     * @param repayAmount         还款金额 默认全额
     * @param status              状态 1-待还 2-已还
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/newUpdateRepayStatus")
    Observable<ResultEntity> updateDebtStatus(@Field("userId") String userId, @Field("liabilitiesDetailId") String liabilitiesDetailId, @Field("repayAmount") double repayAmount, @Field("status") int status);

    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/newUpdateRepayStatus")
    Observable<ResultEntity> updateDebtStatus(@Field("userId") String userId, @Field("liabilitiesDetailId") String liabilitiesDetailId, @Field("status") int status);


    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/updateStatus")
    Observable<ResultEntity<BillState>> updateStatus(@Field("userId") String userId, @Field("billId") String billId, @Field("status") int status);

    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/updateStatus")
    Observable<ResultEntity<BillState>> updateNormalStatus(@Field("userId") String userId, @Field("billId") String billId, @Field("status") int status);

    /**
     * 获取信用卡账单详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/bill")
    Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(@Field("userId") String userId, @Field("recordId") String recordId, @Field("billId") String billId);

    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/bill")
    Observable<ResultEntity<CreditCardDebtDetail>> fetchCreditCardDebtDetail(@Field("userId") String userId, @Field("recordId") String recordId);

    /**
     * 更新快捷账单还款状态
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/bookKeeping/updateStatus")
    Observable<ResultEntity> updateFastDebtBillStatus(@Field("userId") String userId, @Field("billId") String billId, @Field("recordId") String recordId, @Field("status") int status);

    /**
     * 更新快捷账单还款状态
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/bookKeeping/updateStatus")
    Observable<ResultEntity> updateFastDebtBillStatus(@Field("userId") String userId, @Field("billId") String billId, @Field("recordId") String recordId, @Field("status") int status, @Field("amount") double amount);


    /**
     * 修改快捷记账账单名称
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/bookKeeping/updateName")
    Observable<ResultEntity> updateFastDebtName(@Field("userId") String userId, @Field("recordId") String recordId, @Field("projectName") String projectName);

    /**
     * @version 3.1.0
     * @desc 首页列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/index/v310")
    Observable<ResultEntity<TabAccountBean>> queryTabAccountList(@Field("userId") String userId);

    /**
     * @version 3.1.0
     * @desc 网贷分析 底部数据
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/analysis/overview")
    Observable<ResultEntity<AnalysisOverviewBean>> queryAnalysisOverview(@Field("userId") String userId);

    /**
     * @version 3.1.0
     * @desc 网贷分析 底部数据
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/analysis/barGraph")
    Observable<ResultEntity<List<AnalysisChartBean>>> queryAnalysisOverviewChart(@Field("userId") String userId, @Field("type") int type, @Field("startTime") String startTime, @Field("endTime") String endTime);

    /**
     * @version 3.1.0
     * @desc 网贷分析 列表数据
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/analysis/listInTime")
    Observable<ResultEntity<BillLoanAnalysisBean>> queryAnalysisOverviewList(@Field("userId") String userId, @Field("type") int type, @Field("time") String time);

    /**
     * @version 3.1.0
     * @desc 获取最新公告
     */
    @POST(BASE_PATH + "/notice/lastNotice")
    Observable<ResultEntity<LastNoticeBean>> getNewNotice();

    /**
     * @version 3.2.0
     * @desc 查询网贷账单的还款记录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/netLoan/repayment/list")
    Observable<ResultEntity<List<DebeDetailRecord>>> getDebeDetailRecord(@Field("userId") String userId, @Field("billId") String billId);

    /**
     * @version 3.2.0
     * @desc 查询快捷账单的还款记录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/bookKeeping/repayment/list")
    Observable<ResultEntity<List<DebeDetailRecord>>> getFastDetailRecord(@Field("userId") String userId, @Field("billId") String billId);

    /****************************************************************************** 分割线 **************************************************************************************/

    /**
     * 密码登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> login(@Field("account") String account, @Field("pwd") String pwd, @Field("platform") int platform);

    /**
     * 密码登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> loginByCode(@Field("account") String account, @Field("loginType") String loginType, @Field("verifyCode") String verifyCode, @Field("platform") int platform);

    /**
     * 免密码登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> loginNoPwd(@Field("account") String account, @Field("platform") int platform);

    /**
     * 微信登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/wx/login")
    Observable<ResultEntity<UserProfileAbstract>> loginWithWechat(@Field("openId") String openId, @Field("platform") int platform);

    /**
     * 请求验证码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/sms/sendSms")
    Observable<ResultEntity<Phone>> requestSms(@Field("phone") String phone, @Field("type") String type);

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
                                                                       @Field("verificationCodeType") String type, @Field("verificationCode") String code, @Field("platform") String platform);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/register")
    Observable<ResultEntity<UserProfileAbstract>> register(@Field("platform") int platform, @Field("account") String account, @Field("pwd") String pwd,
                                                           @Field("wxOpenId") String exOpenId, @Field("wxName") String wxName, @Field("wxImage") String wxImage,
                                                           @Field("inviteCode") String inviteCode);

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
    Observable<ResultEntity<List<AdBanner>>> querySupernatant(@Field("port") int port, @Field("supernatantType") int supernatantType);

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
     *
     * @author xhb
     * /accounting/myBillList 换成 /accounting/newMyBillList
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/newMyBillList")
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
    Observable<ResultEntity<AppUpdate>> queryAppUpdate(@Field("clientType") String clientType);

    /**
     * 用户反馈
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/insertFeedback")
    Observable<ResultEntity> submitFeedback(@Field("userId") String userId, @Field("content") String content, @Field("image") String image, @Field("imageName") String imageName);

    /**
     * 用户反馈
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/insertFeedback")
    Observable<ResultEntity> submitFeedback(@Field("userId") String userId, @Field("content") String content, @Field("contactWay") String contactWay, @Field("image") String image, @Field("imageName") String imageName);

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
    @POST(BASE_PATH_S_FOUR + "/accounting/queryBaseLoan")
    Observable<ResultEntity<DebtAbstract>> fetchDebtAbstractInfo(@Field("userId") String userId, @Field("billType") int billType);

    /**
     * 查询全部记账信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/allLoan")
    Observable<ResultEntity<AllDebt>> queryAllDebt(@Field("userId") String userId, @Field("status") int status, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /*查询借款记账渠道*/
    /*@POST(BASE_PATH_S_FOUR + "/accounting/loanChannels")
    Observable<ResultEntity<LinkedHashMap<String, List<DebtChannel>>>> queryLoanChannel();*/

    /*查询网贷平台渠道*/
    /*@FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/channels/show")
    Observable<ResultEntity<List<DebtChannel>>> fetchDebtSourceChannel(@Field("appShow") int appShow);*/

    /**
     * 确认记账，保存账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/sureRepayPlan")
    Observable<ResultEntity<PayPlan>> saveDebt(@FieldMap Map<String, Object> params);

    /**
     * 确认记账，不返回还款计划，直接插入数据
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/netLoadBill")
    Observable<ResultEntity> saveDebtImmediately(@FieldMap Map<String, Object> params);

    /**
     * 确认记账 快捷记账的api
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/bookKeeping/save")
    Observable<ResultEntity> saveFastDebt(@FieldMap Map<String, Object> params);

    /**
     * 获取银行列表
     */
    @GET(BASE_PATH_S_FOUR + "/accounting/bankList")
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
    @POST(BASE_PATH_S_FOUR + "/creditcard/updateBill")
    Observable<ResultEntity> updateMonthBillAmount(@Field("userId") String userId, @Field("cardId") String cardId, @Field("billId") String billId, @Field("amount") double amount);

    /**
     * 更新信用卡账单还款状态
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/creditcard/updateBill")
    Observable<ResultEntity> updateCreditCardDebtBillStatus(@Field("userId") String userId, @Field("cardId") String cardId, @Field("billId") String billId, @Field("status") int status);

    /**
     * 更新信用卡账单还款状态
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/creditcard/updateBill")
    Observable<ResultEntity> updateCreditCardDebtBillStatus(@Field("userId") String userId, @Field("billId") String billId, @Field("status") int status);

    /**
     * 删除信用卡账单
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/update")
    Observable<ResultEntity> deleteCreditCardDebt(@Field("userId") String userId, @Field("recordId") String recordId, @Field("status") int status);

    /**
     * 账单还款提醒设置
     * accounting/updateRedmine 新接口替换老接口 accounting/redmine
     * type 类型 1-网贷 2-信用卡 3-手动记账
     * recordId 网贷账单Id/信用卡Id/手动账单Id
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/updateRedmine")
    Observable<ResultEntity> updateDebtRemindStatus(@Field("userId") String userId, @Field("type") String type, @Field("recordId") String recordId, @Field("day") int day);

    /**
     * 获取首页账单信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/indexBillList")
    Observable<ResultEntity<List<AccountBill>>> fetchAccountBills(@Field("userId") String userId);

    /**
     * 更新账单是否在首页显示
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/hide")
    Observable<ResultEntity> updateDebtVisibility(@Field("userId") String userId, @Field("recordId") String recordId, @Field("type") int type, @Field("hide") int hide);

    /**
     * 查询日历模式账单记录月份摘要
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/calendar/tagging")
    Observable<ResultEntity<CalendarAbstract>> fetchCalendarAbstract(@Field("userId") String userId, @Field("begin") String beginDate, @Field("end") String endDate);

    /**
     * 查询日历模式账单记录月份趋势
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/calendar/trend")
    Observable<ResultEntity<Map<String, Float>>> fetchCalendarTrend(@Field("userId") String userId, @Field("begin") String beginDate, @Field("end") String endDate);

    /**
     * 查询日历模式账单记录
     */
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/repayment/billList")
    Observable<ResultEntity<CalendarDebt>> fetchCalendarDebt(@Field("userId") String userId, @Field("begin") String beginDate, @Field("end") String endDate);

    /*查询底部栏图标*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/bottom/auditList")
    Observable<ResultEntity<TabImageBean>> queryBottomImage(@Field("platform") int platform);

    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/sum")
    Observable<ResultEntity<Integer>> queryTotalRewardPoints(@Field("userId") String userId);

    /*查询积分任务信息*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/taskType")
    Observable<ResultEntity<List<RewardPoint>>> queryRewardPoint(@Field("userId") String userId, @Field("taskName") String taskName);

    /*标记积分任务已读*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/isRead")
    Observable<ResultEntity> sendRewardPointRead(@Field("recordId") String recordId, @Field("isRead") int isRead);

    /*添加一个积分任务*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/userInteg/add")
    Observable<ResultEntity> addRewardPoint(@Field("userId") String userId, @Field("taskId") String taskId);

    /*查询菜单，按钮是否显示*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/appconfig/btnmenu/show")
    Observable<ResultEntity<Boolean>> queryMenuVisible(@Field("flag") String flag);

    /*获取网银导入地址*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/collection/create/task/nutH5BankCollection")
    Observable<ResultEntity<EBank>> fetchEBankUrl(@Field("userId") String userId);

    /*获取用户的邮箱列表*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/creditcard/emailList")
    Observable<ResultEntity<List<UsedEmail>>> fetchUsedEmail(@Field("userId") String userId);

    /*获取坚果爬虫的银行列表*/
    @POST(BASE_PATH + "/dataDictionary/emailConfig")
    Observable<ResultEntity<List<NutEmail>>> fetchNutEmail();

    /*查询信用卡账单采集结果*/
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
    /*点击第三方产品外链*/
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/productSkip")
    Observable<ResultEntity> onProductClicked(@Field("userId") String userId, @Field("id") String id);

    /*点击广告，包括启动页，弹窗，banner*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/supernatant/loadSupernatant")
    Observable<ResultEntity> onAdClicked(@Field("id") String id, @Field("userId") String userId, @Field("supernatantType") int supernatantType);

    /*统计站内信点击次数*/
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

    /*数据统计(埋点统计)*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/dataDictionary/countUv")
    Observable<ResultEntity> onCountUv(@FieldMap Map<String, Object> params);

    /*个推消息点击次数统计*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/dataDictionary/message")
    Observable<ResultEntity> onPushClicked(@Field("userId") String userId, @Field("messageId") String message);

    /*查询消息数*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/message/count")
    Observable<ResultEntity<String>> queryMessage(@Field("userId") String userId);

    /*网贷账单/信用卡备注更新*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/updateRemark")
    Observable<ResultEntity> updateLoanOrCreditCardRemark(@Field("userId") String userId, @Field("remark") String remark, @Field("recordId") String recordId, @Field("type") String type);

    /*账单汇总*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/index/personAmount")
    Observable<ResultEntity<BillSummaryBean>> onBillSummary(@Field("userId") String userId, @Field("pageNo") String pageNo);

    /*全部已读*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/message/readAll")
    Observable<ResultEntity> onReadAll(@Field("userId") String userId);

    /*清空消息*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/message/clear")
    Observable<ResultEntity> onDeleteMessageAll(@Field("userId") String userId);

    /*提醒查询*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/message/remind/load")
    Observable<ResultEntity<RemindBean>> onRemindInfo(@Field("userId") String userId);

    /*提醒设置*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/message/remind")
    Observable<ResultEntity> onRemindSetting(@Field("userId") String userId, @Field("geTui") String geTui, @Field("sms") String sms, @Field("wechat") String weChat, @Field("day") String day);

    /*v4.2.0首页数据*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/accounting/index/v420")
    Observable<ResultEntity<HomeData>> home(@Field("userId") String userId, @Field("pageNo") int pageNo);

    /*v4.2.0活动入口*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/activeHomeController/isShowActive")
    Observable<ResultEntity<EventBean>> homeEvent(@Field("location") String location, @Field("port") int port);

    /*v4.2.3活动入口*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/activeHomeController/isShowOwnerActive")
    Observable<ResultEntity<List<EventBean>>> isShowOwnerActive(@Field("location") String location, @Field("port") int port);

    /*获取网贷配置图标*/
    @POST(BASE_PATH_S_FOUR + "/channelsIcon/getIconConfigure")
    Observable<ResultEntity<List<LoanAccountIconBean>>> netIcon();

    /*账单详情头部*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/detailHead")
    Observable<ResultEntity<DetailHead>> detailHead(@Field("userId") String userId, @Field("recordId") String recordId);

    /*账单详情列表*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/detailList")
    Observable<ResultEntity<DetailList>> detailList(@Field("userId") String userId, @Field("recordId") String recordId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /*结清全部*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/netLoan/closeAll")
    Observable<ResultEntity> closeAll(@Field("userId") String userId, @Field("recordId") String recordId);

    /*账单列表*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/creditcard/billList")
    Observable<ResultEntity<List<CreditBill>>> creditList(@Field("userId") String userId, @Field("recordId") String recordId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /*账单详情列表*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/creditcard/bill/detailList")
    Observable<ResultEntity<List<BillDetail>>> billDetail(@Field("userId") String userId, @Field("billId") String billId);

    /*更新信用卡状态 删除*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/creditcard/update")
    Observable<ResultEntity> deleteCredit(@Field("userId") String userId, @Field("recordId") String recordId, @Field("status") int status);

    /*账单详情头部--通用账单*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/detailHead")
    Observable<ResultEntity<DetailHead>> commonDetailHead(@Field("userId") String userId, @Field("recordId") String recordId);

    /*账单详情列表--通用账单*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/detailList")
    Observable<ResultEntity<DetailList>> commonDetailList(@Field("userId") String userId, @Field("recordId") String recordId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /*结清全部*/
    @FormUrlEncoded
    @POST(BASE_PATH_S_FOUR + "/bookKeeping/closeAll")
    Observable<ResultEntity> commonCloseAll(@Field("userId") String userId, @Field("recordId") String recordId);

    /*获取短信内容*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/inviteActiveController/getInviteMsg")
    Observable<ResultEntity<String>> getInviteMsg(@Field("userId") String userId);

    /*分享朋友圈回调*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/inviteActiveController/shareUser")
    Observable<ResultEntity> shareUser(@Field("userId") String userId);

    /*获取短信内容*/
    @FormUrlEncoded
    @POST(BASE_PATH + "/inviteActiveController/uploadImg")
    Observable<ResultEntity> uploadImg(@Field("userId") String userId
            , @Field("phone") String phone
            , @Field("imgType") String imgType
            , @Field("activeName") String activeName
            , @Field("base64") String base64);

    /*钱包*/
    @FormUrlEncoded
    @POST("/s1/purse/balance")
    Observable<ResultEntity<PurseBalance>> purseBalance(@Field("userId") String userId);

    /*提现记录列表*/
    @FormUrlEncoded
    @POST("/s1/purse/trade/listData")
    Observable<ResultEntity<WithdrawRecord>> withdrawRecord(@FieldMap Map<String, Object> map);

    /*获取支付宝账户*/
    @FormUrlEncoded
    @POST("/s1/purse/receipt/accountList")
    Observable<ResultEntity<List<PayAccount>>> payAccount(@Field("userId") String userId);

    /*保存支付宝账户*/
    @FormUrlEncoded
    @POST("/s1/purse/receipt/saveAccount")
    Observable<ResultEntity<PayAccount>> saveAlpAccount(@FieldMap Map<String, Object> map);

    /*提现*/
    @FormUrlEncoded
    @POST("/s1/purse/trade/create")
    Observable<ResultEntity<Withdraw>> withdraw(@FieldMap Map<String, Object> map);

    /*发票列表*/
    @FormUrlEncoded
    @POST("/s1/tools/invoice/listData")
    Observable<ResultEntity<List<Ticket>>> tickets(@Field("userId") String userId);

    /*保存/编辑发票*/
    @FormUrlEncoded
    @POST("/s1/tools/invoice/save")
    Observable<ResultEntity<Ticket>> saveTicket(@FieldMap Map<String, Object> map);

    /*删除发票*/
    @FormUrlEncoded
    @POST("/s1/tools/invoice/delete")
    Observable<ResultEntity> deleteTicket(@Field("userId") String userId, @Field("invoiceId") long invoiceId);

    /*信用查询*/
    @FormUrlEncoded
    @POST("/s1/tools/credit/check")
    Observable<ResultEntity<BlackList>> queryCredit(@FieldMap Map<String, Object> map);

    /*用户主页-个人信息*/
    @FormUrlEncoded
    @POST("/s6/userIndex/baseInfo")
    Observable<ResultEntity<UserInfoBean>> queryUserInfo(@Field("userId") String userID);

    /**
     * 用户主页-用户发表的文章列表
     *
     * @param userID   用户id
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/userIndex/forumInfo")
    Observable<ResultEntity<List<UserTopicBean>>> queryUserTopicInfo(@Field("userId") String userID, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    @POST("s1/version/audit/land")
    Observable<ResultEntity<Audit>> audit();

    /************************************社区************************************/
    /**
     * 社区推荐列表
     *
     * @param pageNo   页码
     * @param pageSize 每页记录数
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumQueryController/queryRecommend")
    Observable<ResultEntity<SocialTopicBean>> queryRecommendTopic(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("/s6/forumQueryController/queryRecommend")
    Observable<ResultEntity<SocialTopicBean>> queryRecommendTopic(@Field("userId") String userId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("/s6/forumQueryController/queryRecommend")
    Observable<ResultEntity<SocialTopicBean>> queryRecommendTopic(@FieldMap Map<String, Object> map);

    /**
     * 图片上传
     *
     * @param base64 图片base64
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumController/uploadFourmImg")
    Observable<ResultEntity<String>> uploadFourmImg(@Field("base64") String base64);

    /**
     * 发布动态
     *
     * @param userId       用户id
     * @param imgKey       图片上传对应的key，多张用#分隔
     * @param forumTitle   动态标题
     * @param forumContent 动态内容
     * @param status       是否提交草稿箱 0 提交 3 未提交
     * @param topicId      话题id，必传
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumController/publicForumInfo")
    Observable<ResultEntity> publicForumInfo(@Field("userId") String userId, @Field("imgKey") String imgKey, @Field("forumTitle") String forumTitle,
                                             @Field("forumContent") String forumContent, @Field("status") String status, @Field("topicId") String topicId,
                                             @Field("forumId") String forumId);

    /**
     * 发布动态
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumController/publicForumInfo")
    Observable<ResultEntity> publicForumInfo(@FieldMap Map<String, Object> map);


    /**
     * 草稿箱动态编辑接口
     *
     * @param forumId
     * @return
     */
    @FormUrlEncoded
    @POST("s6/forumQueryController/editForum")
    Observable<ResultEntity<DraftEditForumBean>> fetchEditForum(@Field("forumId") String forumId);


    /**
     * 查询评论列表
     *
     * @param forumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumQueryController/queryCommentList")
    Observable<ResultEntity<List<CommentReplyBean>>> queryCommentList(@Field("forumId") String forumId, @Field("pageNo") String pageNo, @Field("pageSize") String pageSize);

    /*产品列表查询*/
    @FormUrlEncoded
    @POST("/s3/product/productListForNative")
    Observable<ResultEntity<List<Product>>> products(@FieldMap Map<String, Object> map);

    /**
     * 发表评论回复
     *
     * @param userId
     * @param commentType
     * @param commentContent
     * @param forumId
     * @param toUserId
     * @param selfId
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumController/replyForumInfo")
    Observable<ResultEntity> fetchReplyForumInfo(@Field("userId") String userId, @Field("commentType") String commentType, @Field("commentContent") String commentContent,
                                                 @Field("forumId") String forumId, @Field("toUserId") String toUserId, @Field("selfId") String selfId);

    @FormUrlEncoded
    @POST("/s6/forumController/replyForumInfo")
    Observable<ResultEntity> fetchReplyForumInfo(@FieldMap Map<String, Object> map);

    /**
     * 提交举报信息
     *
     * @param userId
     * @param linkId
     * @param reportType
     * @param reportContent
     * @return
     */
    @FormUrlEncoded
    @POST("/s6/forumController/saveReport")
    Observable<ResultEntity> fetchSaveReport(@Field("userId") String userId, @Field("linkId") String linkId,
                                             @Field("reportType") String reportType, @Field("reportContent") String reportContent);

    /*删除动态*/
    @FormUrlEncoded
    @POST("s6/forumController/cancelForum")
    Observable<ResultEntity> fetchCancelForum(@Field("forumId") String forumId);

    /*删除评论回复*/
    @FormUrlEncoded
    @POST("s6/forumController/cancelReply")
    Observable<ResultEntity> fetchCancelReply(@Field("replyId") String replyId);

    /**
     * 社区评论回复点赞
     *
     * @param praiseType
     * @param forumReplyId
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("s6/forumController/clickPraise")
    Observable<ResultEntity<PraiseBean>> fetchClickPraise(@Field("praiseType") int praiseType, @Field("forumReplayId") String forumReplyId, @Field("userId") String userId);

    /**
     * 社区评论回复取消点赞
     *
     * @param praiseType
     * @param forumReplyId
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("s6/forumController/cancelPraise")
    Observable<ResultEntity> fetchCancelPraise(@Field("praiseType") int praiseType, @Field("forumReplayId") String forumReplyId, @Field("userId") String userId);

    /*保存用户信息*/
    @FormUrlEncoded
    @POST("s6/userIndex/saveUserDetailInfo")
    Observable<ResultEntity> fetchSaveUserInfo(@FieldMap Map<String, Object> map);

    /*保存用户信息*/
    @FormUrlEncoded
    @POST("s6/userIndex/queryCenterForum")
    Observable<ResultEntity<List<DraftsBean>>> queryCenterForum(@FieldMap Map<String, Object> map);

    /*查询点赞列表*/
    @FormUrlEncoded
    @POST("s6/forumQueryController/praiseList")
    Observable<ResultEntity<List<PraiseListBean>>> queryPraiseList(@FieldMap Map<String, Object> map);

    /*查询点赞列表*/
    @FormUrlEncoded
    @POST("s6/forumQueryController/commentList")
    Observable<ResultEntity<List<PraiseListBean>>> queryCommentList(@FieldMap Map<String, Object> map);

    /*保存用户信息*/
    @FormUrlEncoded
    @POST("s6/userIndex/queryCenterForumAudit")
    Observable<ResultEntity<List<DraftsBean>>> queryCenterForumAudit(@FieldMap Map<String, Object> map);

    /*用户认证信息查询*/
    @FormUrlEncoded
    @POST("s1/cashUserController/queryCashUserInfo")
    Observable<ResultEntity<CashUserInfo>> userAuth(@Field("userId") String userId);

    /*用户订单状态检查*/
    @FormUrlEncoded
    @POST("s1/cashOrderController/checkCashOrder")
    Observable<ResultEntity<CashOrder>> cashOrder(@Field("userId") String userId);

    /*银行卡列表*/
    @FormUrlEncoded
    @POST("s1/cashBankController/cardList")
    Observable<ResultEntity<List<BankCard>>> cardList(@Field("userId") String userId);

    /*卡号获取银行名称*/
    @FormUrlEncoded
    @POST("s1/cashBankController/bankName")
    Observable<ResultEntity<BankName>> bankName(@Field("bankCardno") String cardNo);

    /*保存更新银行卡*/
    @FormUrlEncoded
    @POST("s1/cashBankController/saveCard")
    Observable<ResultEntity> saveCard(@FieldMap Map<String, Object> map);

    /*查询银行卡(重新编辑)*/
    @FormUrlEncoded
    @POST("s1/cashBankController/queryCard")
    Observable<ResultEntity<BankCard>> queryCard(@Field("userId") String userId, @Field("cardId") String cardId);

    /*保存借款订单*/
    @FormUrlEncoded
    @POST("s1/cashOrderController/saveCashOrder")
    Observable<ResultEntity<CashOrder>> saveCashOrder(@FieldMap Map<String, Object> map);

    /*实名认证*/
    @FormUrlEncoded
    @POST("s1/cashUserController/saveCashUser")
    Observable<ResultEntity> fetchVertifyIDCard(@Field("userId") String userID, @Field("userName") String userName, @Field("idCard") String idCard);

    /*保存联系人*/
    @FormUrlEncoded
    @POST("s1/cashUserController/saveCashUserContact")
    Observable<ResultEntity> fetchSaveContact(@Field("userId") String userID, @Field("userContact") String userContact,
                                              @Field("userRelate") String userRelate, @Field("mobileNum") String mobileNum);

    /*动态详情加载*/
    @FormUrlEncoded
    @POST("s6/forumQueryController/queryForumInfo")
    Observable<ResultEntity<ForumInfoBean>> queryForumInfo(@Field("userId") String userID, @Field("forumId") String forumId,
                                                           @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /*动态详情加载*/
    @FormUrlEncoded
    @POST("s6/forumQueryController/queryForumInfo")
    Observable<ResultEntity<ForumInfoBean>> queryForumInfo(@FieldMap Map<String, Object> map);

    /*消息-社区数量*/
    @FormUrlEncoded
    @POST("s6/message/countView")
    Observable<ResultEntity<SocialMessageBean>> queryCountView(@Field("userId") String userID);

    /*话题详情(头部信息)*/
    @FormUrlEncoded
    @POST("/s6/forumQueryController/queryTopicDetail")
    Observable<ResultEntity<TopicDetail>> topicDetail(@Field("topicId") String topicId);

    /*话题详情(动态列表)*/
    @FormUrlEncoded//orderType 排序类型 1 最新 2 最热
    @POST("/s6/forumQueryController/queryTopicDetailCommnet")
    Observable<ResultEntity<List<ForumBean>>> topicList(@FieldMap Map<String, Object> map);

    /*首页-社区*/
    @POST("s6/forumQueryController/indexForum")
    Observable<ResultEntity<IndexForum>> indexForum();

    /*好评口子*/
    @FormUrlEncoded
    @POST("/s5/praiseCut/list")
    Observable<ResultEntity<List<Goods>>> hotGoods(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /*下款热门榜*/
    @FormUrlEncoded
    @POST("/s3/product/hotLoan")
    Observable<ResultEntity<List<HotTop>>> hotLoan(@Field("groupId") String groupId);

    /*产品详情-产品信息*/
    @FormUrlEncoded
    @POST("/s5/praiseCut/manage")
    Observable<ResultEntity<GoodsInfo>> goodsInfo(@Field("cutId") String cutId, @Field("manageId") String manageId);

    /**
     * 评价产品
     * 参数       是否必传  说明
     * manageId     Y       产品管理Id
     * loanStatus   Y       是否借到 1-审核被拒, 2-放款成功 3-已还款 4 账单逾期 5-复借
     * flag         Y       产品映象 逗号分隔 QuickLoan放款快/HighLoan额度高/LowThreshold门槛低/ConvenientProcedure手续方便/NoMortgageRequired无需抵押/NoCredit不上征信/AuditTimely审批及时/HeightPass过审高/GoodExperience用户体验好/LargePlatform大平台
     * type         Y       评价类型 -1-差评 0-中评 1-好评
     * userId       Y       用户Id
     * imageUrl     N       评价图片 逗号分隔
     * content      N       评价内容
     */
    @FormUrlEncoded
    @POST("/s5/praiseCut/saveManagePraise")
    Observable<ResultEntity> commentGoods(@FieldMap Map<String, Object> map);

    /*口子产品评价上传图片*/
    @FormUrlEncoded
    @POST("/s5/praiseCut/uploadImage")
    Observable<ResultEntity<UploadImg>> uploadImg(@Field("image") String image);

    /*产品详情-产品评价总览*/
    @FormUrlEncoded
    @POST("/s5/praiseCut/praiseView")
    Observable<ResultEntity<CommentsTotal>> goodsCommentTotal(@Field("cutId") String cutId, @Field("manageId") String manageId);

    /**
     * 产品详情-产品评价分页列表
     * 参数       是否必传  说明
     * manageId     Y 	    产品管理Id
     * cutId 	    Y 	    口子Id
     * type 	    Y 	    评价类型 -1-差评 0-中评 1-好评
     * flag 	    Y 	    标签
     * pageNo 	    Y 	    当前是第几页，从1开始记数
     * pageSize 	Y 	    每页多少条
     */
    @FormUrlEncoded
    @POST("/s5/praiseCut/praiseList")
    Observable<ResultEntity<Comments>> goodsComments(@FieldMap Map<String, Object> map);
}