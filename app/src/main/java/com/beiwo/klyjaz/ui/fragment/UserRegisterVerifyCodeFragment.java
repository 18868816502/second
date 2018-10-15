package com.beiwo.klyjaz.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerRegisterVerifyComponent;
import com.beiwo.klyjaz.injection.module.RegisterVerifyModule;
import com.beiwo.klyjaz.ui.busevents.AuthNavigationEvent;
import com.beiwo.klyjaz.ui.contract.RegisterVerifyContract;
import com.beiwo.klyjaz.ui.presenter.RegisterVerifyPresenter;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.CountDownTimerUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.util.WeakRefToastUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRegisterVerifyCodeFragment extends BaseComponentFragment implements RegisterVerifyContract.View {

    //手机号
    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.verify_code)
    EditText verifyCodeEt;
    //获取验证码
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.next_step)
    TextView nextStepBtn;

    @Inject
    RegisterVerifyPresenter presenter;

    private CountDownTimerUtils countDownTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
        Statistic.onEvent(Events.ENTER_REGISTER);
    }

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
                boolean validateCode = verifyCodeEt.getText().length() == 4;

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
            //获取验证码
            case R.id.fetch_text:
                //umeng统计
                Statistic.onEvent(Events.REGISTER_GET_VERIFY);

                presenter.requestVerification(phoneNumberEt.getText().toString());
                break;
            case R.id.next_step:
                //umeng统计
                Statistic.onEvent(Events.REGISTER_NEXT_STEP);

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
        WeakRefToastUtil.showShort(getContext(), msg, null);
        fetchText.setEnabled(false);
        countDownTimer = new CountDownTimerUtils(fetchText, phoneNumberEt);
        countDownTimer.start();
    }

    @Override
    public void showErrorMsg(String msg) {
        WeakRefToastUtil.showShort(getContext(), msg, null);
    }

    @Override
    public void moveToNextStep(String requestPhone) {
        AuthNavigationEvent event = new AuthNavigationEvent(AuthNavigationEvent.TAG_LOGIN_FAST);
        event.requestPhone = requestPhone;
        EventBus.getDefault().post(event);
    }
}
