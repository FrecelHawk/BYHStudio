package org.android.dialog.utils;

public class Utils {

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if ("null".equals(str))
			return true;
		if ("".equals(str.trim()))
			return true;
		return false;
	}
	
	
	public static boolean isEmpty(CharSequence charSequence){
		if(null==charSequence) return true;
		
		return false;
	}
}
