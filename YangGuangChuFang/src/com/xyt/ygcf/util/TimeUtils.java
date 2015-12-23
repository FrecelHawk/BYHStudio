package com.xyt.ygcf.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author yuyangming
 *	时间格式工具类
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

	public static final String YY_MM_DD = "yy:MM:dd";
	public static final String YY_MM_DD_2 = "yyyy-MM-dd";
	public static final String YY_MM_DD_3 = "yy年MM月dd日";

	public static final String YY_MM_DD_HH_MM = "yy-MM-dd hh:mm";
	public static final String YY_MM_DD_HH_MM_SS = "yy:MM:dd hh:mm:ss";

	private static final SimpleDateFormat format = new SimpleDateFormat(YY_MM_DD);

	/**
	 * 根据时间和想要的格式返回相应的格式
	 * @param sytle
	 * @param time
	 * 		单位是毫秒
	 * @return
	 */
	public static String getFormatTime(String sytle, long time) {
		format.applyPattern(sytle);
		if (time <= 0) {
			time = System.currentTimeMillis();
		}
		return format.format(new Date(time));
	}

	/**
	 * 根据时间返回周几
	 * @param time
	 * 		单位是毫秒
	 * @return 
	 */
	public static String getDayOfWeek(long time) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(new Date(time));
		String string = null;
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				string = "星期日";
				break;
			case Calendar.MONDAY:
				string = "星期一";
				break;
			case Calendar.TUESDAY:
				string = "星期二";
				break;
			case Calendar.WEDNESDAY:
				string = "星期三";
				break;
			case Calendar.THURSDAY:
				string = "星期四";
				break;
			case Calendar.FRIDAY:
				string = "星期五";
				break;
			case Calendar.SATURDAY:
				string = "星期六";
				break;
			default:
				break;
		}

		return string;
	}
	
	/**
	 * 获取指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static String dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		String dayForWeek = "";
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek += "星期天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			dayForWeek += "星期一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			dayForWeek += "星期二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			dayForWeek += "星期三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			dayForWeek += "星期四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			dayForWeek += "星期五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			dayForWeek += "星期六";
		}
		return dayForWeek;
	}
	
	/**
	 * 将时间秒转化为时分
	 * @param time  秒
	 * @return
	 */
	public static String cal(int time){
		int h = 0;
		int m = 0;
		int s = 0;
		int temp =time%3600;
		if(time>3600){
			h = time/3600;
			if(temp>60){
				m = temp/60;
				if(temp%60 !=0){
					++m;
				}
				return "约"+h+"小时"+m+"分";
			}
		}else{
			if(time>60){
				m = time/60;
				if(time%60 !=0){
					++m;
				}
				return "约"+m+"分钟";
			}else {
				return "约1分钟";
			}
			
		}
		return null;
	}
	
	/**
	 * 将米转化为公里
	 * @param meter
	 * @return
	 */
	public static String meterTransformKilometer(int meter){
		if(meter>1000){
			Double d = Math.round(meter/100d)/10d;
			return d+"km";
		}else{
			return meter+"m";
		}
		
	}
}
