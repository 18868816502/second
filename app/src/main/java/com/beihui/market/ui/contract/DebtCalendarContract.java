package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.CalendarDebt;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DebtCalendarContract {

    interface Presenter extends BasePresenter {

        /**
         * 获取账单记录当月摘要，即那些天有账单
         *
         * @param date 查询时间，以月为单位
         */
        void fetchCalendarAbstract(Date date);

        /**
         * 获取账单趋势
         *
         * @param startDate 查询开始日期
         * @param endDate   查询结束日期
         */
        void fetchCalendarTrend(Date startDate, Date endDate);

        /**
         * 获取账单列表
         *
         * @param date       查询时间
         * @param queryMonth 是否是以月为单位
         */
        void fetchDebtList(Date date, boolean queryMonth);

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
         * 显示日历模式账单记录月份摘要
         *
         * @param calendarAbstract 月份摘要
         */
        void showCalendarAbstract(Map<String, Integer> calendarAbstract);

        /**
         * 显示日历模式账单记录月份趋势
         *
         * @param dateList      月份列表
         * @param calendarTrend 月份趋势金额列表
         */
        void showCalendarTrend(List<String> dateList, Map<String, Float> calendarTrend);

        /**
         * 显示账单列表统计信息
         *
         * @param debtAmount   账单金额
         * @param paidAmount   已还金额
         * @param unpaidAmount 待还金额
         */
        void showCalendarDebtSumInfo(double debtAmount, double paidAmount, double unpaidAmount);

        /**
         * 显示账单列表
         *
         * @param list 账单列表
         */
        void showCalendarDebtList(List<CalendarDebt.DetailBean> list);

        /**
         * 唤起网贷账单详情界面
         *
         * @param id 账单
         */
        void navigateLoanDebtDetail(String id);

        /**
         * 唤起信用卡账单详情界面
         *
         * @param id       账单id
         * @param logo     银行icon
         * @param bankName 银行名称
         * @param cardNum  卡号
         * @param byHand   是否手动账单
         */
        void navigateCreditCardDebtDetail(String id, String logo, String bankName, String cardNum, boolean byHand);

    }
}
