package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabMineContract;

import javax.inject.Inject;

public class TabMinePresenter extends BaseRxPresenter implements TabMineContract.Presenter {

    private TabMineContract.View mView;

    private UserHelper userHelper;

    @Inject
    TabMinePresenter(TabMineContract.View view, Context context) {
        mView = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserHelper.Profile profile = userHelper.getProfile();
        if (profile != null) {
            mView.showProfile(profile);
        }
    }

    @Override
    public void checkMessage() {
        if (checkValidUser()) {
            mView.navigateMessage(userHelper.getProfile().getId());
        }
    }

    @Override
    public void checkUserProfile() {
        if (checkValidUser()) {
            mView.navigateUserProfile(userHelper.getProfile().getId());
        }
    }

    @Override
    public void checkCollection() {
        if (checkValidUser()) {
            mView.navigateCollection(userHelper.getProfile().getId());
        }
    }

    @Override
    public void checkInvitation() {
        if (checkValidUser()) {
            mView.navigateInvitation(userHelper.getProfile().getId());
        }
    }

    @Override
    public void checkHelpAndFeedback() {
        if (checkValidUser()) {
            mView.navigateHelpAndFeedback(userHelper.getProfile().getId());
        }
    }

    @Override
    public void checkSetting() {
        if (checkValidUser()) {
            mView.navigateSetting(null);
        }
    }

    private boolean checkValidUser() {
        if (userHelper.getProfile() == null) {
            mView.navigateLogin();
            return false;
        }
        return true;
    }
}
