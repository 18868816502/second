package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.LoanBill;

import java.util.List;

public interface MyLoanBillContract {

    interface Presenter extends BasePresenter {
        /**
         * 获取我的账单
         *
         * @param billType 账单类型， 1-网贷账单，2-信用卡账单
         */
        void fetchLoanBill(int billType);

        /**
         * 点击我的账单
         *
         * @param index 点击的位置
         */
        void clickLoanBill(int index);

        /**
         * 点击显示隐藏账单
         *
         * @param index 点击的位置
         */
        void clickShowHideDebt(int index);

        /**
         * 账单被删除
         *
         * @param debtId 账单
         */
        void debtDeleted(String debtId);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 显示账单数目
         *
         * @param count 账单
         */
        void showLoanBillCount(int count);

        /**
         * 显示我的账单
         *
         * @param list        我的账单列表
         * @param canLoadMore 能否加载更多
         */
        void showLoanBill(List<LoanBill.Row> list, boolean canLoadMore);

        /**
         * 唤起网贷账单详情页
         *
         * @param bill 账单
         */
        void navigateLoanDebtDetail(LoanBill.Row bill);

        /**
         * 唤起信用卡账单详情页
         *
         * @param bill 账单
         */
        void navigateBillDebtDetail(LoanBill.Row bill);

        /**
         * 唤醒快捷记账详情页
         * @param loanBill
         */
        void navigateFastDebtDetail(LoanBill.Row loanBill);
    }
}
