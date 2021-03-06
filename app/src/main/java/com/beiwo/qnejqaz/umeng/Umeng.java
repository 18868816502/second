package com.beiwo.qnejqaz.umeng;


import android.content.Context;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.BuildConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

/**
 * @author xhb 修复 UMConfigure.init 写死为umeng的问题
 * @version 3.0.1
 */
public class Umeng {
    public static void install(Context context) {
        /*微信开放平台申请的账号*/
        PlatformConfig.setWeixin(BuildConfig.WECHAT_APP_ID, BuildConfig.WECHAT_APP_SECRET);
        /*QQ开放平台申请的账号*/
        PlatformConfig.setQQZone(BuildConfig.QQ_APP_ID, BuildConfig.QQ_APP_SECRET);
        /*新浪开放平台申请的账号*/
        PlatformConfig.setSinaWeibo(BuildConfig.SINA_APP_ID, BuildConfig.SINA_APP_SECRET, BuildConfig.SINA_APP_SHARE_URL);

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        config.isOpenShareEditActivity(false);
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(context).setShareConfig(config);
        /**
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel.txt
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(context, BuildConfig.UMENG_APP_KEY, App.sChannelId, UMConfigure.DEVICE_TYPE_PHONE, "");
        //统计使用
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //统计错误
        MobclickAgent.setCatchUncaughtExceptions(true);
        //统计自定义事件
        Statistic.registerContext(context);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
    }
}