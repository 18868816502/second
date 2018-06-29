package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.LoanBill;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.MyLoanBillContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyLoanBillPresenter extends BaseRxPresenter implements MyLoanBillContract.Presenter {

    private final int PAGE_SIZE = 12;

    private Api api;
    private MyLoanBillContract.View view;
    private UserHelper userHelper;

    private int loanBillCount;
    public List<LoanBill.Row> loanBillList = new ArrayList<>();

    private boolean canLoadMore;
    public int loanCurPage = 1;
    public int fastCurPage = 1;
    public int creditCardCurPage = 1;

    @Inject
    MyLoanBillPresenter(Context context, Api api, MyLoanBillContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void fetchLoanBill(final int billTyp) {
        int page;
        if (billTyp == 3) {
            //快捷记账
            page = fastCurPage;
        } else if (billTyp == 1) {
            //网贷记账
            page = loanCurPage;
        } else {
            page = creditCardCurPage;
        }
        Disposable dis = api.fetchMyLoanBill(userHelper.getProfile().getId(), billTyp, page, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<LoanBill>>io2main())
                .subscribe(new Consumer<ResultEntity<LoanBill>>() {
                               @Override
                               public void accept(ResultEntity<LoanBill> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (billTyp == 1) {
                                           loanCurPage++;
                                       } else if (billTyp == 3){
                                           fastCurPage++;
                                       } else {
                                           creditCardCurPage++;
                                       }
                                       int size = 0;
                                       if (result.getData() != null) {
                                           loanBillCount = result.getData().getTotal();
                                           view.showLoanBillCount(result.getData().getTotal());
                                           if (result.getData().getRows() != null && result.getData().getRows().size() > 0) {
                                               loanBillList.addAll(result.getData().getRows());
                                               size = result.getData().getRows().size();
                                           }
                                       }
                                       canLoadMore = size == PAGE_SIZE;
                                       view.showLoanBill(Collections.unmodifiableList(loanBillList), canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(MyLoanBillPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickLoanBill(int index) {
        LoanBill.Row loanBill = loanBillList.get(index);
        if (loanBill.getBillType() == 1) {
            view.navigateLoanDebtDetail(loanBill);
        } else if (loanBill.getBillType() == 2){
            view.navigateBillDebtDetail(loanBill);
        } else {
            view.navigateFastDebtDetail(loanBill);
        }
    }

    @Override
    public void clickShowHideDebt(int index) {
        final LoanBill.Row bill = loanBillList.get(index);
        final int hide = bill.getHide() == 0 ? 1 : 0;
        Disposable dis = api.updateDebtVisibility(userHelper.getProfile().getId(), bill.getRecordId(), bill.getBillType(), hide)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       bill.setHide(hide);
                                       view.showLoanBill(Collections.unmodifiableList(loanBillList), canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(MyLoanBillPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void debtDeleted(String debtId) {
        if (loanBillList.size() > 0) {
            LoanBill.Row deletedBill = null;
            for (LoanBill.Row bill : loanBillList) {
                if (bill.getRecordId().equals(debtId)) {
                    deletedBill = bill;
                    break;
                }
            }
            if (deletedBill != null) {
                loanBillList.remove(deletedBill);
                view.showLoanBill(Collections.unmodifiableList(loanBillList), canLoadMore);

                loanBillCount--;
                view.showLoanBillCount(loanBillCount);
            }
        }
    }
}
