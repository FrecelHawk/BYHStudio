package com.baiyuhui.huangyan.utils;

import com.lidroid.xutils.http.RequestParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PhoneDeviceInfo {
	
	private static Context mContext;

	private static String uid;
	private static String name;
	private static boolean isLogin;
	private static boolean isLogout;
	
	private static final String ACCOUNT_INFO = "account_info";
	private static final String ACCOUNT_NAME = "account_name";
	private static final String ACCOUNT_UID = "account_uid";
	private static final String IS_LOGIN = "is_login";
	
	public PhoneDeviceInfo(Context mContext) {
		super();
		this.mContext = mContext;
		
		SharedPreferences sp = mContext.getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
		isLogin = sp.getBoolean(IS_LOGIN, false);
	}
	
	public static RequestParams getCommonRequestParams(){
		RequestParams  params = new RequestParams();
		return params;
		
	}
	

	public static void setUser(String nameStr, String uidStr){
		if(nameStr != null && !nameStr.equals("")){
			name = nameStr;
		}
		if(uidStr != null && !uidStr.equals("")){
			uid = uidStr;
		}
		setLogin(true);
		SharedPreferences sp = mContext.getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(ACCOUNT_NAME, name);
		editor.putString(ACCOUNT_UID, uid);
		editor.putBoolean(IS_LOGIN, true);
		editor.commit();
		
	}

	public static String getUid() {
		return uid;
	}

	public static void setUid(String uid) {
		PhoneDeviceInfo.uid = uid;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		PhoneDeviceInfo.name = name;
	}

	public static boolean isLogin() {
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		PhoneDeviceInfo.isLogin = isLogin;
	}

	public static boolean isLogout() {
		return isLogout;
	}

	public static void setLogout(boolean isLogout) {
		PhoneDeviceInfo.isLogout = isLogout;
	}
	
	
	

}

