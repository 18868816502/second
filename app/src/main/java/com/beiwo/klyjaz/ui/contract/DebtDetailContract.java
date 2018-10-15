package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.DebtDetail;

public interface DebtDetailContract {

    interface Presenter extends BasePresenter {

        /**
         * 加载借款项目详情
         */
        void loadDebtDetail(String billId);

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
         * 点击菜单
         */
        void clickMenu();

        /**
         * 点击还款提醒
         */
        void clickUpdateRemind();

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
         * 显示菜单
         *
         * @param editable 是否可编辑
         * @param remind   是否是提醒状态
         */
        void showMenu(boolean editable, boolean remind);

        /**
         * 显示更新还款提醒
         *
         * @param remind 是否提醒
         */
        void showUpdateRemind(boolean remind);

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

        void updateLoanDetail(String billId);

    }
}
