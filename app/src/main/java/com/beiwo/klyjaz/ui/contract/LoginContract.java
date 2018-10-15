package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;

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
