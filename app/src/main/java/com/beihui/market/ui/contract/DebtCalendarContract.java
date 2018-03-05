package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.DebtCalendar;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DebtCalendarContract {

    interface Presenter extends BasePresenter {
        /**
         * 加载还款日历
         *
         * @param date 日期
         */
        void loadCalendarDebt(Date date, boolean isMonthUnit);

        /**
         * 加载还款日历
         *
         * @param startDate 起始日期
         * @param endDate   截止日期
         */
        void loadMonthDebt(Date startDate, Date endDate);

        /**
         * 刷新当前选中日期数据
         */
        void refreshCurDate();

        /**
         * 刷新当前选中月份数据
         */
        void refreshCurMonth();

        /**
         * 点击选中月份
         *
         * @param index 点击位置
         */
        void clickMonth(int index);

        /**
         * 点击账单
         *
         * @param index 点击位置
         */
        void clickDebt(int index);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 还款日历加载完成
         *
         * @param list 账单列表
         */
        void showCalendarDebt(List<DebtCalendar.DetailBean> list);

        /**
         * 更新摘要信息
         *
         * @param debtAmount   应还总额
         * @param paidAmount   已还金额
         * @param unpaidAmount 待还金额
         */
        void showDebtAbstractInfo(double debtAmount, double paidAmount, double unpaidAmount);

        /**
         * 更新月份借款数据
         *
         * @param date  查询日期列表
         * @param debts 借款总额map
         */
        void showMonthsDebtAmount(List<String> date, Map<String, Float> debts);

        /**
         * 导航至借款详情
         *
         * @param id 借款项目id
         */
        void navigateDebtDetail(String id);

        /**
         * 显示每月借款记录，对应UI上小圆点
         *
         * @param debtHint 借款记录
         */
        void showCalendarDebtTag(Map<String, Integer> debtHint);
    }
}
