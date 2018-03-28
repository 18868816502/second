package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.CreditCardDebtNewContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CreditCardDebtNewPresenter extends BaseRxPresenter implements CreditCardDebtNewContract.Presenter {

    private Api api;
    private CreditCardDebtNewContract.View view;
    private UserHelper userHelper;

    private CreditCardDebtDetail debtDetail;

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
    public void saveCreditCardDebt(String cardNums, String bankId, final String realName, int billDay, int dueDay, String amount) {
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
        }
        double debtAmount = 0;
        long creditCardBankId = 0;
        try {
            debtAmount = Double.parseDouble(amount);
            creditCardBankId = Long.parseLong(bankId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Disposable dis = api.saveCreditCardDebt(userHelper.getProfile().getId(), cardNums, creditCardBankId, realName, billDay, dueDay, debtAmount)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showSaveCreditCardDebtSuccess();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
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
}
