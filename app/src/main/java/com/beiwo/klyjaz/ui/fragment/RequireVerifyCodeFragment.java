package com.beiwo.klyjaz.ui.fragment;


import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerVerifyCodeComponent;
import com.beiwo.klyjaz.injection.module.VerifyCodeModule;
import com.beiwo.klyjaz.ui.busevents.ResetPsdNavigationEvent;
import com.beiwo.klyjaz.ui.contract.ResetPwdVerifyContract;
import com.beiwo.klyjaz.ui.presenter.ResetPwdVerifyPresenter;
import com.beiwo.klyjaz.util.CountDownTimerUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.LegalInputUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码输入手机号验证
 */
public class RequireVerifyCodeFragment extends BaseComponentFragment implements ResetPwdVerifyContract.View {
    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.next_step)
    TextView nextStepBtn;

    @Inject
    ResetPwdVerifyPresenter presenter;

    private CountDownTimerUtils countDownTimer;

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
        return R.layout.fragment_require_verify_code;
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
                if (validatePhone) {
                    //手机号符合规则，且没有正在请求验证码倒数,则设置enable
                    nextStepBtn.setClickable(true);
                    nextStepBtn.setTextColor(Color.WHITE);
                } else {
                    nextStepBtn.setClickable(false);
                    nextStepBtn.setTextColor(Color.parseColor("#50ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        phoneNumberEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerVerifyCodeComponent.builder()
                .appComponent(appComponent)
                .verifyCodeModule(new VerifyCodeModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.next_step)
    public void onViewClicked() {
        String phone = phoneNumberEt.getText().toString();
        EventBus.getDefault().post(new ResetPsdNavigationEvent(phone));
    }

    @Override
    public void setPresenter(ResetPwdVerifyContract.Presenter presenter) {
        //injected.nothing to do.
    }
}