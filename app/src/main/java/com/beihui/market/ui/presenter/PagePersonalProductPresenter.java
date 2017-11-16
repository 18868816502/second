package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.ui.contract.PagePersonalProductContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PagePersonalProductPresenter extends BaseRxPresenter implements PagePersonalProductContract.Presenter {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private Api api;
    private PagePersonalProductContract.View view;

    private String groupId;

    private int pageNo = 1;
    private List<LoanProduct.Row> products = new ArrayList<>();

    @Inject
    PagePersonalProductPresenter(Api api, PagePersonalProductContract.View view, String groupId) {
        this.api = api;
        this.view = view;
        this.groupId = groupId;
    }

    @Override
    public void loadGroupProduct() {
        if (products.size() > 0) {
            view.showGroupProducts(Collections.unmodifiableList(products));
        } else {
            loadMoreGroupProduct();
        }
    }

    @Override
    public void loadMoreGroupProduct() {
        Disposable dis = api.queryPersonalProducts(groupId, pageNo, DEFAULT_PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           pageNo++;
                                           products.addAll(result.getData().getRows());
                                       }
                                       view.showGroupProducts(products);
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
}
