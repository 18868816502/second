package com.beihui.market.ui.busevents;


public class AuthNavigationEvent {

    public static int TAG_REGISTER = 1;
    public static int TAG_HEAD_TO_LOGIN = 2;

    public int navigationTag;

    public AuthNavigationEvent(int tag) {
        navigationTag = tag;
    }
}
