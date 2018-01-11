package com.beihui.market.ui.busevents;


/**
 * EventBus event for navigating to TabLoan with param query money
 */
public class NavigateLoan {

    public boolean needJumpToSmart;

    public NavigateLoan(boolean needJumpToSmart) {
        this.needJumpToSmart = needJumpToSmart;
    }

}
