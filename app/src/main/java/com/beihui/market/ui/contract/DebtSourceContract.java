package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.DebtChannel;

import java.util.List;

public interface DebtSourceContract {

    interface Presenter extends BasePresenter {
        /**
         * 加载网贷账单渠道
         */
        void fetchSourceChannel();

        /**
         * 点击邮箱导入
         */
        void clickFetchDebtWithMail();

        /**
         * 点击网银导入
         */
        void clickFetchDebtWithVisa();

        /**
         * 点击手动记账
         */
        void clickAddDebtByHand();

        /**
         * 点击网贷渠道
         *
         * @param index 点击位置
         */
        void clickSourceChannel(int index);

        /**
         * 点击更多网贷平台
         */
        void clickMoreSourceChannel();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 网贷渠道列表加载完成
         *
         * @param list 网贷渠道列表
         */
        void showSourceChannel(List<DebtChannel> list);

        /**
         * 唤起邮箱导入界面
         */
        void navigateDebtMail();

        /**
         * 唤起网银导入界面
         */
        void navigateDebtVisa();

        /**
         * 唤起手动记账界面
         */
        void navigateDebtHand();

        /**
         * 唤起更多网贷平台界面
         */
        void navigateMoreSourceChannel();

        /**
         * 唤起网贷记账界面
         *
         * @param channel 网贷平台
         */
        void navigateDebtNew(DebtChannel channel);
    }
}
