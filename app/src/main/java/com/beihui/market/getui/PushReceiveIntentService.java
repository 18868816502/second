package com.beihui.market.getui;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.NewsDetailActivity;
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
            Log.i(TAG, "onReceiveServicePid " + i);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onReceiveClintId " + s);
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
            Log.i(TAG, "onReceiveMessageData " + gtTransmitMessage);
        }
        try {
            String json = new String(gtTransmitMessage.getPayload());
            JSONObject obj = new JSONObject(json);

            String title = obj.getString("title") != null ? obj.getString("title") : "";
            String content = obj.getString("content") != null ? obj.getString("content") : "";

            Intent intent = null;
            int type = obj.getInt("type");
            if (type == 1) {//跳转原生界面
                int localType = obj.getInt("localType");
                String localId = obj.getString("localId");
                if (localType == 1) {
                    //借款产品
                    intent = new Intent(context, LoanDetailActivity.class);
                    intent.putExtra("loanId", localId);
                } else if (localType == 2) {
                    //资讯
                    intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("newsId", localId);
                    intent.putExtra("newsTitle", title);
                }
            } else if (type == 2) {//跳转网页
                intent = new Intent(context, ComWebViewActivity.class);
                intent.putExtra("url", obj.getString("url"));
                intent.putExtra("title", title);
            } else if (type == 3) { //站内推送内容
                intent = new Intent(context, ComWebViewActivity.class);
                intent.putExtra("url", NetConstants.generateInternalMessageUrl(obj.getString("localId")));
                intent.putExtra("title", title);
            } else if (type == 4) {//跳转到首页
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("home", true);
            }
            NotificationUtil.showNotification(context, title, content, intent, context.getPackageName() + ".push_message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onReceiveOnlineState " + b);
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onReceiveCommandResult " + gtCmdMessage);
        }
    }
}
