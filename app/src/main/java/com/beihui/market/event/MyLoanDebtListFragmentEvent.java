package com.beihui.market.event;

/**
 * Created by admin on 2018/6/5.
 */

public class MyLoanDebtListFragmentEvent {

    //0 为通用 1 为 网贷 2为信用卡
    public int type;

    public MyLoanDebtListFragmentEvent(int type) {
        this.type = type;
    }
}
