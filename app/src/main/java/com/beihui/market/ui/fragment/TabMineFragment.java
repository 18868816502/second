package com.beihui.market.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.base.BaseRVFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.activity.LoginActivity;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.presenter.Main1Presenter;
import com.beihui.market.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/1/22.
 * 办事中心页面
 */

public class TabMineFragment extends BaseRVFragment<Main1Presenter> implements Main1Contract.View {


    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.iv_user)
    CircleImageView ivUser;

    public static TabMineFragment newInstance() {
        TabMineFragment f = new TabMineFragment();
        Bundle b = new Bundle();
        b.putString("type", "TabLoanFragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_mine;
    }

    @Override
    protected void immersionInit() {

    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showError(String err) {
        dismissDialog();
    }

    @Override
    public void complete() {
        dismissDialog();
    }



    @OnClick({R.id.iv_msg, R.id.iv_user, R.id.ly_menu1, R.id.ly_menu2, R.id.ly_menu3, R.id.ly_menu4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_msg:
                break;
            case R.id.iv_user:
                LoginActivity.startActivity(getActivity());
                break;
            case R.id.ly_menu1:
                break;
            case R.id.ly_menu2:
                break;
            case R.id.ly_menu3:
                break;
            case R.id.ly_menu4:
                break;
        }
    }
}
