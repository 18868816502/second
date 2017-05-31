package com.beihui.market;

import android.app.Application;

import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerAppComponent;
import com.beihui.market.module.ApiModule;
import com.beihui.market.module.AppModule;
import com.beihui.market.util.FileUtil;

/**
 * Created by C on 2017/5/17.
 */

public class App extends Application {

    private static App instance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;

        initComponent();

        FileUtil.initFileCache(this);

    }


    public static App getInstance() {
        return instance;
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
