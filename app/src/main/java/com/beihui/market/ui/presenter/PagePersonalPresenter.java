package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanGroup;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.ui.contract.PagePersonalContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PagePersonalPresenter extends BaseRxPresenter implements PagePersonalContract.Presenter {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private PagePersonalContract.View view;
    private Api api;

    private List<String> productHints = new ArrayList<>();
    private List<LoanGroup> groups = new ArrayList<>();
    private Map<String, List<LoanProduct.Row>> group2products = new HashMap<>();
    private Map<String, Integer> group2pageNo = new HashMap<>();

    @Inject
    PagePersonalPresenter(Api api, PagePersonalContract.View view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void loadProductHint() {
        Disposable dis = api.queryLoanHint()
                .compose(RxUtil.<ResultEntity<List<String>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<String>>>() {
                               @Override
                               public void accept(ResultEntity<List<String>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       productHints.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           productHints.addAll(result.getData());
                                       }
                                       view.showProductHint(productHints);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(PagePersonalPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));

                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadProductGroup() {
        if (groups.size() > 0) {
            view.showProductGroup(groups);
        } else {
            Disposable dis = api.queryLoanGroup()
                    .compose(RxUtil.<ResultEntity<List<LoanGroup>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<LoanGroup>>>() {
                                   @Override
                                   public void accept(ResultEntity<List<LoanGroup>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           groups.clear();
                                           if (result.getData() != null && result.getData().size() > 0) {
                                               groups.addAll(result.getData());
                                           }
                                           view.showProductGroup(groups);
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }

                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(PagePersonalPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

}
