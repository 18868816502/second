package com.beiwo.klyjaz.event;


public class InsideViewPagerBus {
    public boolean isRequestInterceptTouchEvent;

    public InsideViewPagerBus(boolean isRequestInterceptTouchEvent) {
        this.isRequestInterceptTouchEvent = isRequestInterceptTouchEvent;
    }
}