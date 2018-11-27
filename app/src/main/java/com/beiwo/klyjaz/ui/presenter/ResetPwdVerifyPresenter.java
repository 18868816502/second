package com.beiwo.klyjaz.ui.presenter;


import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.ui.contract.ResetPwdVerifyContract;


public class ResetPwdVerifyPresenter extends BaseRxPresenter implements ResetPwdVerifyContract.Presenter {
    private Api mApi;
    private ResetPwdVerifyContract.View mView;

    public ResetPwdVerifyPresenter(ResetPwdVerifyContract.View view) {
        mApi = Api.getInstance();
        mView = view;
    }
}