package com.beiwo.qnejqaz.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.ui.contract.WeChatBindPhoneContract;
import com.beiwo.qnejqaz.ui.presenter.WeChatBindPhonePresenter;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xhb
 * 微信登录绑定手机号
 */
public class WeChatBindPhoneActivity extends BaseComponentActivity implements WeChatBindPhoneContract.View {

    private final int REQUEST_CODE_SET_PWD = 1;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.hint_block)
    FrameLayout flHintBlock;
    @BindView(R.id.hint_close)
    ImageView ivHintClose;
    @BindView(R.id.phone)
    EditText etPhone;
    @BindView(R.id.send_code)
    TextView tvSendCode;
    @BindView(R.id.verify_code)
    EditText etVerifyCode;

    WeChatBindPhonePresenter presenter;

    private CountDown countDown;

    private String wxOpenId;
    private String wxName;
    private String wxImage;

    @Override
    protected void onDestroy() {
        if (countDown != null && countDown.isRunning) {
            countDown.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SET_PWD) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_we_chat_bind_phone;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);
        presenter = new WeChatBindPhonePresenter(this,this);
        ivHintClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flHintBlock.setVisibility(View.GONE);
            }
        });
        tvSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestVerifyCode(etPhone.getText().toString());
            }
        });

        wxOpenId = getIntent().getStringExtra("openId");
        wxName = getIntent().getStringExtra("name");
        wxImage = getIntent().getStringExtra("profile_image_url");
    }

    @Override
    public void initDatas() {

    }

    @OnClick(R.id.confirm_bind)
    void onItemClicked() {
        presenter.verifyCode(etPhone.getText().toString(), etVerifyCode.getText().toString(), wxOpenId, wxName, wxImage);
    }

    @Override
    public void setPresenter(WeChatBindPhoneContract.Presenter presenter) {
        //
    }

    @Override
    public void showVerifyCodeSend(String msg) {
        WeakRefToastUtil.showShort(this, msg, null);
        countDown = new CountDown();
        countDown.start();
    }

    @Override
    public void navigateSetPwd(String account) {
        Intent intent = new Intent(this, WeChatSetPwdActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("openId", wxOpenId);
        intent.putExtra("name", wxName);
        intent.putExtra("profile_image_url", wxImage);
        startActivityForResult(intent, REQUEST_CODE_SET_PWD);
    }

    @Override
    public void showLoginSuccess() {
        WeakRefToastUtil.showShort(this, "登录成功", R.mipmap.white_success);
        setResult(RESULT_OK);
        finish();
    }

    class CountDown extends CountDownTimer {
        boolean isRunning;

        CountDown() {
            super(60 * 1000, 1000);
        }

        @SuppressLint("SetTextI18n")
        public void onTick(long millisUntilFinished) {
            isRunning = true;
            tvSendCode.setEnabled(false); //设置不可点击
            tvSendCode.setTextColor(getResources().getColor(R.color.black_2));
            tvSendCode.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
        }

        @Override
        public void onFinish() {
            isRunning = false;
            tvSendCode.setText("重新获取");
            tvSendCode.setEnabled(true);
            tvSendCode.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
