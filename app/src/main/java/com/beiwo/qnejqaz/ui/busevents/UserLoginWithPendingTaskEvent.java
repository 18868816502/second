package com.beiwo.qnejqaz.ui.busevents;


import com.beiwo.qnejqaz.entity.AdBanner;

public class UserLoginWithPendingTaskEvent {
    public AdBanner adBanner;

    public UserLoginWithPendingTaskEvent(AdBanner adBanner) {
        this.adBanner = adBanner;
    }
}