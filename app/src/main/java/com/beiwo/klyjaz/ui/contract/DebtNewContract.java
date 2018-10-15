package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.DebtDetail;

import java.util.Date;

public interface DebtNewContract {

    interface Presenter extends BasePresenter {
        /**
         * 一次性还款
         */
        int METHOD_ONE_TIME = 1;
        /**
         * 分期还款，等额本息
         */
        int METHOD_EVEN_DEBT = 2;

        /**
         * 编辑模式，设置原有账单信息
         *
         * @param debtDetail 原有账单信息
         */
        void attachDebtDetail(DebtDetail debtDetail);

        /**
         * 保存一次性还款账单
         *
         * @param payDate    到期还款日，must
         * @param debtAmount 到期还款金额， must
         * @param capital    本金, non-must
         * @param timeLimit  借款期限, non-must
         * @param remark     备注，non-must
         */
        void saveOneTimeDebt(Date payDate, String debtAmount, String capital, String timeLimit, String remark);

        /**
         * 保存分期还款账单
         *
         * @param payDate    首次还款日，must
         * @param timeLimit  借款期限，must
         * @param termAmount 每月还款金额，must
         * @param capital    本金，non-must
         * @param remark     备注，non-must
         */
        void saveEvenDebt(Date payDate, String timeLimit, String termAmount, String capital, String remark);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 绑定原有账单信息
         *
         * @param debtDetail 原有账单信息
         */
        void bindDebtDetail(DebtDetail debtDetail);

        /**
         * 添加账单成功
         */
        void saveDebtSuccess(String msg);

    }
}
