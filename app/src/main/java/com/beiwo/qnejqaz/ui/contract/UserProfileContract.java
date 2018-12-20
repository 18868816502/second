package com.beiwo.qnejqaz.ui.contract;


import android.graphics.Bitmap;

import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.AppUpdate;
import com.beiwo.qnejqaz.helper.UserHelper;

public interface UserProfileContract {

    interface Presenter extends BasePresenter {

        void updateAvatar(Bitmap bitmap);

        void updateUserName(String username);

        void logout();

        void checkVersion();

        void fetchSaveUserInfo(int sex);
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

        void onUpdateSexSucceed();
    }
}
