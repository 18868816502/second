package com.beiwo.klyjaz.tang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.HomeData;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.loan.BillListActivity;
import com.beiwo.klyjaz.tang.activity.CreditBillActivity;
import com.beiwo.klyjaz.tang.activity.CreditQueryActivity;
import com.beiwo.klyjaz.tang.activity.LoanBillActivity;
import com.beiwo.klyjaz.tang.activity.TicketActivity;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.tang.widget.CustomNumTextView;
import com.beiwo.klyjaz.ui.activity.HouseLoanCalculatorActivity;
import com.beiwo.klyjaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.FormatNumberUtils;
import com.beiwo.klyjaz.util.SPUtils;

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
    @BindView(R.id.hold_view)
    View hold_view;
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
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setBackgroundResource(R.color.refresh_one);
        hold_view.setLayoutParams(params);

        userHelper = UserHelper.getInstance(getActivity());
        currentMonth = new SimpleDateFormat("MM", Locale.CHINA).format(System.currentTimeMillis());
        numVisible = SPUtils.getNumVisible(getActivity());
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
                            if (SPUtils.getNumVisible(getActivity())) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(String msg) {
        if (TextUtils.equals("1", msg)) request();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.clb_credit_wrap, R.id.clb_interest_wrap, R.id.clb_ticket_wrap, R.id.fl_add_bill_wrap,
            R.id.fl_credit_wrap, R.id.tv_bill_detail, R.id.iv_bill_visible})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clb_credit_wrap:
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    UserAuthorizationActivity.launch(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), CreditQueryActivity.class));
                break;
            case R.id.clb_interest_wrap:
                startActivity(new Intent(getActivity(), HouseLoanCalculatorActivity.class));
                break;
            case R.id.clb_ticket_wrap:
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    UserAuthorizationActivity.launch(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), TicketActivity.class));
                break;
            case R.id.fl_add_bill_wrap:
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    UserAuthorizationActivity.launch(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), LoanBillActivity.class));
                break;
            case R.id.fl_credit_wrap:
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    UserAuthorizationActivity.launch(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), CreditBillActivity.class));
                break;
            case R.id.tv_bill_detail:
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    UserAuthorizationActivity.launch(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), BillListActivity.class));
                break;
            case R.id.iv_bill_visible:
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    UserAuthorizationActivity.launch(getActivity());
                    return;
                }
                if (numVisible) {
                    iv_bill_visible.setImageResource(R.mipmap.ic_eye_close);
                    tv_bill_num.setText(hideNum);
                    SPUtils.putNumVisible(getActivity(), false);
                } else {
                    iv_bill_visible.setImageResource(R.mipmap.ic_eye_open);

                    String billNum = FormatNumberUtils.FormatNumberFor2(num);
                    String num = userHelper.isLogin() ? billNum : hideNum;
                    tv_bill_num.setText(num);
                    SPUtils.putNumVisible(getActivity(), true);
                }
                numVisible = !numVisible;
                break;
            default:
                break;
        }
    }
}