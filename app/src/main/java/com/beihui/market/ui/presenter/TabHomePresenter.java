package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.ui.contract.TabHomeContract;
import com.beihui.market.util.update.RxUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabHomePresenter extends BaseRxPresenter implements TabHomeContract.Presenter {

    private Api mApi;
    private TabHomeContract.View mView;

    @Inject
    TabHomePresenter(Api api, TabHomeContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        queryBanner();
        queryAd();
        queryScrolling();
    }

    private void queryBanner() {
        Disposable dis = mApi.querySupernatant(RequestConstants.SUP_TYPE_BANNER)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           mView.showBanner(result.getData());
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryAd() {
        Disposable dis = mApi.querySupernatant(RequestConstants.SUP_TYPE_AD)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           mView.showAdDialog(result.getData().get(0));
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryScrolling() {
        Disposable dis = mApi.queryBorrowingScroll()
                .compose(RxUtil.<ResultEntity<List<String>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<String>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<String>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           mView.showBorrowingScroll(result.getData());
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void handleThrowable(Throwable throwable) {
        logError(TabHomePresenter.this, throwable);
        mView.showErrorMsg(generateErrorMsg(throwable));
    }
}
