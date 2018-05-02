package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.request.XAccountInfo;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabAccountContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 * 账单模块 首页
 */
public class TabAccountPresenter extends BaseRxPresenter implements TabAccountContract.Presenter {

    private Context context;
    private Api api;
    private TabAccountContract.View view;
    private UserHelper userHelper;

    private List<XAccountInfo> debts = new ArrayList<>();

    //账单 头信息
    private DebtAbstract anAbstract;


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
                view.showDebtInfo(anAbstract);
                view.showInDebtList(debts, 0);
            }
            //获取头信息
            loadDebtAbstract();
            //获取列表信息
            loadInDebtList(0, true, 1, 10);
        } else {
            view.showNoUserLoginBlock();
        }
//        //获取头信息
//        loadDebtAbstract();
//        //获取列表信息
//        loadInDebtList(0, true, 1, 10);
    }


    /**
     * 获取头信息
     */
    @Override
    public void loadDebtAbstract() {
        Disposable dis = api.queryTabAccountHeaderInfo(userHelper.getProfile().getId(), 3)//获取网贷+信用卡负债摘要
                .compose(RxUtil.<ResultEntity<DebtAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<DebtAbstract>>() {
                               @Override
                               public void accept(ResultEntity<DebtAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null) {
                                           anAbstract = result.getData();
                                       }
                                       view.showDebtInfo(result.getData());
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

    /**
     * 获取列表信息
     */
    @Override
    public void loadInDebtList(final int billStatus, boolean firstScreen, int pageNo, int pageSize) {
        Observable<ResultEntity<List<XAccountInfo>>> observable;
        if (firstScreen) {
            observable = api.queryTabAccountListInfo(userHelper.getProfile().getId(), true);
        } else {
            observable = api.queryTabAccountListInfo(userHelper.getProfile().getId(), billStatus, false, pageNo, pageSize);
        }
        Disposable dis =observable.compose(RxUtil.<ResultEntity<List<XAccountInfo>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<XAccountInfo>>>() {
                               @Override
                               public void accept(ResultEntity<List<XAccountInfo>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debts.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           debts.addAll(result.getData());
                                       }
                                       view.showInDebtList(debts, billStatus);
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
//        AccountBill bill = debts.get(index);
//        if (bill.getBillType() == 1) {
//            //网贷账单
//            Disposable dis = api.updateDebtStatus(userHelper.getProfile().getId(), bill.getBillId(), 2)
//                    .compose(RxUtil.<ResultEntity>io2main())
//                    .subscribe(new Consumer<ResultEntity>() {
//                                   @Override
//                                   public void accept(ResultEntity result) throws Exception {
//                                       if (result.isSuccess()) {
//                                           //状态更新成功后，重新拉取数据
//                                           loadDebtAbstract();
//                                           loadInDebtList();
//                                       } else {
//                                           view.showErrorMsg(result.getMsg());
//                                       }
//                                   }
//                               },
//                            new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    logError(TabAccountPresenter.this, throwable);
//                                    view.showErrorMsg(generateErrorMsg(throwable));
//                                }
//                            });
//            addDisposable(dis);
//        } else {
//            //信用卡账单
//            Disposable dis = api.updateCreditCardBillStatus(userHelper.getProfile().getId(), bill.getRecordId(), bill.getBillId(), 2)
//                    .compose(RxUtil.<ResultEntity>io2main())
//                    .subscribe(new Consumer<ResultEntity>() {
//                                   @Override
//                                   public void accept(ResultEntity result) throws Exception {
//                                       if (result.isSuccess()) {
//                                           //状态更新成功后，重新拉取数据
//                                           loadDebtAbstract();
//                                           loadInDebtList();
//                                       } else {
//                                           view.showErrorMsg(result.getMsg());
//                                       }
//                                   }
//                               },
//                            new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    logError(TabAccountPresenter.this, throwable);
//                                    view.showErrorMsg(generateErrorMsg(throwable));
//                                }
//                            });
//            addDisposable(dis);
//        }
    }

    @Override
    public void clickDebtHide(int index) {
//        AccountBill bill = debts.get(index);
//        Disposable dis = api.updateDebtVisibility(userHelper.getProfile().getId(), bill.getRecordId(), bill.getBillType(), 0)
//                .compose(RxUtil.<ResultEntity>io2main())
//                .subscribe(new Consumer<ResultEntity>() {
//                               @Override
//                               public void accept(ResultEntity result) throws Exception {
//                                   if (result.isSuccess()) {
//                                       //状态更新成功后，重新拉取数据
//                                       loadDebtAbstract();
//                                       loadInDebtList();
//                                   } else {
//                                       view.showErrorMsg(result.getMsg());
//                                   }
//                               }
//                           },
//                        new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                logError(TabAccountPresenter.this, throwable);
//                                view.showErrorMsg(generateErrorMsg(throwable));
//                            }
//                        });
//        addDisposable(dis);
    }

    @Override
    public void clickDebtSync(int index) {
        view.navigateVisaLeadingIn();
    }

    @Override
    public void refresh() {
        if (userHelper.getProfile() != null) {
            loadDebtAbstract();
//            loadInDebtList(3, false, 1, 10);
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
//        AccountBill bill = debts.get(index);
//        if (bill.getBillType() == 1) {
//            view.navigateLoanDebtDetail(bill);
//        } else {
//            view.navigateCreditCardDebtDetail(bill);
//        }
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
