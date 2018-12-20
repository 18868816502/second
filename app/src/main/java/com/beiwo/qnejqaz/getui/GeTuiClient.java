package com.beiwo.qnejqaz.getui;


import com.beiwo.qnejqaz.App;
import com.igexin.sdk.PushManager;

public class GeTuiClient {
    public static void install() {
        PushManager.getInstance().initialize(App.getInstance(), PushService.class);
        PushManager.getInstance().registerPushIntentService(App.getInstance(),
                PushReceiveIntentService.class);
    }
}