package com.beiwo.klyjaz.ui.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Copyright: zhujia (C)2018
 * FileName: ResetPwdActivity
 * Author: jiang
 * Create on: 2018/7/31 14:56
 * Description: 找回密码
 */
public class ResetPwdActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    private String phone;
    @BindView(R.id.psd_visibility_reset)
    CheckBox psdVisibilityCb;
    @BindView(R.id.password_reset)
    ClearEditText passwordEt;
    @BindView(R.id.tv_reset_login)
    TextView confirmBtn;


    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd_layout;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);
        phone = getIntent().getStringExtra("phone");

    }

    @Override
    public void initDatas() {
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

                if (LegalInputUtils.validatePassword(passwordEt.getText().toString())) {
                    confirmBtn.setClickable(true);
                    confirmBtn.setTextColor(Color.WHITE);
                } else {
                    confirmBtn.setClickable(false);
                    confirmBtn.setTextColor(Color.parseColor("#50ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        passwordEt.addTextChangedListener(textWatcher);
    }

    @OnClick(R.id.tv_reset_login)
    void onClickConfirm() {
        if (TextUtils.isEmpty(passwordEt.getText().toString())) {
            ToastUtil.toast("密码不能为空");
            return;
        }
        Api.getInstance().resetPwd(phone, passwordEt.getText().toString())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       //umeng统计
                                       Statistic.onEvent(Events.CHANGE_PASSWORD_SUCCESS);


                                       ToastUtil.toast("密码重置成功");

                                       new Handler().postDelayed(new Runnable() {
                                           public void run() {
                                               if (UserHelper.getInstance(ResetPwdActivity.this).getProfile() == null || UserHelper.getInstance(ResetPwdActivity.this).getProfile().getId() == null) {
                                                   ResetPwdActivity.this.setResult(0);
                                                   ResetPwdActivity.this.finish();
                                               }
                                           }
                                       }, 1200);
                                   } else {
                                       ToastUtil.toast(result.getMsg());

                                       //umeng统计
                                       Statistic.onEvent(Events.CHANGE_PASSWORD_FAILED);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                ToastUtil.toast("网络错误");
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.onDestroy();
    }
}
