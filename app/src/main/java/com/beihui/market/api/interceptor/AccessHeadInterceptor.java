package com.beihui.market.api.interceptor;


import com.beihui.market.api.NetConstants;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessHeadInterceptor implements Interceptor {

    private List<String> sortedKey = new ArrayList<>();
    private Comparator<String> keyComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        long reqTime = System.currentTimeMillis();

        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
                .addHeader("reqTime", "" + reqTime)
                .addHeader("accessKey", NetConstants.ACCESS_KEY);

        HttpUrl url = request.url();
        //排序只有MD5
        sortedKey.clear();
        Set<String> keys = url.queryParameterNames();
        sortedKey.addAll(keys);
        Collections.sort(sortedKey, keyComparator);

        StringBuilder sb = new StringBuilder(NetConstants.SECRET_KEY).append(reqTime);
        for (String key : sortedKey) {
            List<String> values = url.queryParameterValues(key);
            if (values != null && values.size() > 0) {
                sb.append(key).append(values.get(0));
            }
        }
        builder.addHeader("sign", new String(Hex.encodeHex(DigestUtils.md5(new String(Hex.encodeHex(DigestUtils.md5(sb.toString())))))));
        return chain.proceed(builder.build());
    }

}
