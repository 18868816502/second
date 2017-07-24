package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.RegisterSetPwdContract;
import com.beihui.market.util.update.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegisterSetPwdPresenter extends BaseRxPresenter implements RegisterSetPwdContract.Presenter {
    private Api mApi;
    private RegisterSetPwdContract.View mView;
    private Context mContext;

    @Inject
    public RegisterSetPwdPresenter(Api api, RegisterSetPwdContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
    }

    @Override
    public void register(String phone, String pwd, String inviteCode) {
        String channelId = "";
        Disposable dis = mApi.register(phone, pwd, channelId, inviteCode)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {

                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(RegisterSetPwdPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
