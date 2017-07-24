package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Profession;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.EditProfessionContract;
import com.beihui.market.util.update.RxUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class EditProfessionPresenter extends BaseRxPresenter implements EditProfessionContract.Presenter {

    private Api mApi;
    private EditProfessionContract.View mView;

    private UserHelper.Profile profile;

    @Inject
    public EditProfessionPresenter(Api api, EditProfessionContract.View view, Context context) {
        mApi = api;
        mView = view;
        profile = UserHelper.getInstance(context).getProfile();
    }

    @Override
    public void onStart() {
        super.onStart();
        Disposable dis = mApi.queryProfession(profile.getId())
                .compose(RxUtil.<ResultEntity<ArrayList<Profession>>>io2main())
                .subscribe(new Consumer<ResultEntity<ArrayList<Profession>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<ArrayList<Profession>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showProfession(result.getData());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(EditProfessionPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void updateProfession(Profession profession) {
        Disposable dis = mApi.updateUserProfession(profile.getId(), profession.getValue())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showUpdateSuccess();
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(EditProfessionPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
