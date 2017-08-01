package com.beihui.market.getui;


import android.content.Context;

import com.beihui.market.BuildConfig;
import com.beihui.market.util.LogUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public class PushReceiveIntentService extends GTIntentService {
    private static final String TAG = PushReceiveIntentService.class.getSimpleName();

    @Override
    public void onReceiveServicePid(Context context, int i) {
        LogUtils.e(TAG, "onReceiveServicePid " + i);
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveServicePid " + i);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        LogUtils.e(TAG, "onReceiveClintId " + s);
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveClintId " + s);
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        LogUtils.e(TAG, "onReceiveMessageData " + gtTransmitMessage);
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveMessageData " + gtTransmitMessage);
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        LogUtils.e(TAG, "onReceiveOnlineState " + b);
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveOnlineState " + b);
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        LogUtils.e(TAG, "onReceiveCommandResult " + gtCmdMessage);
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveCommandResult " + gtCmdMessage);
        }
    }
}
