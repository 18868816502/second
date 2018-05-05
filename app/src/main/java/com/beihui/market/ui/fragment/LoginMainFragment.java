package com.beihui.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerLoginComponent;
import com.beihui.market.injection.module.LoginModule;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.UserCertificationCodeActivity;
import com.beihui.market.ui.activity.UserProtocolActivity;
import com.beihui.market.ui.activity.WeChatBindPhoneActivity;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.contract.LoginContract;
import com.beihui.market.ui.presenter.LoginPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.ClearEditText;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户免密码登录的Fragment 片段
 */
public class LoginMainFragment extends BaseComponentFragment implements LoginContract.View {

    private final int REQUEST_CODE_BIND_PHONE = 1;


    @Inject
    LoginPresenter presenter;
    @BindView(R.id.phone_number)
    ClearEditText phoneNumber;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.iv_contract)
    ImageView ivContract;
    /**
     * 合同是否选中
     */
    private boolean isCheckContract = true;

    private Map<String, String> wechatInfo;

    @Override
    public void onDestroyView() {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BIND_PHONE) {
            //登录后发送全局事件，更新UI
            EventBus.getDefault().post(new UserLoginEvent());
            if (getView() != null) {
                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                }, 200);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_login_main;
    }

    @Override
    public void configViews() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        phoneNumber.addTextChangedListener(textWatcher);

        String phone = null;
        if (getArguments() != null) {
            phone = getArguments().getString("phone");
        }
        if (phone != null) {
            phoneNumber.setText(phone);
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerLoginComponent.builder().appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    /**
     * 验证按钮是否可以点击
     */
    private void validation(){
        String phone = phoneNumber.getText().toString();
        if (LegalInputUtils.validatePhone(phone) && isCheckContract) {
            tvLogin.setClickable(true);
            tvLogin.setTextColor(Color.WHITE);
        } else {
            tvLogin.setClickable(false);
            tvLogin.setTextColor(Color.parseColor("#50ffffff"));
        }
    }


    @OnClick({R.id.tv_login, R.id.iv_contract, R.id.tv_contract, R.id.iv_login_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //下一步
           case R.id.tv_login:
                if (!TextUtils.isEmpty(phoneNumber.getText().toString()) && isCheckContract) {
                    UserCertificationCodeActivity.launch(getActivity(), phoneNumber.getText().toString());
                }
                break;
            case R.id.iv_contract:
                if (isCheckContract){
                    isCheckContract = false;
                }else{
                    isCheckContract = true;
                }

                if (isCheckContract){
                    ivContract.setImageResource(R.drawable.btn_open_rb);
                }else{
                    ivContract.setImageResource(R.drawable.btn_close_rb);
                }

                validation();

                break;
            /**
             * 跳转到用户协议
             */
            case R.id.tv_contract:
                startActivity(new Intent(getActivity(), UserProtocolActivity.class));
                /**
                 * 用下面 出现bug 用户协议点击返回应该返回登录注册页面，现在返回首页
                 * 因为需要区分设置进入的
                 */
//                Intent intent = new Intent(getActivity(), ComWebViewActivity.class);
//                String title = "用户协议";
//                String url = NetConstants.H5_USER_AGREEMENT;
//                intent.putExtra("title", title);
//                intent.putExtra("url", url);
//                startActivity(intent);
                break;
            case R.id.iv_login_wechat:
                UMAuthListener listener = new UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        showProgress();
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        dismissProgress();
                        wechatInfo = map;
                        presenter.loginWithWeChat(wechatInfo.get("openid"));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        dismissProgress();
                        Log.e("xjb", "i __ " + i + "throwble -- " + throwable.getMessage());
                        ToastUtils.showShort(getContext(), "授权失败", null);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        dismissProgress();
                        ToastUtils.showShort(getContext(), "授权取消", null);
                    }
                };
                UMShareAPI.get(getContext()).getPlatformInfo((Activity) getContext(), SHARE_MEDIA.WEIXIN, listener);
                break;
        }
    }



    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        //injected, do nothing.
    }

    @Override
    public void showLoginSuccess(String msg) {
        dismissProgress();
//        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);
        //登录后发送全局事件,更新UI
        EventBus.getDefault().post(new UserLoginEvent());
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            }, 200);
        }
    }

    @Override
    public void navigateWechatBindAccount() {
        dismissProgress();
        Intent intent = new Intent(getContext(), WeChatBindPhoneActivity.class);
        intent.putExtra("openId", wechatInfo.get("openid"));
        intent.putExtra("name", wechatInfo.get("name"));
        intent.putExtra("profile_image_url", wechatInfo.get("profile_image_url"));
        startActivityForResult(intent, REQUEST_CODE_BIND_PHONE);
    }



}
