package com.beihui.market;

import android.app.Application;

import com.beihui.market.base.Constant;
import com.beihui.market.helper.ActivityTracker;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerAppComponent;
import com.beihui.market.injection.module.ApiModule;
import com.beihui.market.injection.module.AppModule;
import com.beihui.market.umeng.Umeng;

import cn.xiaoneng.uiapi.Ntalker;

public class App extends Application {

    private static App sInstance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        /**
         * 调用Application.registerActivityLifecycleCallbacks()方法，并实现ActivityLifecycleCallbacks接口
         * Application通过此接口提供了一套回调方法，用于让开发者对Activity的生命周期事件进行集中处理
         */
        registerActivityLifecycleCallbacks(ActivityTracker.getInstance());
        initComponent();

        Umeng.install(this);

        Ntalker.getBaseInstance().initSDK(this, Constant.XN_SITE_ID, Constant.XN_SITE_KEY);
        //如果用户已登录，则登录小能客服
        if (UserHelper.getInstance(this).getProfile() != null) {
            Ntalker.getBaseInstance().login(UserHelper.getInstance(this).getProfile().getId(),
                    UserHelper.getInstance(this).getProfile().getUserName());
        }
        Ntalker.getBaseInstance().enableDebug(BuildConfig.DEBUG);
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
