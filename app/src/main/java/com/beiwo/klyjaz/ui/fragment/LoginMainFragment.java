package com.beiwo.klyjaz.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerLoginComponent;
import com.beiwo.klyjaz.injection.module.LoginModule;
import com.beiwo.klyjaz.ui.activity.UserCertificationCodeActivity;
import com.beiwo.klyjaz.ui.activity.UserProtocolActivity;
import com.beiwo.klyjaz.ui.activity.WeChatBindFirstActivity;
import com.beiwo.klyjaz.ui.busevents.UserLoginEvent;
import com.beiwo.klyjaz.ui.contract.LoginContract;
import com.beiwo.klyjaz.ui.presenter.LoginPresenter;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
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
    @BindView(R.id.tv_contract)
    TextView tv_contract;

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
        tv_contract.setText(String.format(getString(R.string.user_protocol), getString(R.string.app_name)));
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
    private void validation() {
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
                if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    return;
                }
                if (!CommonUtils.isMobileNO(phoneNumber.getText().toString())) {
                    ToastUtil.toast("号码不正确");
                    return;
                }
                if (!TextUtils.isEmpty(phoneNumber.getText().toString()) && isCheckContract) {
                    UserCertificationCodeActivity.launch(getActivity(), phoneNumber.getText().toString());
                    getActivity().finish();
                }
                break;
            case R.id.iv_contract:
                if (isCheckContract) {
                    isCheckContract = false;
                } else {
                    isCheckContract = true;
                }

                if (isCheckContract) {
                    ivContract.setImageResource(R.drawable.btn_open_rb);
                } else {
                    ivContract.setImageResource(R.drawable.btn_close_rb);
                }

                validation();

                break;
            /**
             * 跳转到用户协议
             */
            case R.id.tv_contract:
                startActivity(new Intent(getActivity(), UserProtocolActivity.class));
                break;
            case R.id.iv_login_wechat:
                /*判断微信是否安装*/
                PackageManager packageManager = getActivity().getPackageManager();// 获取packagemanager
                List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
                if (pinfo != null) {
                    boolean isShow = true;
                    for (int i = 0; i < pinfo.size(); i++) {
                        String pn = pinfo.get(i).packageName;
                        if (pn.equals("com.tencent.mm")) {
                            wxLogin();
                            isShow = false;
                        }
                    }
                    if (isShow) {
                        WeakRefToastUtil.showShort(getContext(), "请安装微信", null);
                    }
                } else {
                    wxLogin();
                }
                break;
        }
    }

    private void wxLogin() {
        UMAuthListener listener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                wechatInfo = map;
                presenter.loginWithWeChat(wechatInfo.get("unionid"));
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                //throwable.printStackTrace();
                WeakRefToastUtil.showShort(getContext(), "授权失败", null);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                WeakRefToastUtil.showShort(getContext(), "授权取消", null);
            }
        };
        UMShareAPI.get(getContext()).getPlatformInfo((Activity) getContext(), SHARE_MEDIA.WEIXIN, listener);
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        //injected, do nothing.
    }

    @Override
    public void showLoginSuccess(String msg) {
        dismissProgress();
//        WeakRefToastUtil.showShort(getContext(), msg, R.mipmap.white_success);
        //登录后发送全局事件,更新UI
        EventBus.getDefault().post("1");
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
        Intent intent = new Intent(getContext(), WeChatBindFirstActivity.class);
        intent.putExtra("openId", wechatInfo.get("unionid"));
        intent.putExtra("name", wechatInfo.get("name"));
        intent.putExtra("profile_image_url", wechatInfo.get("profile_image_url"));
        startActivityForResult(intent, REQUEST_CODE_BIND_PHONE);
    }
}