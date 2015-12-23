package com.xyt.ygcf.util;

import java.util.Calendar;


public class MyDateUtil {
	private int mYear, mMoth, mDay, mHour, mMin;
	String str = "";
	Calendar calendar = null;
	static MyDateUtil dateUtil = new MyDateUtil();

	public static MyDateUtil getDateUtil() {
		return dateUtil;
	}

	public MyDateUtil() {
		calendar = Calendar.getInstance();
	}

	// ---------------获取当前时间----------------------
	public String getNowDate() {
		mYear = calendar.get(Calendar.YEAR);
		mMoth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		int a = mMoth;
		a++;
		String m = a < 10 ? "0" + a : a + "";
		String d = mDay < 10 ? "0" + mDay : mDay + 1 + "";
		str = mYear + "-" + m + "-" + d;
		return str;
	}

	// ---------------获取当月的第一天----------------------
	public String getOneDate() {
		mYear = calendar.get(Calendar.YEAR);
		mMoth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		int a = mMoth;
		a++;
		String m = a < 10 ? "0" + a : a + "";
		str = mYear + "-" + m + "-01";
		return str;
	}

	public String getLasetDate(String y, String m) {
		int year = Integer.parseInt(y);
		int month = Integer.parseInt(m);
		if (month == 4 || month == 6 || month == 9 || month == 11)
			return  y+"-"+m+"-"+"30";
		if (month == 2)
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
				return  y+"-"+m+"-"+"29";
			else
				return  y+"-"+m+"-"+"28";
		return   y+"-"+m+"-"+"31";

	}
}
