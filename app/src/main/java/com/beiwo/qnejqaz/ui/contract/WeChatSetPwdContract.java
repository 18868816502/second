package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;

public interface WeChatSetPwdContract {

    interface Presenter extends BasePresenter {
        void register(String account, String pwd, String wxOpenId, String wxName, String wxImage);
    }

    interface View extends BaseView<Presenter> {
        void showRegisterSuccess(String msg);
    }
}
