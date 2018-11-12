package com.beiwo.klyjaz.scdk.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.scdk.activity.ScdkLoanActivity;
import com.beiwo.klyjaz.scdk.activity.ScdkVerticyIDActivity;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.SPUtils;

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
    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.tv_seekbar_progress)
    TextView tv_seekbar_progress;
    @BindView(R.id.tv_service_charge)
    TextView tv_service_charge;
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
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);
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
        tv_service_charge.setText(String.format("%.2f", charge));
    }

    public void checking() {
        rl_container_wrap.setVisibility(View.VISIBLE);
        ll_wrap.setVisibility(View.GONE);
    }

    private String generateUrl(String status, String overDate, String auditDate) {
        return BuildConfig.H5_DOMAIN_NEW + "/activity/page/activity-loan-review.html?status=" + status
                + "&overDate=" + overDate + "&auditDate=" + auditDate + "&audit=" + App.audit + NetConstants.sufPublicParam(UserHelper.getInstance(getActivity()).id());
    }

    @Override
    public void initDatas() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                money = 500 + (seekbar.getProgress() * 1500 / 2000 / 100) * 100;
                charge = money / 100;
                tv_seekbar_progress.setText(String.format("%.0f", money));
                tv_service_charge.setText(String.format("%.2f", charge));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    public static LoanFragment newInstance() {
        return new LoanFragment();
    }

    @OnClick({R.id.tv_go_loan, R.id.iv_question})
    public void onClick(View view) {
        if (!UserHelper.getInstance(getActivity()).isLogin()) {
            DlgUtil.loginDlg(getActivity(), null);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_go_loan:
                DataStatisticsHelper.getInstance().onCountUv("JjdLoanImmediately");//借钱—首页—立即借钱按钮
                /*用户认证信息查询*/
                toVertifyActivity();
                break;
            case R.id.iv_question:
                DlgUtil.createDlg(getContext(), R.layout.f_dlg_apl_fail, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        TextView content = dlgView.findViewById(R.id.content);
                        TextView title = dlgView.findViewById(R.id.dlg_title);
                        title.setText("提示");
                        content.setText("服务费：日息0.1%");
                        dlgView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
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