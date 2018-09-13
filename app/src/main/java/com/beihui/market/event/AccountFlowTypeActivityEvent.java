package com.beihui.market.event;

import com.beihui.market.entity.AccountFlowIconBean;



public class AccountFlowTypeActivityEvent {
    public AccountFlowIconBean bean;

    public AccountFlowTypeActivityEvent(AccountFlowIconBean bean) {
        this.bean = bean;
    }
}
