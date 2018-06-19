package com.beihui.market.event;

import com.beihui.market.entity.AccountFlowIconBean;

/**
 * Created by admin on 2018/6/19.
 */

public class AccountFlowTypeActivityEvent {

    public AccountFlowIconBean bean;

    public AccountFlowTypeActivityEvent(AccountFlowIconBean bean) {
        this.bean = bean;
    }
}
