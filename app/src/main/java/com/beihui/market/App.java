package com.beihui.market;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.beihui.market.api.Api;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.Audit;
import com.beihui.market.helper.ActivityTracker;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerAppComponent;
import com.beihui.market.injection.module.ApiModule;
import com.beihui.market.injection.module.AppModule;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
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

import java.util.List;

import cn.xiaoneng.uiapi.Ntalker;


public class App extends Application {

    private static App sInstance;
    private AppComponent appComponent;
    public static WindowManager mWindowManager;//窗口
    public static int mWidthPixels;//屏幕宽度（像素）
    public static String sChannelId = "unknown";
    public static int step = 0;//认证步骤
    public static int audit = 1;//是否审核中

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context).setColorSchemeColors(ContextCompat.getColor(App.getInstance(), R.color.refresh_one));//指定为经典Header，默认是 贝塞尔雷达Header
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
        SPUtils.setShowMainAddBanner(this, true);
        registerActivityLifecycleCallbacks(ActivityTracker.getInstance());//activity生命周期管理
        initComponent();

        Umeng.install(this);//初始化友盟
        MoxieSDK.init(this);//初始化魔蝎

        Ntalker.getBaseInstance().initSDK(this, Constant.XN_SITE_ID, Constant.XN_SITE_KEY);
        //如果用户已登录，则登录小能客服
        if (UserHelper.getInstance(this).isLogin()) {
            Ntalker.getBaseInstance().login(UserHelper.getInstance(this).id(),
                    UserHelper.getInstance(this).getProfile().getUserName());
        }
        Ntalker.getBaseInstance().enableDebug(BuildConfig.DEBUG);
        try {
            sChannelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
            //sChannelId = AnalyticsConfig.getChannel(this);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (TextUtils.equals(getProcessName(this), getPackageName())) {
            //pv，uv统计
            String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            if (UserHelper.getInstance(this).isLogin()) {
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_OPEN_APP);
            } else {
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_OPEN_APP, androidId);
            }
            if (SPUtils.getFirstInstall(this)) {
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_FIRST_INSTALL, androidId);
                SPUtils.setFirstInstall(this, false);
            }
            /*是否审核状态*/
            Api.getInstance().audit()
                    .compose(RxResponse.<Audit>compatT())
                    .subscribe(new ApiObserver<Audit>() {
                        @Override
                        public void onNext(Audit data) {
                            try {
                                audit = data.audit;
                            } catch (Exception e) {
                            }
                        }
                    });

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

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private String getProcessName(Context cxt) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) return null;
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == android.os.Process.myPid()) {
                return procInfo.processName;
            }
        }
        return null;
    }
}