package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.WeChatSetPwdContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WeChatSetPwdPresenter extends BaseRxPresenter implements WeChatSetPwdContract.Presenter {

    private Api api;
    private WeChatSetPwdContract.View view;
    private Context context;
    private UserHelper userHelper;

    @Inject
    WeChatSetPwdPresenter(Context context, Api api, WeChatSetPwdContract.View view) {
        this.context = context;
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void register(final String account, String pwd, String wxOpenId, String wxName, String wxImage) {
        Disposable dis = api.register(account, pwd, wxOpenId, wxName, wxImage, null)
                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                               @Override
                               public void accept(ResultEntity<UserProfileAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       //登录成功后，将用户信息注册到本地
                                       userHelper.update(result.getData(), account, context);
                                       //保存用户id,缓存
                                       SPUtils.setCacheUserId(context, result.getData().getId());

                                       view.showRegisterSuccess("注册成功");
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(WeChatSetPwdPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
