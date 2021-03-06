package com.beiwo.qnejqaz.ui.activity;


import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.ui.contract.WeChatSetPwdContract;
import com.beiwo.qnejqaz.ui.presenter.WeChatSetPwdPresenter;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.LegalInputUtils;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.gyf.barlibrary.ImmersionBar;

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
        presenter = new WeChatSetPwdPresenter(this,this);
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
                WeakRefToastUtil.showShort(WeChatSetPwdActivity.this, msg, R.mipmap.white_success);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
