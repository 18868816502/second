package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;

public interface WeChatBindPhoneContract {

    interface Presenter extends BasePresenter {
        /**
         * 请求验证码
         *
         * @param phone 手机号
         */
        void requestVerifyCode(String phone);

        /**
         * 验证验证码
         *
         * @param phone 手机号
         * @param code  短信验证码
         */
        void verifyCode(String phone, String code, String exOpenId, String wxName, String wxImage);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 验证码发送成功
         */
        void showVerifyCodeSend(String msg);

        /**
         * 验证码验证成功,新用户，去设置密码
         *
         * @param account 请求的手机号
         */
        void navigateSetPwd(String account);

        /**
         * 验证码验证成功，老用户，直接登录
         */
        void showLoginSuccess();
    }
}
