package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.helper.UserHelper;

public interface UserProfileContract {

    interface Presenter extends BasePresenter {

        void updateAvatar(byte[] avatarBytes);
    }

    interface View extends BaseView<Presenter> {
        void showProfile(UserHelper.Profile profile);
    }
}
