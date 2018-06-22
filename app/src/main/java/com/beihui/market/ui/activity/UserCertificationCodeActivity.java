package com.beihui.market.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Phone;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.presenter.RegisterVerifyPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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
    protected void configureComponent(AppComponent appComponent) {

    }


    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }


    @OnClick({R.id.fetch_text, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /**
             * 点击获取验证码
             */
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

                                                   //登录之后，将用户信息注册到本地
                                                   UserHelper.getInstance(UserCertificationCodeActivity.this).update(result.getData(), tvPhone.getText().toString(), UserCertificationCodeActivity.this);
                                                   //保存用户id,缓存
                                                   SPUtils.setCacheUserId(UserCertificationCodeActivity.this, result.getData().getId());
                                                   /**
                                                    * 调用登录接口
                                                    */
                                                   loginNoPwd(result.getData());
                                               } else {
                                                   //umeng统计
                                                   Statistic.onEvent(Events.REGISTER_VERIFICATION_FAILED);
                                                   showErrorMsg(result.getMsg());
                                               }
                                           }
                                       },
                                    new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {
                                            showErrorMsg("网络错误");
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
                                       },
                                    new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {
                                            showErrorMsg("网络错误");
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
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                fetchText.setEnabled(true);
                                showErrorMsg("网络错误");
                            }
                        });
    }


    /**
     * 免密码登录
     */
    private void loginNoPwd(UserProfileAbstract result) {
        if (result.isNewUser()) {
            UserPsdEditActivity.launch(UserCertificationCodeActivity.this,1, pendingPhone);

            //umeng统计
            Statistic.onEvent(Events.LOGIN_REGISTER);
        } else {
            /**
             * 进入账单模块首页
             */
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("account", true);
            startActivity(intent);

            Statistic.login(result.getId());
            //登录之后，将用户信息注册到本地
            UserHelper.getInstance(getApplicationContext()).update(result, pendingPhone, getApplicationContext());
            //保存用户id,缓存
            SPUtils.setCacheUserId(getApplicationContext(), result.getId());
            finish();
        }
    }


    /**
     * 跳转输入验证码页面
     * @param context
     * @param pendingPhone 登陆手机号
     */
    public static void launch(Activity context, String pendingPhone){
        Intent i = new Intent(context,UserCertificationCodeActivity.class);
        i.putExtra("pendingPhone",pendingPhone);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
    }

    /**
     * 绑定的手机号码
     * 跳转输入验证码页面
     * @param context
     * @param bindPhone
     */
    public static void launchBindNewMobile(Activity context, String bindPhone){
        Intent i = new Intent(context,UserCertificationCodeActivity.class);
        i.putExtra("bindPhone",bindPhone);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
        context.finish();
    }

}
