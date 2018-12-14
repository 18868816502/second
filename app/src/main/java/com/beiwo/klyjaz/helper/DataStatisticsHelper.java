package com.beiwo.klyjaz.helper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.entity.CreditCard;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.LogUtils;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.SPUtils;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * 友盟统计事件
 *
 * @author xhb
 */
public class DataStatisticsHelper {
    @SuppressLint("StaticFieldLeak")
    private static DataStatisticsHelper sInstance;
    private static final String TAG = DataStatisticsHelper.class.getSimpleName();

    /*点击联合产品授权注册*/
    public static final String ID_CLICK_THIRD_AUTHORIZATION = "授权注册";
    /*点击我要借款*/
    public static final String ID_CLICK_LOAN_REQUESTED = "我要借款";
    /*点击启动页广告*/
    public static final String ID_CLICK_SPLASH_AD = "启动页";
    /*首页弹窗广告弹出*/
    public static final String ID_SHOW_HOME_AD_DIALOG = "弹框显示";
    /*点击首页弹框广告*/
    public static final String ID_CLICK_HONE_AD_DIALOG = "点击弹框";
    /*进入资讯页面，点击资讯tab*/
    public static final String ID_CLICK_TAB_NEWS = "资讯栏";
    /*进入资讯详情页*/
    public static final String ID_RESUME_NEWS = "资讯详情";
    /*点击去微信*/
    public static final String ID_CLICK_GO_WECHAT = "去微信";
    /*点击Tab栏账单*/
    public static final String ID_CLICK_TAB_ACCOUNT = "BillingBottom";
    /*修改分期状态为已还*/
    public static final String ID_SET_STATUS_PAID = "BillPaidOP";
    /*首页点击推荐信用卡*/
    public static final String ID_CLICK_CREDIT_CARD_RECOMMEND = "indexCreditCardRecommend";
    /*APP开启事件*/
    public static final String ID_OPEN_APP = "OPEN_APP";
    public static final String ID_FIRST_INSTALL = "FirstInstall";

    //事件类型
    public static final String EVENT_TYPE_STAY = "PageStay ";//页面停留
    public static final String EVENT_TYPE_CLICK = "PageClick";//页面点击
    public static final String EVENT_TYPE_EXIT = "ExitApp 	";//退出应用
    public static final String EVENT_TYPE_OPEN = "OpenApp 	";//启动应用
    //viewId类型
    public static final String EVENT_VIEWID_HOMEPAGE = "HomePage";          //首页
    public static final String EVENT_VIEWID_NEWPRODUCTSDIVISION = "NewProductsDivision";          //新品推荐专区
    public static final String EVENT_VIEWID_LIGHTNINGACCOUNTDIVISION = "LightningAccountDivision";            //闪电到账专区
    public static final String EVENT_VIEWID_NOCREDITREPORTINGDIVISION = "NoCreditReportingDivision";            //不查征信专区
    public static final String EVENT_VIEWID_LOANRECOMMENDDIVISION = "LoanRecommendDivision ";          //下款推荐页面
    public static final String EVENT_VIEWID_LOANRECOMMENDPRODUCTDETAIL = "LoanRecommendProductDetail";            //下款推荐产品详情页面
    public static final String EVENT_VIEWID_LOANRECOMMENDPRODUCTSELECT = "LoanRecommendProductSelect";            //下款推荐选择产品页面
    public static final String EVENT_VIEWID_LOANRECOMMENDPRODUCTPRAISE = "LoanRecommendProductPraise";            //下款推荐产品评价页面
    public static final String EVENT_VIEWID_LOANPAGE = "LoanPage";          //贷款页面
    public static final String EVENT_VIEWID_COMMUNITYHOMEPAGE = "CommunityHomePage";          //社区首页
    public static final String EVENT_VIEWID_COMMUNITYPUBLISHPAGE = "CommunityPublishPage";          //社区发布页面
    public static final String EVENT_VIEWID_TOPICDETAILPAGE = "TopicDetailPage";          //话题详情页
    public static final String EVENT_VIEWID_TOOLHOMEPAGE = "ToolHomePage";          //工具首页
    public static final String EVENT_VIEWID_TALLYHOMEPAGE = "TallyHomePage";          //记账首页
    public static final String EVENT_VIEWID_LOANBILLPAGE = "LoanBillPage";          //贷款账单页
    public static final String EVENT_VIEWID_MYPAGE = "MyPage";          //我的页面
    public static final String EVENT_VIEWID_LARGELIMITLOWINTEREST = "LargeLimitLowInterest";          //大额低息专区
    public static final String EVENT_VIEWID_LOGINPAGE = "LoginPage";          //登陆页面
    //eventId类型
    public static final String EVENT_EVENTID_LOANIMMEDIATELY = "LoanImmediately";    //立即借钱
    public static final String EVENT_EVENTID_PRAISECUTMORE = "PraiseCutMore";    //好评口子查看更多
    public static final String EVENT_EVENTID_GETCODE = "GetCode";    //获取验证码
    public static final String EVENT_EVENTID_LOGINIMMEDIATELY = "LoginImmediately";    //立即登录
    public static final String EVENT_EVENTID_AMOUNTBTN = "AmountBtn";    //金额
    public static final String EVENT_EVENTID_CLASSIFYBTN = "ClassifyBtn";    //分类
    public static final String EVENT_EVENTID_SORTBTN = "SortBtn";    //排序
    public static final String EVENT_EVENTID_CREDITINQUIRY = "CreditInquiry";    //信用查询
    public static final String EVENT_EVENTID_MORTGAGECALCULATOR = "MortgageCalculator";    //房贷计算器
    public static final String EVENT_EVENTID_INVOICEASSISTANT = "InvoiceAssistant";    //发票助手
    public static final String EVENT_EVENTID_HEADPORTRAIT = "HeadPortrait";    //头像
    public static final String EVENT_EVENTID_MYPURSE = "MyPurse";    //我的钱包
    public static final String EVENT_EVENTID_BILLSUMMARY = "BillSummary";    //账单汇总
    public static final String EVENT_EVENTID_MYMESSAGE = "MyMessage";    //消息

    Api api;
    Context context;

    private DataStatisticsHelper(Context mContext) {
        api = Api.getInstance();
        context = mContext;
    }

    public static DataStatisticsHelper getInstance(Context mContext) {
        if (sInstance == null) {
            synchronized (DataStatisticsHelper.class) {
                if (sInstance == null) {
                    sInstance = new DataStatisticsHelper(mContext.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void event(String type, String viewId, @Nullable String eventId) {
        Map<String, Object> map = new HashMap<>();
        map.put("platform", 1);
        map.put("type", type);
        map.put("viewId", viewId);
        map.put("eventId", eventId);
    }

    public void onProductClicked(String id) {
        String userId;
        if (UserHelper.getInstance(context).getProfile() != null) {
            userId = UserHelper.getInstance(context).getProfile().getId();
        } else {
            userId = SPUtils.getCacheUserId();
        }
        api.onProductClicked(userId, id)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "internal message statistic error. message " + t.getMessage());
                    }
                });
    }

    public void onAdClicked(String id, int type) {
        String userId;
        if (UserHelper.getInstance(context).getProfile() != null) {
            userId = UserHelper.getInstance(context).getProfile().getId();
        } else {
            userId = App.androidId;
        }
        api.onAdClicked(id, userId, type)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "internal message statistic error. message " + t.getMessage());
                    }
                });
    }

    public void onInternalMessageClicked(String id) {
        api.onInternalMessageClicked(id)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "internal message statistic error. message " + t.getMessage());
                    }
                });
    }

    public void onCreditCardClicked(String id) {
        String userId;
        if (UserHelper.getInstance(context).getProfile() != null) {
            userId = UserHelper.getInstance(context).getProfile().getId();
        } else {
            userId = SPUtils.getCacheUserId();
        }
        //信用卡详情
        api.queryCreditCardDetail(userId, id)
                .compose(RxResponse.<CreditCard.Row>compatT())
                .subscribe(new ApiObserver<CreditCard.Row>() {
                    @Override
                    public void onNext(@NonNull CreditCard.Row data) {
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "credit card statistic error " + t.getMessage());
                    }
                });
        //推荐信用卡点击
        onCountUv(ID_CLICK_CREDIT_CARD_RECOMMEND);
    }

    /*数据统计*/
    public void onCountUv(final String type) {
        String userId;
        if (UserHelper.getInstance(context).isLogin()) {
            userId = UserHelper.getInstance(context).id();
        } else {
            userId = SPUtils.getCacheUserId();
        }
        api.onCountUv(type, userId)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        LogUtils.w(TAG, "count uv error event id=" + type);
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "count uv error event id = " + type + " " + t.getMessage());
                    }
                });
    }

    /*数据统计(埋点统计-社区)*/
    public void onCountUvPv(final String type, String linkId) {
        String userId;
        if (UserHelper.getInstance(context).isLogin()) {
            userId = UserHelper.getInstance(context).id();
        } else {
            userId = SPUtils.getCacheUserId();
        }
        api.onCountUv(ParamsUtils.generateCountUvParams(userId, type, linkId))
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        LogUtils.w(TAG, "count uv error event id=" + type);
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "count uv error event id = " + type + " " + t.getMessage());
                    }
                });
    }

    /*数据统计*/
    public void onCountUv(final String type, String androidId) {
        api.onCountUv(type, androidId)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        LogUtils.w(TAG, "count uv error event id=" + type);
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.w(TAG, "count uv error event id = " + type + " " + t);
                    }
                });
    }
}