package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.ui.contract.LoanProductDetailContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoanDetailPresenter extends BaseRxPresenter implements LoanProductDetailContract.Presenter {

    private Api mApi;
    private LoanProductDetailContract.View mView;

    @Inject
    LoanDetailPresenter(Api api, LoanProductDetailContract.View view) {
        mApi = api;
        mView = view;
    }


    @Override
    public void queryDetail(String id) {
        Disposable dis = mApi.queryLoanProductDetail(id)
                .compose(RxUtil.<ResultEntity<LoanProductDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProductDetail>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProductDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showLoanDetail(result.getData());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(LoanDetailPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
