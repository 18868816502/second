package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.DebtDetailContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtDetailPresenter extends BaseRxPresenter implements DebtDetailContract.Presenter {

    private Api api;
    private DebtDetailContract.View view;
    private UserHelper userHelper;

    private String debtId;

    private DebtDetail debtDetail;

    @Inject
    DebtDetailPresenter(Context context, String debtId, Api api, DebtDetailContract.View view) {
        this.debtId = debtId;
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void loadDebtDetail() {
        Disposable dis = api.queryDebtDetail(userHelper.getProfile().getId(), debtId)
                .compose(RxUtil.<ResultEntity<DebtDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<DebtDetail>>() {
                               @Override
                               public void accept(ResultEntity<DebtDetail> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debtDetail = result.getData();
                                       if (debtDetail != null) {
                                           view.showDebtDetail(debtDetail);
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void updateDebtStatus() {
        //设置已还或者未还
        final int status = debtDetail.getStatus() == 1 ? 2 : 1;
        updateStatus(debtDetail.getTermId(), status);
    }

    @Override
    public void updateDebtStatus(int index, int status) {
        if (debtDetail.getRepayPlan() != null && debtDetail.getRepayPlan().size() > 0) {
            DebtDetail.RepayPlanBean bean = debtDetail.getRepayPlan().get(index);
            if (bean.getStatus() != status) {
                //逾期修改成待还，则不做处理
                if (bean.getStatus() != 3 || status != 2) {
                    updateStatus(bean.getId(), status);
                }
            }
        }
    }

    @Override
    public void deleteDebt() {
        Disposable dis = api.deleteDebt(userHelper.getProfile().getId(), debtId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showDeleteDebtSuccess("删除成功");
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void editDebt() {
        view.navigateAddDebt(debtDetail);
    }

    private void updateStatus(String debtId, int status) {
        Disposable dis = api.updateDebtStatus(userHelper.getProfile().getId(), debtId, status)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showUpdateStatusSuccess("更新成功");
                                       //更新成功后刷新数据
                                       loadDebtDetail();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtDetailPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
