package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.UserProfileContract;
import com.beihui.market.util.update.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserProfilePresenter extends BaseRxPresenter implements UserProfileContract.Presenter {

    private Api mApi;
    private UserProfileContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    UserProfilePresenter(Api api, UserProfileContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserHelper.Profile profile = mUserHelper.getProfile();
        if (profile != null) {
            mView.showProfile(profile);

            Disposable dis = mApi.queryUserProfile(profile.getId())
                    .compose(RxUtil.<ResultEntity<UserProfile>>io2main())
                    .subscribe(new Consumer<ResultEntity<UserProfile>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<UserProfile> result) throws Exception {
                                       if (result.isSuccess()) {
                                           mUserHelper.update(result.getData(), mContext);
                                           mView.showProfile(mUserHelper.getProfile());
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    logError(UserProfilePresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }

    }

    @Override
    public void updateAvatar(byte[] avatarBytes) {

    }
}
