package com.beihui.market.api;


import com.beihui.market.base.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/1/16.
 */

public class Api {

    public static Api instance;
    private ApiService service;

    public Api(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }


    public static Api getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new Api(okHttpClient);
        return instance;
    }



    /**
     * 注册
     *
     * @param account
     * @param pwd
     * @param channelType
     * @return
     */
//    public Observable<Register> aRegister(String account, String pwd, String verifCode, String channelType) {
//        FwsHttpParames parames = new FwsHttpParames();
//        parames.put("account", account);
//        parames.put("verifCode", verifCode);
//        parames.put("pwd", pwd);
//        parames.put("channelType", channelType);
//        parames.put("userType", "1");
//        parames.put("platform", "android");
//        parames.put("tokenKey", BqsDF.getTokenKey());
//        String blackBox = FMAgent.onEvent(App.getInstance());
//        parames.put("tongdunKey", blackBox);
//
//
//        String time = TimeUtils.getSingTime(new Date());
//        String sign = CommonUtils.generateSign(parames, time);
//
//        System.out.println("***************sign******************" + sign);
//
//        return service.register(sign, time, Constant.accessKey, account, pwd, verifCode, channelType, "1", "android", BqsDF.getTokenKey(),blackBox);
//    }



}
