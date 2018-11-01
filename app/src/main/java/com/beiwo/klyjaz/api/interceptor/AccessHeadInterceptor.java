package com.beiwo.klyjaz.api.interceptor;


import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.util.NetUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
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

public class AccessHeadInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Map<String, Object> keyValue = new HashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        long reqTime = System.currentTimeMillis();

        Request request = chain.request();
        //添加公共参数packageId和version
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
            url = url + "?" + "packageId=" + App.sChannelId + "&terminal=1&version=" + BuildConfig.VERSION_NAME + "&userIp=" + NetUtils.getIPAddress(App.getInstance());
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
                    .addEncoded("version", BuildConfig.VERSION_NAME)
                    .addEncoded("userIp", NetUtils.getIPAddress(App.getInstance()))
                    .addEncoded("terminal", "1")
                    .build();
            request = request.newBuilder().post(formBody).build();
        }

        Request.Builder builder = request.newBuilder()
                .addHeader("reqTime", "" + reqTime)
                .addHeader("accessKey", NetConstants.ACCESS_KEY);

        StringBuilder sb = new StringBuilder(NetConstants.SECRET_KEY).append(reqTime);
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            MediaType contentType = requestBody.contentType();
            if (contentType != null && contentType.toString().equals("application/x-www-form-urlencoded")) {
                appendHeadWithBody(request, sb);
            }
        }

        String md5Str = Digest.md5(sb.toString());
        byte[] byteArr = Digest.hexStr2Bytes(md5Str);
        String first = Digest.bytes2HexStr(byteArr);
        String secondMd5str = Digest.md5(first);
        byte[] secondByteArr = Digest.hexStr2Bytes(secondMd5str);
        String second = Digest.bytes2HexStr(secondByteArr);
        builder.addHeader("sign", second);
        //builder.addHeader("sign", new String(Hex.encodeHex(DigestUtils.md5(new String(Hex.encodeHex(DigestUtils.md5(sb.toString())))))));
        return chain.proceed(builder.build());
    }

    private synchronized void appendHeadWithBody(Request request, StringBuilder sb) {
        try {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            if (requestBody != null) {
                requestBody.writeTo(buffer);

                Charset charset = null;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (charset == null) {
                    charset = UTF8;
                }
                String param = buffer.readString(charset);
                String[] pairs = param.split("&");
                String[] keys = new String[pairs.length];
                keyValue.clear();
                for (int i = 0, len = pairs.length; i < len; ++i) {
                    String[] keyvalue = pairs[i].split("=");
                    String keyDecode = URLDecoder.decode(keyvalue[0], "utf-8");
                    keys[i] = keyDecode;
                    if (keyvalue.length != 2) {
                        keyValue.put(keyDecode, "");
                        continue;
                    }
                    keyValue.put(keyDecode, URLDecoder.decode(keyvalue[1], "utf-8"));
                }
                Arrays.sort(keys);
                for (String key : keys) sb.append(key).append(keyValue.get(key));
            }
        } catch (IOException e) {
        }
    }
}