package com.beiwo.klyjaz.helper;


import android.annotation.SuppressLint;
import android.content.Context;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.entity.CreditCard;
import com.beiwo.klyjaz.injection.component.DaggerDataStatisticHelperComponent;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.LogUtils;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.SPUtils;


import javax.inject.Inject;

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
            userId = SPUtils.getCacheUserId();
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