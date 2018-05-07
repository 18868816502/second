package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabMineContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabMinePresenter extends BaseRxPresenter implements TabMineContract.Presenter {

    private Api api;
    private TabMineContract.View view;
    private UserHelper userHelper;

    private int points = 0;

    @Inject
    TabMinePresenter(Context context, Api api, TabMineContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserHelper.Profile profile = userHelper.getProfile();
        if (profile != null) {
            view.showProfile(profile);
            view.showRewardPoints(points);

            Disposable dis = api.queryTotalRewardPoints(profile.getId())
                    .compose(RxUtil.<ResultEntity<Integer>>io2main())
                    .subscribe(new Consumer<ResultEntity<Integer>>() {
                                   @Override
                                   public void accept(ResultEntity<Integer> result) throws Exception {
                                       if (result.isSuccess()) {
                                           points = result.getData();
                                           view.showRewardPoints(points);
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(TabMinePresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);

            /**
             * 请求消息数量
             */
            Disposable disMessage = api.queryMessage(profile.getId())
                    .compose(RxUtil.<ResultEntity<String>>io2main())
                    .subscribe(new Consumer<ResultEntity<String>>() {
                                   @Override
                                   public void accept(ResultEntity<String> result) throws Exception {
                                       if (result.isSuccess()) {
                                           view.updateMessageNum(result.getData());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(TabMinePresenter.this, throwable);
                                }
                            });

            addDisposable(disMessage);
        } else {
            view.updateMessageNum("0");
        }

        Disposable dis = api.queryMenuVisible("my_loan_menu")
                .compose(RxUtil.<ResultEntity<Boolean>>io2main())
                .subscribe(new Consumer<ResultEntity<Boolean>>() {
                               @Override
                               public void accept(ResultEntity<Boolean> result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.updateMyLoanVisible(result.getData());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(TabMinePresenter.this, throwable);
                            }
                        });

        addDisposable(dis);
    }

    @Override
    public void clickUserProfile() {
        if (checkValidUser()) {
            view.navigateUserProfile(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickMineBill() {
        if (checkValidUser()) {
            view.navigateMineBill(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickMessage() {
        if (checkValidUser()) {
            view.navigateMessage(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickCollection() {
        if (checkValidUser()) {
            view.navigateCollection(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickRewardPoints() {
        if (checkValidUser()) {
            view.navigateRewardPoints();
        }
    }

    @Override
    public void clickInvitation() {
        if (checkValidUser()) {
            view.navigateInvitation(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickContactKefu() {
        if (checkValidUser()) {
            view.navigateContactKefu(userHelper.getProfile().getId(), userHelper.getProfile().getUserName());
        }
    }

    @Override
    public void clickHelpAndFeedback() {
        if (checkValidUser()) {
            view.navigateHelpAndFeedback(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickSetting() {
        if (checkValidUser()) {
            view.navigateSetting(null);
        }
    }

    private boolean checkValidUser() {
        if (userHelper.getProfile() == null) {
            view.navigateLogin();
            return false;
        }
        return true;
    }
}
