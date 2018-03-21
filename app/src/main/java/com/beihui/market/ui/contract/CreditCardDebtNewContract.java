package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface CreditCardDebtNewContract {

    interface Presenter extends BasePresenter {
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
    }

    interface View extends BaseView<Presenter> {
        void showSaveCreditCardDebtSuccess();
    }
}
