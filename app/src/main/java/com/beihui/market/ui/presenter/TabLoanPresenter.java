package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.TabLoanContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabLoanPresenter extends BaseRxPresenter implements TabLoanContract.Presenter {

    private Api api;
    private TabLoanContract.View view;

    private List<String> productNotice = new ArrayList<>();
    private boolean hasNoticeInit;

    @Inject
    TabLoanPresenter(Api api, TabLoanContract.View view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void loadProductNotice() {
        if (hasNoticeInit) {
            return;
        }
        Disposable dis = api.queryLoanHint()
                .compose(RxUtil.<ResultEntity<List<String>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<String>>>() {
                               @Override
                               public void accept(ResultEntity<List<String>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       productNotice.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           productNotice.addAll(result.getData());
                                           view.showProductNotice(productNotice);
                                       }
                                       hasNoticeInit = true;
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(TabLoanPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));

                            }
                        });
        addDisposable(dis);
    }
}
