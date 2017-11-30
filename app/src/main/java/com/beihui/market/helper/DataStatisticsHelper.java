package com.beihui.market.helper;


import android.annotation.SuppressLint;
import android.content.Context;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.injection.component.DaggerDataStatisticHelperComponent;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;

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
     * 点击我的借款
     */
    public static final String ID_CLICK_MY_LOAN = "我的借款";
    /**
     * 点击启动页广告
     */
    public static final String ID_CLICK_SPLASH_AD = "启动页";
    /**
     * 进入首页
     */
    public static final String ID_ENTER_HOME = "进入首页";
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
     * 点击微信公众号
     */
    public static final String ID_CLICK_WECHAT = "微信按钮";
    /**
     * 点击去微信
     */
    public static final String ID_CLICK_GO_WECHAT = "去微信";


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
        String userId = UserHelper.getInstance(context).getProfile() != null ?
                UserHelper.getInstance(context).getProfile().getId() : null;
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
        String userId = UserHelper.getInstance(context).getProfile() != null ?
                UserHelper.getInstance(context).getProfile().getId() : null;
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

    /**
     * 数据统计
     *
     * @param id 事件id
     */
    public void onCountUv(final String id) {
        String userId = UserHelper.getInstance(context).getProfile() != null ?
                UserHelper.getInstance(context).getProfile().getId() : null;
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
