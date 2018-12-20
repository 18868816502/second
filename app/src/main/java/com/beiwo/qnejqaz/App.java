package com.beiwo.qnejqaz;

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

import com.beiwo.qnejqaz.base.Constant;
import com.beiwo.qnejqaz.goods.ExitPageUtil;
import com.beiwo.qnejqaz.helper.ActivityTracker;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.umeng.Umeng;
import com.beiwo.qnejqaz.util.SPUtils;
import com.beiwo.qnejqaz.view.jiang.ClassicFooter;
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
    public static WindowManager mWindowManager;//窗口
    public static int mWidthPixels;//屏幕宽度（像素）
    public static String androidId;//设备Id
    public static String sChannelId = "unknown";
    public static int step = 0;//认证步骤
    public static int audit = 1;//是否审核中

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {

            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context).setColorSchemeColors(ContextCompat.getColor(App.getInstance(), R.color.refresh_one));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
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
        try {
            androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            sChannelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
            //sChannelId = AnalyticsConfig.getChannel(this);
        } catch (PackageManager.NameNotFoundException e) {
        }
        SPUtils.setShowMainAddBanner(true);
        registerActivityLifecycleCallbacks(ActivityTracker.getInstance());//activity生命周期管理

        initComponent();
        if (TextUtils.equals(getProcessName(this), getPackageName())) {
            DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_OPEN, "", "", 0);
            //pv，uv统计
            if (UserHelper.getInstance(this).isLogin()) {
                DataHelper.getInstance(this).onCountUv(DataHelper.ID_OPEN_APP);
            } else {
                DataHelper.getInstance(this).onCountUv(DataHelper.ID_OPEN_APP, androidId);
            }
            if (SPUtils.getFirstInstall()) {
                DataHelper.getInstance(this).onCountUv(DataHelper.ID_FIRST_INSTALL, androidId);
                SPUtils.setFirstInstall(false);
            }
        }

        Umeng.install(this);//初始化友盟
        MoxieSDK.init(this);//初始化魔蝎

        Ntalker.getBaseInstance().initSDK(this, Constant.XN_SITE_ID, Constant.XN_SITE_KEY);
        //如果用户已登录，则登录小能客服
        if (UserHelper.getInstance(this).isLogin()) {
            Ntalker.getBaseInstance().login(UserHelper.getInstance(this).id(),
                    UserHelper.getInstance(this).getProfile().getUserName());
        }
        Ntalker.getBaseInstance().enableDebug(BuildConfig.DEBUG);
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