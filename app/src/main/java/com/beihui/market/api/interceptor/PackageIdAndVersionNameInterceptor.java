package com.beihui.market.api.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.util.SoundUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by xhb on 2018/5/9.
 * 公共调用参数 TODO 需要去做
 * https://blog.csdn.net/zx_android/article/details/79273797
 */

public class PackageIdAndVersionNameInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if ("GET".equals(request.method())) {
            HttpUrl httpUrl = request.url();
            Set<String> ps = httpUrl.queryParameterNames();
            Map<String, Object> params = new HashMap<>();
            if (ps != null) {
                for (String key : ps) {
                    Iterator<String> iterator = ps.iterator();
                    while (iterator.hasNext())
                        params.put(key, iterator.next());
                }
            }
            String url = httpUrl.toString();
            url = url + "?" + "packageId=" + BuildConfig.APPLICATION_ID + "&version=" + BuildConfig.VERSION_NAME;
            StringBuilder sb = new StringBuilder();
            if (params.size() > 0) {
                Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    sb.append("&" + next.getKey()).append("=").append(next.getValue());
                }
            }
            request = request.newBuilder().url(url).build();
        } else if ("POST".equals(request.method())) {
            RequestBody body = request.body();
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (body != null && body instanceof FormBody) {
                FormBody fb = (FormBody) body;
                for (int i = 0; i < fb.size(); i++)
                    bodyBuilder.addEncoded(fb.encodedName(i), fb.encodedValue(i));
            }
            FormBody formBody = bodyBuilder
                    .addEncoded("packageId", App.sChannelId)
                    .addEncoded("packageId", BuildConfig.APPLICATION_ID)
                    .addEncoded("version", BuildConfig.VERSION_NAME)
                    .build();
            request = request.newBuilder().post(formBody).build();
        }

        return chain.proceed(request);
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
            return false;
        }
        return true;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
