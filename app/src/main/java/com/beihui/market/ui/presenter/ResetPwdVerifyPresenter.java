package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.ResetPwdVerifyContract;

import javax.inject.Inject;

public class ResetPwdVerifyPresenter extends BaseRxPresenter implements ResetPwdVerifyContract.Presenter {
    private Api mApi;
    private ResetPwdVerifyContract.View mView;

    @Inject
    public ResetPwdVerifyPresenter(Api api, ResetPwdVerifyContract.View view) {
        mApi = api;
        mView = view;
    }

}
