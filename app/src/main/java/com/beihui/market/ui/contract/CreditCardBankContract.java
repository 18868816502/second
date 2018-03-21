package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.CreditCardBank;

import java.util.List;

public interface CreditCardBankContract {

    interface Presenter extends BasePresenter {
        /**
         * 获取银行列表
         */
        void fetchCreditCardBankList();

        /**
         * 选中银行
         *
         * @param index 点击位置
         */
        void clickBank(int index);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 银行列表加载完成
         *
         * @param list 银行列表
         */
        void showCreditCardBankList(List<CreditCardBank> list);

        /**
         * 唤起手动添加信用卡账单界面
         *
         * @param bank 选中的银行
         */
        void navigateCreditCardDebtNew(CreditCardBank bank);
    }
}
