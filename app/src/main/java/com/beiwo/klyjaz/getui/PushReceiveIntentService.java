package com.beiwo.klyjaz.getui;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.NotificationUtil;
import com.beiwo.klyjaz.util.SPUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;

public class PushReceiveIntentService extends GTIntentService {
    @Override
    public void onReceiveServicePid(Context context, int i) {
    }

    @Override
    public void onReceiveClientId(Context context, String clientId) {
        if (TextUtils.isEmpty(clientId)) return;
        //System.out.println("clientId : " + clientId);
        if (UserHelper.getInstance(App.getInstance()).isLogin()) {
            String bindClientId = SPUtils.getPushBindClientId(context);
            String bindUserId = SPUtils.getPushBindUserId(context);
            String userId = UserHelper.getInstance(context).id();
            //如果相同的userId，个推clientId已经绑定过，则不要再发送请求
            if (!userId.equals(bindUserId) || !clientId.equals(bindClientId)) {
                //记录相关id
                SPUtils.setPushBindClientId(context, clientId);
                SPUtils.setPushBindUserId(context, userId);
                //System.out.println(userId + " === " + clientId);
                Api.getInstance().bindClientId(userId, clientId)
                        .compose(RxResponse.compatO())
                        .subscribe(new ApiObserver<Object>() {
                            @Override
                            public void onNext(@NonNull Object data) {
                                //System.out.println("bind getui success");
                            }
                        });
            }
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        try {
            String json = new String(gtTransmitMessage.getPayload());
            JSONObject obj = new JSONObject(json);
            String title = obj.optString("title") != null ? obj.optString("title") : "";
            String content = obj.optString("content") != null ? obj.optString("content") : "";

            Intent intent = new Intent("send_push_clicked_event");
            intent.putExtra("pending_json", json);
            NotificationUtil.showNotification(context, title, content, intent, context.getPackageName() + ".push_message", false);
        } catch (Exception e) {
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
    }
}