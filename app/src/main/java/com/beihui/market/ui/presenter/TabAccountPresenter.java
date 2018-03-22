package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabAccountContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabAccountPresenter extends BaseRxPresenter implements TabAccountContract.Presenter {

    private Context context;
    private Api api;
    private TabAccountContract.View view;
    private UserHelper userHelper;

    private List<AccountBill> debts = new ArrayList<>();

    private double debtAmount;
    private double debtSevenDay;
    private double debtMonth;

    @Inject
    TabAccountPresenter(Context context, Api api, TabAccountContract.View view) {
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

            if (debts.size() > 0) {
                view.showDebtInfo(debtAmount, debtSevenDay, debtMonth);
                view.showInDebtList(Collections.unmodifiableList(debts));
            }
            loadDebtAbstract();
            loadInDebtList();
        } else {
            view.showNoUserLoginBlock();
        }
    }


    @Override
    public void loadDebtAbstract() {
        Disposable dis = api.fetchDebtAbstractInfo(userHelper.getProfile().getId(), 3)//获取网贷+信用卡负债摘要
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
                                logError(TabAccountPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadInDebtList() {
        Disposable dis = api.fetchAccountBills(userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity<List<AccountBill>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AccountBill>>>() {
                               @Override
                               public void accept(ResultEntity<List<AccountBill>> result) throws Exception {
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
                                logError(TabAccountPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickDebtSetStatus(int index) {
        Disposable dis = api.updateDebtStatus(userHelper.getProfile().getId(), debts.get(index).getBillId(), 2)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
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
                                logError(TabAccountPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickDebtHide(int index) {
        AccountBill bill = debts.get(index);
        Disposable dis = api.updateDebtVisibility(userHelper.getProfile().getId(), bill.getRecordId(), bill.getBillType(), 0)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
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
                                logError(TabAccountPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickDebtSync(int index) {

    }

    @Override
    public void refresh() {
        if (userHelper.getProfile() != null) {
            loadDebtAbstract();
            loadInDebtList();
        }
    }

    @Override
    public void clickAdd() {
        if (userHelper.getProfile() != null) {
            view.navigateAdd();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickAddCreditCardDebt() {
        if (userHelper.getProfile() != null) {
            view.navigateAddCreditCardDebt();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickAddLoanDebt() {
        if (userHelper.getProfile() != null) {
            view.navigateAddLoanDebt();
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
        AccountBill bill = debts.get(index);
        if (bill.getBillType() == 1) {
            view.navigateLoanDebtDetail(bill);
        } else {
            view.navigateCreditCardDebtDetail(bill);
        }
    }

    @Override
    public void clickCreditCard() {
        view.navigateCreditCardCenter();
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
