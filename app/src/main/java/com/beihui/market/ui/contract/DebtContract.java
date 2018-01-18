package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.InDebt;

import java.util.List;

public interface DebtContract {

    interface Presenter extends BasePresenter {

        /**
         * 加载账单信息摘要
         */
        void loadDebtAbstract();

        /**
         * 加载待还账单
         */
        void loadInDebtList();

        /**
         * 账单状态更新为已还
         *
         * @param index 更新状态的账单位置
         */
        void updateDebtStatus(int index);

        /**
         * 点击添加账单
         */
        void clickAdd();

        /**
         * 点击还款日历
         */
        void clickCalendar();

        /**
         * 点击负债分析
         */
        void clickAnalyze();

        /**
         * 点击账单
         *
         * @param index 账单位置
         */
        void clickDebt(int index);

        /**
         * 点击信用卡推荐
         */
        void clickCreditCard();

        /**
         * 点击信息隐藏
         *
         * @param checked 是否隐藏
         */
        void clickEye(boolean checked);

        /**
         * 点击全部借款
         */
        void clickAllDebt();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 显示用户未登录的信息
         */
        void showNoUserLoginBlock();

        /**
         * 显示用户已登录的信息
         */
        void showUserLoginBlock();

        /**
         * 显示暂无账单数据信息
         */
        void showNoDebtListBlock();

        /**
         * 显示用户账单摘要信息
         *
         * @param debtAmount   当前负债
         * @param debtSevenDay 近7天待还
         * @param debtMonth    近30天待还
         */
        void showDebtInfo(double debtAmount, double debtSevenDay, double debtMonth);

        /**
         * 显示用户账单列表
         *
         * @param list 账单列表
         */
        void showInDebtList(List<InDebt> list);

        /**
         * 显示引导层
         */
        void showGuide();

        /**
         * 账单更新状态成功
         *
         * @param msg msg
         */
        void showUpdateStatusSuccess(String msg);

        /**
         * 导航至用户登录界面
         */
        void navigateUserLogin();

        /**
         * 导航至账单添加页面
         */
        void navigateAddDebt();

        /**
         * 导航至还款日历页面
         */
        void navigateCalendar();

        /**
         * 导航至负债分析
         */
        void navigateAnalyze();

        /**
         * 导航至账单详情
         *
         * @param inDebt 账单
         */
        void navigateDebtDetail(InDebt inDebt);

        /**
         * 导航至信用卡中心
         */
        void navigateCreditCardCenter();

        /**
         * 导航至全部借款
         */
        void navigateAllDebt();

        /**
         * 显示账单信息
         */
        void showDebtInfo();

        /**
         * 隐藏账单信息
         */
        void hideDebtInfo();

    }
}
