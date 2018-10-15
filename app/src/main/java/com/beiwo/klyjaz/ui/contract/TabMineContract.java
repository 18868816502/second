package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.helper.UserHelper;

public interface TabMineContract {

    interface Presenter extends BasePresenter {

        /**
         * 点击考拉圈圈
         */
        void clickKaolaGroup();

        /**
         * 点击消息
         */
        void clickRemind();

        /**
         * 点击消息
         */
        void clickMessage();

        /**
         * 点击用户资料
         */
        void clickUserProfile();

        /**
         * 点击我的账单
         */
        void clickMineBill();

        /**
         * 点击我的收藏
         */
        void clickCollection();

        /**
         * 点击我的积分
         */
        void clickRewardPoints();

        /**
         * 点击邀请朋友
         */
        void clickInvitation();

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
         * 显示当前积分总额
         *
         * @param points 积分总额
         */
        void showRewardPoints(int points);

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
         * 导航至提醒界面
         *
         * @param userId 用户id
         */
        void navigateRemind(String userId);

        /**
         * 导航至提醒界面
         *
         * @param userId 用户id
         */
        void navigateMessage(String userId);

        /**
         * 导航至我的账单
         *
         * @param userId 用户id
         */
        void navigateMineBill(String userId);

        /**
         * 导航至我的收藏
         *
         * @param userId 用户id
         */
        void navigateCollection(String userId);

        /**
         * 导航至我的积分
         */
        void navigateRewardPoints();

        /**
         * 导航至邀请朋友
         *
         * @param userId 用户id
         */
        void navigateInvitation(String userId);

        /**
         * 导航至考拉圈圈
         */
        void navigateKaolaGroup(String userId, String userName);

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

        /**
         * 更新我的借款按钮是否显示
         *
         * @param visible 是否显示
         */
        void updateMyLoanVisible(boolean visible);

        /**
         * 消息数量
         *
         * @param data
         */
        void updateMessageNum(String data);

    }
}