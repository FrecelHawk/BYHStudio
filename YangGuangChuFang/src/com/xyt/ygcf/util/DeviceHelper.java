package com.xyt.ygcf.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
/**
 * 系统/硬件/网络操作封装
 * 
 * 
 */
public class DeviceHelper {

	/**
	 * 
	 * 取得屏幕宽度和高度(单位px)
	 * 
	 * @return int[0]-width,int[1]-height
	 * */
	public static int[] getDisplaySize(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return new int[] { metric.widthPixels, metric.heightPixels };
	}

	/**
	 * 获得当前手机的屏幕系数
	 * 
	 * @param context
	 * @return
	 */
	public static float getDisplayDensity(Context context) {
		// 获取当前屏幕
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.density;
	}

	/**
	 * 获取手机总RAM大小(单位:kb)
	 * 
	 * @return
	 */
	public static int getTotalMemory() {
		String str1 = "/proc/meminfo";
		String str2 = "";
		int totalMem = 0;
		int i = 0;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				// 开始读取文件 第一行存的是总RAM大小,第二行存的是空闲的Ram大小 单位是kb 25555kb
				if (i == 0) {
					String[] arrayOfString = str2.split("\\s+");
					totalMem = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
					i += 1;
				}
			}
		} catch (IOException e) {}
		return totalMem;
	}

	/**
	 * 
	 * 获取设备顶部状态栏高度(单位dip)
	 * 
	 * @return height
	 * */
	public static int getStatusBarHeight(Activity activity) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = activity.getResources().getDimensionPixelSize(x);
			return sbar;
		} catch (Exception e1) {
			e1.printStackTrace();
			return 0;
		}
	}

	/***
	 * 判断手机设备是否支持多点触摸
	 * 
	 * @return
	 */
	public static Boolean isMultiPointTouch() {
		boolean multiTouchAvailable1 = false;
		boolean multiTouchAvailable2 = false;
		// Not checking for getX(int), getY(int) etc 'cause I'm lazy
		Method methods[] = MotionEvent.class.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getName().equals("getPointerCount"))
				multiTouchAvailable1 = true;
			if (m.getName().equals("getPointerId"))
				multiTouchAvailable2 = true;
		}

		if (multiTouchAvailable1 && multiTouchAvailable2) {
			return true; // 支持多点触摸
		} else {
			return false;
		}
	}

	/**
	 * 判断wifi是否已打开
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean getWifiState(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] infos = connectivity.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断设备能否上网
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean getNetworkState(Context context) {
		boolean NetIsOpen = false;
		if (IsOpenAirplaneMode(context)) {
			NetIsOpen = false;
		} else {
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connMgr.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				NetIsOpen = true;
			}
		}
		return NetIsOpen;
	}

	/**
	 * 判断是否开启飞行模式
	 * 
	 * @param service
	 * @return
	 */
	public static boolean IsOpenAirplaneMode(Context context) {
		return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true
				: false;
	}

	/**
	 * 判断设备GPS是否开启
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean IsOpenGPS(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static String getMacAddress(Context context) {
		String str = "";
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				str = info.getMacAddress();
			}
		}
		return str;
	}

	/**
	 * 获取包信息
	 * 
	 * @param context
	 * @return
	 */
	public static PackageInfo getAppPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 获取当前应用版本号
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
	    // 获取packagemanager的实例
	    PackageManager packageManager = context.getPackageManager();
	    // getPackageName()是你当前类的包名，0代表是获取版本信息
	    PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	    return packInfo.versionName;
	}

	/**
	 * 调用系统安装了的应用分享
	 * 
	 * @param context
	 * @param title
	 * @param url
	 */
	public static void showShareMore(Activity context, final String title) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
		intent.putExtra(Intent.EXTRA_TEXT, title);
		context.startActivity(Intent.createChooser(intent, "选择分享"));
	}
}
