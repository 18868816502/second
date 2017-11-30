package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.LoanProductDetailContract;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
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

                                       int detailStatus = productDetail.getBase().getDetailStatus();
                                       //未注册或者非本平台注册的用户，提示我要借款
                                       if (detailStatus == 1 || detailStatus == 3) {
                                           view.showLoanRequestText("我要借款");
                                       } else {
                                           //已在本平台注册，提示申请借款
                                           view.showLoanRequestText("已注册，申请借款");
                                       }
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
    public void clickCollection() {
        if (productDetail != null && productDetail.getBase() != null) {
            if (productDetail.getBase().isCollected()) {
                //取消收藏
                deleteCollection(productDetail.getBase().getId());
            } else {
                //添加收藏
                addCollection(productDetail.getBase().getId());
            }
        }
    }

    @Override
    public void clickLoanRequested() {
        //umeng统计
        Statistic.onEvent(Events.LOAN_DETAIL_CLICK_LOAN);
        //服务端统计
        DataStatisticsHelper.getInstance().onProductClicked(productDetail.getBase().getId());

        if (productDetail != null && productDetail.getBase() != null) {
            if (productDetail.getBase().getCoopType() == 2) {
                //常规合作的产品直接跳转第三方界面
                view.navigateThirdPartLoanPage(productDetail.getBase().getProductName(), productDetail.getBase().getUrl());
            } else {
                switch (productDetail.getBase().getDetailStatus()) {
                    case 1:
                        //联合注册产品，未注册，跳转到授权界面
                        view.navigateAuthorizationPage(productDetail.getBase().getId());
                        break;
                    case 2:
                        //联合注册产品，已通过本平台注册，跳转到第三方借款界面
                        view.navigateThirdPartLoanPage(productDetail.getBase().getProductName(), productDetail.getBase().getUrl());
                        break;
                    case 3:
                        //联合注册产品，通过非本平台注册，拒绝跳转
                        view.showLoanRequestReject();
                        break;
                    default:
                        break;
                }
            }
        }
    }


    private void addCollection(String id) {
        Disposable dis = api.addOrDeleteCollection(userHelper.getProfile().getId(), id, 1)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       //置为已收藏
                                       productDetail.getBase().setIsCollection(1);
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


    private void deleteCollection(String id) {
        Disposable dis = api.addOrDeleteCollection(userHelper.getProfile().getId(), id, 0)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       //置为未收藏
                                       productDetail.getBase().setIsCollection(0);
                                       view.showDeleteCollectionSuccess("取消收藏");
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
