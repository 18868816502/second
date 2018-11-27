package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.EditUserNameContract;
import com.beiwo.klyjaz.util.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class EditUserNamePresenter extends BaseRxPresenter implements EditUserNameContract.Presenter {

    private Api mApi;
    private EditUserNameContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    public EditUserNamePresenter(EditUserNameContract.View view, Context context) {
        mApi = Api.getInstance();
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }


    @Override
    public void onStart() {
        super.onStart();
        mView.showUserName(mUserHelper.getProfile().getUserName());
    }

    @Override
    public void updateUserName(String username) {
        final String name = username;
        mView.showProgress();
        Disposable dis = mApi.updateUsername(mUserHelper.getProfile().getId(), username)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mUserHelper.updateUsername(name, mContext);
                                       mView.showUpdateNameSuccess(result.getMsg(), name);
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(EditUserNamePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
