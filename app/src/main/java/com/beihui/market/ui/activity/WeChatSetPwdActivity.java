package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerWeChatSetPwdComponent;
import com.beihui.market.injection.module.WeChatSetPwdModule;
import com.beihui.market.ui.contract.WeChatSetPwdContract;
import com.beihui.market.ui.presenter.WeChatSetPwdPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WeChatSetPwdActivity extends BaseComponentActivity implements WeChatSetPwdContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.pwd)
    EditText etPwd;
    @BindView(R.id.pwd_visibility)
    CheckBox cbPwdVisibility;
    @BindView(R.id.confirm)
    TextView tvConfirmBtn;

    @Inject
    WeChatSetPwdPresenter presenter;

    private String account;
    private String wxOpenId;
    private String wxName;
    private String wxImage;

    @Override
    public int getLayoutId() {
        return R.layout.actiivty_we_chat_set_pwd;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        cbPwdVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(etPwd);
            }
        });

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvConfirmBtn.setEnabled(LegalInputUtils.validatePassword(etPwd.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        account = getIntent().getStringExtra("account");
        wxOpenId = getIntent().getStringExtra("openId");
        wxName = getIntent().getStringExtra("name");
        wxImage = getIntent().getStringExtra("profile_image_url");
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerWeChatSetPwdComponent.builder()
                .appComponent(appComponent)
                .weChatSetPwdModule(new WeChatSetPwdModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.confirm)
    void onItemClicked() {
        presenter.register(account, etPwd.getText().toString(), wxOpenId, wxName, wxImage);
    }

    @Override
    public void setPresenter(WeChatSetPwdContract.Presenter presenter) {
        //
    }

    @Override
    public void showRegisterSuccess(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(WeChatSetPwdActivity.this, msg, R.mipmap.white_success);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
