package com.beihui.market.ui.busevents;


/**
 * EventBus event for navigating to TabLoan with param query money
 */
public class NavigateLoan {

    public String queryMoney;

    public NavigateLoan(String queryMoney) {
        this.queryMoney = queryMoney;
    }

}
