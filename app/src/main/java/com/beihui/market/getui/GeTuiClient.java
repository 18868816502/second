package com.beihui.market.getui;


import android.content.Context;

import com.igexin.sdk.PushManager;

public class GeTuiClient {

    public static void install(Context context) {
        PushManager.getInstance().registerPushIntentService(context.getApplicationContext(),
                PushReceiveIntentService.class);
    }
}
