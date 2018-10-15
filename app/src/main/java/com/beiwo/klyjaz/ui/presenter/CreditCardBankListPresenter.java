package com.beiwo.klyjaz.ui.presenter;


import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.CreditCardBank;
import com.beiwo.klyjaz.ui.contract.CreditCardBankContract;
import com.beiwo.klyjaz.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CreditCardBankListPresenter extends BaseRxPresenter implements CreditCardBankContract.Presenter {

    private Api api;
    private CreditCardBankContract.View view;

    private List<CreditCardBank> creditCardBankList = new ArrayList<>();

    @Inject
    CreditCardBankListPresenter(Api api, CreditCardBankContract.View view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void fetchCreditCardBankList() {
        Disposable dis = api.fetchCreditCardBankList()
                .compose(RxUtil.<ResultEntity<List<CreditCardBank>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<CreditCardBank>>>() {
                               @Override
                               public void accept(ResultEntity<List<CreditCardBank>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       creditCardBankList.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           creditCardBankList.addAll(result.getData());
                                       }
                                       view.showCreditCardBankList(Collections.unmodifiableList(creditCardBankList));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(CreditCardBankListPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickBank(int index) {
        view.navigateCreditCardDebtNew(creditCardBankList.get(index));
    }
}
