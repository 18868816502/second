package com.beiwo.klyjaz.ui.presenter;


import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.Phone;
import com.beiwo.klyjaz.ui.contract.RegisterVerifyContract;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegisterVerifyPresenter extends BaseRxPresenter implements RegisterVerifyContract.Presenter {

    private Api mApi;
    private RegisterVerifyContract.View mView;
    /**
     * 是否有正在请求验证码的请求
     */
    private boolean isRequestingVerification;

    @Inject
    RegisterVerifyPresenter(Api api, RegisterVerifyContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void requestVerification(String phone) {
        if (!isRequestingVerification) {
            isRequestingVerification = true;
            Disposable dis = mApi.requestRegisterSms(phone).compose(RxUtil.<ResultEntity<Phone>>io2main())
                    .subscribe(new Consumer<ResultEntity<Phone>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<Phone> result) throws Exception {
                                       isRequestingVerification = false;
                                       if (result.isSuccess()) {
                                           //umeng统计
                                           Statistic.onEvent(Events.REGISTER_GET_VERIFY_SUCCESS);

                                           mView.showVerificationSend(result.getMsg());
                                       } else {
                                           //umeng统计
                                           Statistic.onEvent(Events.REGISTER_GET_VERIFY_FAILED);

                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    isRequestingVerification = false;
                                    logError(RegisterVerifyPresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void nextMove(String phone, String verificationCode) {
        final String requestPhone = phone;
        Disposable dis = mApi.verifyRegisterCode(phone, verificationCode).compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       //umeng统计
                                       Statistic.onEvent(Events.REGISTER_VERIFICATION_SUCCESS);

                                       mView.moveToNextStep(requestPhone);
                                   } else {
                                       //umeng统计
                                       Statistic.onEvent(Events.REGISTER_VERIFICATION_FAILED);

                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(RegisterVerifyPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
