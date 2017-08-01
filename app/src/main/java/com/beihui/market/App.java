package com.beihui.market;

import android.app.Application;

import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerAppComponent;
import com.beihui.market.injection.module.ApiModule;
import com.beihui.market.injection.module.AppModule;
import com.beihui.market.umeng.Umeng;
import com.squareup.leakcanary.LeakCanary;

public class App extends Application {

    private static App sInstance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initComponent();

        LeakCanary.install(this);
        Umeng.install(this);
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
}
