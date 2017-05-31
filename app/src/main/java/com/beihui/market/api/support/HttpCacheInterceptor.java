package com.beihui.market.api.support;

import android.util.Log;

import com.google.gson.Gson;
import com.beihui.market.App;
import com.beihui.market.util.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpCacheInterceptor implements Interceptor {

    private static final String TAG = "HttpManager";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .method(original.method(), original.body());

        Headers.Builder hb = new Headers.Builder();
        addHeader(hb,original);
        if (!NetUtils.isConnected(App.getInstance())) {
            requestBuilder.cacheControl(CacheControl.FORCE_CACHE);
            Log.d(TAG, "网络不可用请求拦截");
        } else {//网络可用
            requestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
        }
        Request request = requestBuilder.headers(hb.build()).build();
        Log.d(TAG, "地址：" + request.url());
        String jsonRequest = new Gson().toJson(request.body());
        Log.d(TAG, "请求参数：" + jsonRequest);
        String jsonRequest1 = new Gson().toJson(request.headers());
        Log.d(TAG, "请求头部：" + jsonRequest1);

        try {
            Response response = chain.proceed(request);

            if (NetUtils.isConnected(App.getInstance())) {//如果网络可用
                int maxAge = 60 * 3;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }catch (Exception err){
            Log.e("HttpManager","http=============" + err.getLocalizedMessage());
        }
        return null;
    }




    /**
     * 增加消息头
     */
    private void addHeader(Headers.Builder header,Request original) {
            header.add("accessKey", "Constant.accessKey");
    }
}
