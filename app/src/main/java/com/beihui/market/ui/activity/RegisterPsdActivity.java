package com.beihui.market.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

public class RegisterPsdActivity extends BaseRVActivity<LoginPresenter> implements LoginContract.View, View.OnTouchListener {


    @BindView(R.id.ly_title)
    LinearLayout lyTitle;
    @BindView(R.id.et_psd)
    ClearEditText etPsd;
    @BindView(R.id.ly_showpsd)
    LinearLayout lyShowpsd;
    @BindView(R.id.et_yqm)
    ClearEditText etYqm;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.fy_main)
    FrameLayout fyMain;


    private boolean isChecked = false;
    private boolean isClicked = false;
    private int count1 = 0, count2 = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_psd;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this)
                .titleBar(lyTitle,false)
                .navigationBarColor(R.color.c_2b4473)
                .init();

        fyMain.setOnTouchListener(this);
        count1 = etPsd.getText().toString().length();
        count2 = etYqm.getText().toString().length();
        checkIsLogin(count1, count2);

        etPsd.addTextChangedListener(new TextWatcher() {
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
        etYqm.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.tv_back, R.id.ly_showpsd, R.id.tv_register, R.id.tv_hetong})
    public void onViewClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(this);
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.ly_showpsd:
                if (isChecked) {
                    isChecked = false;
                } else {
                    isChecked = true;
                }

                if (isChecked) {
                    //如果选中，显示密码
                    lyShowpsd.setSelected(true);
                    etPsd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    lyShowpsd.setSelected(false);
                    etPsd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(etPsd);
                break;
            case R.id.tv_register:
                break;
            case R.id.tv_hetong:
                break;
        }
    }


    public void checkIsLogin(int count1, int count2) {
        if (count1 != 0 && count2 != 0) {
            if (count1 < 6){
                tvRegister.setClickable(false);
                tvRegister.setBackgroundResource(R.drawable.round_login_no);
                isClicked = false;
            }else{
                tvRegister.setClickable(true);
                tvRegister.setBackgroundResource(R.drawable.round_login_select);
                isClicked = true;
            }
        } else if (count1 == 0 && count2 == 0) {
            tvRegister.setClickable(false);
            tvRegister.setBackgroundResource(R.drawable.round_login_no);
            isClicked = false;
        } else {
            tvRegister.setClickable(false);
            if (isClicked) {
                tvRegister.setBackgroundResource(R.drawable.round_login_no);
            }
        }
    }

    public static void startActivity(Context context) {
        Intent i = new Intent(context, RegisterPsdActivity.class);
        context.startActivity(i);
    }


}
