package com.beihui.market.api;


import com.beihui.market.base.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {

    public static Api instance;
    private ApiService service;

    public Api(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }


    public static Api getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new Api(okHttpClient);
        return instance;
    }


}
