package com.beiwo.qnejqaz.ui.presenter;


import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.ui.contract.ResetPwdVerifyContract;


public class ResetPwdVerifyPresenter extends BaseRxPresenter implements ResetPwdVerifyContract.Presenter {
    private Api mApi;
    private ResetPwdVerifyContract.View mView;

    public ResetPwdVerifyPresenter(ResetPwdVerifyContract.View view) {
        mApi = Api.getInstance();
        mView = view;
    }
}