package com.beihui.market.tang.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.activity.CreditQueryActivity;
import com.beihui.market.tang.activity.TicketActivity;
import com.beihui.market.ui.activity.HouseLoanCalculatorActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.ToastUtil;

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

public class SocialFragment extends BaseComponentFragment {
    @BindView(R.id.hold_view)
    View hold_view;

    public static SocialFragment newInstance() {
        return new SocialFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_social;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.clb_credit_wrap, R.id.clb_interest_wrap, R.id.clb_ticket_wrap})
    public void onClick(View view) {
        if (!UserHelper.getInstance(getActivity()).isLogin()) {
            UserAuthorizationActivity.launch(getActivity());
            return;
        }
        switch (view.getId()) {
            case R.id.clb_credit_wrap:
                //ToastUtil.toast("敬请期待...");
                startActivity(new Intent(getActivity(), CreditQueryActivity.class));
                break;
            case R.id.clb_interest_wrap:
//                ToastUtil.toast("敬请期待...");
                startActivity(new Intent(getActivity(), HouseLoanCalculatorActivity.class));
                break;
            case R.id.clb_ticket_wrap:
                startActivity(new Intent(getActivity(), TicketActivity.class));
                break;
            default:
                break;
        }
    }
}
