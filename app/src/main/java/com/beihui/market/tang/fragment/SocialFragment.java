package com.beihui.market.tang.fragment;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.activity.TicketActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static SocialFragment newInstance() {
        return new SocialFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_social;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
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
                ToastUtil.toast("敬请期待...");
                break;
            case R.id.clb_interest_wrap:
                ToastUtil.toast("敬请期待...");
                break;
            case R.id.clb_ticket_wrap:
                startActivity(new Intent(getActivity(), TicketActivity.class));
                break;
            default:
                break;
        }
    }
}
