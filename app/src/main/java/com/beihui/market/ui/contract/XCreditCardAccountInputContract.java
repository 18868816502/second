package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.DebtChannel;

import java.util.List;

/**
 * @author xhb
 * 添加信用卡账单
 */
public interface XCreditCardAccountInputContract {

    interface Presenter extends BasePresenter {

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

    }

    interface View extends BaseView<Presenter> {

        /**
         * 网贷渠道列表加载完成
         *
         * @param list 网贷渠道列表
         */
        void showSourceChannel(List<DebtChannel> list);

        /**
         * 唤起用户邮箱界面
         */
        void navigateUsedMail();

        /**
         * 唤起坚果邮箱界面
         */
        void navigateNutMail();

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
