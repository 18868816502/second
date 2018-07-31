package com.beihui.market.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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
import com.beihui.market.ui.activity.ResetPwdActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.UserCertificationCodeActivity;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.ui.presenter.LoginPresenter;
import com.beihui.market.ui.presenter.ResetPwdSetPwdPresenter;
import com.beihui.market.ui.presenter.UserProfilePresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.ClearEditText;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.tv_reset_phone)
    TextView phoneTv;

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
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean validateCode = verifyCodeEt.getText().length() == 4;

                if (validateCode) {
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
        phoneTv.setText(LegalInputUtils.formatMobile(requestPhone));
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

    @OnClick({R.id.confirm, R.id.fetch_text})
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
        //com.beihui.market.util.ToastUtils.showToast(getActivity(), msg);
        ToastUtil.toast(msg);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (UserHelper.getInstance(getActivity()).getProfile() == null || UserHelper.getInstance(getActivity()).getProfile().getId() == null) {
                    mActivity.finish();
                }
            }
        }, 1200);
//        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);

//        Api.getInstance().logout(UserHelper.getInstance(getActivity()).getProfile().getId())
//                .compose(RxUtil.<ResultEntity>io2main())
//                .subscribe(new Consumer<ResultEntity>() {
//                               @Override
//                               public void accept(@NonNull ResultEntity result) throws Exception {
//                                   if (result.isSuccess()) {
//                                       UserHelper.getInstance(getActivity()).clearUser(getContext());
//                                       //发送用户退出全局事件
//                                       EventBus.getDefault().post(new UserLogoutEvent());
//                                       UserHelper.getInstance(getActivity()).clearUser(getActivity());
//                                       UserAuthorizationActivity.launch(getActivity(), null);
//
//                                       //umeng统计
//                                       Statistic.logout();
//                                   } else {
//                                       showErrorMsg(result.getMsg());
//                                   }
//                               }
//                           },
//                        new Consumer<Throwable>() {
//                            @Override
//                            public void accept(@NonNull Throwable throwable) throws Exception {
//
//                            }
//                        });

//        Api.getInstance().login(requestPhone, passwordEt.getText().toString())
//                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
//                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
//                               @Override
//                               public void accept(@NonNull ResultEntity<UserProfileAbstract> result) throws Exception {
//                                   if (result.isSuccess()) {
//                                       //umeng统计
//                                       Statistic.onEvent(Events.REGISTER_VERIFICATION_SUCCESS);
//
//                                       //登录之后，将用户信息注册到本地
//                                       UserHelper.getInstance(mActivity).update(result.getData(), requestPhone, mActivity);
//                                       //保存用户id,缓存
//                                       SPUtils.setCacheUserId(mActivity, result.getData().getId());
//
//
//                                       mActivity.finish();
//
//                                   } else {
//                                       //umeng统计
//                                       Statistic.onEvent(Events.REGISTER_VERIFICATION_FAILED);
//                                       showErrorMsg(result.getMsg());
//                                   }
//                               }
//                           },
//                        new Consumer<Throwable>() {
//                            @Override
//                            public void accept(@NonNull Throwable throwable) throws Exception {
//                                showErrorMsg("网络错误");
//                            }
//                        });

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
        // presenter.resetPwd(requestPhone, passwordEt.getText().toString());
        Intent intent = new Intent(getActivity(), ResetPwdActivity.class);
        intent.putExtra("phone", requestPhone);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 0) {
            mActivity.finish();
        }
    }
}
