package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.LoanProductDetailContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoanDetailPresenter extends BaseRxPresenter implements LoanProductDetailContract.Presenter {

    private Api api;
    private LoanProductDetailContract.View view;
    private UserHelper userHelper;

    private LoanProductDetail productDetail;

    @Inject
    LoanDetailPresenter(Api api, LoanProductDetailContract.View view, Context context) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }


    @Override
    public void queryDetail(String id) {
        String userId = null;
        if (userHelper.getProfile() != null) {
            userId = userHelper.getProfile().getId();
        }
        Disposable dis = api.queryLoanProductDetail(id, userId)
                .compose(RxUtil.<ResultEntity<LoanProductDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProductDetail>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProductDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       productDetail = result.getData();
                                       view.showLoanDetail(result.getData());
                                   } else if (result.getCode() == 2000039) {
                                       //产品已经下架
                                       view.showLoanOffSell();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(LoanDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void addCollection(String id) {
        Disposable dis = api.addOrDeleteCollection(userHelper.getProfile().getId(), id, 1)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showAddCollectionSuccess("收藏成功");
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(LoanDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));

                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void deleteCollection(String id) {
        Disposable dis = api.addOrDeleteCollection(userHelper.getProfile().getId(), id, 0)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showDeleteCollectionSuccess("取消收藏成功");
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(LoanDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));

                            }
                        });
        addDisposable(dis);
    }
}
