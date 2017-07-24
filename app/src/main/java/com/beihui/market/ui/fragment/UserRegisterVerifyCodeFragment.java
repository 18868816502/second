package com.beihui.market.ui.fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerRegisterVerifyComponent;
import com.beihui.market.injection.module.RegisterVerifyModule;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.ui.contract.RegisterVerifyContract;
import com.beihui.market.ui.presenter.RegisterVerifyPresenter;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRegisterVerifyCodeFragment extends BaseComponentFragment implements RegisterVerifyContract.View {

    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.verify_code)
    EditText verifyCodeEt;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.next_step)
    Button nextStepBtn;

    @Inject
    RegisterVerifyPresenter presenter;

    private CountDownTimerUtils countDownTimer;

    @Override
    public void onDestroyView() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_register_verify_code;
    }

    @Override
    public void configViews() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = phoneNumberEt.getText().toString();
                boolean validatePhone = LegalInputUtils.validatePhone(phone);
                if (validatePhone && !(countDownTimer != null && countDownTimer.isRunning())) {
                    //手机号符合规则，且没有正在请求验证码倒数,则设置enable
                    fetchText.setEnabled(true);
                } else {
                    fetchText.setEnabled(false);
                }
                boolean validateCode = verifyCodeEt.getText().length() > 0;

                nextStepBtn.setEnabled(validatePhone && validateCode);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        phoneNumberEt.addTextChangedListener(textWatcher);
        verifyCodeEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerRegisterVerifyComponent.builder()
                .appComponent(appComponent)
                .registerVerifyModule(new RegisterVerifyModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.head_to_login, R.id.fetch_text, R.id.next_step})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_to_login:
                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_HEAD_TO_LOGIN));
                break;
            case R.id.fetch_text:
                presenter.requestVerification(phoneNumberEt.getText().toString());
                break;
            case R.id.next_step:
                presenter.nextMove(phoneNumberEt.getText().toString(), verifyCodeEt.getText().toString());
                break;
        }
    }

    @Override
    public void setPresenter(RegisterVerifyContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showVerificationSend(String msg) {
        fetchText.setEnabled(false);
        countDownTimer = new CountDownTimerUtils(fetchText, phoneNumberEt);
        countDownTimer.start();
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(getContext(), msg, null);
    }

    @Override
    public void moveToNextStep(String requestPhone) {
        AuthNavigationEvent event = new AuthNavigationEvent(AuthNavigationEvent.TAG_SET_PSD);
        event.requestPhone = requestPhone;
        EventBus.getDefault().post(event);
    }
}
