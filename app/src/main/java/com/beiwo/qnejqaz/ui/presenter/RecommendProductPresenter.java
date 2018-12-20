package com.beiwo.qnejqaz.ui.presenter;


import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.entity.LoanProduct;
import com.beiwo.qnejqaz.ui.contract.RecommendProductContract;
import com.beiwo.qnejqaz.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RecommendProductPresenter extends BaseRxPresenter implements RecommendProductContract.Presenter {

    private Api api;
    private RecommendProductContract.View view;

    private List<LoanProduct.Row> products = new ArrayList<>();

    public RecommendProductPresenter(RecommendProductContract.View view) {
        this.api = Api.getInstance();
        this.view = view;
    }

    @Override
    public void loadRecommendProduct(int amount) {
        Disposable dis = api.queryRecommendProduct(amount)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           products.addAll(result.getData().getRows());
                                       }
                                       view.showRecommendProduct(Collections.unmodifiableList(products));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(RecommendProductPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
