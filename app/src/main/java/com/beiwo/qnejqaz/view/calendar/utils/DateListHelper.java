package com.beiwo.qnejqaz.view.calendar.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateListHelper {

    public static final int ROW = 6;
    public static final int COLUMN = 7;

    private static Date startDate;
    private static Date endDate;

    private static Calendar calendar = Calendar.getInstance(Locale.CHINA);

    public static void initDateRange(Date start, Date end) {
        calendar.setTime(start);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = calendar.getTime();

        calendar.setTime(end);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        endDate = calendar.getTime();
    }

    public static int getDateMonthRange() {
        return monthsFromStartDate(endDate);
    }

    public static int getDateWeeksRange() {
        return weeksFromStartDate(endDate);
    }

    public static int monthsFromStartDate(Date date) {
        checkStartDate();

        Calendar start = Calendar.getInstance(Locale.CHINA);
        start.setTime(startDate);
        Calendar end = Calendar.getInstance(Locale.CHINA);
        end.setTime(date);

        return (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + (end.get(Calendar.MONTH) - start.get(Calendar.MONTH));
    }

    public static int weeksFromStartDate(Date date) {
        checkStartDate();

        Calendar start = Calendar.getInstance(Locale.CHINA);
        start.setTime(startDate);
        Calendar end = Calendar.getInstance(Locale.CHINA);
        end.setTime(date);

        long days = TimeUnit.DAYS.convert(date.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
        int extra = 0;
        if (days % 7 != 0 && end.get(Calendar.DAY_OF_WEEK) <= start.get(Calendar.DAY_OF_WEEK)) {
            extra = 1;
        }
        return (int) days / 7 + extra;
    }

    public static Date calculateMonth(int position) {
        checkStartDate();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, position);
        return calendar.getTime();
    }

    public static Date calculateWeeks(int position) {
        checkStartDate();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, position * COLUMN);
        return calendar.getTime();
    }

    /**
     * 比较两个日期月份大小
     *
     * @return 相等返回0，date1小于date2返回负数，date1大于date2返回正数
     */
    public static int compareMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance(Locale.CHINA);
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance(Locale.CHINA);
        cal2.setTime(date2);

        int year = cal1.get(Calendar.YEAR) >= cal2.get(Calendar.YEAR) ? (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) ? 0 : 1) : -1;
        int month = cal1.get(Calendar.MONTH) >= cal2.get(Calendar.MONTH) ? (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) ? 0 : 1) : -1;
        return year * 2 + month;
    }

    public static List<Date> generateMonthDateList(int position) {
        checkStartDate();

        List<Date> list = new ArrayList<>();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, position);
        int monthSize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date curMonthDate = calendar.getTime();

        //填充上一个月date
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = dayOfWeek - 1; i > 0; --i) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            list.add(0, calendar.getTime());
        }
        //添加当月date
        calendar.setTime(curMonthDate);
        for (int i = 0; i < monthSize; ++i) {
            list.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //填充下一月date，补全所需数据
        calendar.setTime(curMonthDate);
        calendar.add(Calendar.MONTH, 1);
        int needSize = ROW * COLUMN - list.size();
        for (int i = 0; i < needSize; ++i) {
            list.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public static List<Date> generateWeekDateList(int position) {
        checkStartDate();

        List<Date> list = new ArrayList<>();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, position * COLUMN);
        Date curDate = calendar.getTime();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = dayOfWeek - 1; i > 0; --i) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            list.add(0, calendar.getTime());
        }
        calendar.setTime(curDate);
        for (int i = dayOfWeek; i <= 7; ++i) {
            list.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        return list;
    }

    public static boolean equalsMonth(Date date1, Date date2) {
        calendar.setTime(date1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        calendar.setTime(date2);
        return year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH);
    }

    public static boolean equalsDay(Date date1, Date date2) {
        calendar.setTime(date1);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(date2);
        return year == calendar.get(Calendar.YEAR) && day == calendar.get(Calendar.DAY_OF_YEAR);

    }

    private static void checkStartDate() {
        if (startDate == null) {
            throw new IllegalStateException("start date has not been set.");
        }
    }
}
