package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface SettingContract {

    interface Presenter extends BasePresenter {
        void logout();
    }

    interface View extends BaseView<Presenter> {
        void showLatestVersion(String version);

        void showLogoutSuccess();
    }
}
