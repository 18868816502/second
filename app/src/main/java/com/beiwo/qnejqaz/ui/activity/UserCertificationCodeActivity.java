package com.beiwo.qnejqaz.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.Phone;
import com.beiwo.qnejqaz.entity.UserProfileAbstract;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.ui.busevents.UserLoginEvent;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.CountDownTimerUtils;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.util.LegalInputUtils;
import com.beiwo.qnejqaz.util.RxUtil;
import com.beiwo.qnejqaz.util.SPUtils;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 快速登陆输入验证码页面
 */
public class UserCertificationCodeActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.verify_code)
    ClearEditText verifyCode;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private CountDownTimerUtils countDownTimer;

    //手机号
    private String pendingPhone;
    private Intent intent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodUtil.closeSoftKeyboard(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verification_code;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();

        intent = getIntent();
        if (TextUtils.isEmpty(intent.getStringExtra("bindPhone"))) {
            pendingPhone = intent.getStringExtra("pendingPhone");
            tvPhone.setText(LegalInputUtils.formatMobile(pendingPhone));
            tvLogin.setText("登录");
            tvLogin.setClickable(false);
        } else {
            pendingPhone = intent.getStringExtra("bindPhone");
            tvPhone.setText(LegalInputUtils.formatMobile(pendingPhone));
            tvLogin.setText("下一步");
            tvLogin.setClickable(false);
        }
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        verifyCode.setMaxLenght(4);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean validateCode = verifyCode.getText().length() == 4;
                if (validateCode) {
                    tvLogin.setClickable(true);
                    tvLogin.setTextColor(Color.WHITE);
                } else {
                    tvLogin.setClickable(false);
                    tvLogin.setTextColor(Color.parseColor("#50ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        verifyCode.addTextChangedListener(textWatcher);
        clickGetCode();
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        activity = null;
    }

    @OnClick({R.id.fetch_text, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fetch_text:
                clickGetCode();
                break;
            //点击登录 验证验证码
            case R.id.tv_login:
                if (TextUtils.isEmpty(intent.getStringExtra("bindPhone"))) {
                    Api.getInstance().loginByCode(pendingPhone, verifyCode.getText().toString()).compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                            .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                                @Override
                                public void accept(@NonNull ResultEntity<UserProfileAbstract> result) throws Exception {
                                    if (result.isSuccess()) {
                                        //umeng统计
                                        Statistic.onEvent(Events.REGISTER_VERIFICATION_SUCCESS);
                                        EventBus.getDefault().post("1");
                                        EventBus.getDefault().post(new UserLoginEvent());
                                        //登录之后，将用户信息注册到本地
                                        UserHelper.getInstance(UserCertificationCodeActivity.this).update(result.getData(), tvPhone.getText().toString(), UserCertificationCodeActivity.this);
                                        //保存用户id,缓存
                                        SPUtils.setCacheUserId(result.getData().getId());
                                        /*调用登录接口*/
                                        loginNoPwd(result.getData());
                                    } else {
                                        //umeng统计
                                        Statistic.onEvent(Events.REGISTER_VERIFICATION_FAILED);
                                        showErrorMsg(result.getMsg());
                                    }
                                }
                            });
                } else {
                    Api.getInstance().updateBindPhone(UserHelper.getInstance(this).getProfile().getAccount(), pendingPhone, verifyCode.getText().toString()).compose(RxUtil.<ResultEntity>io2main())
                            .subscribe(new Consumer<ResultEntity>() {
                                @Override
                                public void accept(@NonNull ResultEntity result) throws Exception {
                                    if (result.isSuccess()) {
                                        UserHelper.getInstance(UserCertificationCodeActivity.this).updateBindPhone(pendingPhone, UserCertificationCodeActivity.this);
                                    } else {
                                        showErrorMsg(result.getMsg());
                                    }
                                    finish();
                                }
                            });
                }
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void clickGetCode() {
        fetchText.setEnabled(false);

        countDownTimer = new CountDownTimerUtils(fetchText, verifyCode);
        countDownTimer.start();

        Api.getInstance().requestPhoneLogin(pendingPhone)
                .compose(RxUtil.<ResultEntity<Phone>>io2main())
                .subscribe(new Consumer<ResultEntity<Phone>>() {
                    @Override
                    public void accept(ResultEntity<Phone> result) throws Exception {
                        fetchText.setEnabled(true);
                        if (result.isSuccess()) {
                            //umeng统计
                            Statistic.onEvent(Events.REGISTER_GET_VERIFY_SUCCESS);
                        } else {
                            //umeng统计
                            Statistic.onEvent(Events.REGISTER_GET_VERIFY_FAILED);
                            showErrorMsg(result.getMsg());
                        }
                    }
                });
    }


    /**
     * 免密码登录
     */
    private void loginNoPwd(UserProfileAbstract result) {
        EventBus.getDefault().post(new UserLoginEvent());
        if (result.isNewUser()) {
            UserPsdEditActivity.launch(this, 1, pendingPhone);
            //umeng统计
            Statistic.onEvent(Events.LOGIN_REGISTER);
        } else {
            Statistic.login(result.getId());
            //登录之后，将用户信息注册到本地
            UserHelper.getInstance(getApplicationContext()).update(result, pendingPhone, getApplicationContext());
            //保存用户id,缓存
            SPUtils.setCacheUserId(result.getId());
            finish();
        }
    }

    /**
     * 跳转输入验证码页面
     *
     * @param context
     * @param pendingPhone 登陆手机号
     */
    public static void launch(Activity context, String pendingPhone) {
        activity = (UserAuthorizationActivity) context;
        Intent i = new Intent(context, UserCertificationCodeActivity.class);
        i.putExtra("pendingPhone", pendingPhone);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
    }

    /**
     * 绑定的手机号码
     * 跳转输入验证码页面
     *
     * @param context
     * @param bindPhone
     */
    public static void launchBindNewMobile(Activity context, String bindPhone) {
        Intent i = new Intent(context, UserCertificationCodeActivity.class);
        i.putExtra("bindPhone", bindPhone);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
        context.finish();
    }

    private static UserAuthorizationActivity activity = null;
}