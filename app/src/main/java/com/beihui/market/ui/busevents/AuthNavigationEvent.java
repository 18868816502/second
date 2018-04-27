package com.beihui.market.ui.busevents;


public class AuthNavigationEvent {

    /**
     *账号密码登陆
     */
    public static int TAG_LOGIN_PSD = 1;
    /**
     *回到快速登陆注册
     */
    public static int TAG_HEAD_TO_LOGIN = 2;
    /**
     *快速登陆注册
     * TAG_SET_PSD
     */
    public static int TAG_LOGIN_FAST = 3;

    public int navigationTag;

    public String requestPhone;

    public AuthNavigationEvent(int tag) {
        navigationTag = tag;
    }
}
