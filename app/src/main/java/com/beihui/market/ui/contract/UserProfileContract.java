package com.beihui.market.ui.contract;


import android.graphics.Bitmap;

import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.helper.UserHelper;

public interface UserProfileContract {

    interface Presenter extends BasePresenter {

        void updateAvatar(Bitmap bitmap);

        void updateUserName(String username);

        void logout();

        void checkVersion();
    }

    interface View extends BaseView<Presenter> {
        void showProfile(UserHelper.Profile profile);

        void showAvatarUpdateSuccess(String avatar);

        void showUserName(String name);

        void showUpdateNameSuccess(String msg);

        void showLogoutSuccess();

        void showLatestVersion(String version);

        void showHasBeenLatest(String msg);

        void showUpdate(AppUpdate appUpdate);
    }
}
