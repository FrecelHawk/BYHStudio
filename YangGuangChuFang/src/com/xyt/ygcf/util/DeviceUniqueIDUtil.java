package com.xyt.ygcf.util;

import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @说明 获取设备唯一的标识
 * @需要权限：READ_PHONE_STATE 和 ACCESS_WIFI_STATE
 */
public class DeviceUniqueIDUtil {

	private static String deviceID = null;

	public static String getDeviceUniqueId(Context context) {
		return getDeviceUniqueId(context, false);
	}

	public static String getDeviceUniqueId(Context context, boolean binding) {
		if (!TextUtils.isEmpty(deviceID))
			return deviceID;

		SpHelper spHelper = SpHelper.init(context);
		deviceID = spHelper.getStringValue("deviceUID", null);
		if (!TextUtils.isEmpty(deviceID))
			return deviceID;

		String uuid = spHelper.getStringValue("unLoginUid", null);
		if (!TextUtils.isEmpty(uuid) && !binding)
			return uuid;

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		deviceID = tm.getDeviceId();
		if (deviceID != null) {
			char[] chs = deviceID.toCharArray();
			int size = chs.length;
			boolean all0 = true;
			for (int i = 0; i < size; i++) {
				if (chs[i] != '0') {
					all0 = false;
					break;
				}
			}
			if (all0) {
				deviceID = null;
			}
		}

		if (TextUtils.isEmpty(deviceID)) {
			deviceID = getMacAddress(context);
		}

		if (binding) {
			String tmp = deviceID;
			deviceID = null;
			return tmp;
		}
		spHelper.setStringValue("deviceUID", deviceID);
		spHelper = null;
		return deviceID;
	}

	private static String getMacAddress(Context context) {
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		SpHelper spHelper = SpHelper.init(context);
		if (info != null) {
			String macAddress = info.getMacAddress();
			if (macAddress == null) {
				String uuid = spHelper.getStringValue("uuidKey", null);
				if (uuid == null) {
					UUID u = UUID.randomUUID();
					macAddress = "id" + u.toString().replaceAll("-", "").substring(2);
					spHelper.setStringValue("uuidKey", macAddress);
				} else {
					macAddress = uuid;
				}
			}
			macAddress = macAddress.toLowerCase(Locale.ENGLISH);
			spHelper = null;
			return macAddress.replaceAll(":", "");
		} else {
			String uuid = spHelper.getStringValue("uuidKey", null);
			if (uuid == null) {
				UUID u = UUID.randomUUID();
				uuid = "id" + u.toString().replaceAll("-", "").substring(2);
				spHelper.setStringValue("uuidKey", uuid);
			}
			spHelper = null;
			return uuid;
		}
	}

}
