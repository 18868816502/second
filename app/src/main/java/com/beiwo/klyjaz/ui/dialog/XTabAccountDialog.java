package com.beiwo.klyjaz.ui.dialog;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.activity.DebtChannelActivity;
import com.beiwo.klyjaz.ui.activity.FastAddDebtActivity;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.moxie.client.exception.ExceptionType;
import com.moxie.client.exception.MoxieException;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.moxie.client.model.TitleParams;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * @author xhb
 * 网贷记账
 * 信用卡账单
 * 快速记账
 */
public class XTabAccountDialog extends DialogFragment {


    Unbinder unbinder;

    private UMWeb umWeb;
    private MxParam mxParam;
    private CommNoneAndroidLoading loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XTaoAccountDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xlayout_dialog_tab_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //用来清理数据或解除引用
        MoxieSDK.getInstance().clear();
    }

    public static final String TAG ="aaaa";

    @SuppressLint("InlinedApi")
    @OnClick({R.id.ll_dialog_tab_account_net_loan, R.id.ll_dialog_tab_account_credit_card,  R.id.ll_dialog_tab_account_hand, R.id.ll_dialog_tab_account_cancel})
    void OnViewClicked(View view) {
        /**
         * 防止重复点击
         */
        if (FastClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            //网贷记账
            case R.id.ll_dialog_tab_account_net_loan:
                startActivity(new Intent(getContext(), DebtChannelActivity.class));
                dismiss();
                break;
            //信用卡账单 目前只需要网银导入 进入H5页面
            case R.id.ll_dialog_tab_account_credit_card:
//                startActivity(new Intent(getContext(), EBankActivity.class));
//                dismiss();
                startMoxie();
                dismiss();
                break;
            /**
             * 快速记账
             * 不需要渠道ID ChannelID
             * 不需要Logo
             */
            case R.id.ll_dialog_tab_account_hand:
                //pv，uv统计 快捷记账按钮
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_NET_FAST_ACCOUNT);
                Intent intent = new Intent(getContext(), FastAddDebtActivity.class);
                startActivity(intent);
                dismiss();
                break;
            case R.id.ll_dialog_tab_account_cancel:
                dismiss();
                break;
        }
    }

    private void startMoxie() {

        mxParam = new MxParam();
        mxParam.setUserId(UserHelper.getInstance(getActivity()).getProfile().getId());
        mxParam.setApiKey(BuildConfig.MOXIE_APP_KEY);
        mxParam.setFunction(MxParam.PARAM_FUNCTION_ONLINEBANK);
        HashMap<String, String> loginCustomBank = new HashMap<String, String>();
        loginCustomBank.put(MxParam.PARAM_CUSTOM_LOGIN_TYPE, MxParam.PARAM_ITEM_TYPE_CREDITCARD);// MxParam.PARAM_ITEM_TYPE_CREDITCAR:信用卡 MxParam.PARAM_ITEM_TYPE_DEBITCARD:借记卡
        mxParam.setLoginCustom(loginCustomBank);
        mxParam.setQuitDisable(false);


        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);
        mxParam.setAgreementEntryText("同意《数据采集服务协议》");
        //自定义Title, 还有更多方法请用IDE查看
        TitleParams titleParams = new TitleParams.Builder()
                //设置返回键的icon，不设置此方法会默认使用魔蝎的icon
                .leftNormalImgResId(R.mipmap.btn_back_normal_black)
                //用于设置selector，表示按下的效果，不设置默认使leftNormalImgResId()设置的图片
                .leftPressedImgResId(R.mipmap.btn_back_normal_black)
                //标题字体颜色
                .titleColor(getContext().getResources().getColor(R.color.black_1))
                //title背景色
                .backgroundColor(getContext().getResources().getColor(R.color.white))
                //设置右边icon
                .rightNormalImgResId(R.drawable.moxie_client_banner_refresh_black)
                //是否支持沉浸式
                .immersedEnable(true)
                .build();
        mxParam.setTitleParams(titleParams);

        MoxieSDK.getInstance().start(getActivity(), mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {

                Log.e("adaf", Thread.currentThread().getName());
                Log.e(TAG, "魔蝎回调 成功");
                if (moxieCallBackData != null) {
                    Log.e(TAG, "MoxieSDK Callback Data : "+ moxieCallBackData.toString());
                    Log.e(TAG, "MoxieSDK Callback Code : "+ moxieCallBackData.toString());
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
                            if(moxieCallBackData.isLoginDone()) {
                                //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
                                //Log.d(TAG, "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            } else {
                                //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
                                //Log.d(TAG, "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
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
                            Log.e(TAG, "任务未开始");
                            break;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
//                                    Toast.makeText(getContext(), "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
//                                    Toast.makeText(getContext(), "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
//                                    Toast.makeText(getContext(), "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
//                                    Toast.makeText(getContext(), "导入失败", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            //Log.d(TAG, "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_FUNCTION_EMAIL:
//                                            Toast.makeText(getContext(), "邮箱导入成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case MxParam.PARAM_FUNCTION_ONLINEBANK:
//                                            Toast.makeText(getContext(), "网银导入成功", Toast.LENGTH_SHORT).show();
                                    break;
                                //.....
                                default:
//                                            Toast.makeText(getContext(), "导入成功", Toast.LENGTH_SHORT).show();
                            }
                            moxieContext.finish();
                            return true;
                    }
                }
                moxieContext.finish();
                return false;
            }

            @Override
            public void onError(MoxieContext moxieContext, MoxieException moxieException) {
                super.onError(moxieContext, moxieException);
                Log.e(TAG,"魔蝎失败" + moxieException.getMessage());
                if(moxieException.getExceptionType() == ExceptionType.SDK_HAS_STARTED){
                            Toast.makeText(getActivity(), moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                } else if(moxieException.getExceptionType() == ExceptionType.SDK_LACK_PARAMETERS){
                            Toast.makeText(getActivity(), moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //用来清理数据或解除引用
        MoxieSDK.getInstance().clear();
    }


}