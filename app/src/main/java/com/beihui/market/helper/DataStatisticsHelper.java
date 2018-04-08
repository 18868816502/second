package com.beihui.market.helper;


import android.annotation.SuppressLint;
import android.content.Context;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.entity.CreditCard;
import com.beihui.market.injection.component.DaggerDataStatisticHelperComponent;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
     */
    public static final String ID_CLICK_DEBT_CALENDAR = "RepayCalendarBtn";

    /**
     * 点击负债分析
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
     */
    public static final String ID_ACCOUNT_GO_TO_CREDIT_CARD_CENTER = "indexCreditCard";

    /**
     * 账单首页-新增信用卡账单
     */
    public static final String ID_ACCOUNT_HOME_CLICK_NEW_CREDIT_CARD_BILL = "CCB0001";
    /**
     * 账单首页-新增网贷账单
     */
    public static final String ID_ACCOUNT_HOME_CLICK_NEW_LOAN_BILL = "CCB0002";
    /**
     * 账单首页-新增账单（点击"+"）
     */
    public static final String ID_ACCOUNT_HOME_NEW_BILL = "CCB0003";
    /**
     * 新增账单-网银导入
     */
    public static final String ID_BILL_ENTER_EBANK_LEAD_IN = "CCB0004";
    /**
     * 新增账单-点击邮箱导入
     */
    public static final String ID_BILL_CLICK_EMAIL_LEAD_IN = "CCB0006";
    /**
     * 新增账单-点击手动记账
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
        api.onProductClicked(userId, id).compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity resultEntity) throws Exception {
                                   if (!resultEntity.isSuccess()) {
                                       LogUtils.e(TAG, "product statistics error. message " + resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "product statistics error " + throwable);
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
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity resultEntity) throws Exception {
                                   if (!resultEntity.isSuccess()) {
                                       LogUtils.e(TAG, "ad statistics error. message " + resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "ad statistics error " + throwable);
                            }
                        });
    }

    public void onInternalMessageClicked(String id) {
        api.onInternalMessageClicked(id)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity resultEntity) throws Exception {
                                   if (!resultEntity.isSuccess()) {
                                       LogUtils.e(TAG, "internal message statistic error. message " + resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "internal message statistic error " + throwable);
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
                .compose(RxUtil.<ResultEntity<CreditCard.Row>>io2main())
                .subscribe(new Consumer<ResultEntity<CreditCard.Row>>() {
                               @Override
                               public void accept(ResultEntity<CreditCard.Row> resultEntity) throws Exception {
                                   if (!resultEntity.isSuccess()) {
                                       LogUtils.e(TAG, "credit card statistic error. message " + resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "credit card statistic error " + throwable);
                            }
                        });
        //推荐信用卡点击
        onCountUv(ID_CLICK_CREDIT_CARD_RECOMMEND);

    }

    /**
     * 数据统计
     *
     * @param id 事件id
     */
    public void onCountUv(final String id) {
        String userId;
        if (UserHelper.getInstance(context).getProfile() != null) {
            userId = UserHelper.getInstance(context).getProfile().getId();
        } else {
            userId = SPUtils.getCacheUserId(context);
        }
        api.onCountUv(id, userId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) throws Exception {
                                   if (!resultEntity.isSuccess()) {
                                       LogUtils.e(TAG, "count uv error event id=" + id + ", message=" + resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(TAG, "count uv error event id = " + id + " " + throwable);
                            }
                        });
    }
}
