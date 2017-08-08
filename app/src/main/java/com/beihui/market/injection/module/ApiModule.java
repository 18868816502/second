package com.beihui.market.injection.module;


import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.api.Api;
import com.beihui.market.api.interceptor.AccessHeadInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class ApiModule {
    @Provides
    public OkHttpClient provideOkHttpClient() {
        File cacheFile = new File(App.getInstance().getCacheDir().getAbsolutePath(), "ShopHttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(new AccessHeadInterceptor());


        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }


    @Provides
    protected Api provideApi(OkHttpClient okHttpClient) {
        return Api.getInstance(okHttpClient);
    }


}
