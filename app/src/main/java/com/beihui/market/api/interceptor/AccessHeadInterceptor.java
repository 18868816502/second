package com.beihui.market.api.interceptor;


import com.beihui.market.api.NetConstants;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessHeadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        long reqTime = System.currentTimeMillis();

        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
                .addHeader("reqTime", "" + reqTime)
                .addHeader("accessKey", NetConstants.ACCESS_KEY);

        StringBuilder sb = new StringBuilder(NetConstants.SECRET_KEY).append(reqTime);
        HttpUrl url = request.url();
        for (String key : url.queryParameterNames()) {
            sb.append(key).append(url.queryParameterValues(key).get(0));
        }
        builder.addHeader("sign", new String(Hex.encodeHex(DigestUtils.md5(new String(Hex.encodeHex(DigestUtils.md5(sb.toString())))))));
        return chain.proceed(builder.build());
    }

}
