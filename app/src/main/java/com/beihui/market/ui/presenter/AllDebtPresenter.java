package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AllDebt;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.AllDebtContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AllDebtPresenter extends BaseRxPresenter implements AllDebtContract.Presenter {

    private static final int PAGE_SIZE = 10;

    private Api api;
    private AllDebtContract.View view;
    private int status;
    private UserHelper userHelper;

    private int count;
    private double debtAmount;
    private double capitalAmount;
    private double interestAmount;
    private List<AllDebt.Row> debts = new ArrayList<>();

    private int curPageNo = 1;
    private boolean canLoadMore;

    @Inject
    AllDebtPresenter(Context context, Api api, AllDebtContract.View view, int status) {
        this.api = api;
        this.view = view;
        this.status = status;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void loadDebts() {
        if (debts.size() > 0) {
            view.showDebtInfo(count, debtAmount, capitalAmount, interestAmount);
            view.showDebts(Collections.unmodifiableList(debts), canLoadMore);
        } else {
            loadMoreDebts();
        }
    }

    @Override
    public void loadMoreDebts() {
        Disposable dis = api.queryAllDebt(userHelper.getProfile().getId(), status, curPageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<AllDebt>>io2main())
                .subscribe(new Consumer<ResultEntity<AllDebt>>() {
                               @Override
                               public void accept(ResultEntity<AllDebt> result) throws Exception {
                                   if (result.isSuccess()) {
                                       curPageNo++;

                                       int size = 0;
                                       if (result.getData() != null) {
                                           AllDebt allDebt = result.getData();
                                           count = allDebt.getTotal();
                                           debtAmount = allDebt.getPayableAmount();
                                           capitalAmount = allDebt.getCapital();
                                           interestAmount = allDebt.getInterest();

                                           if (allDebt.getRows() != null && allDebt.getRows().size() > 0) {
                                               debts.addAll(allDebt.getRows());
                                               size = allDebt.getRows().size();
                                           }
                                       }
                                       view.showDebtInfo(count, debtAmount, capitalAmount, interestAmount);

                                       canLoadMore = size >= PAGE_SIZE;
                                       view.showDebts(Collections.unmodifiableList(debts), canLoadMore);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(AllDebtPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickDebt(int index) {
        view.navigateDebtDetail(debts.get(index));
    }

    @Override
    public void debtDeleted(String debtId) {
        AllDebt.Row deleteDebt = null;
        for (int i = 0; i < debts.size(); ++i) {
            AllDebt.Row row = debts.get(i);
            if (row.getId().equals(debtId)) {
                deleteDebt = row;
                debts.remove(row);
            }
        }
        if (deleteDebt != null) {
            count -= 1;
            debtAmount -= deleteDebt.getPayableAmount();
            capitalAmount -= deleteDebt.getCapital();
            interestAmount -= deleteDebt.getInterest();

            view.showDebtInfo(count, debtAmount, capitalAmount, interestAmount);

            view.showDebts(Collections.unmodifiableList(debts), canLoadMore);
        }
    }


}
