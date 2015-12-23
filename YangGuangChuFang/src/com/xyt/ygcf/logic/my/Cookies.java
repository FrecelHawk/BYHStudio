package com.xyt.ygcf.logic.my;

import org.apache.http.client.CookieStore;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.ygcf.entity.my.RSABeen;
import com.xyt.ygcf.urls.UrlMy;

/**
 * 
 * @author yuyangming
 * 
 */
public class Cookies {

	private static final int RETRY_CONNECT_COUNT = 3;

	private static Cookies cookies = new Cookies();

	private volatile CookieStore cookieStore;

	private RSABeen rsaBeen;

	private Cookies() {
	}

	public interface ICookiesListener {
		public void onSuccess(CookieStore cookieStore);

		public void onFail(HttpException exception, String message);
	}

	public static Cookies getInstance() {
		if (cookies == null) {
			cookies = new Cookies();
		}
		return cookies;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	/**
	 * 因为此方法有自动登录、手动登录，其他方法获取数据前需要获取cookies时候调用，三种方式可能同时发生，所以必须要考虑异步问题
	 * 
	 * @param cookieStore
	 * @param login
	 */
	public void setCookieStore(CookieStore cookieStore, boolean fromLogin) {
		if (this.cookieStore == null || fromLogin) {
			synchronized (this) {
				if (fromLogin) {
					this.cookieStore = cookieStore;
				} else if (this.cookieStore == null) {
					this.cookieStore = cookieStore;
				}
			}
		}
	}

	public void setRsaBean(RSABeen rsaBeen) {
		this.rsaBeen = rsaBeen;
	}

	public RSABeen getRsaBeen() {
		return rsaBeen;
	}

	private int retryConnectCount = 1;

	public void getVisitorCookies(final HttpUtils httpUtils, final ICookiesListener listener) {
		if (cookieStore != null) {
			callBackToRequest(listener);
		} else {
			httpUtils.send(HttpMethod.GET, UrlMy.COOKIES, new RequestParams(), new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException exception, String message, int arg2) {
					if (cookieStore != null) {
						callBackToRequest(listener);
					} else {
						if (retryConnectCount <= RETRY_CONNECT_COUNT) {
							retryConnectCount++;
							getVisitorCookies(httpUtils, listener);
						} else {
							retryConnectCount = 0;
							if (listener != null) {
								listener.onFail(exception, message);
							}
						}
					}

				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0, int arg1) {
					setCookieStore(arg0.getCookieStore(), false);
					callBackToRequest(listener);
				}
			}, 0);
		}

	}

	private void callBackToRequest(final ICookiesListener listener) {
		if (listener != null) {
			listener.onSuccess(cookieStore);
		}
		LoginHelper.setTimerServiceState(false);
	}
}
