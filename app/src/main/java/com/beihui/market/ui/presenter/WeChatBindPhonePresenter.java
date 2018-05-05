package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Phone;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.WeChatBindPhoneContract;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WeChatBindPhonePresenter extends BaseRxPresenter implements WeChatBindPhoneContract.Presenter {

    private Context context;
    private Api api;
    private WeChatBindPhoneContract.View view;
    private UserHelper userHelper;

    @Inject
    WeChatBindPhonePresenter(Context context, Api api, WeChatBindPhoneContract.View view) {
        this.context = context;
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void requestVerifyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("请输入手机号");
            return;
        }
        Disposable dis = api.requestWeChatBindPwdSms(phone)
                .compose(RxUtil.<ResultEntity<Phone>>io2main())
                .subscribe(new Consumer<ResultEntity<Phone>>() {
                               @Override
                               public void accept(ResultEntity<Phone> result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showVerifyCodeSend(result.getMsg());
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(WeChatBindPhonePresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void verifyCode(String phone, String code, String wxOpenId, String wxName, String wxImage) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            view.showErrorMsg("请输入验证码");
            return;
        }
        final String account = phone;

        Disposable dis = api.verifyWeChatBindCode(phone, wxOpenId, wxName, wxImage, code)
                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                               @Override
                               public void accept(ResultEntity<UserProfileAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       UserProfileAbstract profile = result.getData();
                                       /**
                                        * 不需要判断老用户
                                        */
//                                       if (profile.isNewUser()) {
//                                           //新用户，去设置密码
//                                           view.navigateSetPwd(account);
//                                       } else {
//                                           //老用户，直接作为登录
//                                           //登录之后，将用户信息注册到本地
//                                           userHelper.update(result.getData(), account, context);
//                                           //保存用户id,缓存
//                                           SPUtils.setCacheUserId(context, result.getData().getId());
//
//                                           view.showLoginSuccess();
//                                       }

                                       //老用户，直接作为登录
                                       //登录之后，将用户信息注册到本地
                                       userHelper.update(result.getData(), account, context);
                                       //保存用户id,缓存
                                       SPUtils.setCacheUserId(context, result.getData().getId());

                                       view.showLoginSuccess();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(WeChatBindPhonePresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
