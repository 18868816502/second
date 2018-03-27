package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface LoginContract {

    interface Presenter extends BasePresenter {
        /**
         * 手机号登录
         *
         * @param account 手机号
         * @param pwd     密码
         */
        void login(String account, String pwd);

        /**
         * 微信登录
         *
         * @param openId 微信openId
         */
        void loginWithWeChat(String openId);

    }

    interface View extends BaseView<Presenter> {
        void showLoginSuccess(String msg);

        /**
         * 唤起微信绑定手机号界面
         */
        void navigateWechatBindAccount();
    }
}
