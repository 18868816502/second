package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.CreditCardDebtDetail;

public interface CreditCardDebtNewContract {

    interface Presenter extends BasePresenter {
        /**
         * 设置旧版本信用卡账单
         *
         * @param debtDetail 旧版本信用卡账单
         */
        void attachCreditCardDebt(CreditCardDebtDetail debtDetail);

        /**
         * 手动添加账单
         *
         * @param cardNums 信用卡后4位
         * @param bankId   银行id
         * @param realName 姓名
         * @param billDay  绽放单日
         * @param dueDay   还款日
         * @param amount   账单金额
         */
        void saveCreditCardDebt(String cardNums, String bankId, String realName, int billDay, int dueDay, String amount);

        /**
         * 更新信用卡账单信息
         *
         * @param billDay 账单日
         * @param dueDay  还款日
         * @param amount  账单金额
         */
        void updateCreditCardDebt(int billDay, int dueDay, String amount);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 新账单添加成功
         *
         * @param msg 提示信息
         */
        void showSaveCreditCardDebtSuccess(String msg);

        /**
         * 更新信用卡账单成功
         *
         * @param msg 提示信息
         */
        void showUpdateCreditCardDebtSuccess(String msg);

        /**
         * 填充旧版本账单信息
         *
         * @param debtDetail 旧版本账单
         */
        void bindOldCreditCardDebt(CreditCardDebtDetail debtDetail);
    }
}
