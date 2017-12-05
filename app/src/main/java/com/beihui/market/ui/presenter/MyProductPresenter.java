package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.MyProduct;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.MyProductContract;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyProductPresenter extends BaseRxPresenter implements MyProductContract.Presenter {

    private static final int PAGE_SIZE = 10;

    private Api api;
    private MyProductContract.View view;
    private UserHelper userHelper;

    private List<MyProduct.Row> product = new ArrayList<>();
    private int pageNo = 1;
    private boolean canLoadMore = true;

    @Inject
    MyProductPresenter(Context context, Api api, MyProductContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void refreshMyProduct() {
        canLoadMore = true;
        pageNo = 1;

        Disposable dis = api.queryMyProduct(userHelper.getProfile().getId(), pageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<MyProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<MyProduct>>() {
                               @Override
                               public void accept(ResultEntity<MyProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo++;
                                       product.clear();
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           product.addAll(result.getData().getRows());
                                           canLoadMore = result.getData().getRows().size() == PAGE_SIZE;
                                           view.showMyProduct(Collections.unmodifiableList(product), canLoadMore);

                                           view.showSuccessCount(result.getData().getSuccess());
                                       } else {
                                           view.showMyProductEmpty();
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(MyProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadMoreMyProduct() {
        Disposable dis = api.queryMyProduct(userHelper.getProfile().getId(), pageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<MyProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<MyProduct>>() {
                               @Override
                               public void accept(ResultEntity<MyProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo++;
                                       int size = 0;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           product.addAll(result.getData().getRows());
                                           size = result.getData().getRows().size();
                                       }
                                       canLoadMore = size == PAGE_SIZE;
                                       view.showMyProduct(Collections.unmodifiableList(product), canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(MyProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickMyProduct(int position) {
        MyProduct.Row data = product.get(position);
        if (data.getStatus() == 1) {
            //注册成功
            view.navigateThirdProduct(data.getProductName(), data.getUrl());
        } else {
            view.navigateRecommendProduct(CommonUtils.convertStringAmount2Int(data.getBorrowingHighText()));
        }
    }
}
