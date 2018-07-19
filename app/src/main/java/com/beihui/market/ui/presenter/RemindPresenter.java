package com.beihui.market.ui.presenter;

import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.RemindContract;

import javax.inject.Inject;

/**
 * Copyright: kaola (C)2018
 * FileName: RemindPresenter
 * Author: jiang
 * Create on: 2018/7/18 15:39
 * Description: TODO
 */
public class RemindPresenter extends BaseRxPresenter implements RemindContract.Presenter {

    private Context context;
    private Api api;
    private RemindContract.View view;
    private UserHelper userHelper;


    @Override
    public void repaymentTime() {
        view.showRepaymentTime();

    }

    @Override
    public void pushRemind() {

    }

    @Override
    public void messageRemind() {

    }

    @Inject
    RemindPresenter(Context context, RemindContract.View view, Api api) {
        this.context = context;
        this.view = view;
        this.api = api;
        userHelper = UserHelper.getInstance(context);

    }


}
