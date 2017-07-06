package com.beihui.market.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseRVActivity;
import com.beihui.market.base.Constant;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.contract.LoginContract;
import com.beihui.market.ui.dialog.JiekuanDialog;
import com.beihui.market.ui.presenter.LoginPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.ToastView;
import com.beihui.market.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by C on 2017/7/6.
 */

public class RegisterActivity extends BaseRVActivity<LoginPresenter> implements LoginContract.View, View.OnTouchListener {


    @BindView(R.id.ly_title)
    LinearLayout lyTitle;
    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.tv_mail)
    TextView tvMail;
    @BindView(R.id.et_yzm)
    ClearEditText etYzm;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.fy_main)
    FrameLayout fyMain;


    public static RegisterActivity instance;
    private int count1 = 0, count2 = 0;
    private boolean isClicked = false;


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void configViews() {
        instance = this;

        ImmersionBar.with(this)
                .titleBar(lyTitle,false)
                .navigationBarColor(R.color.c_2b4473)
                .init();



        fyMain.setOnTouchListener(this);
        count1 = etName.getText().toString().length();
        count2 = etYzm.getText().toString().length();
        checkIsLogin(count1, count2);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                count1 = s.toString().length();
                checkIsLogin(count1, count2);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etYzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                count2 = s.toString().length();
                checkIsLogin(count1, count2);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodUtil.closeSoftKeyboard(this);
        return false;
    }

    @Override
    public void showError(String err) {
        if (!CommonUtils.isNetworkAvailable(this)) {
            ToastView.initToast().textToast(this, Constant.IsNetWorkError);
        } else {
            if (SPUtils.isOnLine(this)) {
                ToastView.initToast().textToast(this, Constant.IsServiceError);
            } else {
                new JiekuanDialog(this, "失败原因", err).show();
            }
        }
    }

    @Override
    public void complete() {

    }


    @OnClick({R.id.tv_back, R.id.tv_mail, R.id.tv_next, R.id.tv_login})
    public void onViewClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(this);
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_mail:
                CountDownTimerUtils mTimerUtils = new CountDownTimerUtils(tvMail, 60000, 1000);
                mTimerUtils.start();
                break;
            case R.id.tv_next:
                RegisterPsdActivity.startActivity(this);
                break;
            case R.id.tv_login:
                finish();
                break;
        }
    }



    public void checkIsLogin(int count1, int count2) {

        if (count1 == 11){
            tvMail.setClickable(true);
            tvMail.setTextColor(Color.parseColor("#528bff"));
        }else{
            tvMail.setClickable(false);
            tvMail.setTextColor(Color.parseColor("#909298"));
        }


        if (count1 != 0 && count2 != 0) {
            if (count1 == 11){
                tvNext.setClickable(true);
                tvNext.setBackgroundResource(R.drawable.round_login_select);
                isClicked = true;
            }else{
                tvNext.setClickable(false);
                tvNext.setBackgroundResource(R.drawable.round_login_no);
                isClicked = false;
            }

        } else if (count1 == 0 && count2 == 0) {
            tvNext.setClickable(false);
            tvNext.setBackgroundResource(R.drawable.round_login_no);
            isClicked = false;
        } else {
            tvNext.setClickable(false);
            if (isClicked) {
                tvNext.setBackgroundResource(R.drawable.round_login_no);
            }
        }
    }


    public static void startActivity(Context context) {
        Intent i = new Intent(context, RegisterActivity.class);
        context.startActivity(i);
    }

}
