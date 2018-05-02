package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.request.XAccountInfo;

import java.util.List;

/**
 * @author xhb
 * 账单模块 首页
 */
public interface TabAccountContract {

    interface Presenter extends BasePresenter {

        /**
         * @author xhb
         * 加载待还账单
         */
        void loadInDebtList(int billStatus, boolean firstScreen, int pageNo, int pageSize);


        /**
         * @author xhb 头信息
         * 加载账单信息摘要
         */
        void loadDebtAbstract();






        /**
         * 点击设为已还
         *
         * @param index 点击位置
         */
        void clickDebtSetStatus(int index);

        /**
         * 点击隐藏
         *
         * @param index 点击位置
         */
        void clickDebtHide(int index);

        /**
         * 点击同步
         *
         * @param index 点击位置
         */
        void clickDebtSync(int index);

        /**
         * 刷新数据
         */
        void refresh();

        /**
         * 点击添加账单
         */
        void clickAdd();

        /**
         * 点击添加信用卡账单
         */
        void clickAddCreditCardDebt();

        /**
         * 点击添加网贷账单
         */
        void clickAddLoanDebt();

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
         * 显示用户账单 头信息
         */
        void showDebtInfo(DebtAbstract debtAbstract);

        /**
         * 显示用户账单列表
         *
         * @param list 账单列表
         */
        void showInDebtList(List<XAccountInfo> list, int type);

        /**
         * 显示引导层
         */
        void showGuide();

        /**
         * 导航至用户登录界面
         */
        void navigateUserLogin();

        /**
         * 唤起账单添加界面
         */
        void navigateAdd();

        /**
         * 唤起信用卡账单添加界面
         */
        void navigateAddCreditCardDebt();

        /**
         * 唤起网贷账单添加界面
         */
        void navigateAddLoanDebt();

        /**
         * 唤起网银导入账单界面
         */
        void navigateVisaLeadingIn();

        /**
         * 导航至还款日历页面
         */
        void navigateCalendar();

        /**
         * 导航至负债分析
         */
        void navigateAnalyze();

        /**
         * 唤起网贷账单详情界面
         *
         * @param accountBill 账单信息
         */
        void navigateLoanDebtDetail(AccountBill accountBill);

        /**
         * 唤起信用卡账单详情界面
         *
         * @param accountBill 账单信息
         */
        void navigateCreditCardDebtDetail(AccountBill accountBill);

        /**
         * 导航至信用卡中心
         */
        void navigateCreditCardCenter();

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
