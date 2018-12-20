package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.AppUpdate;

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
