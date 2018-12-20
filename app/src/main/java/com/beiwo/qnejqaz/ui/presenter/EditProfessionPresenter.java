package com.beiwo.qnejqaz.ui.presenter;


import android.content.Context;

import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.entity.Profession;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.ui.contract.EditProfessionContract;
import com.beiwo.qnejqaz.util.RxUtil;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class EditProfessionPresenter extends BaseRxPresenter implements EditProfessionContract.Presenter {

    private Api mApi;
    private EditProfessionContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    public EditProfessionPresenter(EditProfessionContract.View view, Context context) {
        mApi = Api.getInstance();
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Disposable dis = mApi.queryProfession(mUserHelper.getProfile().getId())
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
        final Profession pr = profession;
        if (profession != null) {
            mView.showProgress();
            Disposable dis = mApi.updateUserProfession(mUserHelper.getProfile().getId(), profession.getValue())
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           mUserHelper.updateProfession(pr.getText(), mContext);
                                           mView.showUpdateSuccess(result.getMsg());
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
}
