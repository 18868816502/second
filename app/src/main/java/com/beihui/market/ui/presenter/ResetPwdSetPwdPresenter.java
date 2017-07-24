package com.beihui.market.ui.presenter;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.util.update.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ResetPwdSetPwdPresenter extends BaseRxPresenter implements ResetPwdSetPwdContract.Presenter {

    private Api mApi;
    private ResetPwdSetPwdContract.View mView;

    @Inject
    public ResetPwdSetPwdPresenter(Api api, ResetPwdSetPwdContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void resetPwd(String phone, String pwd) {
        Disposable dis = mApi.resetPwd(phone, pwd)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {

                                   } else {
                                       mView.showError(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(ResetPwdSetPwdPresenter.this, throwable);
                                mView.showError(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
