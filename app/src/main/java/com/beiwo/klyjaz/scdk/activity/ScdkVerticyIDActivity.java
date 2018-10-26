package com.beiwo.klyjaz.scdk.activity;


import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.jjd.CircleProgressBar;
import com.beiwo.klyjaz.ui.activity.VestMainActivity;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.SPUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market
 * @class describe 认证页面
 * @time
 */
public class ScdkVerticyIDActivity extends BaseComponentActivity implements View.OnClickListener, CircleProgressBar.OnAnimatorFinishedListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.stateContainer)
    LinearLayout stateContainer;
    @BindView(R.id.tv_start_vertify)
    TextView tvStartVertify;
    /**
     * 认证状态
     * 1:第一步 2:第二步 3:第三步
     */
    private int mVertifyState = 1;
    private EditText etIDCardName;
    private EditText etIDCardNo;

    private EditText etRelative;
    private EditText etRelativeName;
    private EditText etPhone;

    private TextView tvAuthTip;
    private CircleProgressBar progressBar;
    private TextView tvBackTips;

    private String mContactRelate;
    private String mIDCardName;
    private String mIDCardNo;
    private String mContactName;
    private String mContactPhone;
    private static final int REQUESTCODE_VERTIFY_CONTACT = 100;
    public static final int RESULTCODE_VERTIFY_CONTACT = 101;

    private TimeCounter timeCounter = new TimeCounter(5000, 1000);

    @Override
    public int getLayoutId() {
        return R.layout.activity_verticy_id;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        Intent intent = getIntent();
        if (intent != null) {
            mVertifyState = intent.getIntExtra("mVertifyState", 1);
            switchVertifyLayout();
        } else {
            loadFirstLayout();
        }
        tvStartVertify.setOnClickListener(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_vertify:
                switchSateFunction();
                break;
            case R.id.et_contact_name:
                break;
            default:
                break;
        }
    }

    /*切换提交按钮状态*/
    private void switchSateFunction() {
        switch (mVertifyState) {
            case 1:
                if (checkVertifyCondition()) {
                    loadSecondLayout();
                    SPUtils.setVertifyState(this, 2, UserHelper.getInstance(this).getProfile().getAccount());
                }
                break;
            case 2:
                if (checkVertifyContactCondition()) {
                    loadThirdLayout();
                    SPUtils.setVertifyState(this, 3, UserHelper.getInstance(this).getProfile().getAccount());
                    InputMethodUtil.closeSoftKeyboard(ScdkVerticyIDActivity.this, etIDCardName);
                }
                break;
            case 3:
                App.step = 3;
                tvAuthTip.setText(getString(R.string.jjd_vertify_auth_ing));
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(100);
                SPUtils.setVertifyState(this, 4, UserHelper.getInstance(this).getProfile().getAccount());
                SPUtils.setPhone(this,
                        UserHelper.getInstance(this).getProfile().getId(),
                        UserHelper.getInstance(this).getProfile().getAccount());
                tvStartVertify.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /*检查认证条件*/
    private boolean checkVertifyCondition() {
        mIDCardName = etIDCardName.getText().toString();
        if (TextUtils.isEmpty(mIDCardName)) {
            ToastUtil.toast(getString(R.string.jjd_truth_name_input));
            return false;
        }
        mIDCardNo = etIDCardNo.getText().toString();
        if (TextUtils.isEmpty(mIDCardNo)) {
            ToastUtil.toast(getString(R.string.jjd_idcard_no_input));
            return false;
        }
        return true;
    }

    /*检查联系人认证条件*/
    private boolean checkVertifyContactCondition() {
        mContactRelate = etRelative.getText().toString();
        if (TextUtils.isEmpty(mContactRelate)) {
            ToastUtil.toast(getString(R.string.jjd_contact_relative_input));
            return false;
        }
        mContactName = etRelativeName.getText().toString();
        if (TextUtils.isEmpty(mContactName)) {
            ToastUtil.toast(getString(R.string.jjd_truth_name_input));
            return false;
        }
        mContactPhone = etPhone.getText().toString();
        if (TextUtils.isEmpty(mContactPhone)) {
            ToastUtil.toast(getString(R.string.jjd_contact_phone_input));
            return false;
        }
        return true;
    }


    private void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULTCODE_VERTIFY_CONTACT && requestCode == REQUESTCODE_VERTIFY_CONTACT) {
            if (data != null) {
                mContactPhone = data.getStringExtra("contact_phone");
                mContactName = data.getStringExtra("contact_name");
                etRelativeName.setText(mContactName);
                etPhone.setText(mContactPhone);
            }
        }
    }

    @Override
    public void onFinished() {
        progressBar.setVisibility(View.GONE);
        tvBackTips.setVisibility(View.VISIBLE);
        tvAuthTip.setText(getString(R.string.jjd_vertify_zhima_auth_via));
        timeCounter.start();
    }

    /**
     * 切换布局
     */
    private void switchVertifyLayout() {
        switch (mVertifyState) {
            case 1:
                loadFirstLayout();
                break;
            case 2:
                loadSecondLayout();
                break;
            case 3:
                loadThirdLayout();
                break;
            default:
                break;
        }
    }

    /*加载状态1布局*/
    private void loadFirstLayout() {
        App.step = 0;
        View container01 = LayoutInflater.from(this).inflate(R.layout.layout_verticy_state01, stateContainer);
        etIDCardName = container01.findViewById(R.id.et_idcard_name);
        etIDCardNo = container01.findViewById(R.id.et_idcard_no);
    }

    /*加载状态2布局*/
    private void loadSecondLayout() {
        App.step = 1;
        mVertifyState = 2;
        stateContainer.removeAllViews();
        ivState.setBackgroundResource(R.drawable.vertify_state02);
        tvStartVertify.setText(getString(R.string.jjd_vertify_next_text));
        View container02 = LayoutInflater.from(this).inflate(R.layout.layout_verticy_state02_scdk, stateContainer);
        etRelative = container02.findViewById(R.id.et_contact_relative);
        etRelativeName = container02.findViewById(R.id.et_contact_name);
        etPhone = container02.findViewById(R.id.et_contact_phone);
    }

    /*加载状态3布局*/
    private void loadThirdLayout() {
        App.step = 2;
        mVertifyState = 3;
        stateContainer.removeAllViews();
        ivState.setBackgroundResource(R.drawable.vertify_state03);
        tvStartVertify.setText(getString(R.string.jjd_vertify_start_auth));
        View container03 = LayoutInflater.from(this).inflate(R.layout.layout_verticy_state03, stateContainer);
        tvAuthTip = container03.findViewById(R.id.tv_auth_tip);
        progressBar = container03.findViewById(R.id.progressbar);
        tvBackTips = container03.findViewById(R.id.tv_back_tips);
        progressBar.setOnAnimatorFinishedListener(this);
    }

    private class TimeCounter extends CountDownTimer {
        TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvBackTips.setText(getString(R.string.jjd_vertify_auth_back_time_tips, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(ScdkVerticyIDActivity.this, VestMainActivity.class));
            finish();
        }
    }
}