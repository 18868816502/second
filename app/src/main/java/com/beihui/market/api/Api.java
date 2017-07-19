package com.beihui.market.api;


import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.entity.request.ReqLogin;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {

    private static Api sInstance;
    private ApiService service;

    public static Api getInstance(OkHttpClient okHttpClient) {
        synchronized (Api.class) {
            if (sInstance == null) {
                synchronized (Api.class) {
                    sInstance = new Api(okHttpClient);
                }
            }
        }
        return sInstance;
    }

    private Api(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    /**
     * 登录
     *
     * @param account 用户账号
     * @param pwd     用户密码
     */
    public Observable<ResultEntity<UserProfileAbstract>> login(String account, String pwd) {
        return service.login(new ReqLogin(account, pwd));
    }


}
