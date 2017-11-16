package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.helper.UserHelper;

public interface TabMineContract {

    interface Presenter extends BasePresenter {
        /**
         * 点击用户资料
         */
        void clickUserProfile();

        /**
         * 点击消息
         */
        void clickMessage();

        /**
         * 点击我的收藏
         */
        void clickCollection();

        /**
         * 点击邀请朋友
         */
        void clickInvitation();

        /**
         * 点击联系客服
         */
        void clickContactKefu();

        /**
         * 点击帮助与反馈
         */
        void clickHelpAndFeedback();

        /**
         * 点击设置
         */
        void clickSetting();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示用户资料
         *
         * @param profile 用户资料
         */
        void showProfile(UserHelper.Profile profile);

        /**
         * 导航至登录
         */
        void navigateLogin();

        /**
         * 导航至用户资料
         *
         * @param userId 用户id
         */
        void navigateUserProfile(String userId);

        /**
         * 导航至消息中心
         *
         * @param userId 用户id
         */
        void navigateMessage(String userId);

        /**
         * 导航至我的收藏
         *
         * @param userId 用户id
         */
        void navigateCollection(String userId);

        /**
         * 导航至邀请朋友
         *
         * @param userId 用户id
         */
        void navigateInvitation(String userId);

        /**
         * 导航至联系客服
         */
        void navigateContactKefu(String userId, String userName);

        /**
         * 导航至帮助与反馈
         *
         * @param userId 用户id
         */
        void navigateHelpAndFeedback(String userId);

        /**
         * 导航至设置
         *
         * @param userId 用户id
         */
        void navigateSetting(String userId);

    }
}
