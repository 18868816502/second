package com.beihui.market.helper;


import android.annotation.SuppressLint;
import android.content.Context;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.entity.CreditCard;
import com.beihui.market.injection.component.DaggerDataStatisticHelperComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 *         友盟统计事件
 */
public class DataStatisticsHelper {
    @SuppressLint("StaticFieldLeak")
    private static DataStatisticsHelper sInstance;

    private static final String TAG = DataStatisticsHelper.class.getSimpleName();
    /**
     * 点击热门产品换一换
     */
    public static final String ID_REFRESH_HOT_PRODUCT = "换一换";
    /**
     * 点击热门产品
     */
    public static final String ID_CLICK_HOT_PRODUCT = "热门产品";
    /**
     * 点击一键借款
     */
    public static final String ID_ONE_KEY_LOAN = "一键借款";
    /**
     * 点击联合产品授权注册
     */
    public static final String ID_CLICK_THIRD_AUTHORIZATION = "授权注册";
    /**
     * 点击精选产品
     */
    public static final String ID_CLICK_CHOICE_PRODUCT = "精选产品";
    /**
     * 进入借款界面，点击底部借款tab
     */
    public static final String ID_CLICK_TAB_LOAN = "借款栏";
    /**
     * 进入精选好借界面
     */
    public static final String ID_RESUME_PERSONAL_PRODUCT = "精选好借";
    /**
     * 进入智能推荐界面
     */
    public static final String ID_RESUME_SMART_PRODUCT = "智能推荐";
    /**
     * 选中精选好借分组列表
     */
    public static final String ID_SELECT_PERSONAL_GROUP = "分组";
    /**
     * 点击我要借款
     */
    public static final String ID_CLICK_LOAN_REQUESTED = "我要借款";
    /**
     * 点击启动页广告
     */
    public static final String ID_CLICK_SPLASH_AD = "启动页";
    /**
     * 进入首页
     */
    public static final String ID_ENTER_HOME = "首页";
    /**
     * 点击banner
     */
    public static final String ID_CLICK_BANNER = "banner";
    /**
     * 首页弹窗广告弹出
     */
    public static final String ID_SHOW_HOME_AD_DIALOG = "弹框显示";
    /**
     * 点击首页弹框广告
     */
    public static final String ID_CLICK_HONE_AD_DIALOG = "点击弹框";
    /**
     * 点击测一测身价
     */
    public static final String ID_CLICK_QUALITY_TEST = "测身价";
    /**
     * 点击借款攻略
     */
    public static final String ID_CLICK_HOT_NEWS = "借款攻略";
    /**
     * 进入资讯页面，点击资讯tab
     */
    public static final String ID_CLICK_TAB_NEWS = "资讯栏";
    /**
     * 进入资讯详情页
     */
    public static final String ID_RESUME_NEWS = "资讯详情";
    /**
     * 点击微信公众号
     */
    public static final String ID_CLICK_WECHAT = "微信按钮";
    /**
     * 点击去微信
     */
    public static final String ID_CLICK_GO_WECHAT = "去微信";

    /**
     * 点击Tab栏账单
     */
    public static final String ID_CLICK_TAB_ACCOUNT = "BillingBottom";

    /**
     * 点击还款日历
     * 注释了点击事件
     */
    public static final String ID_CLICK_DEBT_CALENDAR = "RepayCalendarBtn";

    /**
     * 点击负债分析
     * 注释了点击事件
     */
    public static final String ID_CLICK_DEBT_ANALYZE = "LiabilitiesBtn";

    /**
     * 查看还款详情
     */
    public static final String ID_DEBT_DETAIL = "BillDetailBtn";

    /**
     * 修改分期状态为已还
     */
    public static final String ID_SET_STATUS_PAID = "BillPaidOP";

    /**
     * 修改分期状态为待还
     * 注释了点击事件 暂时没有这个功能
     */
    public static final String ID_SET_STATUS_UNPAID = "BillWaitOP";

    /**
     * 首页点击精选好借
     */
    public static final String ID_HOME_MODULE_PERSONAL_PRODUCT = "indexSelectGoodCount";

    /**
     * 首页点击智能推荐
     */
    public static final String ID_HOME_MODULE_SMART = "indexWisdomCount";

    /**
     * 首页点击信用卡
     */
    public static final String ID_HOME_MODULE_CREDIT_CARD = "indexCreditCardLogo";

    /**
     * 首页点击推荐信用卡
     */
    public static final String ID_CLICK_CREDIT_CARD_RECOMMEND = "indexCreditCardRecommend";

    /**
     * 账单首页进入信用卡中心
     * 注释了点击事件
     */
    public static final String ID_ACCOUNT_GO_TO_CREDIT_CARD_CENTER = "indexCreditCard";

    /**
     * 账单首页-新增信用卡账单
     */
    public static final String ID_ACCOUNT_HOME_CLICK_NEW_CREDIT_CARD_BILL = "CCB0001";
    /**
     * 账单首页-新增网贷账单
     * 注释了点击事件
     */
    public static final String ID_ACCOUNT_HOME_CLICK_NEW_LOAN_BILL = "CCB0002";
    /**
     * 账单首页-新增账单（点击"+"）
     */
    public static final String ID_ACCOUNT_HOME_NEW_BILL = "CCB0003";
    /**
     * 新增账单-网银导入 xhb
     */
    public static final String ID_BILL_ENTER_EBANK_LEAD_IN = "CCB0004";
    /**
     * 新增账单-点击邮箱导入 xhb
     */
    public static final String ID_BILL_CLICK_EMAIL_LEAD_IN = "CCB0006";
    /**
     * 新增账单-点击手动记账
     * 注释了点击事件
     */
    public static final String ID_BILL_CLICK_NEW_BY_HAND = "CCB0008";
    /**
     * 新增账单-点击网贷渠道
     */
    public static final String ID_BILL_CLICK_LOAN_CHANNEL = "CCB0009";
    /**
     * 信用卡账单详情
     */
    public static final String ID_BILL_ENTER_CREDIT_CARD_BILL_DETAIL = "CCB0010";


    /**
     * 卡片下拉按钮点击数据
     */
    public static final String ID_BILL_TAB_ACCOUNT_CARD_ARROW = "IndexBtn0001";


    /**
     * 卡片还部分点击
     */
    public static final String ID_BILL_TAB_ACCOUNT_CARD_PART_PAY = "IndexBtn0002";

    /**
     * 详情页还部分点击
     */
    public static final String ID_BILL_DETAIL_PART_PAY = "DetailBtn0001";

    /**
     * 网贷记账自定义
     */
    public static final String ID_BILL_NET_LOAN_CUSTOM_ACCOUNT = "NetLoadBtn0001";

    /**
     * 网贷记账tab栏分期还款点击
     */
    public static final String ID_BILL_NET_LOAN_TAB_BY_STAGES = "NetLoadBtn0002";

    /**
     * APP开启事件
     */
    public static final String ID_OPEN_APP = "OPEN_APP";

    /**
     * 快捷记账按钮
     */
    public static final String ID_BILL_NET_FAST_ACCOUNT = "BookKeepingBtn0001";

    /**
     * 负债分析按钮
     */
    public static final String ID_BILL_NET_BILL_LOAN_ANALYSIS = "DebtAnalysis0001";

    @Inject
    Api api;
    @Inject
    Context context;

    private DataStatisticsHelper() {
        DaggerDataStatisticHelperComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .build()
                .inject(this);
    }

    public static DataStatisticsHelper getInstance() {
        if (sInstance == null) {
            synchronized (DataStatisticsHelper.class) {
                if (sInstance == null) {
                    sInstance = new DataStatisticsHelper();
                }
            }
        }
        return sInstance;
    }

    public void onProductClicked(String id) {
        String userId;
        if (UserHelper.getInstance(context).getProfile() != null) {
            userId = UserHelper.getInstance(context).getProfile().getId();
        } else {
            userId = SPUtils.getCacheUserId(App.getInstance());
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
            userId = SPUtils.getCacheUserId(App.getInstance());
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
            userId = SPUtils.getCacheUserId(App.getInstance());
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

    /**
     * 数据统计
     *
     * @param type 事件id
     */
    public void onCountUv(final String type) {
        String userId;
        if (UserHelper.getInstance(context).isLogin()) {
            userId = UserHelper.getInstance(context).id();
        } else {
            userId = SPUtils.getCacheUserId(context);
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

    /**
     * 数据统计
     *
     * @param type 事件id
     */
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