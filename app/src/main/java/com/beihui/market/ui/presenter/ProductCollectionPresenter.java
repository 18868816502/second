package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.ProductCollectionContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ProductCollectionPresenter extends BaseRxPresenter implements ProductCollectionContract.Presenter {

    private static final int PAGE_SIZE = 15;

    private Api api;
    private ProductCollectionContract.View view;
    private UserHelper userHelper;

    private List<LoanProduct.Row> products = new ArrayList<>();

    private int pageNo = 1;

    @Inject
    ProductCollectionPresenter(Context context, Api api, ProductCollectionContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void loadCollection() {
        Disposable dis = api.queryProductionCollection(userHelper.getProfile().getId(), pageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo++;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           products.addAll(result.getData().getRows());
                                       }
                                       view.showProductCollection(Collections.unmodifiableList(products));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(ProductCollectionPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void deleteCollection(final int index) {
        Disposable dis = api.addOrDeleteCollection(userHelper.getProfile().getId(), products.get(index).getId(), 0)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showDeleteCollectionSuccess(null);
                                       products.remove(index);
                                       view.showProductCollection(Collections.unmodifiableList(products));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(ProductCollectionPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
