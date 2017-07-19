package com.beihui.market.ui.fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.CommonUtils;

import butterknife.BindView;

public class UserRegisterSetPsdFragment extends BaseComponentFragment {
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.invitation_code)
    EditText invitationCodeEt;
    @BindView(R.id.register)
    Button registerBtn;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_register_set_psd;
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
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int psdLen = passwordEt.getText().length();
                if (psdLen > 6) {
                    if (!registerBtn.isEnabled()) {
                        registerBtn.setEnabled(true);
                    }
                } else {
                    if (registerBtn.isEnabled()) {
                        registerBtn.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
