package com.baiyuhui.huangyan.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;


public class Utils {

	private static List<Cookie> cookies;

	public static List<Cookie> getCookies() {
		return cookies != null ? cookies : new ArrayList<Cookie>();
	}

	public static void setCookies(List<Cookie> cookies) {
		Utils.cookies = cookies;
	}

	
}
