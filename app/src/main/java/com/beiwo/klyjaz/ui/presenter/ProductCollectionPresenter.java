package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.LoanProduct;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.ProductCollectionContract;
import com.beiwo.klyjaz.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ProductCollectionPresenter extends BaseRxPresenter implements ProductCollectionContract.Presenter {

    private static final int PAGE_SIZE = 10;

    private Api api;
    private ProductCollectionContract.View view;
    private UserHelper userHelper;

    private List<LoanProduct.Row> products = new ArrayList<>();

    private int pageNo = 1;
    /**
     * 是否还能加载更多收藏
     */
    private boolean canLoadMore;

    @Inject
    ProductCollectionPresenter(Context context, Api api, ProductCollectionContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void loadCollection() {
        pageNo = 1;
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
                                           canLoadMore = result.getData().getRows().size() == PAGE_SIZE;
                                           view.showProductCollection(Collections.unmodifiableList(products), canLoadMore);
                                       } else {
                                           view.showNoCollection();
                                       }
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
    public void loadMoreCollection() {
        Disposable dis = api.queryProductionCollection(userHelper.getProfile().getId(), pageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo++;
                                       int size = 0;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           products.addAll(result.getData().getRows());
                                           size = result.getData().getRows().size();
                                       }
                                       canLoadMore = size == PAGE_SIZE;
                                       view.showProductCollection(Collections.unmodifiableList(products), canLoadMore);
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
                                       view.showProductCollection(Collections.unmodifiableList(products), canLoadMore);
                                       if (products.size() == 0) {
                                           view.showNoCollection();
                                       }
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
    public void clickCollection(int index) {
        view.navigateLoanDetail(products.get(index));
    }
}