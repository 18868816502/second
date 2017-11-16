package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabMineContract;

import javax.inject.Inject;

public class TabMinePresenter extends BaseRxPresenter implements TabMineContract.Presenter {

    private TabMineContract.View view;

    private UserHelper userHelper;

    @Inject
    TabMinePresenter(TabMineContract.View view, Context context) {
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserHelper.Profile profile = userHelper.getProfile();
        if (profile != null) {
            view.showProfile(profile);
        }
    }

    @Override
    public void clickUserProfile() {
        if (checkValidUser()) {
            view.navigateUserProfile(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickMessage() {
        if (checkValidUser()) {
            view.navigateMessage(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickCollection() {
        if (checkValidUser()) {
            view.navigateCollection(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickInvitation() {
        if (checkValidUser()) {
            view.navigateInvitation(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickContactKefu() {
        if (checkValidUser()) {
            view.navigateContactKefu(userHelper.getProfile().getId(), userHelper.getProfile().getUserName());
        }
    }

    @Override
    public void clickHelpAndFeedback() {
        if (checkValidUser()) {
            view.navigateHelpAndFeedback(userHelper.getProfile().getId());
        }
    }

    @Override
    public void clickSetting() {
        if (checkValidUser()) {
            view.navigateSetting(null);
        }
    }

    private boolean checkValidUser() {
        if (userHelper.getProfile() == null) {
            view.navigateLogin();
            return false;
        }
        return true;
    }
}
