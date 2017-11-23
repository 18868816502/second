package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Invitation;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.InvitationContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class InvitationPresenter extends BaseRxPresenter implements InvitationContract.Presenter {

    private Api api;
    private InvitationContract.View view;
    private UserHelper userHelper;

    private List<Invitation.Row> invitation = new ArrayList<>();

    @Inject
    InvitationPresenter(Api api, InvitationContract.View view, Context context) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        view.showInvitationCode(userHelper.getProfile().getAccount());

        Disposable dis = api.queryInvitation(userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity<Invitation>>io2main())
                .subscribe(new Consumer<ResultEntity<Invitation>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<Invitation> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getRows() != null
                                               && result.getData().getRows().size() > 0) {
                                           invitation.addAll(result.getData().getRows());
                                       }
                                       view.showInvitations(Collections.unmodifiableList(invitation));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(InvitationPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

}
