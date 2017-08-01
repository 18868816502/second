package com.beihui.market.api;


public class NetConstants {

    public static final String DOMAIN = "http://116.62.113.207:9070";

    public static final String SECRET_KEY = "0bca3e8e2baa42218040c5dbf6978f315e104e5c";

    public static final String ACCESS_KEY = "699b9305418757ef9a26e5a32ca9dbfb";

    /**********H5 static field********/
    public static final String H5_DOMAIN = "http://192.168.1.34:99";

    public static final String H5_ADVICE = H5_DOMAIN + "/advice?isApp=1";

    public static final String H5_HELPER = H5_DOMAIN + "/helpcenter?isApp=1";

    public static final String H5_TEST = H5_DOMAIN + "/test?isApp=1";

    public static final String H5_NEWS_DETAIL = H5_DOMAIN + "/newsDetail";

    public static String generateNewsUrl(String id) {
        return H5_NEWS_DETAIL + "?id=" + id + "&isApp=1";
    }
}
