package com.beiwo.klyjaz.tang.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.activity.CreditBillActivity;
import com.beiwo.klyjaz.tang.activity.CreditQueryActivity;
import com.beiwo.klyjaz.tang.activity.LoanBillActivity;
import com.beiwo.klyjaz.tang.activity.TicketActivity;
import com.beiwo.klyjaz.ui.activity.HouseLoanCalculatorActivity;
import com.beiwo.klyjaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.klyjaz.util.CommonUtils;

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
 * @date: 2018/9/4
 */

public class ToolFragment extends BaseComponentFragment {
    @BindView(R.id.hold_view)
    View hold_view;

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
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.clb_credit_wrap, R.id.clb_interest_wrap, R.id.clb_ticket_wrap, R.id.fl_add_bill_wrap, R.id.fl_credit_wrap, R.id.tv_bill_detail})
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
                startActivity(new Intent(getActivity(), CreditBillActivity.class));
                break;
            default:
                break;
        }
    }
}