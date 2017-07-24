package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabMineContract;

import javax.inject.Inject;

public class TabMinePresenter extends BaseRxPresenter implements TabMineContract.Presenter {

    private Api mApi;
    private TabMineContract.View mView;

    private UserHelper mUserHelper;

    private UserHelper.Profile profile;

    @Inject
    TabMinePresenter(Api api, TabMineContract.View view, Context context) {
        mApi = api;
        mView = view;

        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        profile = mUserHelper.getProfile();
        if (profile != null) {
            mView.showProfile(profile);
        }
        mView.showHasMessage(true);
    }

    @Override
    public void checkMessage() {
        if (checkValidUser()) {
            mView.navigateMessage(profile.getId());
        }
    }

    @Override
    public void checkUserProfile() {
        if (checkValidUser()) {
            mView.navigateUserProfile(profile.getId());
        }
    }

    @Override
    public void checkInvitation() {
        if (checkValidUser()) {
            mView.navigateInvitation(profile.getId());
        }
    }

    @Override
    public void checkHelpAndFeedback() {
        if (checkValidUser()) {
            mView.navigateHelpAndFeedback(profile.getId());
        }
    }

    @Override
    public void checkSetting() {
        mView.navigateSetting(null);
    }

    private boolean checkValidUser() {
        if (profile == null) {
            mView.navigateLogin();
            return false;
        }
        return true;
    }
}
