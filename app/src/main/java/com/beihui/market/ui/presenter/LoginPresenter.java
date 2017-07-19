package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.ui.contract.LoginContract;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.update.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginPresenter extends BaseRxPresenter implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private Api mApi;
    private LoginContract.View mView;

    @Inject
    LoginPresenter(Api api, LoginContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void login(String account, String pwd) {
        mView.showLoading();
        Disposable dis = mApi.login(account, pwd)
                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                    @Override
                    public void accept(@NonNull ResultEntity<UserProfileAbstract> result) throws Exception {
                        if (result.isSuccess()) {
                            mView.showLoginSuccess();
                        } else {
                            mView.showErrorMsg(result.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "login exception thrown throwable " + throwable);
                        mView.showErrorMsg(throwable.getMessage());
                    }
                });
        addDisposable(dis);
    }
}
