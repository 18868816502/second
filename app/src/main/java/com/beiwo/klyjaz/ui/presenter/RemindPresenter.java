package com.beiwo.klyjaz.ui.presenter;

import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.RemindContract;

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
