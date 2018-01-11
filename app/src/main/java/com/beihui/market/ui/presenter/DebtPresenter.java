package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.InDebt;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.DebtContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtPresenter extends BaseRxPresenter implements DebtContract.Presenter {

    private Context context;
    private Api api;
    private DebtContract.View view;
    private UserHelper userHelper;

    private List<InDebt> debts = new ArrayList<>();

    private double debtAmount;
    private double debtSevenDay;
    private double debtMonth;

    @Inject
    DebtPresenter(Context context, Api api, DebtContract.View view) {
        this.context = context;
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (userHelper.getProfile() != null) {
            view.showUserLoginBlock();
            loadDebtAbstract();
            loadInDebtList();
        } else {
            view.showNoUserLoginBlock();
        }
    }


    @Override
    public void loadDebtAbstract() {
        Disposable dis = api.queryBaseLoan(userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity<DebtAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<DebtAbstract>>() {
                               @Override
                               public void accept(ResultEntity<DebtAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null) {
                                           debtAmount = result.getData().getCurrentDebt();
                                           debtSevenDay = result.getData().getLast7DayStayStill();
                                           debtMonth = result.getData().getLast30DayStayStill();
                                       }
                                       view.showDebtInfo(debtAmount, debtSevenDay, debtMonth);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadInDebtList() {
        Disposable dis = api.queryInDebtList(userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity<List<InDebt>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<InDebt>>>() {
                               @Override
                               public void accept(ResultEntity<List<InDebt>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debts.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           debts.addAll(result.getData());
                                       }
                                       view.showInDebtList(Collections.unmodifiableList(debts));

                                       if (debts.size() > 0) {
                                           //有数据的情况下，才显示引导
                                           if (!SPUtils.getTabAccountGuideShowed(context)) {
                                               view.showGuide();
                                               SPUtils.setTabAccountGuideShowed(context, true);
                                           }
                                       } else {
                                           view.showNoDebtListBlock();
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void updateDebtStatus(final int index) {
        Disposable dis = api.updateDebtStatus(userHelper.getProfile().getId(), debts.get(index).getTermId(), 2)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       debts.remove(index);
                                       view.showInDebtList(Collections.unmodifiableList(debts));

                                       //状态更新成功后，重新拉取数据
                                       loadDebtAbstract();
                                       loadInDebtList();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickAdd() {
        if (userHelper.getProfile() != null) {
            view.navigateAddDebt();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickCalendar() {
        if (userHelper.getProfile() != null) {
            view.navigateCalendar();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickAnalyze() {
        if (userHelper.getProfile() != null) {
            view.navigateAnalyze();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickDebt(int index) {
        view.navigateDebtDetail(debts.get(index));
    }

    @Override
    public void clickCreditCard() {
        if (userHelper.getProfile() != null) {
            view.navigateCreditCardCenter();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickEye(boolean checked) {
        if (debts.size() > 0) {
            if (checked) {
                view.hideDebtInfo();
            } else {
                view.showDebtInfo();
            }
        }
    }
}
