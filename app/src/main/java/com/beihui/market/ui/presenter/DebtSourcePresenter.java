package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.UsedEmail;
import com.beihui.market.helper.UserHelper;
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
    private UserHelper userHelper;

    private List<DebtChannel> debtChannelList = new ArrayList<>();

    private boolean hasEmailBeenFetched;
    private boolean hasUsedEmail;

    @Inject
    DebtSourcePresenter(Context context, Api api, DebtSourceContract.View view) {
        this.api = api;
        this.view = view;
        this.userHelper = UserHelper.getInstance(context);
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
        if (hasEmailBeenFetched) {
            if (hasUsedEmail) {
                view.navigateUsedMail();
            } else {
                view.navigateNutMail();
            }
        } else {
            Disposable dis = api.fetchUsedEmail(userHelper.getProfile().getId())
                    .compose(RxUtil.<ResultEntity<List<UsedEmail>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<UsedEmail>>>() {
                                   @Override
                                   public void accept(ResultEntity<List<UsedEmail>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           hasEmailBeenFetched = true;
                                           hasUsedEmail = result.getData() != null && result.getData().size() > 0;

                                           if (hasUsedEmail) {
                                               view.navigateUsedMail();
                                           } else {
                                               view.navigateNutMail();
                                           }
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(DebtSourcePresenter.this, throwable);
                                }
                            });
            addDisposable(dis);
        }
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
        view.navigateDebtNew(debtChannelList.get(index));
    }

    @Override
    public void clickMoreSourceChannel() {
        view.navigateMoreSourceChannel();
    }
}
