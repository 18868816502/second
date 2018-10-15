package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.DebtChannel;
import com.beiwo.klyjaz.entity.UsedEmail;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.DebtSourceContract;
import com.beiwo.klyjaz.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 * 添加信用卡账单
 */
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


}