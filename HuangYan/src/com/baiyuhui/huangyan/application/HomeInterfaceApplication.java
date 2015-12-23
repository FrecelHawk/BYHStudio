package com.baiyuhui.huangyan.application;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.utils.HomeCrashHandler;

public class HomeInterfaceApplication extends Application {

	public static Toast toast = null;
	public static HomeInterfaceApplication context;

	public final String SHARE_PREF = "HOMEEMPLOYEE_SHARE_PREF";
	public SharedPreferences sharePref = null;
	public static HomeInterfaceApplication getDBAdapter;

	HashMap<String , String > hm_cookie = new HashMap<String, String>();

	public static HomeInterfaceApplication getContext() {
		return context;
	}

	ArrayList<Activity> list = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		context = this;
		sharePref = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
	
		HomeCrashHandler crashHandler = HomeCrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	// 判断从工作进度进来的为TRUE 从接单界面进来的为false
	public void setHmCookie(HashMap<String, String> hm_cookie) {
		this.hm_cookie = hm_cookie;
	}

	public HashMap<String, String> getHmCookie() {
		return this.hm_cookie;
	}
	
}
