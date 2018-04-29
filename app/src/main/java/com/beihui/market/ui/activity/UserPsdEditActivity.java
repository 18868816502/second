package com.beihui.market.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.presenter.ResetPwdSetPwdPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
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
 * 快速登陆设置密码
 */
public class UserPsdEditActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.password)
    ClearEditText passwordEt;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;
    @BindView(R.id.tv_login)
    TextView tvLogin;


    private CountDownTimerUtils countDownTimer;

    private int returnType = 1;

    //手机号
    private String pendingPhone;

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
        return R.layout.activity_psd_set;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();

        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);
        tvLogin.setClickable(false);
        returnType = getIntent().getIntExtra("returnType",1);
        pendingPhone = getIntent().getStringExtra("pendingPhone");
        if (returnType == 2){
            tvTitle.setText("设置新密码");
        }else{
            tvTitle.setText("设置密码");
        }


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
    }

    @Override
    public void initDatas() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean validateCode = LegalInputUtils.validatePassword(passwordEt.getText().toString());

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
        passwordEt.addTextChangedListener(textWatcher);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }


    @OnClick({R.id.tv_skip, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                login();
                break;
            //修改密码
            case R.id.tv_login:
                Api.getInstance().resetPwd(pendingPhone, passwordEt.getText().toString())
                        .compose(RxUtil.<ResultEntity>io2main())
                        .subscribe(new Consumer<ResultEntity>() {
                                       @Override
                                       public void accept(@NonNull ResultEntity result) throws Exception {
                                           if (result.isSuccess()) {
                                               //umeng统计
                                               Statistic.onEvent(Events.CHANGE_PASSWORD_SUCCESS);

                                               login();
                                           } else {
                                               //umeng统计
                                               Statistic.onEvent(Events.CHANGE_PASSWORD_FAILED);

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
                break;
        }
    }

    /**
     * 上一页就登录成功
     */
    private void login() {
        /**
         * 进入我的模块首页
         */
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("mine", true);
        startActivity(intent);
        finish();

    }

    /**
     * 跳转设置密码界面
     * @param context
     * @param returnType  1.正常跳转，输入验证码页面跳转 标题“设置密码”
     *                    2.微信快速登陆跳转，标题“设置新密码”
     */
    public static void launch(Activity context, int returnType){
        Intent i = new Intent(context,UserPsdEditActivity.class);
        i.putExtra("returnType",returnType);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
    }

    /**
     * 跳转设置密码界面
     * @param context
     * @param returnType  1.正常跳转，输入验证码页面跳转 标题“设置密码”
     *                    2.微信快速登陆跳转，标题“设置新密码”
     */
    public static void launch(Activity context, int returnType, String pendingPhone){
        Intent i = new Intent(context,UserPsdEditActivity.class);
        i.putExtra("returnType",returnType);
        i.putExtra("pendingPhone",pendingPhone);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
        context.finish();
    }
}
