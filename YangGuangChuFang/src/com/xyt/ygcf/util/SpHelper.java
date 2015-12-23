package com.xyt.ygcf.util;

import com.xyt.ygcf.logic.my.ConfusionAlgorithm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author freesonfish
 * 
 */
public class SpHelper {

	private static final String SHARED_FILE_NAME = "system_config";
	private static SharedPreferences sharedPreferences = null;
	private static SpHelper spHelper = new SpHelper();

	private SpHelper() {

	}

	public static synchronized SpHelper init(Context context) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SHARED_FILE_NAME, Context.MODE_PRIVATE);
		}
		return spHelper;
	}

	/**
	 * 存取byte 字节
	 * 
	 * @param key
	 * @param value
	 */
	public void setByteValue(String key, byte value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}

	public byte getByteValue(String key, byte defaultValue) {
		return (byte) sharedPreferences.getInt(key, defaultValue);
	}

	/**
	 * 存取short 短整型
	 * 
	 * @param key
	 * @param value
	 */
	public void setShortValue(String key, short value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}

	public short getShortValue(String key, short defaultValue) {
		return (short) sharedPreferences.getInt(key, defaultValue);
	}

	/**
	 * 存取int 整型
	 * 
	 * @param key
	 * @param value
	 */
	public void setIntValue(String key, int value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}

	public int getIntValue(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	/**
	 * 存取long 长整型
	 * 
	 * @param key
	 * @param value
	 */
	public void setLongValue(String key, long value) {
		sharedPreferences.edit().putLong(key, value).commit();
	}

	public long getLongValue(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);
	}

	/**
	 * 存取float 浮点型
	 * 
	 * @param key
	 * @param value
	 */
	public void setFloatValue(String key, float value) {
		sharedPreferences.edit().putFloat(key, value).commit();
	}

	public float getFloatValue(String key, float defaultValue) {
		return sharedPreferences.getFloat(key, defaultValue);
	}

	/**
	 * 存取double 双浮点型
	 * 
	 * @param key
	 * @param value
	 */
	public void setDoubleValue(String key, double value) {
		sharedPreferences.edit().putString(key, String.valueOf(value)).commit();
	}

	public double getDoubleValue(String key, double defaultValue) {
		return Double.valueOf(sharedPreferences.getString(key, String.valueOf(defaultValue)));
	}

	/**
	 * 存取boolean 布尔型
	 * 
	 * @param key
	 * @param value
	 */
	public void setBooleanValue(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	public boolean getBooleanValue(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	/**
	 * 存取char 字符型
	 * 
	 * @param key
	 * @param value
	 */
	public void setCharValue(String key, char value) {
		sharedPreferences.edit().putString(key, String.valueOf(value)).commit();
	}

	public char getLongValue(String key, char defaultValue) {
		return sharedPreferences.getString(key, String.valueOf(defaultValue)).charAt(0);
	}

	/**
	 * 存取String 字符串型
	 * 
	 * @param key
	 * @param value
	 */
	public void setStringValue(String key, String value) {
		sharedPreferences.edit().putString(key, value).commit();
	}

	public String getStringValue(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	public void setUserCode(String userCode) {
		setStringValue("user_code", userCode);
	}

	public String getUserCode() {
		return getStringValue("user_code", "");
	}

	public void setUserPassword(String password) {
		password = ConfusionAlgorithm.getConfusionText(password);
		setStringValue("user_psw", password);
	}

	public String getUserPassword() {
		String password = getStringValue("user_psw", "");
		password = ConfusionAlgorithm.getRecoveryText(password);
		return password;
	}

	public void setSavePassword(boolean check) {
		setBooleanValue("save_psw", check);
	}

	public boolean getSavePassword() {
		return getBooleanValue("save_psw", false);
	}

	// public void setUserLocation(String userLocation) {
	// setStringValue("user_location", userLocation);
	// }
	//
	// public String getUserLocation() {
	// return getStringValue("user_location", "");
	// }

	public void setAutoLogin(boolean autoLogin) {
		setBooleanValue("auto_login", autoLogin);
	}

	public boolean getAutoLogin() {
		return getBooleanValue("auto_login", false);
	}

	/**当前定位城市的编号*/
	public void setCityCode(String cityCode) {
		setStringValue("cityCode", cityCode);
	}

	public String getCityCode() {
		return getStringValue("cityCode", "4401");
	}

	/**
	 * 保存定位城市的经度
	 */
	public void setCityLongitude(Double longitude) {
		setDoubleValue("city_longitude", longitude);
	}

	/**
	 *获取定位城市的经度
	 */
	public Double getCityLongitude() {
		return getDoubleValue("city_longitude", 0.0);
	}

	/**
	 * 保存定位城市的纬度
	 */
	public void setCityLatitude(Double latitude) {
		setDoubleValue("city_latitude",latitude);
	}

	/**
	 * 获取定位城市的纬度
	 */
	public Double getCityLatitude() {
		return getDoubleValue("city_latitude", 0.0);
	}

	/**
	 * 当前定位城市
	 * @param city
	 */
	public void setCityName(String city) {
		setStringValue("cityname", city);
	}

	public String getCityName() {
		return getStringValue("cityname", "");

	}

	public void setLoginOut(boolean loginOut) {
		setBooleanValue("login_out", loginOut);
	}

	public boolean getLoginOut() {
		return getBooleanValue("login_out", false);
	}
	
	public void setLoginErrorCount(int loginErrorCount) {
		setIntValue("error_count", loginErrorCount);
	}

	public int getLoginErrorCount() {
		return getIntValue("error_count", 0);
	}
	
	/**
	 * 当前选择的城市
	 */
	public final void setCurrentCity(String currentCity){
		setStringValue("currentCity", currentCity);
	}
	
	/**
	 * 获取当前选择的城市
	 * @return
	 */
	public final String getCurrentCity(){
		return getStringValue("currentCity",null);
	}
	
	
	/**
	 * 当前选择的城市ID
	 */
	public final void setCurrentCityID(String currentCityID){
		setStringValue("currentCityID", currentCityID);
	}
	
	/**
	 * 获取当前选择的城市id
	 * @return
	 */
	public final String getCurrentCityID(){
		return getStringValue("currentCityID",null);
	}
	
}
