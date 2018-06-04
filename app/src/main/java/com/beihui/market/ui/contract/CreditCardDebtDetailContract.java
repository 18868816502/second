package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.entity.CreditCardDebtDetail;

import java.util.List;

public interface CreditCardDebtDetailContract {

    interface Presenter extends BasePresenter {
        /**
         * 加载账单详情
         */
        void fetchDebtDetail(String billId);

        /**
         * 加载月份账单列表
         */
        void fetchDebtMonthBill();

        /**
         * 加载月份账单详情
         *
         * @param dataIndex 月份账单数据的位置
         * @param viewIndex 月份账单view的位置
         */
        void fetchBillDetail(int dataIndex, int viewIndex);

        /**
         * 更新月份账单金额
         *
         * @param index  选择的位置
         * @param amount 更新的金额
         */
        void updateBillAmount(int index, double amount);

        /**
         * 点击设为已还
         */
        void clickSetStatus();

        /**
         * 点击菜单
         */
        void clickMenu();

        /**
         * 点击还款提醒
         */
        void clickUpdateRemind();

        /**
         * 点击编辑
         */
        void clickEdit();

        /**
         * 点击删除
         */
        void clickDelete();


    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示账单详情信息
         *
         * @param debtDetail 账单详情
         */
        void showDebtDetailInfo(CreditCardDebtDetail debtDetail);

        /**
         * 显示账单详情信息
         *
         */
        void showStatus(int status);

        /**
         * 显示月份账单列表
         *
         * @param list        月份账单列表
         * @param canLoadMore 是否还能加载更多
         */
        void showDebtBillList(List<CreditCardDebtBill> list, boolean canLoadMore);

        /**
         * 显示月份账单详情
         *
         * @param list  月份账单详情
         * @param index 月份账单的位置
         */
        void showBillDetail(List<BillDetail> list, int index);

        /**
         * 显示设为已还成功
         */
        void showSetStatusSuccess();

        /**
         * 显示菜单
         *
         * @param remind 是否设置了还款提醒
         */
        void showMenu(boolean remind);

        /**
         * 删除成功
         */
        void showDeleteSuccess();

        /**
         * 更新还款提醒成功
         *
         * @param remind 是否提醒
         */
        void showUpdateRemindStatus(boolean remind);

        /**
         * 更新账单金额成功
         */
        void showUpdateBillAmountSuccess();

    }
}
