package com.beihui.market.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class TimeUtils {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat sdformat7 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdformat19 = new SimpleDateFormat("yyyy.MM.dd");
	public static SimpleDateFormat sdformat2 = new SimpleDateFormat("MM-dd HH:mm");
	public static SimpleDateFormat sdformat3 = new SimpleDateFormat("MM");
	public static SimpleDateFormat sdformat4 = new SimpleDateFormat("dd");
	public static SimpleDateFormat sdformat5 = new SimpleDateFormat("yyyy/MM/dd");
	public static SimpleDateFormat sdformat8 = new SimpleDateFormat("yyyy年MM月dd日");
	public static SimpleDateFormat sdformat9 = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat sdformat6 = new SimpleDateFormat("MM-dd");
	public static SimpleDateFormat year = new SimpleDateFormat("yyyy");
	public static SimpleDateFormat xingqi = new SimpleDateFormat("F");
	public static SimpleDateFormat sdformat10 = new SimpleDateFormat("yyyy-MM");
	public static SimpleDateFormat sdformat11 = new SimpleDateFormat("MM月dd日");
	public static SimpleDateFormat sdformat12 = new SimpleDateFormat("HH");
	public static SimpleDateFormat sdformat13 = new SimpleDateFormat("mm");

	public static SimpleDateFormat sdformat14 = new SimpleDateFormat("MM/dd");
	public static SimpleDateFormat sdformat15 = new SimpleDateFormat("MM/dd HH:mm");
	public static SimpleDateFormat sdformat16 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdformat17 = new SimpleDateFormat("yyyy年MM月");


	public static SimpleDateFormat sdformat18 = new SimpleDateFormat("yyyyMMddHHmmss");

	/** 获取时间 yyyy-MM-dd HH:mm */
	public static String getTime(Date date) {
		return sdformat.format(date);
	}


	public static String getSingTime(Date date) {
		return sdformat18.format(date);
	}



	/** 获取时间 yyyy-MM-dd HH:mm:ss */
	public static String getTimesdf(Date date) {
		return sdf.format(date);
	}

	/** 获取时间 yyyy-MM-dd */
	public static String getTime2(Date date) {
		return sdformat7.format(date);
	}

	/** 获取时间 yyyy-MM */
	public static String getTime10(Date date) {
		return sdformat10.format(date);
	}

	/** 获取时间 HH:mm */
	public static String getTime9(Date date) {
		return sdformat9.format(date);
	}

	/** 获取时间 yyyy-MM-dd */
	public static String getTime5(Date date) {
		return sdformat5.format(date);
	}
	/** 获取时间 yy-MM-dd */
	public static String getTime16(Date date) {
		return sdformat16.format(date);
	}
	public static String getTimeYear(Date date) {
		return year.format(date);
	}

	public static String getTimeMonth(Date date) {
		return sdformat3.format(date);
	}

	public static String getTimeDay(Date date) {
		return sdformat4.format(date);
	}

	/** 获取时间MM月dd日 */
	public static String getTime11(Date date) {
		return sdformat11.format(date);
	}


	public static String longToDate(String millSec){
		long millSecLong = Long.valueOf(millSec);
		Date date= new Date(millSecLong);
		return sdformat7.format(date);
	}


	public static String longToDate1(String millSec){
		long millSecLong = Long.valueOf(millSec);
		Date date= new Date(millSecLong);
		return sdformat19.format(date);
	}


	/** 审批获取年 */
	public static String appGetYear(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}
	/** 获取年 yyyy-MM */
	public static String getYear10(String time) {
		Date date = null;
		try {
			date = sdformat10.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}
	/** 获取月 yyyy-MM */
	public static String getMonth10(String time) {
		Date date = null;
		try {
			date = sdformat10.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdformat3.format(date);
	}
	/** 月份添加 输入 yyyy-MM 返回 yyyy-MM */
	public static String MonthAdd(String time,int num){
		String year = TimeUtils.getYear10(time);
		String month = TimeUtils.getMonth10(time);

		int nowYear = Integer.valueOf(year);
		int nowMonth = Integer.valueOf(month)-1;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, nowYear);
		calendar.set(Calendar.MONTH, nowMonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.add(Calendar.MONTH,num);
		Date date = calendar.getTime();
		return sdformat10.format(date);
	}
	/** 获取年 yyyy-MM-dd */
	public static String getYear7(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}
	/** 获取月 yyyy-MM */
	public static String getMonth7(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdformat3.format(date);
	}
	/** 获取日 yyyy-MM */
	public static String getDay7(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdformat4.format(date);
	}
	/** 月份添加 输入 yyyy-MM-dd 返回 yyyy-MM-dd */
	public static String DayAdd(String time,int num){
		String year = TimeUtils.getYear7(time);
		String month = TimeUtils.getMonth7(time);
		String day = TimeUtils.getDay7(time);

		int nowYear = Integer.valueOf(year);
		int nowMonth = Integer.valueOf(month)-1;
		int nowDay = Integer.valueOf(day);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, nowYear);
		calendar.set(Calendar.MONTH, nowMonth);
		calendar.set(Calendar.DAY_OF_MONTH, nowDay);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.add(Calendar.DAY_OF_MONTH,num);
		Date date = calendar.getTime();
		return sdformat7.format(date);
	}

	/** 日期转换 yyyy-MM   --> yyyy年MM月 */
	public static String getTime10To17(String time) {
		Date date = null;
		try {
			date = sdformat10.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat17.format(date);
	}

	/** 首次打卡设备时间 */
	public static String getDeviceTime(String time) {
		Date date = null;
		try {
			date = sdformat10.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdformat16.format(date);
	}
	/** 审批获取年 */
	public static String appGetYearFor5(String time) {
		Date date = null;
		try {
			date = sdformat5.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}

	/** MM-dd */
	public static String appMonthAndDay(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat6.format(date);
	}

	/** yyyy-MM-dd */
	public static String appYearMonthAndDay(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat7.format(date);
	}

	/** 今日日历获取年 */
	public static String appTodayGetYear(Date date) {

		return year.format(date);
	}

	/** 今日日历获取月 */
	public static String appTodayGetmonthOfYear(Date date) {
		return sdformat3.format(date);
	}

	/** 今日日历获取月日 */
	public static String appTodayGetMonthDay(Date date) {

		return sdformat6.format(date);
	}

	/** 审批获取月 */
	public static String appGetmonthOfYear(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat3.format(date);
	}

	/** 审批获取月 */
	public static String appGetmonthOfYearFor5(String time) {
		Date date = null;
		try {
			date = sdformat5.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat3.format(date);
	}

	/** 审批获取日 */
	public static String appGetdayOfMonth(String time) {
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat4.format(date);
	}

	/** 审批获取日 */
	public static String appGetdayOfMonthFor5(String time) {
		Date date = null;
		try {
			date = sdformat5.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat4.format(date);
	}

	/** 审批获取日 */
	public static String appGetday(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdformat4.format(date);
	}

	/** 获取考勤的服务器时间，转换格式 */
	public static String appGetServiceTime(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdf.format(date);
	}

	/** 获取我的上下班打卡时间时间，转换格式 */
	public static String appGetCheckinTime(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		return sdformat9.format(date);
	}

	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/** 获取星期 */
	public static String appGetdayOfXingQi(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/** 获取周 返回 周几 yyyy-MM-dd HH:mm:ss */
	public static String appGetdayOfZhou(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/** 获取周 返回 周几 yyyy-MM-dd */
	public static String getWeek(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/** 获取周 返回 星期几*/
	public static String getNowWeek() {
		Date date = date = new Date();
		String[] weekDays = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}



	/**
	 * 星期几
	 * 
	 * @return
	 */
	public static String getDays(int day) {
		String days = "星期";
		if (day > 7) {
			day = day - 7;
		}
		switch (day) {
		case 2:
			days = days + "一";
			break;
		case 3:
			days = days + "二";
			break;
		case 4:
			days = days + "三";
			break;
		case 5:
			days = days + "四";
			break;
		case 6:
			days = days + "五";
			break;
		case 7:
			days = days + "六";
			break;
		case 1:
			days = days + "天";
			break;

		default:
			break;
		}
		return days;
	}

	/**
	 * 周几
	 * 
	 * @return
	 */
	public static String getDays_Zhou(int day) {
		String days = "";
		if (day > 7) {
			day = day - 7;
		}
		switch (day) {
		case 2:
			days = days + "一";
			break;
		case 3:
			days = days + "二";
			break;
		case 4:
			days = days + "三";
			break;
		case 5:
			days = days + "四";
			break;
		case 6:
			days = days + "五";
			break;
		case 7:
			days = days + "六";
			break;
		case 1:
			days = days + "日";
			break;

		default:
			break;
		}
		return days;
	}

	/**
	 * 周几
	 * 
	 * @return
	 */
	public static String getDaysWeek(int day) {
		String days = "周";
		if (day > 7) {
			day = day - 7;
		}
		switch (day) {
		case 2:
			days = days + "一";
			break;
		case 3:
			days = days + "二";
			break;
		case 4:
			days = days + "三";
			break;
		case 5:
			days = days + "四";
			break;
		case 6:
			days = days + "五";
			break;
		case 7:
			days = days + "六";
			break;
		case 1:
			days = days + "日";
			break;

		default:
			break;
		}
		return days;
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return MM-dd
	 */
	public static String formatTime6(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat6.format(date);
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return MM-dd
	 */
	public static String formatTime10(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat10.format(date);
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd
	 * @return MM-dd
	 */
	public static String formatTime2_10(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat10.format(date);
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return yyyy/MM/dd
	 */
	public static String formatTime5(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat5.format(date);
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return yyyy年MM月dd
	 */
	public static String formatTime8(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat8.format(date);
	}

	/**
	 * 
	 * @param time
	 *            HH:mm
	 * @return 8:33
	 */
	public static String formatTime9(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat9.format(date);
	}

	/**
	 * 
	 * @param time
	 *            HH:mm
	 * @return 8:33
	 */
	public static String formatTimeYear(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}

	/**
	 * 
	 * @param time
	 *            HH:mm
	 * @return 8:33
	 */
	public static String formatTimeMonth(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat3.format(date);
	}

	/**
	 * 
	 * @param time
	 *            HH:mm
	 * @return 8:33
	 */
	public static String formatTimeDay(String time) {
		if (TextUtils.isEmpty(time)) {
			return "";
		}
		Date date = null;
		try {
			time = time.replace("T", " ");
			date = sdf.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat4.format(date);
	}

	/**
	 * 
	 *            yyyy-MM
	 * @return 8:33
	 */
	public static String getCurrtTime() {
		Date date = new Date(System.currentTimeMillis());
		return sdformat10.format(date);
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return 负数 过期几天 正数 剩余几天
	 */
	public static long getDay(String time) {
		if (TextUtils.isEmpty(time))
			return 0;
		time = time.replace("T", " ");
		Date setDay;
		try {
			setDay = sdf.parse(time);
		} catch (ParseException e1) {
			setDay = new Date();
		}
		// 午夜12点准点
		String setStr =
				year.format(setDay) + "-" + sdformat3.format(setDay) + "-"
						+ sdformat4.format(setDay) + " 24:00";
		long setTime = 0;
		try {
			setTime = sdformat.parse(setStr).getTime();
		} catch (ParseException e) {
			setTime = System.currentTimeMillis();
		}

		long spaceTime = setTime - System.currentTimeMillis();
		if (spaceTime > 0) {
			// 剩余时间
			return ((Math.abs(spaceTime) / (1000 * 60 * 60 * 24))) + 1;
		} else {
			// 逾期时间
			return -(((Math.abs(spaceTime) / (1000 * 60 * 60 * 24))) + 1);
		}
	}

	/**
	 * 某一个月第一天和最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Map<String, String> getFirstday_Lastday_Month(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		day_first = str.toString();

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		day_last = endStr.toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("first", day_first);
		map.put("last", day_last);
		return map;
	}

	/**
	 * 某一个月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstday_Month(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = sdformat7.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		day_first = str.toString();

		return day_first;
	}

	/**
	 * 某一个月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstday_MonthFor5(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = sdformat5.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		day_first = str.toString();

		return day_first;
	}

	/**
	 * 某一个月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastday_Month(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date theDate = calendar.getTime();

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		day_last = endStr.toString();

		return day_last;
	}

	/**
	 * 某一个月第一天和最后一天
	 * 
	 * @return
	 */
	public static Map<String, String> getFirstday_Lastday_Month() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.MONTH, -1);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		day_first = str.toString();

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		day_last = endStr.toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("first", day_first);
		map.put("last", day_last);
		return map;
	}


	/** "yyyy-MM-dd HH:mm" */
	public static String formatTime(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return sdformat.format(date);
	}

	/**
	 * "yyyy-MM-dd HH:mm" boolean isShort:是否缩略时间显示 比如今年则隐藏年 今天则隐藏月日
	 */
	public static String formatTime(String time, boolean isShort) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		if (!isShort) {
			return sdformat.format(date);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		int year2 = calendar2.get(Calendar.YEAR);
		boolean isHasYear = (year != year2);

		if (isHasYear) {
			return sdformat.format(date);
		} else {
			boolean isShowMonth =
					(calendar.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH) == 0)
							&& (calendar.get(Calendar.DAY_OF_MONTH)
								- calendar2.get(Calendar.DAY_OF_MONTH) == 0);
			if (isShowMonth) {
				return sdformat9.format(date);
			}
			return sdformat2.format(date);
		}
	}

	/**
	 * "yyyy-MM-dd" boolean isShort:是否缩略时间显示 比如今年则隐藏年 return yyyy-MM-dd 或者 MM-dd
	 */
	public static String formatTimeDay(String time, boolean isShort) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			return time;
		}
		if (!isShort) {
			return sdformat7.format(date);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		int year2 = calendar2.get(Calendar.YEAR);
		boolean isHasYear = (year != year2);

		if (isHasYear) {
			return sdformat7.format(date);
		} else {
			return sdformat6.format(date);
		}
	}

	/** "yyyy-MM-dd" */
	public static String formatTime2(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return sdformat7.format(date);
	}

	/** "MM月dd日" */
	public static String formatTime11(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return sdformat11.format(date);
	}

	/** 获取时间毫秒值 */
	public static long getTimeLong(String time) {
		if (time == null) {
			return System.currentTimeMillis();
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return System.currentTimeMillis();
		}
		return date.getTime();
	}

	/** 获取时间毫秒值 如果格式不正确则返回 当前时间戳减去longtime */
	public static long getTimeLong(String time, long longtime) {
		if (time == null) {
			return System.currentTimeMillis();
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return System.currentTimeMillis() - longtime;
		}
		return date.getTime();
	}

	/** 获取时间毫秒值 */
	public static long getTimeLong_day(String time) {
		if (time == null) {
			return System.currentTimeMillis();
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			return System.currentTimeMillis();
		}
		return date.getTime();
	}

	/** MM-dd HH:mm */
	public static String getTime(String time) {
		return getTime(time, false);
	}

	/**
	 * 根据hasyear来显示 是否显示年 MM-dd HH:mm
	 */
	public static String getTime(String time, boolean hasYear) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		long ten_fen = 60 * 1000 * 10;// 10分钟
		long one_day = 60 * 1000 * 60 * 24;// 1天
		long one_hour = 60 * 1000 * 60;// 1个小时
		long one_fen = 60 * 1000;// 1分钟
		long currMillisTime = System.currentTimeMillis();
		long millisTime = date.getTime();
		long relTime = currMillisTime - millisTime;
		if ((relTime) < ten_fen) {// 十分钟以内
			return "刚刚";
		}
		if ((relTime) < one_day) {
			int hours = (int) (relTime / one_hour);
			if (hours > 0) {
				return hours + "小时前";
			} else {
				int fen = (int) (relTime / one_fen);
				return fen + "分钟前";
			}
		}
		// 判断 是否需要显示年 今年的就不显示 不是今年的显示
		if (hasYear) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(new Date());
			int year2 = calendar2.get(Calendar.YEAR);
			hasYear = (year != year2);
		}
		if (hasYear) {
			return sdformat.format(date);
		} else {
			return sdformat2.format(date);
		}
	}

	/**
	 * 根据当前时间来计算 已经过了多久
	 */
	public static String hasGoneLongTime(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		long ten_fen = 60 * 1000 * 10;// 10分钟
		long one_day = 60 * 1000 * 60 * 24;// 1天
		long one_hour = 60 * 1000 * 60;// 1个小时
		long one_fen = 60 * 1000;// 1分钟
		long currMillisTime = System.currentTimeMillis();
		long millisTime = date.getTime();
		long relTime = currMillisTime - millisTime;
		if ((relTime) < (one_day * 2)) {
			int hours = (int) (relTime / one_hour);
			if (hours > 0) {
				return hours + "个小时";
			} else {
				return 1 + "个小时";
			}
		} else {
			int day = (int) (relTime / one_day);
			if (day > 0) {
				return day + "天";
			} else {
				return 1 + "天";
			}
		}
	}

	/**
	 * 将秒转化为时间表示 ［几小时几分几秒］
	 * 
	 * @param second
	 * @return
	 */
	public static String getTimeUnit(long second) {
		if (second >= 3600) {
			long ss = second % 3600;
			if (ss == 0) {
				return (second / 3600) + "小时";
			} else {
				long mm = ss % 60;
				if (mm == 0) {
					return (second / 3600) + "小时" + (ss / 60) + "分";
				} else {
					return (second / 3600) + "小时" + (ss / 60) + "分" + mm + "秒";
				}
			}
		} else {
			if (second >= 60) {
				long mm = second % 60;
				if (mm == 0) {
					return (second / 60) + "分";
				} else {
					return (second / 60) + "分" + mm + "秒";
				}
			} else {
				return second + "秒";
			}
		}
	}

	/**
	 * 判断 日期是否是 今天及以后
	 * 
	 * @param time
	 * @return "yyyy-MM-dd"
	 */
	public static boolean isBigDay(String time) {
		if (time == null) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			return false;
		}
		long result = System.currentTimeMillis() - date.getTime();
		if (result < 1000 * 60 * 60 * 24)
			return true;
		else
			return false;
	}

	/**
	 * 判断 日期是否是 time>=time2
	 * 
	 * @param time
	 * @return "yyyy-MM-dd"
	 */
	public static boolean isBigDay(String time, String time2) {
		if (TextUtils.isEmpty(time2)) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			return false;
		}
		if (time2 == null) {
			return false;
		}
		time2 = time2.replace("T", " ");
		Date date2 = null;
		try {
			date2 = sdformat7.parse(time2);
		} catch (ParseException e) {
			return false;
		}
		long result = date2.getTime() - date.getTime();
		if (result < 1000 * 60 * 60 * 24)
			return true;
		else
			return false;
	}

	/**
	 * 判断 日期是否是 time>=time2
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm
	 * @return "yyyy-MM-dd"
	 */
	public static boolean isBigDay2(String time, String time2) {
		if (TextUtils.isEmpty(time2)) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			return false;
		}
		if (time2 == null) {
			return false;
		}
		time2 = time2.replace("T", " ");
		Date date2 = null;
		try {
			date2 = sdformat.parse(time2);
		} catch (ParseException e) {
			return false;
		}
		long result = date2.getTime() - date.getTime();
		if (result < 1000 * 60 * 60 * 24)
			return true;
		else
			return false;
	}

	/**
	 * 判断 日期是否是 time>time2
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm
	 * @return "yyyy-MM-dd"
	 */
	public static boolean isBigDay2OnlyBig(String time, String time2) {
		if (TextUtils.isEmpty(time2)) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			return false;
		}
		if (time2 == null) {
			return false;
		}
		time2 = time2.replace("T", " ");
		Date date2 = null;
		try {
			date2 = sdformat.parse(time2);
		} catch (ParseException e) {
			return false;
		}
		long result = date2.getTime() - date.getTime();
		if (result < 0)
			return true;
		else
			return false;
	}
	/**
	 * 判断 日期是否是 time>time2
	 *
	 * @param time
	 *            yyyy-MM-dd
	 * @return "yyyy-MM-dd"
	 */
	public static boolean isBigDayOnlyBig(String time, String time2) {
		if (TextUtils.isEmpty(time2)) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat7.parse(time);
		} catch (ParseException e) {
			return false;
		}
		if (time2 == null) {
			return false;
		}
		time2 = time2.replace("T", " ");
		Date date2 = null;
		try {
			date2 = sdformat7.parse(time2);
		} catch (ParseException e) {
			return false;
		}
		long result = date2.getTime() - date.getTime();
		if (result < 0)
			return true;
		else
			return false;
	}

	/**
	 * 判断 日期是否是 time>=time2
	 * 
	 * @param time
	 *            HH:mm
	 * @return "boolean"
	 */
	public static boolean isBigTime(String time, String time2) {
		if (TextUtils.isEmpty(time2)) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat9.parse(time);
		} catch (ParseException e) {
			return false;
		}
		if (time2 == null) {
			return false;
		}
		time2 = time2.replace("T", " ");
		Date date2 = null;
		try {
			date2 = sdformat9.parse(time2);
		} catch (ParseException e) {
			return false;
		}
		long result = date2.getTime() - date.getTime();
		if (result <= 0)
			return true;
		else
			return false;
	}

	/**
	 * 判断 日期是否是 time>time2
	 * 
	 * @param time
	 *            HH:mm
	 * @return "boolean"
	 */
	public static boolean isBigTimeOnlyBig(String time, String time2) {
		if (TextUtils.isEmpty(time2)) {
			return false;
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdformat9.parse(time);
		} catch (ParseException e) {
			return false;
		}
		if (time2 == null) {
			return false;
		}
		time2 = time2.replace("T", " ");
		Date date2 = null;
		try {
			date2 = sdformat9.parse(time2);
		} catch (ParseException e) {
			return false;
		}
		long result = date2.getTime() - date.getTime();
		if (result < 0)
			return true;
		else
			return false;
	}

	/**
	 * 获取本周的第一天 （周一）
	 * 
	 * @return "yyyy-MM-dd"
	 */
	public static String getNowWeekBegin() {
		int mondayPlus;
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			mondayPlus = 0;
		} else {
			mondayPlus = 1 - dayOfWeek;
		}
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		String preMonday = sdformat7.format(monday);

		return preMonday;
	}

	/**
	 * 获取本周的最后一天 （周日）
	 * 
	 * @return "yyyy-MM-dd"
	 */
	public static String getNowWeekEnd() {
		int mondayPlus;
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 0) {
			mondayPlus = 0;
		} else {
			mondayPlus = 7 - dayOfWeek;
		}
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		String preMonday = sdformat7.format(monday);

		return preMonday;
	}

	/**
	 * 获取本周的第一天 （周一）
	 * 
	 * @return "yyyy-MM-dd"
	 */
	public static String getNowWeekBegin(String time) {
		int mondayPlus;
		Calendar cd = Calendar.getInstance();
		String[] split = time.split("-");
		cd.set(Calendar.HOUR, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		cd.set(Calendar.YEAR, Integer.valueOf(split[0]));
		cd.set(Calendar.MONTH, Integer.valueOf(split[1]) - 1);
		cd.set(Calendar.DAY_OF_MONTH, Integer.valueOf(split[2]));
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 0) {
			mondayPlus = 7;
		} else {
			mondayPlus = dayOfWeek;
		}
		long time_long = cd.getTimeInMillis() - (mondayPlus - 1) * 24 * 60 * 60 * 1000;
		Date date = new Date(time_long);
		return getTime2(date);
	}

	/**
	 * 获取之前几天的时间或者之后几天的时间
	 * 
	 * @param date
	 * @param day
	 *            负数为之前几天 正数为之后几天
	 * @return "yyyy-MM-dd"
	 */
	public static String addDayToDate(String date, int day) {
		long time = getTimeLong_day(date) + day * 24 * 60 * 60 * 1000;
		Date d = new Date(time);
		return sdformat7.format(d);
	}

	public static String addDayToDate(long date, int day) {
		long time = date + day * 24 * 60 * 60 * 1000;
		Date d = new Date(time);
		return sdformat7.format(d);
	}


	/**
	 * 获取之前几个月的时间或者之后几个月的时间
	 * 
	 * @param date
	 * @param month
	 *            负数为之前几个月 正数为之后几个月
	 * @return "yyyy-MM-dd"
	 */
	public static String addMonthToDate(String date, int month) {
		long time = getTimeLong_day(date);
		Date d = new Date(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, month);
		Date d2 = new Date(calendar.getTimeInMillis());
		return sdformat7.format(d2);
	}

	/**
	 * 返回固定格式的时间
	 * 
	 * @param date
	 * @return "yyyy-MM-dd 星期几"
	 */
	public static String getMyFormat(String date) {
		String str = formatTime2(date) + "\t" + appGetdayOfXingQi(date);
		return str;
	}

	/**
	 * 返回固定格式的时间
	 * 
	 * @param date
	 * @return "周几 yyyy-MM-dd"
	 */
	public static String getMyFormat_NoYear(String date) {
//		String str = formatTime11(date) + "\t" + appGetdayOfXingQi(date);
		String str = getWeek(formatTime2(date))+ "\t" +formatTime2(date);
		return str;
	}

	/**
	 * 返回固定格式的时间
	 * 
	 * @param date
	 * @return "星期几(MM-dd)"
	 */
	public static String getMyFormat_Week(String date) {
		String str = appGetdayOfXingQi(date) + "(" + formatTime6(date) + ")";
		return str;
	}

	/**
	 * 返回固定格式的时间
	 * 
	 * @param date
	 * @return "星期几(MM-dd)"
	 */
	public static String getScheduleTime(String date) {
		if (TextUtils.isEmpty(date)) {
			return "无期限";
		}
		String year = appGetYear(date);
		if (!year.equals(Calendar.getInstance().get(Calendar.YEAR) + "")) {
			return formatTime2(date);
		}
		String nowDate = getTime2(new Date());
		if (formatTime2(date).equals(nowDate)) {
			return "今天";
		} else if (TimeUtils.addDayToDate(nowDate, 1).endsWith(formatTime2(date))) {
			return "明天";
		} else if (TimeUtils.addDayToDate(nowDate, 2).endsWith(formatTime2(date))) {
			return "后天";
		} else {
			return formatTime6(date);
		}
	}

	/***
	 * 返回格式 MM-dd HH:mm 周几
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getWorkReportListTime(String date) {
		StringBuffer stbuff = new StringBuffer("");
		if (formatTimeYear(date).equals(year.format(new Date()))) {
			stbuff.append(formatTime6(date) + " " + formatTime9(date) + " " + appGetdayOfZhou(date));
		} else {
			stbuff.append(formatTime2(date) + " " + formatTime9(date) + " " + appGetdayOfZhou(date));
		}
		return stbuff.toString();
	}

	/** "MM-dd HH:mm" */
	public static String formatCrmTime(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return sdformat2.format(date);
	}

	/** "MM/dd" */
	public static String formatToupiaoTime1(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return sdformat15.format(date);
	}

	/** "MM/dd HH:mm" */
	public static String formatToupiaoTime2(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return sdformat2.format(date);
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 * 
	 * @return
	 */
	public static String convertTimeToFormat(String time) {
		if (time == null) {
			return "";
		}
		time = time.replace("T", " ");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return time;
		}
		long millisTime = date.getTime();
		long curTime = System.currentTimeMillis() / 1000;
		long longTime = curTime - millisTime;

		if (longTime < 60 && longTime >= 0) {
			return "刚刚";
		} else if (longTime >= 60 && longTime < 3600) {
			return longTime / 60 + "分钟前";
		} else if (longTime >= 3600 && longTime < 3600 * 24) {
			return longTime / 3600 + "小时前";
		} else if (longTime >= 3600 * 24 && longTime < 3600 * 24 * 30) {
			return longTime / 3600 / 24 + "天前";
		} else if (longTime >= 3600 * 24 * 30 && longTime < 3600 * 24 * 30 * 12) {
			return longTime / 3600 / 24 / 30 + "个月前";
		} else if (longTime >= 3600 * 24 * 30 * 12) {
			return longTime / 3600 / 24 / 30 / 12 + "年前";
		} else {
			return "刚刚";
		}
	}

	/** 获取年 yyyy-MM-dd HH:mm */
	public static String timeGetYear(String time) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}

	/** 获取月 yyyy-MM-dd HH:mm */
	public static String timeGetMonth(String time) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat3.format(date);
	}

	/** 审批获取日 yyyy-MM-dd HH:mm */
	public static String timeGetDay(String time) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat4.format(date);
	}

	/** 获取xia yyyy-MM-dd HH:mm */
	public static String timeGetHour(String time) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat12.format(date);
	}

	/** 获取分钟 yyyy-MM-dd HH:mm */
	public static String timeGetMins(String time) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat13.format(date);
	}

	/** 获取年 format */
	public static String timeGetYear(String time, String format) {
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return year.format(date);
	}

	/** 获取月 yyyy-MM-dd HH:mm */
	public static String timeGetMonth(String time, String format) {
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat3.format(date);
	}

	/** 审批获取日 yyyy-MM-dd HH:mm */
	public static String timeGetDay(String time, String format) {
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat4.format(date);
	}

	/** 获取xia yyyy-MM-dd HH:mm */
	public static String timeGetHour(String time, String format) {
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat12.format(date);
	}

	/** 获取分钟 yyyy-MM-dd HH:mm */
	public static String timeGetMins(String time, String format) {
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			date = new Date();
		}

		return sdformat13.format(date);
	}

	/**
	 * 分钟数转成 HH:mm 格式
	 * @param time
	 * @return
	 */
	public static String timeIntToStr(int time) {
		int hour = time / 60;
		int min = time % 60;
		StringBuffer sb = new StringBuffer("");
		if (hour > 9) {
			sb.append("" + hour);
		} else {
			sb.append("0" + hour);
		}
		if (min > 9) {
			sb.append(":" + min);
		} else {
			sb.append(":0" + min);
		}
		return sb.toString();
	}
	/**
	 *  HH:mm 格式 转 分钟数
	 * @param time
	 * @return
	 */
	public static int timeStrToInt(String time) {
		String hour = TimeUtils.timeGetHour(time, "HH:mm");
		String min = TimeUtils.timeGetMins(time, "HH:mm");
		int h = -1;
		int m = -1;
		try {
			h = Integer.valueOf(hour);
			m = Integer.valueOf(min);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			Calendar calendar = Calendar.getInstance();
			if (h == -1) {
				h = calendar.get(Calendar.HOUR_OF_DAY);
			}
			if (m == -1) {
				m = calendar.get(Calendar.MINUTE);
			}
		}
		return h * 60 + m;
	}

	/** 获取间隔的小时数  返回包含一位小数的字符串 yyyy-MM-dd HH:mm */
	public static String getHours(String time, String time2) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		Date date2 = null;
		try {
			date2 = sdformat.parse(time2);
		} catch (ParseException e) {
			date2 = new Date();
		}
		double m = Math.abs(date.getTime() - date2.getTime());
		DecimalFormat df = new DecimalFormat("######0.0");
		double d1 = 1000*60*60;
		double d = m/d1;
		String st = df.format(d);
		return st;
	}
	/** 获取间隔的分钟数  返回包含一位小数的字符串 yyyy-MM-dd HH:mm
	 * time - time2 */
	public static int getMins(String time, String time2) {
		Date date = null;
		try {
			date = sdformat.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		Date date2 = null;
		try {
			date2 = sdformat.parse(time2);
		} catch (ParseException e) {
			date2 = new Date();
		}
		long m = Math.abs(date.getTime() - date2.getTime());
		int d = (int) (m/(1000*60));
		return d;
	}
}
