package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.CreditCardDebtDetailContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CreditCardDebtDetailPresenter extends BaseRxPresenter implements CreditCardDebtDetailContract.Presenter {

    private final int PAGE_SIZE = 8;

    private Api api;
    private CreditCardDebtDetailContract.View view;
    private String debtId;
    private UserHelper userHelper;

    private CreditCardDebtDetail debtDetail;
    private List<CreditCardDebtBill> billList = new ArrayList<>();
    private Map<CreditCardDebtBill, List<BillDetail>> bill2Detail = new HashMap<>();
    private int curPageNo = 1;

    @Inject
    CreditCardDebtDetailPresenter(Context context, Api api, CreditCardDebtDetailContract.View view, String debtId) {
        this.api = api;
        this.view = view;
        this.debtId = debtId;
        this.userHelper = UserHelper.getInstance(context);
    }


    @Override
    public void fetchDebtDetail() {
        Disposable dis = api.fetchCreditCardDebtDetail(userHelper.getProfile().getId(), debtId)
                .compose(RxUtil.<ResultEntity<CreditCardDebtDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<CreditCardDebtDetail>>() {
                               @Override
                               public void accept(ResultEntity<CreditCardDebtDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debtDetail = result.getData();
                                       view.showDebtDetailInfo(debtDetail);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(CreditCardDebtDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchDebtMonthBill() {
        Disposable dis = api.fetchCreditCardDebtBill(userHelper.getProfile().getId(), debtId, curPageNo, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<List<CreditCardDebtBill>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<CreditCardDebtBill>>>() {
                               @Override
                               public void accept(ResultEntity<List<CreditCardDebtBill>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       curPageNo++;
                                       int size = 0;
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           billList.addAll(result.getData());
                                           size = result.getData().size();
                                       }
                                       view.showDebtBillList(Collections.unmodifiableList(billList), size == PAGE_SIZE);
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(CreditCardDebtDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchBillDetail(int dataIndex, final int viewIndex) {
        final CreditCardDebtBill bill = billList.get(dataIndex);
        if (bill2Detail.containsKey(bill)) {
            view.showBillDetail(bill2Detail.get(bill), viewIndex);
        } else {
            Disposable dis = api.fetchCreditCardBillDetail(userHelper.getProfile().getId(), bill.getId())
                    .compose(RxUtil.<ResultEntity<List<BillDetail>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<BillDetail>>>() {
                                   @Override
                                   public void accept(ResultEntity<List<BillDetail>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           List<BillDetail> list = new ArrayList<>();
                                           bill2Detail.put(bill, list);
                                           if (result.getData() != null && result.getData().size() > 0) {
                                               list.addAll(result.getData());
                                           }
                                           view.showBillDetail(list, viewIndex);
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(CreditCardDebtDetailPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void updateBillAmount(int index, double amount) {
        final CreditCardDebtBill bill = billList.get(index);
        Disposable dis = api.updateMonthBillAmount(userHelper.getProfile().getId(), bill.getId(), debtDetail.getId(), amount)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showUpdateBillAmountSuccess();
                                       //更新成功后重新拉去数据
                                       billList.clear();
                                       bill2Detail.clear();
                                       curPageNo = 1;
                                       fetchDebtDetail();
                                       fetchDebtMonthBill();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(CreditCardDebtDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickSetStatus() {
        if (debtDetail != null) {
            //设为已还
            Disposable dis = api.updateCreditCardBillStatus(userHelper.getProfile().getId(), debtDetail.getShowBill().getId(), 2)
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           view.showSetStatusSuccess();
                                           //更新成功后重新拉去数据
                                           billList.clear();
                                           bill2Detail.clear();
                                           curPageNo = 1;
                                           fetchDebtDetail();
                                           fetchDebtMonthBill();
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(CreditCardDebtDetailPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void clickMenu() {
        view.showMenu(debtDetail.getRemind() != -1);
    }

    @Override
    public void clickUpdateRemind() {
        if (debtDetail != null) {
            final int day = debtDetail.getRemind() == -1 ? 1 : -1;
            Disposable dis = api.updateRemindStatus(userHelper.getProfile().getId(), null, debtId, day)
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           debtDetail.setRemind(day);
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                       view.showUpdateRemindStatus(debtDetail.getRemind() != -1);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(CreditCardDebtDetailPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void clickEdit() {

    }

    @Override
    public void clickDelete() {
        if (debtDetail != null) {
            Disposable dis = api.deleteCreditCardDebt(userHelper.getProfile().getId(), debtDetail.getId())
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           view.showDeleteSuccess();
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(CreditCardDebtDetailPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }
}