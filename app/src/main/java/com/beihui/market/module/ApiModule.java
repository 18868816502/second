package com.beihui.market.module;


import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.support.LoggingInterceptor;
import com.beihui.market.util.LogUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/1/16.
 */
@Module
public class ApiModule {

    @Provides
    public OkHttpClient provideOKHttpClient() {
        LoggingInterceptor logging = new LoggingInterceptor(new MyLog());
        logging.setLevel(LoggingInterceptor.Level.BODY);
        File cacheFile = new File(App.getInstance().getCacheDir().getAbsolutePath(), "ShopHttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
//                .addInterceptor(new HttpCacheInterceptor())
                .addInterceptor(logging)
                .cache(cache);

        return builder.build();
    }


    @Provides
    protected Api provideService(OkHttpClient okHttpClient){
        return Api.getInstance(okHttpClient);
    }


    public static class MyLog implements LoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogUtils.i("oklog: " + message);
        }
    }


}
