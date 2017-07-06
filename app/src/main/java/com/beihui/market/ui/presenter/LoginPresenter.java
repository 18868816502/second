package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.base.RxPresenter;
import com.beihui.market.ui.contract.LoginContract;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/1/16.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View>{

    private Api api;

    @Inject
    public LoginPresenter(Api api){
      this.api = api;
    }



}
