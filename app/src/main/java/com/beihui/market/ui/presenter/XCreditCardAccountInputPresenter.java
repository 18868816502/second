package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.UsedEmail;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.XCreditCardAccountInputContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 * 添加信用卡账单
 */
public class XCreditCardAccountInputPresenter extends BaseRxPresenter implements XCreditCardAccountInputContract.Presenter {

    private Api api;
    private XCreditCardAccountInputContract.View view;
    private UserHelper userHelper;

    private List<DebtChannel> debtChannelList = new ArrayList<>();

    private boolean hasEmailBeenFetched;
    private boolean hasUsedEmail;

    @Inject
    XCreditCardAccountInputPresenter(Context context, Api api, XCreditCardAccountInputContract.View view) {
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
                                    logError(XCreditCardAccountInputPresenter.this, throwable);
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
