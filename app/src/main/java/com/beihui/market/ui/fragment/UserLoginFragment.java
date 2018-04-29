package com.beihui.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerLoginComponent;
import com.beihui.market.injection.module.LoginModule;
import com.beihui.market.ui.activity.ResetPsdActivity;
import com.beihui.market.ui.activity.WeChatBindPhoneActivity;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.contract.LoginContract;
import com.beihui.market.ui.presenter.LoginPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户密码登录的Fragment 片段
 */
public class UserLoginFragment extends BaseComponentFragment implements LoginContract.View {

    private final int REQUEST_CODE_BIND_PHONE = 1;

    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.login)
    TextView loginBtn;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;

    @Inject
    LoginPresenter presenter;

    private Map<String, String> wechatInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
        Statistic.onEvent(Events.ENTER_LOGIN);
    }

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
        return R.layout.fragment_user_login;
    }

    @Override
    public void configViews() {

        psdVisibilityCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(passwordEt);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = phoneNumberEt.getText().toString();
                String pwd = passwordEt.getText().toString();
                if (LegalInputUtils.isPhoneAndPwdLegal(phone, pwd)) {
                    loginBtn.setClickable(true);
                    loginBtn.setTextColor(Color.WHITE);
                } else {
                    loginBtn.setClickable(false);
                    loginBtn.setTextColor(Color.parseColor("#50ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        phoneNumberEt.addTextChangedListener(textWatcher);
        passwordEt.addTextChangedListener(textWatcher);


        String phone = null;
        if (getArguments() != null) {
            phone = getArguments().getString("phone");
        }
        if (phone != null) {
            phoneNumberEt.setText(phone);
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


    @OnClick({R.id.forget_psd, R.id.login, R.id.login_with_wechat})
    void onViewClicked(View view) {
        switch (view.getId()) {
            //忘记密码
            case R.id.forget_psd:
                Intent toResetPsd = new Intent(getActivity(), ResetPsdActivity.class);
                startActivity(toResetPsd);
                break;
            case R.id.login:
                //umeng统计
                Statistic.onEvent(Events.LOGIN_LOGIN);

                presenter.login(phoneNumberEt.getText().toString(), passwordEt.getText().toString());
                break;
            //微信一键登录
            case R.id.login_with_wechat:
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
        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);
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
