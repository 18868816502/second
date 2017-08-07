package com.beihui.market.util;


public class FastClickUtils {

    private static long lastClickTime;

    public static boolean isFastClick() {
        long last = lastClickTime;
        return (lastClickTime = System.currentTimeMillis()) - last < 1000;
    }
}
