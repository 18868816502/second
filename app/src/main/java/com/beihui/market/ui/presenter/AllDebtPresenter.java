package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Debt;
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

    private Api api;
    private AllDebtContract.View view;
    private int status;
    private UserHelper userHelper;

    private int count;
    private double debtAmount;
    private double capitalAmount;
    private double interestAmount;
    private List<Debt> debts = new ArrayList<>();

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

            view.showDebts(Collections.unmodifiableList(debts));
        } else {
            Disposable dis = api.queryAllDebt(userHelper.getProfile().getId(), status)
                    .compose(RxUtil.<ResultEntity<List<Debt>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<Debt>>>() {
                                   @Override
                                   public void accept(ResultEntity<List<Debt>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           debts.clear();
                                           if (result.getData() != null && result.getData().size() > 0) {
                                               debts.addAll(result.getData());
                                           }
                                           computeDebtInfo();
                                           view.showDebtInfo(count, debtAmount, capitalAmount, interestAmount);

                                           view.showDebts(Collections.unmodifiableList(debts));
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
    }

    @Override
    public void clickDebt(int index) {
        view.navigateDebtDetail(debts.get(index));
    }

    private void computeDebtInfo() {
        count = debts.size();
        debtAmount = 0;
        capitalAmount = 0;
        interestAmount = 0;
        for (Debt debt : debts) {
            capitalAmount += debt.getCapital();
            interestAmount += debt.getInterest();
        }
        debtAmount += capitalAmount + interestAmount;
    }

}
