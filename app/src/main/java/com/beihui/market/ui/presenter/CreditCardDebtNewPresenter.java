package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.CreditCardDebtNewContract;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class CreditCardDebtNewPresenter extends BaseRxPresenter implements CreditCardDebtNewContract.Presenter {

    private Api api;
    private CreditCardDebtNewContract.View view;
    private UserHelper userHelper;
    /**
     * 如果该字段不为空，则当前处于编辑模式
     */
    private CreditCardDebtDetail debtDetail;

    /**
     * 添加账单的流程是否已经成功
     */
    private boolean hasDebtBeenAdded;

    @Inject
    CreditCardDebtNewPresenter(Context context, Api api, CreditCardDebtNewContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void attachCreditCardDebt(CreditCardDebtDetail debtDetail) {
        this.debtDetail = debtDetail;
        if (this.debtDetail != null) {
            view.bindOldCreditCardDebt(this.debtDetail);
        }
    }

    @Override
    public void saveCreditCardDebt(final String cardNums, String bankId, final String realName, final int billDay, final int dueDay, String amount) {
        if (TextUtils.isEmpty(cardNums)) {
            view.showErrorMsg("请输入信用卡后4位");
            return;
        }
        if (TextUtils.isEmpty(bankId)) {
            view.showErrorMsg("请选择所属银行");
            return;
        }
        if (TextUtils.isEmpty(realName)) {
            view.showErrorMsg("请输入姓名");
            return;
        }
        if (billDay == 0) {
            view.showErrorMsg("请选择账单日");
            return;
        }
        if (dueDay == 0) {
            view.showErrorMsg("请选择还款日");
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            view.showErrorMsg("请输入账单金额");
            return;
        } else {
            if (Double.parseDouble(amount) <= 0) {
                view.showErrorMsg("账单金额必须大于0");
                return;
            }
        }


        double debtAmount = 0;
        long creditCardBankId = 0;
        try {
            debtAmount = Double.parseDouble(amount);
            creditCardBankId = Long.parseLong(bankId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        final double paramAmount = debtAmount;
        final long paramBankId = creditCardBankId;

        view.showProgress();
        Disposable dis = api.saveCreditCardDebt(userHelper.getProfile().getId(), cardNums, paramBankId, realName, billDay, dueDay, paramAmount)
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResultEntity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(ResultEntity result) throws Exception {
                        //保存账单不成功
                        if (!result.isSuccess()) {
                            view.dismissProgress();
                            view.showErrorMsg(result.getMsg());
                        }
                        //添加账单流程是否已成功走完
                        hasDebtBeenAdded = result.isSuccess();
                        return Observable.just(result.isSuccess());
                    }
                })
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        //如果保存账单成功，则查询积分任务，否者结束流程
                        return aBoolean;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                    @Override
                    public ObservableSource<ResultEntity<List<RewardPoint>>> apply(Boolean aBoolean) throws Exception {
                        //如果保存成功，则查询积分任务状态
                        return api.queryRewardPoints(userHelper.getProfile().getId(), Constant.REWARD_POINTS_TASK_NAME_ADD_DEBT);
                    }
                })
                .compose(RxUtil.<ResultEntity<List<RewardPoint>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<RewardPoint>>>() {
                               @Override
                               public void accept(ResultEntity<List<RewardPoint>> result) throws Exception {
                                   //还款计划保存成功
                                   String msg = "记账成功";
                                   if (result.isSuccess()) {
                                       //检查积分任务数据
                                       List<RewardPoint> list = result.getData();
                                       int points = 0;
                                       if (list != null && list.size() > 0) {
                                           for (RewardPoint point : list) {
                                               //需要弹框
                                               if (point.getFlag() == 1) {
                                                   points += point.getInteg();
                                                   if (Constant.REWARD_POINTS_TASK_NAME_ADD_DEBT_FIRST.equals(point.getTaskName())) {
                                                       msg = "首次记账";
                                                   }
                                                   //设置已读状态
                                                   sendPoint(point.getRecordId());
                                               }
                                           }
                                       }
                                       if (points > 0) {
                                           msg += " 积分+" + points;
                                       }
                                   }
                                   view.dismissProgress();

                                   view.showSaveCreditCardDebtSuccess(msg);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.dismissProgress();
                                logError(CreditCardDebtNewPresenter.this, throwable);
                                //如果添加账单的流程已经走完，则表示账单已添加，做成功提示
                                if (hasDebtBeenAdded) {
                                    view.showSaveCreditCardDebtSuccess("记账成功");
                                } else {
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void updateCreditCardDebt(int billDay, int dueDay, String amount) {
        if (TextUtils.isEmpty(amount)) {
            view.showErrorMsg("请输入账单金额");
            return;
        } else {
            if (Double.parseDouble(amount) <= 0) {
                view.showErrorMsg("账单金额必须大于0");
                return;
            }
        }
        Disposable dis = api.updateCreditCardDebt(userHelper.getProfile().getId(), debtDetail.getShowBill().getCardId(), billDay, dueDay, Double.parseDouble(amount))
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) throws Exception {
                                   if (resultEntity.isSuccess()) {
                                       view.showUpdateCreditCardDebtSuccess("");
                                   } else {
                                       view.showErrorMsg(resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(CreditCardDebtNewPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void sendPoint(String id) {
        Disposable dis = api.sendReadPointRead(id)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(throwable);

                            }
                        });
        addDisposable(dis);
    }
}
