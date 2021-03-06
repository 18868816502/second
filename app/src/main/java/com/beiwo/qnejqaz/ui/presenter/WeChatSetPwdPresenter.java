package com.beiwo.qnejqaz.ui.presenter;


import android.content.Context;

import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.entity.UserProfileAbstract;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.ui.contract.WeChatSetPwdContract;
import com.beiwo.qnejqaz.util.RxUtil;
import com.beiwo.qnejqaz.util.SPUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WeChatSetPwdPresenter extends BaseRxPresenter implements WeChatSetPwdContract.Presenter {

    private Api api;
    private WeChatSetPwdContract.View view;
    private Context context;
    private UserHelper userHelper;

    public WeChatSetPwdPresenter(Context context,WeChatSetPwdContract.View view) {
        this.context = context;
        this.api = Api.getInstance();
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
                                       SPUtils.setCacheUserId(result.getData().getId());

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
