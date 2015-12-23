package com.baiyuhui.huangyan.utils;

import android.util.Log;

public class HLog{

	static String TAG = "HuangYan";
	public static void e(String msg, String head){
		if(msg.equals(""))
		{
			Log.e(TAG, head);
		}
		else {
			Log.e(TAG, head+"====="+msg);	
		}
		
	}
	public static void e(String msg){
		//Log.e(TAG, "====="+msg);
	System.out.println("====="+msg);
	}
	
	public static void i(String msg, String head){
		Log.i(TAG, head+"====="+msg);
	}
	public static void i(String msg){
		System.out.println("====="+msg);
	}
	
	public static void w(String msg, String head){
		Log.w(TAG, head+"====="+msg);
	}
	public static void w(String msg){
		Log.w(TAG, "====="+msg);
	}
	
	public static void d(String msg, String head){
		Log.d(TAG, head+"====="+msg);
	}
	public static void d(String msg){
		Log.d(TAG, "====="+msg);
	}
}
