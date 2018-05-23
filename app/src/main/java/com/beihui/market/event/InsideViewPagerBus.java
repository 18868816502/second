package com.beihui.market.event;

/**
 * *      ┏┓　　　┏┓
 * *    ┏┛┻━━━┛┻┓
 * *    ┃　　　　　　　┃
 * *    ┃　　　━　　　┃
 * *    ┃　┳┛　┗┳　┃
 * *    ┃　　　　　　　┃
 * *    ┃　　　┻　　　┃
 * *    ┃　　　　　　　┃
 * *    ┗━┓　　　┏━┛
 * *       ┃　　　┃   神兽保佑
 * *       ┃　　　┃   代码无BUG！
 * *       ┃　　　┗━━━┓
 * *       ┃　　　　　　　┣┓
 * *       ┃　　　　　　　┏┛
 * *       ┗┓┓┏━┳┓┏┛━━━━━┛
 * *         ┃┫┫　┃┫┫
 * *         ┗┻┛　┗┻┛
 * *
 * Created by opq on 2018/1/8.
 */

public class InsideViewPagerBus {

    public boolean isRequestInterceptTouchEvent;

    public InsideViewPagerBus(boolean isRequestInterceptTouchEvent) {
        this.isRequestInterceptTouchEvent = isRequestInterceptTouchEvent;
    }
}
