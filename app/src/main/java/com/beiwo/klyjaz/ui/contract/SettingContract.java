package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.AppUpdate;

public interface SettingContract {
    interface Presenter extends BasePresenter {
        void checkVersion();

        void logout();
    }

    interface View extends BaseView<Presenter> {
        void showLatestVersion(String version);

        void showLogoutSuccess();

        void showUpdate(AppUpdate appUpdate);

        void showHasBeenLatest(String msg);
    }
}
