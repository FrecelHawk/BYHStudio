package com.xyt.ygcf.logic.my;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.ygcf.base.AppContext;
import com.xyt.ygcf.entity.BaseJsonBeen;
import com.xyt.ygcf.entity.my.User;
import com.xyt.ygcf.logic.BaseJsonParse;
import com.xyt.ygcf.servcie.TimerTaskService;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.HttpTool;
import com.xyt.ygcf.util.SpHelper;

public class LoginHelper {

	private static final int REQUEST_LOGIN = 0;
	private static final int REQUEST_LOGIN_OUT = 1;

	private static final HttpUtils httpUtils = new HttpUtils();

	public static User user;

	private static RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onFailure(HttpException arg0, String arg1, int arg2) {
			if (arg2 == REQUEST_LOGIN) {
				HttpTool.getVisitorCookies(httpUtils);
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0, int arg1) {
			if (arg1 == REQUEST_LOGIN) {
				Cookies.getInstance().setCookieStore(arg0.getCookieStore(), true);
			}
			handleResult(arg0.result, arg1);
		}
	};

	public static void autoLogin(Context context) {
		boolean loginOut = SpHelper.init(context).getLoginOut();
		if (loginOut) {
			HttpTool.getVisitorCookies(httpUtils);
			return;
		}
		boolean autoLogin = SpHelper.init(context).getAutoLogin();
		if (!autoLogin) {
			HttpTool.getVisitorCookies(httpUtils);
			return;
		}
		final String name = SpHelper.init(context).getUserCode();
		final String password = SpHelper.init(context).getUserPassword();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
			HttpTool.getVisitorCookies(httpUtils);
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("code", name);
		login(callBack, params, password, REQUEST_LOGIN);
	}

	public static void login(final RequestCallBack<String> requestCallBack, final RequestParams params,
			String password, final int which) {
		params.addQueryStringParameter("password", password);
		HttpTool.sendRequestOnlyByLogin(httpUtils, HttpMethod.GET, UrlMy.LOGIN, params, requestCallBack, which);
	}

	public static void loginOut() {
		setTimerServiceState(true);
		if (LoginHelper.isLogin()) {
			HttpTool.sendRequest(httpUtils, HttpMethod.GET, UrlMy.LOGIN_OUT, new RequestParams(), callBack,
					REQUEST_LOGIN_OUT);
		}
	}

	private static void handleJson(String json, int which) {
		switch (which) {
			case REQUEST_LOGIN:
				try {
					user = MyJsonParse.parseLogin(json);
					setTimerServiceState(false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case REQUEST_LOGIN_OUT:
				user = null;
				break;
			default:

				break;
		}

	}

	public static void setTimerServiceState(boolean stop) {
		AppContext context = AppContext.curApp();
		if (context != null) {
			Intent intent = new Intent(context.getApplicationContext(), TimerTaskService.class);
			intent.putExtra("action", stop ? "stop" : "start");
			context.startService(intent);
			context = null;
		}
	}

	private static void handleResult(String json, int which) {
		BaseJsonBeen been = BaseJsonParse.parseBaseJson(json);
		if (been != null) {
			if ("true".equals(been.success)) {
				handleJson(been.data, which);
			}
		}
	}

	public static boolean isLogin() {
		return user != null;
	}

}
