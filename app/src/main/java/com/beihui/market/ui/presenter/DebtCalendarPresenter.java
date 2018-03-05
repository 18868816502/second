package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtCalendar;
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

    private List<DebtCalendar.DetailBean> debts = new ArrayList<>();
    private Map<String, Float> monthDebts;
    private List<String> dates = new ArrayList<>();

    private String curMonth;
    private String curDate;


    private Map<String, List<DebtCalendar.DetailBean>> calendarDebts = new HashMap<>();
    private Map<String, Double[]> calendarDebtAbstract = new HashMap<>();
    //0未还，1已还
    private Map<String, Integer> calendarDebtTag = new HashMap<>();

    private Disposable calendarLastRequest;


    @Inject
    DebtCalendarPresenter(Context context, Api api, DebtCalendarContract.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void loadCalendarDebt(Date date, boolean isMonthUnit) {
        if (calendarLastRequest != null) {
            calendarLastRequest.dispose();
        }
        curDate = dateFormat.format(date);
        String beginDay, endDay;
        if (isMonthUnit) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            beginDay = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            endDay = dateFormat.format(calendar.getTime());
        } else {
            beginDay = endDay = curDate;
        }

        Disposable dis = api.queryDebtCalendar(userHelper.getProfile().getId(), beginDay, endDay, 3, true)
                .compose(RxUtil.<ResultEntity<DebtCalendar>>io2main())
                .subscribe(new Consumer<ResultEntity<DebtCalendar>>() {
                               @Override
                               public void accept(ResultEntity<DebtCalendar> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debts.clear();
                                       calendarDebtTag.clear();

                                       DebtCalendar debtCalendar = result.getData();
                                       if (debtCalendar != null) {
                                           view.showDebtAbstractInfo(debtCalendar.getPayableAmount(), debtCalendar.getReturnedAmount(), debtCalendar.getStayReturnedAmount());

                                           if (debtCalendar.getDetail() != null && debtCalendar.getDetail().size() > 0) {
                                               debts.addAll(result.getData().getDetail());
                                           }
                                           Map<String, Integer> unpaid = result.getData().getUnReturnHash();
                                           if (unpaid != null) {
                                               for (Map.Entry<String, Integer> entry : unpaid.entrySet()) {
                                                   calendarDebtTag.put(entry.getKey(), entry.getValue() > 0 ? 0 : 1);
                                               }
                                           }
                                       }
                                       view.showCalendarDebtTag(Collections.unmodifiableMap(calendarDebtTag));

                                       view.showCalendarDebt(Collections.unmodifiableList(debts));
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
        calendarLastRequest = dis;
    }

    @Override
    public void loadMonthDebt(final Date startDate, final Date endDate) {
        Disposable dis = api.queryMonthDebt(userHelper.getProfile().getId(), rangeDateFormat.format(startDate), rangeDateFormat.format(endDate))
                .compose(RxUtil.<ResultEntity<HashMap<String, Float>>>io2main())
                .subscribe(new Consumer<ResultEntity<HashMap<String, Float>>>() {
                               @Override
                               public void accept(ResultEntity<HashMap<String, Float>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       dates.clear();
                                       Calendar calendar = Calendar.getInstance(Locale.CHINA);
                                       calendar.setTime(startDate);
                                       while (calendar.getTime().compareTo(endDate) <= 0) {
                                           dates.add(rangeDateFormat.format(calendar.getTime()));
                                           calendar.add(Calendar.MONTH, 1);
                                       }
                                       monthDebts = result.getData();
                                       view.showMonthsDebtAmount(dates, monthDebts);

                                       if (dates.size() > 0) {
                                           //月份还款数据刷新后，默认选中第一个
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
    public void refreshCurDate() {
        if (curDate != null) {
            try {
                loadCalendarDebt(dateFormat.parse(curDate), false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void refreshCurMonth() {
        if (curMonth != null) {
            try {
                loadMonthlyDebt(rangeDateFormat.parse(curMonth));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clickMonth(int index) {
        try {
            curMonth = dates.get(index);
            loadMonthlyDebt(rangeDateFormat.parse(curMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickDebt(int index) {
        view.navigateDebtDetail(debts.get(index).getRecordId());
    }

    private void loadMonthlyDebt(Date month) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = calendar.getTime();

        Disposable dis = api.queryDebtCalendar(userHelper.getProfile().getId(), dateFormat.format(startDate), dateFormat.format(endDate), 3, false)
                .compose(RxUtil.<ResultEntity<DebtCalendar>>io2main())
                .subscribe(new Consumer<ResultEntity<DebtCalendar>>() {
                               @Override
                               public void accept(ResultEntity<DebtCalendar> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debts.clear();
                                       DebtCalendar debtCalendar = result.getData();
                                       double debtAmount = 0;
                                       double paidAmount = 0;
                                       double unpaidAmount = 0;
                                       if (debtCalendar != null && debtCalendar.getDetail() != null &&
                                               debtCalendar.getDetail().size() > 0) {
                                           debts.addAll(debtCalendar.getDetail());

                                           debtAmount = debtCalendar.getPayableAmount();
                                           paidAmount = debtCalendar.getReturnedAmount();
                                           unpaidAmount = debtCalendar.getStayReturnedAmount();
                                       }
                                       view.showDebtAbstractInfo(debtAmount, paidAmount, unpaidAmount);
                                       view.showCalendarDebt(Collections.unmodifiableList(debts));
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

    /**
     * api changed
     */
    @Deprecated
    private void loadDebts(Date date) {
        String newMonth = rangeDateFormat.format(date);
        final String newDate = dateFormat.format(date);
        if (newMonth.equals(curMonth)) {
            if (calendarDebtAbstract.containsKey(newDate)) {
                Double[] debtAds = calendarDebtAbstract.get(newDate);
                view.showDebtAbstractInfo(debtAds[0], debtAds[1], debtAds[2]);
            } else {
                view.showDebtAbstractInfo(0, 0, 0);
            }

            debts.clear();
            if (calendarDebts.containsKey(newDate)) {
                debts.addAll(calendarDebts.get(newDate));
            }
            view.showCalendarDebt(Collections.unmodifiableList(debts));
        } else {
            final Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date endDate = calendar.getTime();

            Disposable dis = api.queryDebtCalendar(userHelper.getProfile().getId(), dateFormat.format(startDate), dateFormat.format(endDate), 3, false)
                    .compose(RxUtil.<ResultEntity<DebtCalendar>>io2main())
                    .subscribe(new Consumer<ResultEntity<DebtCalendar>>() {
                                   @Override
                                   public void accept(ResultEntity<DebtCalendar> result) throws Exception {
                                       if (result.isSuccess()) {
                                           calendarDebtTag.clear();
                                           calendarDebtAbstract.clear();
                                           calendarDebts.clear();

                                           DebtCalendar debtCalendar = result.getData();
                                           if (debtCalendar != null && debtCalendar.getDetail() != null &&
                                                   debtCalendar.getDetail().size() > 0) {

                                               List<DebtCalendar.DetailBean> list = debtCalendar.getDetail();
                                               for (DebtCalendar.DetailBean bean : list) {
                                                   String dateStr = bean.getTermRepayDate();
                                                   if (calendarDebts.containsKey(dateStr)) {
                                                       //如果有待还的则优先显示待还
                                                       calendarDebtTag.put(dateStr, calendarDebtTag.get(dateStr) & (bean.getStatus() - 1));

                                                       Double[] debtAds = calendarDebtAbstract.get(dateStr);
                                                       debtAds[0] += bean.getTermPayableAmount();
                                                       if (bean.getStatus() == 2) {
                                                           debtAds[1] += bean.getTermPayableAmount();
                                                       } else {
                                                           debtAds[2] += bean.getTermPayableAmount();
                                                       }

                                                       List<DebtCalendar.DetailBean> debtList = calendarDebts.get(dateStr);
                                                       debtList.add(bean);
                                                   } else {
                                                       calendarDebtTag.put(dateStr, bean.getStatus() - 1);

                                                       Double[] debtAbs = new Double[3];
                                                       debtAbs[0] = bean.getTermPayableAmount();
                                                       if (bean.getStatus() == 2) {
                                                           debtAbs[1] = bean.getTermPayableAmount();
                                                           debtAbs[2] = 0.0;
                                                       } else {
                                                           debtAbs[1] = 0.0;
                                                           debtAbs[2] = bean.getTermPayableAmount();
                                                       }
                                                       calendarDebtAbstract.put(dateStr, debtAbs);

                                                       List<DebtCalendar.DetailBean> debtList = new ArrayList<>();
                                                       debtList.add(bean);
                                                       calendarDebts.put(dateStr, debtList);
                                                   }
                                               }
                                           }

                                           view.showCalendarDebtTag(Collections.unmodifiableMap(calendarDebtTag));

                                           if (calendarDebtAbstract.containsKey(newDate)) {
                                               Double[] debtAds = calendarDebtAbstract.get(newDate);
                                               view.showDebtAbstractInfo(debtAds[0], debtAds[1], debtAds[2]);
                                           } else {
                                               view.showDebtAbstractInfo(0, 0, 0);
                                           }

                                           debts.clear();
                                           if (calendarDebts.containsKey(newDate)) {
                                               debts.addAll(calendarDebts.get(newDate));
                                           }
                                           view.showCalendarDebt(Collections.unmodifiableList(debts));
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
        curMonth = newMonth;
        curDate = newDate;
    }
}
