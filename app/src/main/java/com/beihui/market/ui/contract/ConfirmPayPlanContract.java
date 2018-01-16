package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;

import java.util.Date;
import java.util.List;

public interface ConfirmPayPlanContract {

    interface Presenter extends BasePresenter {

        /**
         * 添加附属账单信息,用于稍后删除该账单
         *
         * @param pendingDebt 附属账单信息
         */
        void attachPendingDebt(DebtDetail pendingDebt);

        /**
         * 编辑还款计划金额
         *
         * @param index  编辑的计划位置
         * @param amount 计划金额
         */
        void editPayPlanAmount(int index, String amount);

        /**
         * 编辑还款计划日期
         *
         * @param index 编辑的计划位置
         * @param date  计划日期
         */
        void editPayPlanDate(int index, Date date);

        /**
         * 确认保存还款计划
         */
        void confirmPayPlan();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 显示还款计划摘要信息
         *
         * @param payPlan 还款计划
         */
        void showPayPlanAbstract(PayPlan payPlan);

        /**
         * 显示还款计划详细列表
         *
         * @param list 还款计划列表
         */
        void showPayPlanList(List<PayPlan.RepayPlanBean> list);

        /**
         * 确认保存还款计划成功
         */
        void showConfirmSuccess(String msg);

        /**
         * 显示引导
         */
        void showGuide();
    }
}
