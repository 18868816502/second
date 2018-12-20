package com.beiwo.qnejqaz.tang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentFragment;
import com.beiwo.qnejqaz.entity.HomeData;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.loan.BillListActivity;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.activity.CreditBillActivity;
import com.beiwo.qnejqaz.tang.activity.CreditQueryActivity;
import com.beiwo.qnejqaz.tang.activity.LoanBillActivity;
import com.beiwo.qnejqaz.tang.activity.TicketActivity;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.tang.widget.CustomNumTextView;
import com.beiwo.qnejqaz.ui.activity.HouseLoanCalculatorActivity;
import com.beiwo.qnejqaz.util.FormatNumberUtils;
import com.beiwo.qnejqaz.util.SPUtils;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/4
 */

public class ToolFragment extends BaseComponentFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_month_num)
    TextView tv_month_num;
    @BindView(R.id.tv_bill_num)
    CustomNumTextView tv_bill_num;
    @BindView(R.id.iv_bill_visible)
    ImageView iv_bill_visible;

    private UserHelper userHelper;
    private double num;
    private String currentMonth;
    private boolean numVisible;
    private String hideNum = "****";

    public static ToolFragment newInstance() {
        return new ToolFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_tool;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).init();
        userHelper = UserHelper.getInstance(getActivity());
        currentMonth = new SimpleDateFormat("MM", Locale.CHINA).format(System.currentTimeMillis());
        numVisible = SPUtils.getNumVisible();
    }

    @Override
    public void initDatas() {
        request();
    }

    private void request() {
        if (userHelper.isLogin()) {
            Api.getInstance().home(userHelper.id(), 1)
                    .compose(RxResponse.<HomeData>compatT())
                    .subscribe(new ApiObserver<HomeData>() {
                        @Override
                        public void onNext(@NonNull HomeData data) {
                            //账单头
                            tv_month_num.setText(String.format(getString(R.string.x_month_repay), data.getXmonth()));//x月应还
                            num = data.getTotalAmount();
                            if (SPUtils.getNumVisible()) {
                                tv_bill_num.setText(FormatNumberUtils.FormatNumberFor2(num));//应还金额
                                iv_bill_visible.setImageResource(R.mipmap.ic_eye_open);
                            } else {
                                tv_bill_num.setText(hideNum);//应还金额
                                iv_bill_visible.setImageResource(R.mipmap.ic_eye_close);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            notLogin();
                        }
                    });
        } else notLogin();
    }

    private void notLogin() {
        tv_month_num.setText(String.format(getString(R.string.x_month_repay), currentMonth));//x月应还
        tv_bill_num.setText(hideNum);//应还金额
        iv_bill_visible.setImageResource(R.mipmap.ic_eye_close);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private long nao;
    private boolean viewVisible;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        viewVisible = hidden;
        if (!hidden) {
            nao = System.currentTimeMillis();
        } else {
            if (viewVisible && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < Integer.MAX_VALUE) {
                DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_TOOLHOMEPAGE, "", System.currentTimeMillis() - nao);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        nao = System.currentTimeMillis();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!viewVisible && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < Integer.MAX_VALUE) {
            DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_TOOLHOMEPAGE, "", System.currentTimeMillis() - nao);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(String msg) {
        if (TextUtils.equals("1", msg)) request();
    }

    @OnClick({R.id.clb_credit_wrap, R.id.clb_interest_wrap, R.id.clb_ticket_wrap, R.id.fl_add_bill_wrap,
            R.id.fl_credit_wrap, R.id.tv_bill_detail, R.id.iv_bill_visible})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clb_credit_wrap://信用查询
                DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_CLICK, DataHelper.EVENT_VIEWID_TOOLHOMEPAGE, DataHelper.EVENT_EVENTID_CREDITINQUIRY, 0);
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                startActivity(new Intent(getActivity(), CreditQueryActivity.class));
                break;
            case R.id.clb_interest_wrap://房贷计算器
                DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_CLICK, DataHelper.EVENT_VIEWID_TOOLHOMEPAGE, DataHelper.EVENT_EVENTID_MORTGAGECALCULATOR, 0);
                startActivity(new Intent(getActivity(), HouseLoanCalculatorActivity.class));
                break;
            case R.id.clb_ticket_wrap://发票助手
                DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_CLICK, DataHelper.EVENT_VIEWID_TOOLHOMEPAGE, DataHelper.EVENT_EVENTID_INVOICEASSISTANT, 0);
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                startActivity(new Intent(getActivity(), TicketActivity.class));
                break;
            case R.id.fl_add_bill_wrap://记一笔
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                startActivity(new Intent(getActivity(), LoanBillActivity.class));
                break;
            case R.id.fl_credit_wrap://导入信用卡
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                startActivity(new Intent(getActivity(), CreditBillActivity.class));
                break;
            case R.id.tv_bill_detail://账单首页
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                startActivity(new Intent(getActivity(), BillListActivity.class));
                break;
            case R.id.iv_bill_visible://金额是否可见
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                if (numVisible) {
                    iv_bill_visible.setImageResource(R.mipmap.ic_eye_close);
                    tv_bill_num.setText(hideNum);
                    SPUtils.putNumVisible(false);
                } else {
                    iv_bill_visible.setImageResource(R.mipmap.ic_eye_open);

                    String billNum = FormatNumberUtils.FormatNumberFor2(num);
                    String num = userHelper.isLogin() ? billNum : hideNum;
                    tv_bill_num.setText(num);
                    SPUtils.putNumVisible(true);
                }
                numVisible = !numVisible;
                break;
            default:
                break;
        }
    }
}