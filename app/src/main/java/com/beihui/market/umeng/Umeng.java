package com.beihui.market.umeng;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

/**
 * @version 3.0.1
 * @author xhb
 * 修复 UMConfigure.init 写死为umeng的问题
 */
public class Umeng {

    public static void install(Context context) {
        //分享
        /**
         * 老的微信账号
         */
        PlatformConfig.setWeixin("wx85ba05e3d5eca8a5", "ed4bfef7288e94df20e2b3a4ef92d792");
        //PlatformConfig.setWeixin("wx82d44ebf242141d3", "1865f152b919af142cb882f3375a1ffe");
        PlatformConfig.setQQZone("1106217443", "UiOL1Ct0h3tGOirD");
        PlatformConfig.setSinaWeibo("2037274409", "ad8ac41cb179ffcb92f28b312a055074", "http://sns.whalecloud.com");

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        config.isOpenShareEditActivity(false);
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(context).setShareConfig(config);


        String umengChannel = null;
        try {
            ApplicationInfo applicationInfo = App.getInstance().getApplicationContext().getPackageManager().getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
                if (applicationInfo.metaData != null) {
                    umengChannel = applicationInfo.metaData.getString("UMENG_CHANNEL");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
//        UMConfigure.init(context, "596c4b3dbbea83542c001b01", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.init(context, "596c4b3dbbea83542c001b01", umengChannel, UMConfigure.DEVICE_TYPE_PHONE, "");



        //统计使用
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //统计错误
        MobclickAgent.setCatchUncaughtExceptions(true);
        //统计自定义事件
        Statistic.registerContext(context);

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);

    }
}
