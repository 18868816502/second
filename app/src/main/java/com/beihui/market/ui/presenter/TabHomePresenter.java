package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabHomeContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabHomePresenter extends BaseRxPresenter implements TabHomeContract.Presenter {

    private Api api;
    private TabHomeContract.View view;
    private Context context;

    private boolean hasAdInit = false;
    private NoticeAbstract notice;
    private List<AdBanner> banners = new ArrayList<>();
    private List<LoanProduct.Row> hotProducts = new ArrayList<>();
    private List<LoanProduct.Row> choiceProducts = new ArrayList<>();
    private List<HotNews> hotNews = new ArrayList<>();
    private List<String> notices = new ArrayList<>();

    @Inject
    TabHomePresenter(Api api, TabHomeContract.View view, Context context) {
        this.api = api;
        this.view = view;
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        //如果已经过初始化，则直接返回数据
        //ad dialog
        if (!hasAdInit) {
            queryAd();
        }
        //notice
        if (notice == null) {
            queryNotice();
        } else if (!SPUtils.getNoticeClosed(context)) {
            view.showNotice(notice);
        }

        //banner
        if (banners.size() == 0) {
            queryBanner();
        } else {
            view.showBanner(Collections.unmodifiableList(banners));
        }
        //hot loan products
        if (hotProducts.size() == 0) {
            queryHotLoanProducts();
        } else {
            view.showHotProducts(hotProducts);
        }
        //choice products
        if (choiceProducts.size() == 0) {
            queryChoiceProducts();
        } else {
            view.showChoiceProducts(choiceProducts);
        }
        //news
        if (hotNews.size() == 0) {
            queryHotNews();
        } else {
            view.showHotNews(hotNews);
        }
        //loan success notice
        if (notices.size() == 0) {
            queryScrolling();
        } else {
            view.showBorrowingScroll(Collections.unmodifiableList(notices));
        }
    }

    @Override
    public void refresh() {
        queryBanner();
        queryScrolling();
        queryHotLoanProducts();
        queryChoiceProducts();
        queryHotNews();
    }

    @Override
    public void refreshHotProduct() {
        queryHotLoanProducts();
    }

    @Override
    public void checkMyWorth() {
        if (UserHelper.getInstance(context).getProfile() != null) {
            view.navigateWorthTest();
        } else {
            view.navigateLogin();
        }
    }

    private void queryBanner() {
        Disposable dis = api.querySupernatant(RequestConstants.SUP_TYPE_BANNER)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           banners.clear();
                                           banners.addAll(result.getData());
                                           view.showBanner(Collections.unmodifiableList(banners));
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryAd() {
        Disposable dis = api.querySupernatant(RequestConstants.SUP_TYPE_DIALOG)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       hasAdInit = true;
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           AdBanner adBanner = result.getData().get(0);
                                           String lastId = SPUtils.getLastDialogAdId(context);
                                           //同一个广告只显示一次
                                           if (lastId == null || !lastId.equals(adBanner.getLocalId())) {
                                               view.showAdDialog(result.getData().get(0));
                                           }
                                           //记录已展示的id
                                           SPUtils.setLastDialogAdId(context, adBanner.getLocalId());
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryScrolling() {
        Disposable dis = api.queryBorrowingScroll()
                .compose(RxUtil.<ResultEntity<List<String>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<String>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<String>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           notices.clear();
                                           notices.addAll(result.getData());
                                           view.showBorrowingScroll(Collections.unmodifiableList(notices));
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                handleThrowable(throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void queryHotNews() {
        Disposable dis = api.queryHotNews()
                .compose(RxUtil.<ResultEntity<List<HotNews>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<HotNews>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<HotNews>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           hotNews.clear();
                                           hotNews.addAll(result.getData());
                                           view.showHotNews(Collections.unmodifiableList(hotNews));
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void queryHotLoanProducts() {
        Disposable dis = api.queryHotLoanProducts()
                .compose(RxUtil.<ResultEntity<List<LoanProduct.Row>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<LoanProduct.Row>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<LoanProduct.Row>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           hotProducts.clear();
                                           hotProducts.addAll(result.getData());
                                           view.showHotProducts(Collections.unmodifiableList(hotProducts));
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void queryChoiceProducts() {
        Disposable dis = api.queryHotLoanProducts()
                .compose(RxUtil.<ResultEntity<List<LoanProduct.Row>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<LoanProduct.Row>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<LoanProduct.Row>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           choiceProducts.clear();
                                           choiceProducts.addAll(result.getData());
                                           view.showChoiceProducts(Collections.unmodifiableList(choiceProducts));
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void queryNotice() {
        Disposable dis = api.queryNoticeHome()
                .compose(RxUtil.<ResultEntity<NoticeAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<NoticeAbstract>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<NoticeAbstract> result) throws Exception {
                                   if (result.isSuccess() && result.getData() != null) {
                                       notice = result.getData();
                                       if (notice.getId() != null) {
                                           if (!notice.getId().equals(SPUtils.getLastNoticeId(context))) {
                                               view.showNotice(notice);
                                               SPUtils.setNoticeClosed(context, false);
                                           } else if (!SPUtils.getNoticeClosed(context)) {
                                               view.showNotice(notice);
                                           }
                                           SPUtils.setLastNoticeId(context, notice.getId());
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(TabHomePresenter.this, throwable);
                            }
                        });
        addDisposable(dis);
    }

    private void handleThrowable(Throwable throwable) {
        logError(TabHomePresenter.this, throwable);
        view.showErrorMsg(generateErrorMsg(throwable));

        if (isAllDataEmpty()) {
            view.showError();
        }
    }

    private boolean isAllDataEmpty() {
        return notice == null
                && banners.size() == 0
                && notices.size() == 0
                && hotNews.size() == 0
                && hotProducts.size() == 0
                && choiceProducts.size() == 0;
    }
}
