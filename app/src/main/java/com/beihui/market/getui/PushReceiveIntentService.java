package com.beihui.market.getui;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.util.NotificationUtil;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;

public class PushReceiveIntentService extends GTIntentService {
    private static final String TAG = PushReceiveIntentService.class.getSimpleName();

    @Override
    public void onReceiveServicePid(Context context, int i) {
        if (BuildConfig.DEBUG) {
            //Log.i(TAG, "onReceiveServicePid " + i);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        if (BuildConfig.DEBUG) {
            //Log.i(TAG + 726, "onReceiveClintId " + s);
        }
        if (UserHelper.getInstance(App.getInstance()).getProfile() != null) {
            String bindClientId = SPUtils.getPushBindClientId(context);
            String bindUserId = SPUtils.getPushBindUserId(context);
            String userId = UserHelper.getInstance(App.getInstance()).getProfile().getId();
            //如果相同的userId，个推clientId已经绑定过，则不要再发送请求
            if (!userId.equals(bindUserId) || !s.equals(bindClientId)) {
                //记录相关id
                SPUtils.setPushBindClientId(context, s);
                SPUtils.setPushBindUserId(context, userId);

                Api.getInstance().bindClientId(userId, s)
                        .compose(RxUtil.<ResultEntity>io2main())
                        .subscribe(new Consumer<ResultEntity>() {
                                       @Override
                                       public void accept(ResultEntity result) throws Exception {
                                           if (!result.isSuccess()) {
                                               Log.e(TAG, result.getMsg());
                                           }
                                       }
                                   },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e(TAG, "throwable " + throwable);
                                    }
                                });
            }
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        if (BuildConfig.DEBUG) {
            //Log.i(TAG, "onReceiveMessageData " + gtTransmitMessage);
        }
        try {
            String json = new String(gtTransmitMessage.getPayload());
            JSONObject obj = new JSONObject(json);
            String title = obj.getString("title") != null ? obj.getString("title") : "";
            String content = obj.getString("content") != null ? obj.getString("content") : "";

            Intent intent = new Intent("send_push_clicked_event");
            intent.putExtra("pending_json", json);
            NotificationUtil.showNotification(context, title, content, intent, context.getPackageName() + ".push_message", false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        if (BuildConfig.DEBUG) {
            //Log.i(TAG, "onReceiveOnlineState " + b);
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        if (BuildConfig.DEBUG) {
            //Log.i(TAG, "onReceiveCommandResult " + gtCmdMessage);
        }
    }
}
