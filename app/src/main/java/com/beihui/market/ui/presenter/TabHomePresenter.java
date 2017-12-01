package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.HotLoanProduct;
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

    private static final int PAGE_SIZE = 20;

    private Api api;
    private TabHomeContract.View view;
    private Context context;

    private boolean hasAdInit = false;
    private NoticeAbstract notice;
    private List<AdBanner> banners = new ArrayList<>();
    private List<LoanProduct.Row> hotProducts = new ArrayList<>();
    private List<LoanProduct.Row> choiceProducts = new ArrayList<>();
    private List<HotNews> hotNews = new ArrayList<>();
    private List<String> headlines = new ArrayList<>();
    /**
     * 一键借款是否显示
     */
    private boolean oneKeyLoanVisible = false;

    /**
     * 精选产品是否还能加载更多
     */
    private boolean canChoiceLoadMore;

    /**
     * 热门产品查询页，初始值1，之后依据server传回的值更新
     */
    private int hotProductPageNo = 1;
    /**
     * 精选产品查询页
     */
    private int choiceProductPageNo = 1;

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
            loadBanner();
        } else {
            view.showBanner(Collections.unmodifiableList(banners));
        }
        //一键借款状态
        view.updateOneKeyLoanVisibility(oneKeyLoanVisible);
        //hot products
        if (hotProducts.size() == 0) {
            refreshHotProducts();
        } else {
            view.showHotProducts(hotProducts);
        }
        //choice products
        if (choiceProducts.size() == 0) {
            loadChoiceProducts(true);
        } else {
            view.showChoiceProducts(choiceProducts, canChoiceLoadMore);
        }
        //news
        if (hotNews.size() == 0) {
            loadHotNews();
        } else {
            view.showHotNews(hotNews);
        }
        //headlines
        if (headlines.size() == 0) {
            loadHeadline();
        } else {
            view.showHeadline(Collections.unmodifiableList(headlines));
        }
    }

    @Override
    public void refresh() {
        loadBanner();
        loadHeadline();
        //刷新时，热门产品刷新到第一页
        hotProductPageNo = 1;
        refreshHotProducts();
        //刷新时，精选产品刷到到第一页
        choiceProductPageNo = 1;
        canChoiceLoadMore = true;
        loadChoiceProducts(true);
        loadHotNews();
    }

    @Override
    public void loadBanner() {
        Disposable dis = api.querySupernatant(RequestConstants.SUP_TYPE_BANNER)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       banners.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           banners.addAll(result.getData());
                                       }
                                       view.showBanner(Collections.unmodifiableList(banners));
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

    @Override
    public void loadHeadline() {
        Disposable dis = api.queryBorrowingScroll()
                .compose(RxUtil.<ResultEntity<List<String>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<String>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<String>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       headlines.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           headlines.addAll(result.getData());
                                       }
                                       view.showHeadline(Collections.unmodifiableList(headlines));
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

    @Override
    public void refreshHotProducts() {
        Disposable dis = api.queryHotProduct(hotProductPageNo)
                .compose(RxUtil.<ResultEntity<HotLoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<HotLoanProduct>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<HotLoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       hotProducts.clear();
                                       if (result.getData() != null) {
                                           //下次刷新的页码
                                           hotProductPageNo = result.getData().getPageNo();
                                           //一键借款是否显示
                                           oneKeyLoanVisible = result.getData().getButton() == 1;
                                           hotProducts.addAll(result.getData().getRows());
                                       }
                                       view.showHotProducts(Collections.unmodifiableList(hotProducts));
                                       view.updateOneKeyLoanVisibility(oneKeyLoanVisible);
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

    @Override
    public void loadMoreChoiceProducts() {
        loadChoiceProducts(false);
    }

    @Override
    public void loadHotNews() {
        Disposable dis = api.queryHotNews()
                .compose(RxUtil.<ResultEntity<List<HotNews>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<HotNews>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<HotNews>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       hotNews.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           hotNews.addAll(result.getData());
                                       }
                                       view.showHotNews(Collections.unmodifiableList(hotNews));
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

    @Override
    public void clickQualityTest() {
        if (UserHelper.getInstance(context).getProfile() != null) {
            view.navigateWorthTest();
        } else {
            view.navigateLogin();
        }
    }

    @Override
    public void clickBanner(int position) {
        AdBanner adBanner = banners.get(position);
        //需要先登录
        if (adBanner.needLogin()) {
            if (UserHelper.getInstance(context).getProfile() == null) {
                view.navigateLoginWithPending(adBanner);
                return;
            }
        }
        //跳原生还是跳Web
        if (adBanner.isNative()) {
            view.navigateProductDetail(null, adBanner.getLocalId());
        } else if (!TextUtils.isEmpty(adBanner.getUrl())) {
            //url地址不为空才跳转
            view.navigateWeb(adBanner.getTitle(), adBanner.getUrl());
        }

    }

    @Override
    public void clickHotProduct(int position) {
        if (UserHelper.getInstance(context).getProfile() != null) {
            view.navigateProductDetail(hotProducts.get(position), null);
        } else {
            view.navigateLogin();
        }
    }

    @Override
    public void clickOneKeyLoan() {
        if (hotProducts != null && hotProducts.size() > 0) {
            final String[] ids = new String[hotProducts.size()];
            for (int i = 0; i < hotProducts.size(); ++i) {
                ids[i] = hotProducts.get(i).getId();
            }

            Disposable dis = api.queryOneKeyLoanQuality(ids)
                    .compose(RxUtil.<ResultEntity<Integer>>io2main())
                    .subscribe(new Consumer<ResultEntity<Integer>>() {
                                   @Override
                                   public void accept(ResultEntity<Integer> result) throws Exception {
                                       if (result.isSuccess()) {
                                           //有数据
                                           if (result.getData() == 1) {
                                               view.navigateThirdAuthorization(ids);
                                           } else {
                                               //无数据
                                               view.navigateChoiceProduct();
                                           }
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(TabHomePresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void clickChoiceProduct(int position) {
        if (UserHelper.getInstance(context).getProfile() != null) {
            view.navigateProductDetail(choiceProducts.get(position), null);
        } else {
            view.navigateLogin();
        }
    }

    /**
     * 加载精选产品
     *
     * @param refresh 是否是刷新动作，如果是，则先清除原数据，否则就直接添加新数据
     */
    private void loadChoiceProducts(boolean refresh) {
        if (refresh) {
            choiceProducts.clear();
        }
        Disposable dis = api.queryChoiceProduct(choiceProductPageNo, PAGE_SIZE, 0) //正序查询
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       choiceProductPageNo++;
                                       int size = 0;
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           choiceProducts.addAll(result.getData().getRows());
                                           size = result.getData().getRows().size();
                                       }
                                       canChoiceLoadMore = size == PAGE_SIZE;
                                       view.showChoiceProducts(Collections.unmodifiableList(choiceProducts), canChoiceLoadMore);
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
                                           //距离上次展示时间超过设定的间隔才显示广告
                                           if (System.currentTimeMillis() - SPUtils.getLastAdShowTime(context)
                                                   > (adBanner.getEndTime() - adBanner.getBeginTime()) / adBanner.getShowTimes()) {
                                               view.showAdDialog(result.getData().get(0));
                                           }
                                           //更新广告展示时间
                                           SPUtils.setLastAdShowTime(context, System.currentTimeMillis());
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
                && headlines.size() == 0
                && hotNews.size() == 0
                && hotProducts.size() == 0
                && choiceProducts.size() == 0;
    }
}
