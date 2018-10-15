package com.beiwo.klyjaz.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Phone;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.util.CountDownTimerUtils;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.SPUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Copyright: zhujia (C)2018
 * FileName: WechatBindDoneActivity
 * Author: jiang
 * Create on: 2018/7/31 10:19
 * Description: 微信绑定成功界面
 */
public class WechatBindDoneActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_wechat_phone)
    TextView tvPhone;
    @BindView(R.id.verify_wechat_code)
    ClearEditText verifyCode;
    @BindView(R.id.get_code_text)
    TextView fetchText;
    @BindView(R.id.tv_wechat_login)
    TextView tvLogin;

    //手机号
    private String pendingPhone;
    private Intent intent;
    private String wxOpenId;
    private String wxName;
    private String wxImage;

    private CountDownTimerUtils countDownTimer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_wechat_bind_done_layout;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();
        intent = getIntent();

        pendingPhone = intent.getStringExtra("phone");
        wxOpenId = getIntent().getStringExtra("openId");
        wxName = getIntent().getStringExtra("name");
        wxImage = getIntent().getStringExtra("profile_image_url");

        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        tvPhone.setText(LegalInputUtils.formatMobile(pendingPhone));
        fetchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVerifyCode(pendingPhone);

            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(pendingPhone, verifyCode.getText().toString(), wxOpenId, wxName, wxImage);
            }
        });

        verifyCode.setMaxLenght(4);
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

        requestVerifyCode(pendingPhone);

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    /**
     * 获取验证码
     */
    public void requestVerifyCode(String phone) {
        fetchText.setEnabled(false);
        countDownTimer = new CountDownTimerUtils(fetchText, verifyCode);
        countDownTimer.start();
        Api.getInstance().requestWeChatBindPwdSms(phone)
                .compose(RxUtil.<ResultEntity<Phone>>io2main())
                .subscribe(new Consumer<ResultEntity<Phone>>() {
                               @Override
                               public void accept(ResultEntity<Phone> result) throws Exception {
                                   if (result.isSuccess()) {
                                       ToastUtil.toast(result.getMsg());
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                ToastUtil.toast("网络错误");

                            }
                        });
    }

    /**
     * 绑定
     */

    public void verifyCode(String phone, String code, String wxOpenId, String wxName, String wxImage) {
        if (TextUtils.isEmpty(code)) {
            ToastUtil.toast("请输入验证码");
            return;
        }
        final String account = phone;
        Api.getInstance().verifyWeChatBindCode(phone, wxOpenId, wxName, wxImage, code)
                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                               @Override
                               public void accept(ResultEntity<UserProfileAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       UserHelper.getInstance(WechatBindDoneActivity.this).update(result.getData(), account, WechatBindDoneActivity.this);
                                       //保存用户id,缓存
                                       SPUtils.setCacheUserId(WechatBindDoneActivity.this, result.getData().getId());
                                       WechatBindDoneActivity.this.setResult(RESULT_OK);
                                       WechatBindDoneActivity.this.finish();

                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {


                            }
                        });
    }
}
