package com.beihui.market.ui.busevents;


import com.beihui.market.entity.AdBanner;

public class UserLoginWithPendingTaskEvent {

    public AdBanner adBanner;

    public UserLoginWithPendingTaskEvent(AdBanner adBanner) {
        this.adBanner = adBanner;
    }
}
