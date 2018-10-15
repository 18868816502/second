package com.beiwo.klyjaz.getui;


import android.content.Context;
import android.content.Intent;

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
    public void onReceiveClientId(Context context, String s) {
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
                        .compose(RxResponse.compatO())
                        .subscribe(new ApiObserver<Object>() {
                            @Override
                            public void onNext(@NonNull Object data) {
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
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
    }
}