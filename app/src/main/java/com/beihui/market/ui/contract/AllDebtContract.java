package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.Debt;

import java.util.List;

public interface AllDebtContract {

    interface Presenter extends BasePresenter {
        int STATUS_IN = 1;
        int STATUS_OFF = 2;
        int STATUS_ALL = 3;

        /**
         * 加载账单列表
         */
        void loadDebts();

        /**
         * 点击借款项目
         *
         * @param index 点击位置
         */
        void clickDebt(int index);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 账单信息计算完成
         *
         * @param count          账单数量
         * @param debtAmount     应还总额
         * @param capitalAmount  本金总额
         * @param interestAmount 利息总额
         */
        void showDebtInfo(int count, double debtAmount, double capitalAmount, double interestAmount);

        /**
         * 账单列表加载完成
         *
         * @param list 账单列表
         */
        void showDebts(List<Debt> list);

        /**
         * 导航至借款项目详情
         *
         * @param debt 借款项目
         */
        void navigateDebtDetail(Debt debt);
    }
}