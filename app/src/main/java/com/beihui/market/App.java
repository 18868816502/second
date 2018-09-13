package com.beihui.market;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.beihui.market.base.Constant;
import com.beihui.market.helper.ActivityTracker;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerAppComponent;
import com.beihui.market.injection.module.ApiModule;
import com.beihui.market.injection.module.AppModule;
import com.beihui.market.umeng.Umeng;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.jiang.ClassicFooter;
import com.moxie.client.manager.MoxieSDK;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.umeng.analytics.AnalyticsConfig;

import cn.xiaoneng.uiapi.Ntalker;


public class App extends Application {

    private static App sInstance;
    private AppComponent appComponent;

    //窗口
    public static WindowManager mWindowManager;
    //屏幕宽度（像素）
    public static int mWidthPixels;

    /**
     * 渠道
     */
    public static String sChannelId = "unknown";

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context).setColorSchemeColors(App.getInstance().getResources().getColor(R.color.c_ff5240));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setEnableAutoLoadMore(false);//使上拉加载具有弹性效果
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        /**
         * 用于启动首页广告
         */
        SPUtils.setShowMainAddBanner(this, true);
        /**
         * 调用Application.registerActivityLifecycleCallbacks()方法，并实现ActivityLifecycleCallbacks接口
         * Application通过此接口提供了一套回调方法，用于让开发者对Activity的生命周期事件进行集中处理
         */
        registerActivityLifecycleCallbacks(ActivityTracker.getInstance());
        initComponent();

        Umeng.install(this);

        //初始化魔蝎
        MoxieSDK.init(this);

        Ntalker.getBaseInstance().initSDK(this, Constant.XN_SITE_ID, Constant.XN_SITE_KEY);
        //如果用户已登录，则登录小能客服
        if (UserHelper.getInstance(this).getProfile() != null) {
            Ntalker.getBaseInstance().login(UserHelper.getInstance(this).getProfile().getId(),
                    UserHelper.getInstance(this).getProfile().getUserName());
        }
        Ntalker.getBaseInstance().enableDebug(BuildConfig.DEBUG);


        //生成发现页链接
        try {
            sChannelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        String channel = AnalyticsConfig.getChannel(this);
        Log.e("xhb", "友盟渠道名称-----> " + channel);

        /**
         * app开启事件
         */
        //pv，uv统计
        String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (UserHelper.getInstance(this).isLogin()) {
            DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_OPEN_APP);
        } else {
            DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_OPEN_APP, androidId);
        }

        //获取WindowManager
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //分辨率
        DisplayMetrics metric = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        //屏幕宽度（像素）
        mWidthPixels = metric.widthPixels;
    }

    public static App getInstance() {
        return sInstance;
    }

    /**
     * 两个module
     */
    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}