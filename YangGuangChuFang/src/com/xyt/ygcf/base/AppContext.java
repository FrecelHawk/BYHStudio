package com.xyt.ygcf.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.huamaitel.api.HMDefines;
import com.huamaitel.api.HMDefines.DeviceInfo;
import com.huamaitel.api.HMJniInterface;
import com.xyt.ygcf.Variable;
import com.xyt.ygcf.util.SpHelper;

/**
 * 全局应用程序类：用于保存和调用全局应用配置
 * 
 */
public class AppContext extends Application {
	// 华迈视频
	public static final String TAG = "MainApp";
//	private static HMJniInterface mJni = null;
	public static int mUserId = 0;
	public static int mVideoHandle = 0;
	public static int mAudioHandle = 0;
	public static int mTalkHandle = 0;
	public static int mAlarmHandle = 0;
	public static int mRecordHandle = 0;
	public static byte[] mCapputureHandle = null;
	public static int mLanSearchHandle = 0;
	public static HMDefines.DeviceInfo mDeviceInfo = null;
	public static HMDefines.ChannelCapacity mChannelCapacity[] = null;
	public static int serverId = 0;
	public static int treeId = 0;
	public static int userId = 0;
	public static int curNodeHandle = 0;
	public static int curNodeChannel;
	public static DeviceInfo deviceInfo = null;
	public static List<Integer> rootList;
	private static HMJniInterface jni = null;
	public String mRecordPath = ""; // The path of video record file.
	public static String mCapturePath = ""; // The path of captured picture
											// file.
	public static String mLoginServerError = ""; // The error message of login
													// sever.
	public static boolean mIsUserLogin = true;

	private static AppContext curAppContext;
	
	private SharedPreferences sharedPreferences;
	
	private LocationManagerProxy mAMapLocationManager;
	private AmapLocationListener amListener;
	Handler handler;
	// 全局参数
	private Map<String, Object> globalParams = new HashMap<String, Object>();

	@Override
	public void onCreate() {
		super.onCreate();
		curAppContext = this;
		
		HandlerThread ht =new HandlerThread("handlerThread");
		ht.start();
		handler = new Handler(ht.getLooper());
		handler.post(locatRunnable);
		rootList = new ArrayList<Integer>();
		caculateBitmapSize();
	}

	
	private void caculateBitmapSize() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		final float density = metrics.density;
		if (density <= 2.2f) {
			Variable.itemBitmapSize = "S";
		} else if (density > 2.2f && density <= 3.3f) {
			Variable.itemBitmapSize = "M";
		} else {
			Variable.itemBitmapSize = "B";
			Variable.detailBitmapSize = "B";
		}
	}

	public static AppContext curApp() {
		return curAppContext;
	}

	/**
	 * 获取当前模式，true 正式 false 测试
	 * 
	 * @return
	 */
	public boolean getMode() {
		Resources res = curAppContext.getResources();
		int id = res.getIdentifier("app_mode", "string", getPackageName());
		String mode = curAppContext.getResources().getString(id);
		return Boolean.parseBoolean(mode);
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getSharePre("uniqueID", "");
		if ("".equals(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setSharePre("uniqueID", uniqueID);
		}
		return uniqueID;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public Object getGParamObject(String name) {
		return globalParams.get(name);
	}

	public void removeGParam(String name) {
		globalParams.remove(name);
	}

	public void setGParam(String name, Object val) {
		globalParams.put(name, val);
	}

	public Map<String, Object> getGParamMap() {
		return globalParams;
	}

	@SuppressWarnings("deprecation")
	public String getSharePre(String name, String defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = getSharedPreferences("sharedPre", Context.MODE_WORLD_WRITEABLE
					| Context.MODE_WORLD_READABLE);
		}
		return sharedPreferences.getString(name, defValue);
	}

	@SuppressWarnings("deprecation")
	public void setSharePre(String name, String value) {
		if (sharedPreferences == null) {
			sharedPreferences = getSharedPreferences("sharedPre", Context.MODE_WORLD_WRITEABLE
					| Context.MODE_WORLD_READABLE);
		}
		sharedPreferences.edit().putString(name, value).commit();
	}

	// 华迈视频
	public static HMJniInterface getJni() {
		if (null == jni) {
			jni = new HMJniInterface();
			
		}
		return jni;
	}
	
	/**
	 * 启动线程来定位
	 */
	Runnable locatRunnable = new Runnable() {
		@Override
		public void run() {
			activate();
		}
	};
	
	/**
	 * 定位
	 * @param listener
	 */
	private final void activate() {
		if (null == mAMapLocationManager) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(curAppContext);
			amListener = new AmapLocationListener();
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 5000, 100,
					amListener);
		}
	}
	
	/**
	 * 停止定位
	 */
	private final void deactivate() {
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(amListener);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
		handler.removeCallbacks(locatRunnable);
		System.out.println("---------停止定位-----------");

	}
	
	/**
	 * 获取定位信息的类
	 * @author Administrator
	 *
	 */
	private class AmapLocationListener implements AMapLocationListener{
		
		/**
		 * 定位成功后回调的方法
		 */
		@Override
		public void onLocationChanged(AMapLocation location) {
			System.out.println("---------定位成功-----------");
			if (null != location) {
				deactivate();
				System.out.println("------------------"+location.getLatitude());
				System.out.println("------------------"+location.getLongitude());
				SpHelper spHelper =SpHelper.init(getApplicationContext());
				spHelper.setCityName(location.getCity());//定位当前城市的名称
				spHelper.setCityCode(location.getAdCode().substring(0, 4));//定位当前城市的编号，如广州：4401
				spHelper.setCityLatitude(location.getLatitude());
				spHelper.setCityLongitude(location.getLongitude());
				
			}
			
		}

		@Override
		public void onLocationChanged(Location arg0) {
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}

		
	}
	

}
