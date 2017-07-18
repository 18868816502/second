package com.beihui.market.ui.fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.util.CountDownTimerUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRegisterVerifyCodeComponentFragment extends BaseComponentFragment {

    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.verify_code)
    EditText verifyCodeEt;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.next_step)
    Button nextStepBtn;

    private CountDownTimerUtils countDownTimer;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int phoneLen = phoneNumberEt.getText().length();
            int codeLen = verifyCodeEt.getText().length();
            if (phoneLen > 0 && codeLen > 0) {
                if (!nextStepBtn.isEnabled()) {
                    nextStepBtn.setEnabled(true);
                }
            } else {
                if (nextStepBtn.isEnabled()) {
                    nextStepBtn.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onDestroyView() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_register_verify_code;
    }

    @Override
    public void configViews() {
        phoneNumberEt.addTextChangedListener(textWatcher);
        verifyCodeEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.head_to_login, R.id.fetch_text, R.id.next_step})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_to_login:
                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_HEAD_TO_LOGIN));
                break;
            case R.id.fetch_text:
                countDownTimer = new CountDownTimerUtils(fetchText, 60 * 1000, 1000);
                countDownTimer.start();
                break;
            case R.id.next_step:
                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_SET_PSD));
                break;
        }
    }
}
