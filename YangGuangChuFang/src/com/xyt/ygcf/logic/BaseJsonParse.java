package com.xyt.ygcf.logic;

import org.json.JSONException;
import org.json.JSONObject;

import com.xyt.ygcf.entity.BaseJsonBeen;

public class BaseJsonParse {

	public static BaseJsonBeen parseBaseJson(String json) {
		JSONObject object;
		BaseJsonBeen been = null;
		try {
			object = new JSONObject(json);
			been = new BaseJsonBeen();
			been.success = object.optString("success", "false");
			been.data = object.optString("data", "");
			been.message = object.optString("message", "数据解析错误");
			been.messageCode = object.optString("messageCode");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return been;
	}
}
