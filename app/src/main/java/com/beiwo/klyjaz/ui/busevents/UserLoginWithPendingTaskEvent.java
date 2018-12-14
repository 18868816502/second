package com.beiwo.klyjaz.ui.busevents;


import com.beiwo.klyjaz.entity.AdBanner;

public class UserLoginWithPendingTaskEvent {
    public AdBanner adBanner;

    public UserLoginWithPendingTaskEvent(AdBanner adBanner) {
        this.adBanner = adBanner;
    }
}