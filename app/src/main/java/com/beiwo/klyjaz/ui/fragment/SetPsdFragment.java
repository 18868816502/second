package com.beiwo.klyjaz.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerSetPwdComponent;
import com.beiwo.klyjaz.injection.module.SetPwdModule;
import com.beiwo.klyjaz.ui.activity.ResetPwdActivity;
import com.beiwo.klyjaz.ui.contract.ResetPwdSetPwdContract;
import com.beiwo.klyjaz.ui.presenter.ResetPwdSetPwdPresenter;
import com.beiwo.klyjaz.util.CountDownTimerUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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
        WeakRefToastUtil.showShort(getContext(), msg, null);
    }

    /**
     * 设置密码成功
     */
    @Override
    public void showRestPwdSuccess(String msg) {
        dismissProgress();
        ToastUtil.toast(msg);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (UserHelper.getInstance(getActivity()).getProfile() == null || UserHelper.getInstance(getActivity()).getProfile().getId() == null) {
                    mActivity.finish();
                }
            }
        }, 1200);}


    @Override
    public void showVerificationSend(String msg) {
        WeakRefToastUtil.showShort(getContext(), msg, null);
        fetchText.setEnabled(false);
        countDownTimer = new CountDownTimerUtils(fetchText, verifyCodeEt);
        countDownTimer.start();
    }

    @Override
    public void moveToNextStep(String requestPhone) {
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
