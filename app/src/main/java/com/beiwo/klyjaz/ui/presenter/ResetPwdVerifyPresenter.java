package com.beiwo.klyjaz.ui.presenter;


import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.ui.contract.ResetPwdVerifyContract;

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
