package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;

import java.util.Date;

public interface DebtAddContract {

    interface Presenter extends BasePresenter {
        int METHOD_ONE_TIME = 1;
        int METHOD_EVEN_DEBT = 2;
        int METHOD_EVEN_CAPITAL = 3;

        /**
         * 编辑模式填充旧数据
         *
         * @param debtDetail 借款详情
         */
        void attachDebtDetail(DebtDetail debtDetail);

        /**
         * 选择还款方式
         */
        void clickPayMethod();

        /**
         * 选中还款方式
         *
         * @param index 选中的位置
         */
        void clickPayMethod(int index);

        /**
         * 选中借款渠道
         *
         * @param channel 借款渠道
         */
        void selectDebtChannel(DebtChannel channel);

        /**
         * 保存账单
         *
         * @param projectName     账单名字，可空
         * @param capital         本金
         * @param term            借款期限
         * @param startDate       起息日期
         * @param debtAmount      到期应还
         * @param everyTermAmount 每期应还，等额本息使用
         * @param termAmount      某期应还 等额本金使用
         * @param termNum         某期期数
         * @param remark          备注
         */
        void saveDebt(String projectName, String capital, String term, Date startDate, String debtAmount, String everyTermAmount, String termAmount, String termNum, String remark);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示附属数据
         *
         * @param payMethod   还款方式
         * @param termLife    借款期限
         * @param startDate   起息日期
         * @param capital     借款本金
         * @param projectName 项目名称
         * @param remark      备注
         */
        void showAttachData(int payMethod, int termLife, String startDate, double capital, double debtAmount, String projectName, String remark);

        /**
         * 显示还款方式
         *
         * @param methods 还款方式
         */
        void showMethod(String[] methods);

        /**
         * 显示选中的还款方式
         *
         * @param method     方式type
         * @param methodName 方式名称
         * @param methodDes  方式说明
         */
        void showSelectedMethod(int method, String methodName, String methodDes);

        /**
         * 显示选中的借款渠道
         *
         * @param channel 借款渠道
         */
        void showDebtChannel(DebtChannel channel);

        /**
         * 导航至还款计划
         *
         * @param payPlan 还款计划
         */
        void navigatePayPlan(PayPlan payPlan);

    }
}
