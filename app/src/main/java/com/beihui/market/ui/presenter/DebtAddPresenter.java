package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.DebtAddContract;
import com.beihui.market.util.RxUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtAddPresenter extends BaseRxPresenter implements DebtAddContract.Presenter {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final static String[] METHOD_NAME = {"一次性还本付息", "等额本息", "等额本金"};
    private final static String[] METHOD_DES = {"到期一次性还本付息", "每月还相同的钱", "第一月还款最多，每月本金一样多"};

    private Api api;
    private DebtAddContract.View view;
    private UserHelper userHelper;

    //当前选中的还款方式
    private int selectedMethod = 0;

    private DebtChannel debtChannel;

    @Inject
    DebtAddPresenter(Context context, Api api, DebtAddContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        view.showSelectedMethod(selectedMethod + 1, METHOD_NAME[selectedMethod], METHOD_DES[selectedMethod]);
    }

    @Override
    public void attachDebtDetail(DebtDetail debtDetail) {
        DebtChannel channel = new DebtChannel();
        channel.setType("whatever");
        channel.setId(debtDetail.getChannelId());
        channel.setChannelName(debtDetail.getChannelName());
        this.selectDebtChannel(channel);

        this.clickPayMethod(debtDetail.getRepayType() - 1);
        //填充附属数据
        view.showAttachData(debtDetail.getRepayType(), debtDetail.getTerm(), debtDetail.getStartDate(),
                debtDetail.getCapital(), debtDetail.getPayableAmount(), debtDetail.getRepayPlan().get(0).getTermPayableAmount(),
                debtDetail.getProjectName(), debtDetail.getRemark());
    }

    @Override
    public void clickPayMethod() {
        view.showMethod(METHOD_NAME);
    }

    @Override
    public void clickPayMethod(int index) {
        selectedMethod = index;
        view.showSelectedMethod(selectedMethod + 1, METHOD_NAME[selectedMethod], METHOD_DES[selectedMethod]);
    }

    @Override
    public void selectDebtChannel(DebtChannel channel) {
        if (channel != null) {
            debtChannel = channel;
            view.showDebtChannel(debtChannel);
        }
    }

    @Override
    public void saveDebt(String projectName, String capital, String term, Date startDate, String debtAmount, String everyTermAmount, String termAmount, String termNum, final String remark) {
        if (debtChannel == null) {
            view.showErrorMsg("请输入借款渠道");
            return;
        }
        if (isDataEmpty(term)) {
            view.showErrorMsg(selectedMethod == 0 ? "请输入借款期数" : "请输入借款期限");
            return;
        }
        if (startDate == null) {
            view.showErrorMsg("请输入起息日期");
            return;
        }
        if (isDataEmpty(capital)) {
            view.showErrorMsg("请输入借款金额");
            return;
        }
        switch (selectedMethod + 1) {
            case METHOD_ONE_TIME:
                if (isDataEmpty(debtAmount)) {
                    view.showErrorMsg("请输入到期应还");
                    return;
                }
                break;
            case METHOD_EVEN_DEBT:
                if (isDataEmpty(everyTermAmount)) {
                    view.showErrorMsg("请输入每期应还");
                    return;
                }
                break;
            case METHOD_EVEN_CAPITAL:
                if (isDataEmpty(termAmount)) {
                    view.showErrorMsg("请输入应还金额");
                    return;
                }
                break;
        }
        //借款期限类型，一次性还本付息1，等额本金，本息2
        int pTermType = selectedMethod == 0 ? 1 : 2;
        double pPayableAmount = debtAmount != null ? Double.parseDouble(debtAmount) : -1.0;
        double pEveryTermAmount = everyTermAmount != null ? Double.parseDouble(everyTermAmount) : -1.0;
        double pTermAmount = termAmount != null ? Double.parseDouble(termAmount) : -1.0;
        int pTermNum = termNum != null ? Integer.parseInt(termNum) : -1;
        String channelId = (TextUtils.isEmpty(debtChannel.getType()) || debtChannel.isCustom()) ? debtChannel.getCustomId() : debtChannel.getId();

        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userHelper.getProfile().getId());
        params.put("channelId", channelId);
        params.put("channelName", debtChannel.getChannelName());
        if (!TextUtils.isEmpty(projectName)) {
            params.put("projectName", projectName);
        }
        params.put("repayType", selectedMethod + 1);
        params.put("capital", Double.parseDouble(capital));
        params.put("term", Integer.parseInt(term));
        params.put("termType", pTermType);
        params.put("startDate", dateFormat.format(startDate));
        if (pPayableAmount > 0) {
            params.put("payableAmount", pPayableAmount);
        }
        if (pEveryTermAmount > 0) {
            params.put("everyTermAmount", pEveryTermAmount);
        }
        if (pTermAmount > 0 && pTermNum > 0) {
            params.put("termAmount", pTermAmount);
            params.put("termNum", pTermNum);
        }
        if (!TextUtils.isEmpty(remark)) {
            params.put("remark", remark);
        }

        Disposable dis = api.confirmDebt(params)
                .compose(RxUtil.<ResultEntity<PayPlan>>io2main())
                .subscribe(new Consumer<ResultEntity<PayPlan>>() {
                               @Override
                               public void accept(ResultEntity<PayPlan> result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.navigatePayPlan(result.getData());
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtAddPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private boolean isDataEmpty(String... params) {
        boolean empty = false;
        for (String data : params) {
            empty = empty || TextUtils.isEmpty(data);
        }
        return empty;
    }

}
