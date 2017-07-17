package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.activity.ResetPsdActivity;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


public class UserLoginFragment extends BaseFragment {
    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.login)
    Button loginBtn;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int phoneLen = phoneNumberEt.getText().length();
            int psdLen = passwordEt.getText().length();
            if (phoneLen > 0 && psdLen > 0) {
                if (!loginBtn.isEnabled()) {
                    loginBtn.setEnabled(true);
                }
            } else {
                if (loginBtn.isEnabled()) {
                    loginBtn.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_user_login;
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
        phoneNumberEt.addTextChangedListener(textWatcher);
        passwordEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.register, R.id.forget_psd, R.id.login})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register:
                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_REGISTER));
                break;
            case R.id.forget_psd:
                Intent toResetPsd = new Intent(getActivity(), ResetPsdActivity.class);
                startActivity(toResetPsd);
                break;
            case R.id.login:
                break;
        }
    }


}
