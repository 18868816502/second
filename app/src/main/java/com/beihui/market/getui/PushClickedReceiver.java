package com.beihui.market.getui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beihui.market.BuildConfig;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.util.RxUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.functions.Consumer;

public class PushClickedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent paramIntent) {
        try {
            JSONObject obj = new JSONObject(paramIntent.getStringExtra("pending_json"));


            String userId = null;
            if (UserHelper.getInstance(context).getProfile() != null) {
                userId = UserHelper.getInstance(context).getProfile().getId();
            }
            Api.getInstance().sendPushClicked(userId, obj.getString("messageId"))
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(ResultEntity resultEntity) throws Exception {
                                       if (BuildConfig.DEBUG) {
                                           if (!resultEntity.isSuccess()) {
                                               Log.e("PushClickedReceiver", "error " + resultEntity.getMsg());
                                           }
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    if (BuildConfig.DEBUG) {
                                        Log.e("PushClickedReceiver", "thrbowable " + throwable);
                                    }
                                }
                            });

            String title = obj.getString("title") != null ? obj.getString("title") : "";
            String json = paramIntent.getStringExtra("pending_json");

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
            } else if (type == 5) {//跳转到账单首页
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("account", true);
            } else if (type == 6) {//弹框
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("tankuang", json);
                intent.putExtra("istk", true);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
