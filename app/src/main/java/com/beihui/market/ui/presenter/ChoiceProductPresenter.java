package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.ui.contract.ChoiceProductContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChoiceProductPresenter extends BaseRxPresenter implements ChoiceProductContract.Presenter {

    private static final int PAGE_SIZE = 1000;

    private Api api;
    private ChoiceProductContract.View view;

    private List<LoanProduct.Row> choiceProduct = new ArrayList<>();

    private boolean canLoadMore = true;
    private int pageNo = 1;

    @Inject
    ChoiceProductPresenter(Api api, ChoiceProductContract.View view) {
        this.api = api;
        this.view = view;
    }


    @Override
    public void refreshChoiceProduct() {
        pageNo = 1;
        canLoadMore = true;
        Disposable dis = api.queryChoiceProduct(pageNo, PAGE_SIZE, 1) //倒序查询
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo++;
                                       choiceProduct.clear();
                                       int size = 0;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           choiceProduct.addAll(result.getData().getRows());
                                           size = result.getData().getRows().size();
                                       }
                                       canLoadMore = size == PAGE_SIZE;
                                       view.showChoiceProduct(Collections.unmodifiableList(choiceProduct), canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(ChoiceProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadMoreChoiceProduct() {
        Disposable dis = api.queryChoiceProduct(pageNo, PAGE_SIZE, 1)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo++;
                                       int size = 0;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           choiceProduct.addAll(result.getData().getRows());
                                           size = result.getData().getRows().size();
                                       }
                                       canLoadMore = size == PAGE_SIZE;
                                       view.showChoiceProduct(Collections.unmodifiableList(choiceProduct), canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(ChoiceProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
