package com.beihui.market.ui.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSetPwdComponent;
import com.beihui.market.injection.module.SetPwdModule;
import com.beihui.market.ui.activity.UserCertificationCodeActivity;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.ui.presenter.LoginPresenter;
import com.beihui.market.ui.presenter.ResetPwdSetPwdPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.ClearEditText;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码设置密码的 片段
 */
public class SetPsdFragment extends BaseComponentFragment implements ResetPwdSetPwdContract.View {
    @BindView(R.id.tv_set_pwd_title_name)
    TextView titleName;

    @BindView(R.id.password)
    ClearEditText passwordEt;
    @BindView(R.id.confirm)
    TextView confirmBtn;

    @BindView(R.id.verify_code)
    ClearEditText verifyCodeEt;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;

    public Activity mActivity;

    private CountDownTimerUtils countDownTimer;

    @Inject
    ResetPwdSetPwdPresenter presenter;

    private String requestPhone;

    @Override
    public void onDestroyView() {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_set_psd;
    }

    @Override
    public void configViews() {
        mActivity = getActivity();
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

                boolean validateCode = verifyCodeEt.getText().length() == 4;

                if (LegalInputUtils.validatePassword(passwordEt.getText().toString()) && validateCode) {
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
        verifyCodeEt.addTextChangedListener(textWatcher);

    }

    @Override
    public void initDatas() {
        requestPhone = getArguments().getString("requestPhone");
        String tileName = getActivity().getIntent().getStringExtra("tileName");
        if (!TextUtils.isEmpty(tileName)) {
            titleName.setText(tileName);
        } else {
            titleName.setText("忘记买吗");
        }
        presenter.requestVerification(requestPhone);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSetPwdComponent.builder()
                .appComponent(appComponent)
                .setPwdModule(new SetPwdModule(this))
                .build()
                .inject(this);

    }

    @OnClick({R.id.confirm,R.id.fetch_text})
    void onViewClicked(View view) {
        //获取验证码
        if (view.getId() == R.id.fetch_text) {
            presenter.requestVerification(requestPhone);
        } else {
            //设置密码 确认按钮
            presenter.nextMove(requestPhone, verifyCodeEt.getText().toString());
        }
    }

    @Override
    public void setPresenter(ResetPwdSetPwdContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, null);
    }

    /**
     * 设置密码成功
     */
    @Override
    public void showRestPwdSuccess(String msg) {
        /**
         * 用户密码登录
         */
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);


        Api.getInstance().login(requestPhone, passwordEt.getText().toString())
                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<UserProfileAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       //umeng统计
                                       Statistic.onEvent(Events.REGISTER_VERIFICATION_SUCCESS);

                                       //登录之后，将用户信息注册到本地
                                       UserHelper.getInstance(mActivity).update(result.getData(), requestPhone, mActivity);
                                       //保存用户id,缓存
                                       SPUtils.setCacheUserId(mActivity, result.getData().getId());


                                       mActivity.finish();

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

    }


    @Override
    public void showVerificationSend(String msg) {
        ToastUtils.showShort(getContext(), msg, null);
        fetchText.setEnabled(false);
        countDownTimer = new CountDownTimerUtils(fetchText, verifyCodeEt);
        countDownTimer.start();
    }

    @Override
    public void moveToNextStep(String requestPhone) {
        presenter.resetPwd(requestPhone, passwordEt.getText().toString());
    }
}
