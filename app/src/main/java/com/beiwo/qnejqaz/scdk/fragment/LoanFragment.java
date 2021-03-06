package com.beiwo.qnejqaz.scdk.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentFragment;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.scdk.activity.ScdkLoanActivity;
import com.beiwo.qnejqaz.scdk.activity.ScdkVerticyIDActivity;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.util.SPUtils;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/14
 */

public class LoanFragment extends BaseComponentFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_seekbar_progress)
    TextView tv_seekbar_progress;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.rl_container_wrap)
    RelativeLayout rl_container_wrap;
    @BindView(R.id.ll_wrap)
    LinearLayout ll_wrap;

    private float money;
    private float charge;
    public String webViewUrl = "";

    @Override
    public int getLayoutResId() {
        return R.layout.vest_fragment_loan;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        if (UserHelper.getInstance(getActivity()).isLogin()) {
            int code = SPUtils.getVertifyState(getActivity(), UserHelper.getInstance(getActivity()).getProfile().getAccount());
            String phone = SPUtils.getPhone(getActivity(), UserHelper.getInstance(getActivity()).getProfile().getId());
            if (code == 5 && !TextUtils.isEmpty(phone)) {
                checking();
            } else {
                normalState();
            }
        } else {
            normalState();
        }
    }

    public void normalState() {
        rl_container_wrap.setVisibility(View.GONE);
        ll_wrap.setVisibility(View.VISIBLE);
        //8.0适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbar.setMax(2000);
            seekbar.setMin(0);
            seekbar.setProgress(0);
        }
        money = 500 + (seekbar.getProgress() * 1500 / 2000 / 100) * 100;
        charge = money / 100;
        tv_seekbar_progress.setText(String.format("%.0f", money));
    }

    public void checking() {
        rl_container_wrap.setVisibility(View.VISIBLE);
        ll_wrap.setVisibility(View.GONE);
    }

    @Override
    public void initDatas() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                money = 500 + (seekbar.getProgress() * 1500 / 2000 / 100) * 100;
                charge = money / 100;
                tv_seekbar_progress.setText(String.format("%.0f", money));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public static LoanFragment newInstance() {
        return new LoanFragment();
    }

    @OnClick({R.id.tv_go_loan})
    public void onClick(View view) {
        if (!UserHelper.getInstance(getActivity()).isLogin()) {
            DlgUtil.loginDlg(getActivity(), null);
            return;
        }
        DataHelper.getInstance(getActivity()).onCountUv("JjdLoanImmediately");//借钱—首页—立即借钱按钮
        /*用户认证信息查询*/
        toVertifyActivity();
    }

    private void toVertifyActivity() {
        int code = SPUtils.getVertifyState(getActivity(), UserHelper.getInstance(getActivity()).getProfile().getAccount());
        String phone = SPUtils.getPhone(getActivity(), UserHelper.getInstance(getActivity()).getProfile().getId());
        if (code == 4 && !TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(getActivity(), ScdkLoanActivity.class);
            intent.putExtra("money", money);
            intent.putExtra("charge", charge);
            startActivity(intent);
        } else {
            Intent verifyIntent = new Intent(getActivity(), ScdkVerticyIDActivity.class);
            verifyIntent.putExtra("mVertifyState", code);
            verifyIntent.putExtra("money", money);
            verifyIntent.putExtra("charge", charge);
            startActivity(verifyIntent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveMsg(String msg) {
        if (TextUtils.equals("1", msg)) initView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}