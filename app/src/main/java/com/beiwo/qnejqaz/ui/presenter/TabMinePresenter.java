package com.beiwo.qnejqaz.ui.presenter;


import android.content.Context;

import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.ui.contract.TabMineContract;
import com.beiwo.qnejqaz.util.RxUtil;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TabMinePresenter extends BaseRxPresenter implements TabMineContract.Presenter {

    private Api api;
    private TabMineContract.View view;
    private UserHelper userHelper;

    private int points = 0;

    public TabMinePresenter(Context context,TabMineContract.View view) {
        this.api = Api.getInstance();
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
    public void clickKaolaGroup() {
        if (checkValidUser()) {
            view.navigateKaolaGroup(userHelper.getProfile().getId(), userHelper.getProfile().getUserName());
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
