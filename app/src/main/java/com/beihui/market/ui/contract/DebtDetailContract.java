package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.DebtDetail;

public interface DebtDetailContract {

    interface Presenter extends BasePresenter {

        /**
         * 加载借款项目详情
         */
        void loadDebtDetail();

        /**
         * 点击设置还款状态
         *
         * @param index 点击位置，如果index=-1，则当前点击的是当前期
         */
        void clickSetStatus(int index);

        /**
         * 当期借款项目设置为已还
         */
        void updateDebtStatus();

        /**
         * 更新借款项目状态
         *
         * @param index  借款项目位置
         * @param status 新状态
         */
        void updateDebtStatus(int index, int status);

        /**
         * 删除借款信息
         */
        void deleteDebt();

        /**
         * 编辑借款信息
         */
        void editDebt();

    }

    interface View extends BaseView<Presenter> {

        /**
         * 借款详情加载完成
         *
         * @param debtDetail 借款详情
         */
        void showDebtDetail(DebtDetail debtDetail);

        /**
         * 显示设置还款状态按钮
         *
         * @param index     选中的位置，-1则为当前期
         * @param newStatus 新的还款状态
         */
        void showSetStatus(int index, int newStatus);

        /**
         * 更新账单状态成功
         *
         * @param msg 相关消息
         */
        void showUpdateStatusSuccess(String msg);

        /**
         * 删除账单信息成功
         *
         * @param msg 相关消息
         */
        void showDeleteDebtSuccess(String msg);

        /**
         * 导航至新增账单
         *
         * @param debtDetail 借款详情
         */
        void navigateAddDebt(DebtDetail debtDetail);
    }
}
