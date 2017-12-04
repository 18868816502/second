package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.PagePersonalProductContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PagePersonalProductPresenter extends BaseRxPresenter implements PagePersonalProductContract.Presenter {

    private static final int PAGE_SIZE = 10;

    private Api api;
    private PagePersonalProductContract.View view;
    private UserHelper userHelper;

    private String groupId;

    private int pageNo = 1;
    private List<LoanProduct.Row> products = new ArrayList<>();

    /**
     * 该组产品是否还能加载更多
     */
    private boolean canLoadMore = true;

    @Inject
    PagePersonalProductPresenter(Context context, Api api, PagePersonalProductContract.View view, String groupId) {
        this.api = api;
        this.view = view;
        this.groupId = groupId;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void refresh() {
        String userId = null;
        if (userHelper.getProfile() != null) {
            userId = userHelper.getProfile().getId();
        }
        Disposable dis = api.queryPersonalProducts(groupId, userId, 1, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       pageNo = 2;
                                       products.clear();
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           products.addAll(result.getData().getRows());
                                           canLoadMore = products.size() == PAGE_SIZE;
                                           view.showGroupProducts(Collections.unmodifiableList(products), canLoadMore);
                                       } else {
                                           view.showNoGroupProducts();
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                       view.showNetError();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(PagePersonalProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                                view.showNetError();
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadGroupProduct() {
        if (products.size() > 0) {
            view.showGroupProducts(Collections.unmodifiableList(products), canLoadMore);
        } else {
            refresh();
        }
    }

    @Override
    public void loadMoreGroupProduct() {
        String userId = null;
        if (userHelper.getProfile() != null) {
            userId = userHelper.getProfile().getId();
        }
        Disposable dis = api.queryPersonalProducts(groupId, userId, pageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       int size = 0;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           pageNo++;
                                           products.addAll(result.getData().getRows());
                                           size = result.getData().getRows().size();
                                       }
                                       canLoadMore = size == PAGE_SIZE;
                                       view.showGroupProducts(products, canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(PagePersonalProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickProduct(int position) {
        if (userHelper.getProfile() != null) {
            view.navigateProductDetail(products.get(position));
        } else {
            view.navigateLogin();
        }
    }
}
