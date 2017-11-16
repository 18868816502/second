package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.ui.contract.PageSmartContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PageSmartPresenter extends BaseRxPresenter implements PageSmartContract.Presenter {
    private static final int PAGE_SIZE = 10;
    private String moneySelection[] = {"1000元", "3000元", "5000元", "1万元", "不限"};
    private int moneyList[] = {1000, 3000, 5000, 10000, 0};
    private String dueTimes[] = {"1个月及以下", "3个月", "6个月", "12个月", "24个月", "36个月及以上", "不限"};
    private String sortGroup[] = {"默认排序", "借款利率", "最高额度"};

    private Api api;
    private PageSmartContract.View mView;

    private int dueTimeSelected = 6;
    private int sortSelected = 0;
    private int moneySelected = 0;
    private List<LoanProduct.Row> loanProductList = new ArrayList<>();
    private int curPage = 1;

    private Disposable curTask;

    @Inject
    PageSmartPresenter(Api api, PageSmartContract.View view) {
        this.api = api;
        mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mView.showFilters(moneySelection[moneySelected], dueTimes[dueTimeSelected], sortGroup[sortSelected]);
        if (loanProductList.size() > 0) {
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), loanProductList.size() >= PAGE_SIZE);
        } else {
            refresh();
        }
    }


    @Override
    public void clickAmount(int selected) {
        if (selected != moneySelected) {
            moneySelected = selected;
            mView.showFilters(moneySelection[selected], dueTimes[dueTimeSelected], sortGroup[sortSelected]);

            loanProductList.clear();
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), false);
            refresh();
        }
    }

    @Override
    public void clickDueTime(int selected) {
        if (selected != dueTimeSelected) {
            dueTimeSelected = selected;
            mView.showFilters(moneySelection[moneySelected], dueTimes[dueTimeSelected], sortGroup[sortSelected]);

            loanProductList.clear();
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), false);
            refresh();
        }
    }

    @Override
    public void clickSort(int selected) {
        if (selected != sortSelected) {
            sortSelected = selected;
            mView.showFilters(moneySelection[moneySelected], dueTimes[dueTimeSelected], sortGroup[sortSelected]);

            loanProductList.clear();
            mView.showLoanProduct(Collections.unmodifiableList(loanProductList), false);
            refresh();
        }
    }

    @Override
    public void refresh() {
        cancelCurTask();
        curPage = 1;
        Disposable dis = api.queryLoanProduct(moneyList[moneySelected], dueTimeSelected + 1, sortSelected, curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           loanProductList.clear();
                                           loanProductList.addAll(result.getData().getRows());
                                           mView.showLoanProduct(Collections.unmodifiableList(loanProductList),
                                                   loanProductList.size() >= PAGE_SIZE);

                                       } else {
                                           mView.showNoLoanProduct();
                                       }
                                   } else {
                                       //当前没有任何原始数据，切换至网络错误状态，否则只需提示信息
                                       if (loanProductList.size() == 0) {
                                           mView.showNetError();
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(PageSmartPresenter.this, throwable);
                                //当前没有任何原始数据，切换至网络错误状态，否则只需提示信息
                                if (loanProductList.size() == 0) {
                                    mView.showNetError();
                                } else {
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            }
                        });
        curTask = dis;
        addDisposable(dis);
    }

    @Override
    public void loadMore() {
        curPage++;
        Disposable dis = api.queryLoanProduct(moneyList[moneySelected], dueTimeSelected + 1, sortSelected, curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanProduct>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanProduct>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<LoanProduct> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           loanProductList.addAll(result.getData().getRows());
                                           mView.showLoanProduct(Collections.unmodifiableList(loanProductList),
                                                   result.getData().getTotal() >= PAGE_SIZE);

                                       } else {
                                           mView.showNoMoreLoanProduct();
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                       //请求失败，回溯页数
                                       curPage--;
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(PageSmartPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                                //请求失败，回溯页数
                                curPage--;
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public String[] getMoneySelection() {
        return moneySelection;
    }

    @Override
    public int getMoneySelected() {
        return moneySelected;
    }

    @Override
    public String[] getDueTimeSelection() {
        return dueTimes;
    }

    @Override
    public int getDueTimeSelected() {
        return dueTimeSelected;
    }

    @Override
    public String[] getSortGroup() {
        return sortGroup;
    }

    @Override
    public int getSortGroupIndex() {
        return sortSelected;
    }


    private void cancelCurTask() {
        if (curTask != null && !curTask.isDisposed()) {
            curTask.dispose();
        }
    }
}
