package com.beihui.market.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 快速登陆输入验证码页面
 */
public class UserCertificationCodeActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.verify_code)
    ClearEditText verifyCode;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.tv_login)
    TextView tvLogin;


    private CountDownTimerUtils countDownTimer;

    private String pendingPhone;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodUtil.closeSoftKeyboard(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verification_code;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();

        pendingPhone = getIntent().getStringExtra("pendingPhone");
        tvPhone.setText(pendingPhone);
        tvLogin.setClickable(false);
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean validateCode = verifyCode.getText().length() == 4;

                if (validateCode) {
                    tvLogin.setClickable(true);
                    tvLogin.setTextColor(Color.WHITE);
                } else {
                    tvLogin.setClickable(false);
                    tvLogin.setTextColor(Color.parseColor("#50ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        verifyCode.addTextChangedListener(textWatcher);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }


    @OnClick({R.id.fetch_text, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fetch_text:
                fetchText.setEnabled(false);
                countDownTimer = new CountDownTimerUtils(fetchText, verifyCode);
                countDownTimer.start();
                break;
            case R.id.tv_login:
                UserPsdEditActivity.launch(this,1);
                finish();
                break;
        }
    }


    /**
     * 跳转输入验证码页面
     * @param context
     * @param pendingPhone 登陆手机号
     */
    public static void launch(Activity context, String pendingPhone){
        Intent i = new Intent(context,UserCertificationCodeActivity.class);
        i.putExtra("pendingPhone",pendingPhone);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
    }


}
