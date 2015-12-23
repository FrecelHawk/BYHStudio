package com.xyt.ygcf.util;

import java.io.File;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.ygcf.logic.my.Cookies;
import com.xyt.ygcf.logic.my.Cookies.ICookiesListener;
import com.xyt.ygcf.logic.my.LoginHelper;

public class HttpTool {

	/**
	 * 这个方法只能是登录的时候调用
	 * @param httpUtils
	 * @param method
	 * @param url
	 * @param params
	 * @param callBack
	 * @param which
	 */
	public static void sendRequestOnlyByLogin(final HttpUtils httpUtils, final HttpMethod method, final String url,
			final RequestParams params, final RequestCallBack<String> callBack, final int which) {
		httpUtils.send(method, url, getRequestParams(method, params), callBack, which);
	}

	public static void sendRequest(final HttpUtils httpUtils, final HttpMethod method, final String url,
			final RequestParams params, final RequestCallBack<String> callBack, final int which) {
		final CookieStore cookieStore = Cookies.getInstance().getCookieStore();
		if (cookieStore != null) {
			final List<Cookie> list = cookieStore.getCookies();
			if (list != null && list.size() > 0) {
				httpUtils.configCookieStore(cookieStore);
				httpUtils.send(method, url, getRequestParams(method, params), callBack, which);
			} else {
				getVisitorCookies(httpUtils, method, url, params, callBack, which);
			}
		} else {
			getVisitorCookies(httpUtils, method, url, params, callBack, which);
		}

		// httpUtils.send(method, url, getRequestParams(method, params),
		// callBack, which);
	}

	public static void getVisitorCookies(final HttpUtils httpUtils, final HttpMethod method, final String url,
			final RequestParams params, final RequestCallBack<String> callBack, final int which) {
		Cookies.getInstance().getVisitorCookies(httpUtils, new ICookiesListener() {

			@Override
			public void onSuccess(CookieStore cookieStore) {
				httpUtils.configCookieStore(cookieStore);
				httpUtils.send(method, url, getRequestParams(method, params), callBack, which);
			}

			@Override
			public void onFail(HttpException exception, String message) {
				callBack.onFailure(exception, message, which);
			}
		});

	}

	public static void getVisitorCookies(final HttpUtils httpUtils) {
		Cookies.getInstance().getVisitorCookies(httpUtils, null);
	}

	public static void downLoadFile(HttpUtils httpUtils, String filePath, String url, RequestParams params,
			RequestCallBack<File> callBack, final boolean reuse) {

		httpUtils.download(url, filePath, getRequestParams(HttpMethod.GET, params), reuse, callBack);
	}

	private static RequestParams getRequestParams(HttpMethod method, RequestParams params) {
		if (params == null) {
			params = new RequestParams();
		}

		if (LoginHelper.isLogin()) {
			if (method == HttpMethod.GET) {
				params.addQueryStringParameter("", LoginHelper.user.token);
			} else {
				params.addBodyParameter("", LoginHelper.user.token);
			}
		}
		if (method == HttpMethod.GET) {
			params.addQueryStringParameter("client", "MB");
		} else {
			params.addBodyParameter("client", "MB");
		}

		return params;
	}

}
