package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerRemindComponent;
import com.beihui.market.injection.module.RemindModule;
import com.beihui.market.ui.contract.RemindContract;
import com.beihui.market.ui.presenter.RemindPresenter;
import com.beihui.market.util.FastClickUtils;
import com.gyf.barlibrary.ImmersionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright: kaola (C)2018
 * FileName: RemindActivity
 * Author: jiang
 * Create on: 2018/7/18 14:50
 * Description: 提醒界面
 */
public class RemindActivity extends BaseComponentActivity implements RemindContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Inject
    RemindPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_remind_layout;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        presenter.onStart();

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerRemindComponent.builder()
                .appComponent(appComponent)
                .remindModule(new RemindModule(this))
                .build()
                .inject(this);

    }

    @Override
    public void showRepaymentTime() {


    }

    @Override
    public void selectPushRemindType() {

    }

    @Override
    public void selectmessageRemindType() {

    }

    @Override
    public void setPresenter(RemindContract.Presenter presenter) {

    }

    @OnClick({R.id.time_layout})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_layout:
                if (!FastClickUtils.isFastClick()) {
                    presenter.repaymentTime();
                }

                break;

        }
    }


}
