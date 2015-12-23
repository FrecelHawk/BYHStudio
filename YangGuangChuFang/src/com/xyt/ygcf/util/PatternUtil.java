package com.xyt.ygcf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class PatternUtil {

	private PatternUtil() {
	}

	public static boolean isMobile(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return false;
		}
		final String regex = "(\\+86|86|12593)?\\s*[1]{1}(3|4|5|7|8){1}\\d{1}\\s?(-|)?\\d{4}\\s?(-|)?\\d{4}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		// String str =
		// "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		// Pattern p = Pattern.compile(str);

		Pattern p = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
				+ "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

		Matcher m = p.matcher(email);
		return m.matches();
	}
}
