package com.beihui.market.module;


import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.api.Api;
import com.beihui.market.api.support.LoggingInterceptor;
import com.beihui.market.util.LogUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module
public class ApiModule {

    @Provides
    public OkHttpClient provideOkHttpClient() {
        File cacheFile = new File(App.getInstance().getCacheDir().getAbsolutePath(), "ShopHttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache);

        if (BuildConfig.DEBUG) {
            LoggingInterceptor logging = new LoggingInterceptor(new DebugLog());
            logging.setLevel(LoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }


    @Provides
    protected Api provideApi(OkHttpClient okHttpClient) {
        return Api.getInstance(okHttpClient);
    }


    static class DebugLog implements LoggingInterceptor.Logger {

        private static final String TAG = "HttpDebug";

        @Override
        public void log(String message) {
            LogUtils.i(TAG, message);
        }
    }


}
