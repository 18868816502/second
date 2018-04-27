package com.beihui.market.ui.fragment;


import android.graphics.Color;
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
import com.beihui.market.injection.component.DaggerSetPwdComponent;
import com.beihui.market.injection.module.SetPwdModule;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.ui.presenter.ResetPwdSetPwdPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPsdFragment extends BaseComponentFragment implements ResetPwdSetPwdContract.View {
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.confirm)
    TextView confirmBtn;

    @BindView(R.id.verify_code)
    EditText verifyCodeEt;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;


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
        if (view.getId() == R.id.fetch_text) {
            presenter.requestVerification(requestPhone);
        } else {
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

    @Override
    public void showRestPwdSuccess(String msg) {
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);
        getActivity().finish();
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
