package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AnnounceAbstract;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.MessageCenterContract;
import com.beihui.market.util.update.RxUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MessageCenterPresenter extends BaseRxPresenter implements MessageCenterContract.Presenter {

    private Api mApi;
    private MessageCenterContract.View mView;
    private UserHelper mUserHelper;

    @Inject
    MessageCenterPresenter(Api api, MessageCenterContract.View view, Context context) {
        mApi = api;
        mView = view;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Disposable dis = Observable.concat(mApi.queryAnnounceHome(), mApi.querySysMsgHome(mUserHelper.getProfile().getId()))
                .compose(RxUtil.io2main())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        ResultEntity result = (ResultEntity) o;
                        if (result.isSuccess()) {
                            Object data = result.getData();
                            if (data instanceof AnnounceAbstract) {
                                mView.showAnnounce((AnnounceAbstract) data);
                            } else if (data instanceof SysMsgAbstract) {
                                mView.showSysMsg((SysMsgAbstract) data);
                            }
                        } else {
                            mView.showErrorMsg(result.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        logError(MessageCenterPresenter.this, throwable);
                        mView.showErrorMsg(generateErrorMsg(throwable));
                    }
                });
        addDisposable(dis);

        mView.showNoRecommend();
    }
}
