package com.beihui.market.api.interceptor;


import com.beihui.market.api.NetConstants;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class AccessHeadInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        long reqTime = System.currentTimeMillis();

        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
                .addHeader("reqTime", "" + reqTime)
                .addHeader("accessKey", NetConstants.ACCESS_KEY);
        String sign = null;
        try {
            sign = generateSign(request, reqTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sign != null) {
            builder.addHeader("sign", sign);
        }
        return chain.proceed(builder.build());
    }

    private String generateSign(Request request, long reqTime) throws JSONException, IOException {
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            if (isPlaintext(buffer)) {
                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                String str = buffer.readString(charset);
                JSONObject jsonObject = new JSONObject(str);
                //sign rule
                StringBuilder sb = new StringBuilder(NetConstants.SECRET_KEY);
                sb.append(reqTime);
                Iterator<String> it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    sb.append(key).append(jsonObject.getString(key));
                }
                return new String(Hex.encodeHex(DigestUtils.md5(new String(Hex.encodeHex(DigestUtils.md5(sb.toString()))))));
            }
        }
        return null;
    }

    boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
