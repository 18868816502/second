package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface WeChatSetPwdContract {

    interface Presenter extends BasePresenter {
        void register(String account, String pwd, String wxOpenId, String wxName, String wxImage);
    }

    interface View extends BaseView<Presenter> {
        void showRegisterSuccess(String msg);
    }
}
