package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.CalendarAbstract;
import com.beihui.market.entity.CalendarDebt;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.DebtCalendarContract;
import com.beihui.market.util.RxUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtCalendarPresenter extends BaseRxPresenter implements DebtCalendarContract.Presenter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private final SimpleDateFormat rangeDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);

    private Api api;
    private DebtCalendarContract.View view;
    private UserHelper userHelper;

    //账单摘要
    //0未还，1已还
    private Map<String, Integer> calendarAbstract = new HashMap<>();
    //账单趋势
    private List<String> trendDateList = new ArrayList<>();
    private Map<String, Float> calendarTrend = new HashMap<>();
    //账单列表
    private List<CalendarDebt.DetailBean> calendarDebtList = new ArrayList<>();

    @Inject
    DebtCalendarPresenter(Context context, Api api, DebtCalendarContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void fetchCalendarAbstract(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String beginDate = dateFormat.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        String endDate = dateFormat.format(calendar.getTime());

        Disposable dis = api.fetchCalendarAbstract(userHelper.getProfile().getId(), beginDate, endDate)
                .compose(RxUtil.<ResultEntity<CalendarAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<CalendarAbstract>>() {
                               @Override
                               public void accept(ResultEntity<CalendarAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       Map<String, Integer> unpaid = result.getData().getUnReturnHash();
                                       if (unpaid != null && unpaid.size() > 0) {
                                           for (Map.Entry<String, Integer> entry : unpaid.entrySet()) {
                                               calendarAbstract.put(entry.getKey(), entry.getValue() > 0 ? 0 : 1);
                                           }
                                       }
                                       view.showCalendarAbstract(Collections.unmodifiableMap(calendarAbstract));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtCalendarPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }


    @Override
    public void fetchCalendarTrend(Date startDate, Date endDate) {
        final Date start = startDate;
        final Date end = endDate;
        Disposable dis = api.fetchCalendarTrend(userHelper.getProfile().getId(), rangeDateFormat.format(startDate), rangeDateFormat.format(endDate))
                .compose(RxUtil.<ResultEntity<Map<String, Float>>>io2main())
                .subscribe(new Consumer<ResultEntity<Map<String, Float>>>() {
                               @Override
                               public void accept(ResultEntity<Map<String, Float>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       trendDateList.clear();
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           //添加月份显示列表
                                           Calendar calendar = Calendar.getInstance(Locale.CHINA);
                                           calendar.setTime(start);
                                           while (calendar.getTime().compareTo(end) < 0) {
                                               trendDateList.add(rangeDateFormat.format(calendar.getTime()));
                                               calendar.add(Calendar.MONTH, 1);
                                           }

                                           calendarTrend.putAll(result.getData());
                                       }
                                       view.showCalendarTrend(Collections.unmodifiableList(trendDateList), Collections.unmodifiableMap(calendarTrend));
                                       //如果有数据则默认选中第一个
                                       if (calendarTrend.size() > 0) {
                                           clickMonth(0);
                                       }
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtCalendarPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchDebtList(Date date, boolean queryMonth) {
        String beginDate, endDate;
        if (queryMonth) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            beginDate = dateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            endDate = dateFormat.format(calendar.getTime());
        } else {
            beginDate = endDate = dateFormat.format(date);
        }

        Disposable dis = api.fetchCalendarDebt(userHelper.getProfile().getId(), beginDate, endDate)
                .compose(RxUtil.<ResultEntity<CalendarDebt>>io2main())
                .subscribe(new Consumer<ResultEntity<CalendarDebt>>() {
                               @Override
                               public void accept(ResultEntity<CalendarDebt> result) throws Exception {
                                   if (result.isSuccess()) {
                                       calendarDebtList.clear();
                                       double debtAmount = 0;
                                       double paidAmount = 0;
                                       double unpaidAmount = 0;
                                       CalendarDebt calendarDebt = result.getData();
                                       if (calendarDebt != null) {
                                           //账单统计信息
                                           debtAmount = calendarDebt.getPayableAmount();
                                           paidAmount = calendarDebt.getReturnedAmount();
                                           unpaidAmount = calendarDebt.getStayReturnedAmount();

                                           if (calendarDebt.getDetail() != null && calendarDebt.getDetail().size() > 0) {
                                               calendarDebtList.addAll(calendarDebt.getDetail());
                                           }
                                       }
                                       //显示账单统计信息
                                       view.showCalendarDebtSumInfo(debtAmount, paidAmount, unpaidAmount);
                                       //显示账单记录列表
                                       view.showCalendarDebtList(Collections.unmodifiableList(calendarDebtList));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtCalendarPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void clickMonth(int index) {
        try {
            fetchDebtList(rangeDateFormat.parse(trendDateList.get(index)), true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickDebt(int index) {
        CalendarDebt.DetailBean bill = calendarDebtList.get(index);
        if (bill.getBillType() == 1) {
            //网贷账单
            view.navigateLoanDebtDetail(bill.getRecordId());
        } else {
            //信用卡账单
            view.navigateCreditCardDebtDetail(bill.getRecordId(), bill.getBillId(), bill.getLogo(), bill.getTopic(), bill.getBillName(), bill.getBillSource() == 3);
        }
    }

}
