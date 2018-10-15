package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.DebtDetail;
import com.beiwo.klyjaz.event.MyLoanDebtListFragmentEvent;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.DebtDetailContract;
import com.beiwo.klyjaz.ui.contract.DebtNewContract;
import com.beiwo.klyjaz.util.RxUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtDetailPresenter extends BaseRxPresenter implements DebtDetailContract.Presenter {

    private Api api;
    private DebtDetailContract.View view;
    private UserHelper userHelper;

    public String debtId;
    public String billId;

    //在查询单个借款项目查看 就保存了该bean
    public DebtDetail debtDetail;

    @Inject
    DebtDetailPresenter(Context context, String debtId,  Api api, DebtDetailContract.View view) {
        this.debtId = debtId;
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    /**
     * 单个借款项目查看
     */
    @Override
    public void loadDebtDetail(String billId) {
        this.billId = billId;
        Disposable dis = api.fetchLoanDebtDetail(userHelper.getProfile().getId(), debtId, billId)
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
    public void clickSetStatus(int index) {
        int status = (index == -1 ? debtDetail.getTermStatus() : debtDetail.getRepayPlan().get(index).getStatus());
        view.showSetStatus(index, status);
    }

    @Override
    public void updateDebtStatus() {
        //设置已还或者未还
        final int status = debtDetail.getTermStatus() == 2 ? 1 : 2;
        updateStatus(debtDetail.getTermId(), status);
    }

    @Override
    public void updateDebtStatus(int index, int status) {
        if (debtDetail.getRepayPlan() != null && debtDetail.getRepayPlan().size() > 0) {
            DebtDetail.RepayPlanBean bean = debtDetail.getRepayPlan().get(index);
//            if (bean.getStatus() != status) {
                updateStatus(bean.getId(), status);
//            }
        }
    }

    /**
     * 通过 getRepayType 判断展示菜单是否 需要编辑栏
     */
    @Override
    public void clickMenu() {
        boolean editable = debtDetail.getRepayType() == DebtNewContract.Presenter.METHOD_ONE_TIME || debtDetail.getRepayType() == DebtNewContract.Presenter.METHOD_EVEN_DEBT;
        view.showMenu(editable, debtDetail.getRedmineDay() != -1);
    }

    @Override
    public void clickUpdateRemind() {
        final int remind = debtDetail.getRedmineDay() == -1 ? 3 : -1;
        Disposable dis = api.updateRemindStatus(userHelper.getProfile().getId(), "1", debtDetail.getId(), remind)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       debtDetail.setRedmineDay(remind);
                                       view.showUpdateRemind(debtDetail.getRedmineDay() != -1);
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
                                       view.updateLoanDetail(billId);
                                       EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(1));
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