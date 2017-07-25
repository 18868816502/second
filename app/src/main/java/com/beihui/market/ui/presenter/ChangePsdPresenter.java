package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.ChangePsdContract;
import com.beihui.market.util.update.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChangePsdPresenter extends BaseRxPresenter implements ChangePsdContract.Presenter {

    private Api mApi;
    private ChangePsdContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    ChangePsdPresenter(Api api, ChangePsdContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void updatePsd(String origin, String newPsd, String confirm) {
        if (origin != null && newPsd != null && confirm != null) {
            if (!newPsd.equals(confirm)) {
                mView.showErrorMsg("两次输入的密码不一致");
                return;
            }

            UserHelper.Profile profile = mUserHelper.getProfile();
            Disposable dis = mApi.updatePwd(profile.getId(), profile.getAccount(), newPsd, origin)
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           mUserHelper.clearUser(mContext);
                                           mView.showUpdateSuccess();
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    logError(ChangePsdPresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);

        } else {
            mView.showErrorMsg("输入错误");
        }
    }
}
