package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.ChangePsdContract;
import com.beiwo.klyjaz.util.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChangePsdPresenter extends BaseRxPresenter implements ChangePsdContract.Presenter {

    private Api mApi;
    private ChangePsdContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    public ChangePsdPresenter(ChangePsdContract.View view, Context context) {
        mApi = Api.getInstance();
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void updatePsd(String origin, String newPsd, String confirm) {
        if (origin != null && newPsd != null && confirm != null) {
            if (!newPsd.equals(confirm)) {
                mView.showErrorMsg("新密码与确认新密码不一致");
                return;
            }
            mView.showProgress();
            UserHelper.Profile profile = mUserHelper.getProfile();
            Disposable dis = mApi.updatePwd(profile.getId(), profile.getAccount(), newPsd, origin)
                    .compose(RxUtil.<ResultEntity>io2main())
                    .subscribe(new Consumer<ResultEntity>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity result) throws Exception {
                                       if (result.isSuccess()) {
                                           String account = mUserHelper.getProfile().getAccount();
                                           mUserHelper.clearUser(mContext);
                                           mView.showUpdateSuccess(result.getMsg(), account);
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
