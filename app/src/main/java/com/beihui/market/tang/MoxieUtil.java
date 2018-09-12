package com.beihui.market.tang;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.activity.MainActivity;
import com.moxie.client.exception.ExceptionType;
import com.moxie.client.exception.MoxieException;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.moxie.client.model.TitleParams;

import org.greenrobot.eventbus.EventBus;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/24
 */

public class MoxieUtil {
    public static void sychronized(String bankCode, final Activity activity) {
        MxParam mxParam = new MxParam();
        mxParam.setUserId(UserHelper.getInstance(activity).getProfile().getId());
        mxParam.setApiKey(BuildConfig.MOXIE_APP_KEY);
        mxParam.setQuitDisable(false);

        mxParam.setQuitLoginDone(MxParam.PARAM_COMMON_YES);
        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);
        mxParam.setFunction(MxParam.PARAM_FUNCTION_ONLINEBANK);
        mxParam.setItemType(MxParam.PARAM_ITEM_TYPE_CREDITCARD);  //信用卡

        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);

        //自定义Title, 还有更多方法请用IDE查看
        TitleParams titleParams = new TitleParams.Builder()
                //设置返回键的icon，不设置此方法会默认使用魔蝎的icon
                .leftNormalImgResId(R.mipmap.btn_back_normal_black)
                //用于设置selector，表示按下的效果，不设置默认使leftNormalImgResId()设置的图片
                .leftPressedImgResId(R.mipmap.btn_back_normal_black)
                //标题字体颜色
                .titleColor(ContextCompat.getColor(activity, R.color.black_1))
                //title背景色
                .backgroundColor(ContextCompat.getColor(activity, R.color.white))
                //设置右边icon
                .rightNormalImgResId(R.drawable.moxie_client_banner_refresh_black)
                //是否支持沉浸式
                .immersedEnable(true)
                .build();
        mxParam.setTitleParams(titleParams);

        mxParam.setItemCode(bankCode);
        MoxieSDK.getInstance().start(activity, mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                if (moxieCallBackData != null) {
                    switch (moxieCallBackData.getCode()) {
                        /**
                         * 账单导入中
                         *
                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
                         * Task通知：登录成功/登录失败
                         * Bill通知：账单通知
                         */
                        case MxParam.ResultCode.IMPORTING:
                            if (moxieCallBackData.isLoginDone()) {
                                //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
                                //Log.d("customMoxie", "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            } else {
                                //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
                                //Log.d("customMoxie", "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            }
                            break;
                        /**
                         * 任务还未开始
                         *
                         * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
                         *
                         * example:
                         *  case MxParam.ResultCode.IMPORT_UNSTART:
                         *      showDialog(moxieContext);
                         *      return true;
                         * */
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            //Log.e("customMoxie", "任务未开始");
                            moxieContext.finish();
                            return true;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            //Log.e("customMoxie", "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_FUNCTION_EMAIL:
                                    break;
                                case MxParam.PARAM_FUNCTION_ONLINEBANK:
                                    break;
                                default:
                                    break;
                            }
                            moxieContext.finish();
                            return true;
                    }
                }
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("account", true);
                //intent.putExtra("moxieMsg", "3秒后刷新页面信用卡就会显示啦");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                EventBus.getDefault().post("1");
                return false;
            }

            @Override
            public void onError(MoxieContext moxieContext, MoxieException moxieException) {
                super.onError(moxieContext, moxieException);
                //Log.e("customMoxie", "魔蝎失败" + moxieException.getMessage());
                if (moxieException.getExceptionType() == ExceptionType.SDK_HAS_STARTED) {
                    Toast.makeText(activity, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (moxieException.getExceptionType() == ExceptionType.SDK_LACK_PARAMETERS) {
                    Toast.makeText(activity, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
