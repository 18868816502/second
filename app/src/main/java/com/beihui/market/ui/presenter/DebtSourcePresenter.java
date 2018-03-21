package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.ui.contract.DebtSourceContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtSourcePresenter extends BaseRxPresenter implements DebtSourceContract.Presenter {

    private Api api;
    private DebtSourceContract.View view;

    private List<DebtChannel> debtChannelList = new ArrayList<>();

    @Inject
    DebtSourcePresenter(Api api, DebtSourceContract.View view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void fetchSourceChannel() {
        Disposable dis = api.fetchDebtSourceChannel()
                .compose(RxUtil.<ResultEntity<List<DebtChannel>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<DebtChannel>>>() {
                               @Override
                               public void accept(ResultEntity<List<DebtChannel>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debtChannelList.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           debtChannelList.addAll(result.getData());
                                       }
                                       view.showSourceChannel(Collections.unmodifiableList(debtChannelList));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtSourcePresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickFetchDebtWithMail() {
        view.navigateDebtMail();
    }

    @Override
    public void clickFetchDebtWithVisa() {
        view.navigateDebtVisa();
    }

    @Override
    public void clickAddDebtByHand() {
        view.navigateDebtHand();
    }

    @Override
    public void clickSourceChannel(int index) {
        if (index > 0 && index < debtChannelList.size()) {
            view.navigateDebtNew(debtChannelList.get(index));
        }
    }

    @Override
    public void clickMoreSourceChannel() {
        view.navigateMoreSourceChannel();
    }
}
