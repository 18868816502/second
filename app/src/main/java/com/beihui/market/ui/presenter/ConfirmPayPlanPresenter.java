package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.ConfirmPayPlanContract;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ConfirmPayPlanPresenter extends BaseRxPresenter implements ConfirmPayPlanContract.Presenter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private Context context;
    private Api api;
    private ConfirmPayPlanContract.View view;
    private PayPlan payPlan;
    private UserHelper userHelper;

    /**
     * 如果该字段不为空，则说明当前是编辑模式，确认还款计划后，需要删除该旧账单
     */
    private DebtDetail pendingDebt;

    @Inject
    ConfirmPayPlanPresenter(Context context, PayPlan payPlan, Api api, ConfirmPayPlanContract.View view) {
        this.context = context;
        userHelper = UserHelper.getInstance(context);
        this.payPlan = payPlan;
        this.api = api;
        this.view = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (payPlan != null) {
            view.showPayPlanAbstract(payPlan);
            view.showPayPlanList(payPlan.getRepayPlan());

            if (payPlan.getRepayPlan().size() > 0) {
                //有数据时才显示引导
                if (!SPUtils.getConfirmPlanGuideShowed(context)) {
                    SPUtils.setConfirmPlanGuideShowed(context, true);
                    view.showGuide();
                }
            }

        }
    }

    @Override
    public void attachPendingDebt(DebtDetail pendingDebt) {
        this.pendingDebt = pendingDebt;
    }

    @Override
    public void editPayPlanAmount(int index, String amount) {
        payPlan.getRepayPlan().get(index).setTermPayableAmount(Double.parseDouble(amount));
        view.showPayPlanList(payPlan.getRepayPlan());
    }

    @Override
    public void editPayPlanDate(int index, Date date) {
        payPlan.getRepayPlan().get(index).setTermRepayDate(dateFormat.format(date));
        view.showPayPlanList(payPlan.getRepayPlan());
    }

    @Override
    public void confirmPayPlan() {
        if (payPlan != null) {
            final HashMap<String, Object> params = new HashMap<>();
            params.put("userId", userHelper.getProfile().getId());
            params.put("channelId", payPlan.getChannelId());
            params.put("channelName", payPlan.getChannelName());
            if (!TextUtils.isEmpty(payPlan.getProjectName())) {
                params.put("projectName", payPlan.getProjectName());
            }
            if (!TextUtils.isEmpty(payPlan.getRepayType())) {
                params.put("repayType", Integer.parseInt(payPlan.getRepayType()));
            }
            if (!TextUtils.isEmpty(payPlan.getCapital())) {
                params.put("capital", Double.parseDouble(payPlan.getCapital()));
            }
            if (!TextUtils.isEmpty(payPlan.getInterest())) {
                params.put("interest", Double.parseDouble(payPlan.getInterest()));
            }
            if (!TextUtils.isEmpty(payPlan.getTerm())) {
                params.put("term", Integer.parseInt(payPlan.getTerm()));
            }
            if (!TextUtils.isEmpty(payPlan.getTermType())) {
                params.put("termType", Integer.parseInt(payPlan.getTermType()));
            }
            params.put("startDate", payPlan.getStartDate());
            if (!TextUtils.isEmpty(payPlan.getPayableAmount())) {
                params.put("payableAmount", Double.parseDouble(payPlan.getPayableAmount()));
            }
            if (!TextUtils.isEmpty(payPlan.getEveryTermAmount())) {
                params.put("everyTermAmount", Double.parseDouble(payPlan.getEveryTermAmount()));
            }
            if (!TextUtils.isEmpty(payPlan.getTermAmount())) {
                params.put("termAmount", Double.parseDouble(payPlan.getTermAmount()));
            }
            if (!TextUtils.isEmpty(payPlan.getTermNum())) {
                params.put("termNum", Integer.parseInt(payPlan.getTermNum()));
            }
            if (!TextUtils.isEmpty(payPlan.getRate())) {
                params.put("rate", Double.parseDouble(payPlan.getRate()));
            }
            if (!TextUtils.isEmpty(payPlan.getRemark())) {
                params.put("remark", payPlan.getRemark());
            }

            if (payPlan.getRepayPlan() != null) {
                for (int i = 0; i < payPlan.getRepayPlan().size(); ++i) {
                    PayPlan.RepayPlanBean bean = payPlan.getRepayPlan().get(i);
                    if (!TextUtils.isEmpty(bean.getId())) {
                        params.put("repayPlan[" + i + "].id", bean.getId());
                    }
                    params.put("repayPlan[" + i + "].termNo", bean.getTermNo() + "");
                    params.put("repayPlan[" + i + "].termRepayDate", bean.getTermRepayDate());
                    params.put("repayPlan[" + i + "].termPayableAmount", bean.getTermPayableAmount());
                    params.put("repayPlan[" + i + "].status", bean.getStatus());
                }
            }

            Disposable dis = Observable.just(pendingDebt != null ? pendingDebt : new DebtDetail())
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<DebtDetail, ObservableSource<ResultEntity>>() {
                        @Override
                        public ObservableSource<ResultEntity> apply(DebtDetail debtDetail) throws Exception {
                            //如果处于编辑模式，则先删除原有的debt
                            if (debtDetail.getId() != null) {
                                return api.deleteDebt(userHelper.getProfile().getId(), debtDetail.getId());
                            }
                            return Observable.just(new ResultEntity());
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<ResultEntity, ObservableSource<ResultEntity>>() {
                        @Override
                        public ObservableSource<ResultEntity> apply(ResultEntity resultEntity) throws Exception {
                            //保存还款计划
                            return api.savePayPlan(params);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate<ResultEntity>() {
                        @Override
                        public boolean test(ResultEntity result) throws Exception {
                            //如果保存还款计划不成功，则提示错误，结束流程
                            if (!result.isSuccess()) {
                                view.showErrorMsg(result.getMsg());
                            }
                            return result.isSuccess();
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<ResultEntity, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                        @Override
                        public ObservableSource<ResultEntity<List<RewardPoint>>> apply(ResultEntity result) throws Exception {
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
                                       view.showConfirmSuccess(msg);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(ConfirmPayPlanPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
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
