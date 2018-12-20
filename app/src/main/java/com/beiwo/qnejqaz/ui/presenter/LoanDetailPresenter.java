package com.beiwo.qnejqaz.ui.presenter;


import android.content.Context;

import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.entity.LoanProductDetail;
import com.beiwo.qnejqaz.entity.ThirdAuthResult;
import com.beiwo.qnejqaz.entity.ThirdAuthorization;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.contract.LoanProductDetailContract;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class LoanDetailPresenter extends BaseRxPresenter implements LoanProductDetailContract.Presenter {

    private Api api;
    private LoanProductDetailContract.View view;
    private UserHelper userHelper;
    private Context mContext;

    private LoanProductDetail productDetail;

    public LoanDetailPresenter(LoanProductDetailContract.View view, Context context) {
        this.api = Api.getInstance();
        this.view = view;
        userHelper = UserHelper.getInstance(context);
        this.mContext = context;
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

        if (productDetail != null && productDetail.getBase() != null) {
            //服务端统计
            DataHelper.getInstance(mContext).onProductClicked(productDetail.getBase().getId());


            if (productDetail.getBase().getCoopType() == 1) {
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
                        view.navigateRecommendProduct(CommonUtils.convertStringAmount2Int(productDetail.getBase().getBorrowingHighText()));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void queryThirdAuthorization() {
        Disposable dis = api.queryThirdAuthorization(userHelper.getProfile().getId(), productDetail.getBase().getId())
                .compose(RxUtil.<ResultEntity<List<ThirdAuthorization>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<ThirdAuthorization>>>() {
                               @Override
                               public void accept(ResultEntity<List<ThirdAuthorization>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           view.showThirdAuthorization(result.getData().get(0));
                                       }
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
    public void clickConfirmAuthorize() {
        //开启等待，进度条
        view.updateRegisterDialogVisibility(true);
        final String userId = userHelper.getProfile().getId();
        final String pids = productDetail.getBase().getId();

        api.authorize(userHelper.getProfile().getId(), productDetail.getBase().getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .filter(new Predicate<ResultEntity>() {
                    @Override
                    public boolean test(ResultEntity result) throws Exception {
                        if (!result.isSuccess()) {
                            //请求失败
                            view.updateRegisterDialogVisibility(false);
                            view.showErrorMsg(result.getMsg());
                        }
                        return result.isSuccess();
                    }
                })
                //开启轮询，间隔1秒钟
                .flatMap(new Function<ResultEntity, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(ResultEntity resultEntity) throws Exception {
                        return Observable.interval(1, TimeUnit.SECONDS);
                    }
                })
                //查询授权结果
                .flatMap(new Function<Long, ObservableSource<ResultEntity<ThirdAuthResult>>>() {
                    @Override
                    public ObservableSource<ResultEntity<ThirdAuthResult>> apply(Long aLong) throws Exception {
                        //3秒超时
                        if (aLong <= 3) {
                            return api.authorizationResult(userId, pids);
                        } else {
                            //轮询最长5秒钟，超时后当做注册失败处理
                            ThirdAuthResult.Row row = new ThirdAuthResult.Row();
                            row.setStatus(4);
                            List<ThirdAuthResult.Row> list = new ArrayList<>();
                            list.add(row);

                            ThirdAuthResult thirdAuthResult = new ThirdAuthResult();
                            thirdAuthResult.setRows(list);


                            ResultEntity<ThirdAuthResult> entity = new ResultEntity<>();
                            entity.setCode(1000000);
                            entity.setData(thirdAuthResult);

                            //Log.e("LoanDetailPresenter", "轮询超时，失败处理");
                            return Observable.just(entity);
                        }
                    }
                })
                .compose(RxUtil.<ResultEntity<ThirdAuthResult>>io2main())
                .subscribe(new Observer<ResultEntity<ThirdAuthResult>>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        addDisposable(disposable);
                    }

                    @Override
                    public void onNext(ResultEntity<ThirdAuthResult> result) {
                        if (result.isSuccess()) {
                            if (result.getData() != null && result.getData().getRows() != null
                                    && result.getData().getRows().size() > 0) {
                                ThirdAuthResult.Row res = result.getData().getRows().get(0);
                                switch (res.getStatus()) {
                                    case 1://注册成功
                                        view.updateRegisterDialogVisibility(false);
                                        view.showAuthorizeResult(true, null);

                                        view.navigateThirdPartLoanPage(productDetail.getBase().getProductName(), productDetail.getBase().getUrl());

                                        //结束流程，刷新数据
                                        disposable.dispose();
                                        onComplete();
                                        break;
                                    case 2://已注册
                                        view.updateRegisterDialogVisibility(false);
                                        view.showAuthorizeResult(false, null);

                                        view.navigateRecommendProduct(CommonUtils.convertStringAmount2Int(productDetail.getBase().getBorrowingHighText()));

                                        //结束流程，刷新数据
                                        disposable.dispose();
                                        onComplete();
                                        break;
                                    case 3://注册中
                                        break;
                                    case 4://注册失败
                                        view.updateRegisterDialogVisibility(false);
                                        view.showAuthorizeResult(false, "注册失败，请稍后再试");

                                        //结束流程，刷新数据
                                        disposable.dispose();
                                        onComplete();
                                    default:
                                        break;
                                }
                            }
                        } else {
                            //如果出现失败，则停止流程
                            disposable.dispose();
                            view.updateRegisterDialogVisibility(false);
                            view.showErrorMsg(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        logError(LoanDetailPresenter.this, e);
                        view.updateRegisterDialogVisibility(false);
                        view.showErrorMsg(generateErrorMsg(e));
                    }

                    @Override
                    public void onComplete() {
                        view.updateRegisterDialogVisibility(false);
                        //刷新详情数据
                        queryDetail(productDetail.getBase().getId());
                    }
                });
    }


    private void addCollection(String id) {
        api.addOrDeleteCollection(userHelper.getProfile().getId(), id, 1)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        //置为已收藏
                        productDetail.getBase().setIsCollection(1);
                        view.showAddCollectionSuccess("收藏成功");
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        view.showErrorMsg(generateErrorMsg(t));
                    }
                });
    }

    private void deleteCollection(String id) {
        api.addOrDeleteCollection(userHelper.getProfile().getId(), id, 0)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        //置为未收藏
                        productDetail.getBase().setIsCollection(0);
                        view.showDeleteCollectionSuccess("取消收藏");
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        view.showErrorMsg(generateErrorMsg(t));
                    }
                });
    }
}