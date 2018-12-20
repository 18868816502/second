package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.helper.UserHelper;

public interface TabMineContract {

    interface Presenter extends BasePresenter {
        /**
         * 点击考拉圈圈
         */
        void clickKaolaGroup();

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
         * 导航至考拉圈圈
         */
        void navigateKaolaGroup(String userId, String userName);

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