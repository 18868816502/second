package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.ConfirmPayPlanContract;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
            HashMap<String, Object> params = new HashMap<>();
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

            //先删除旧账单
            if (pendingDebt != null) {
                Disposable dis = api.deleteDebt(userHelper.getProfile().getId(), pendingDebt.getId())
                        .compose(RxUtil.<ResultEntity>io2main())
                        .subscribe(new Consumer<ResultEntity>() {
                                       @Override
                                       public void accept(ResultEntity resultEntity) throws Exception {
                                           LogUtils.i(resultEntity);
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
            Disposable dis = api.savePayPlan(params)
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           view.showConfirmSuccess();
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
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
}
