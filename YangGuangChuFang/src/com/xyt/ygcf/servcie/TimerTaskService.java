package com.xyt.ygcf.servcie;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.ygcf.entity.BaseJsonBeen;
import com.xyt.ygcf.logic.BaseJsonParse;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.HttpTool;

/***
 * 这是一个执行心跳频率的网络请求服务，目的是为了session不失效（服务器判断失效条件为30分钟）；
 * 
 * @author freesonfish
 * 
 */
public class TimerTaskService extends Service {

	private static final int WHAT = 0;

	private static final int STOP = 1;

	/** 心跳频率为20分钟 */
	private static final int TIME_REPEAT = 20 * 60 * 1000;

	/** 错误重连时间 30s */
	private static final int ERROR_RETRY_TIME = 60 * 1000;

	private HttpUtils httpUtils = null;

	private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {

		@Override
		public void onFailure(HttpException arg0, String arg1, int arg2) {
			setNextErrorRequestTime();
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0, int arg1) {
			BaseJsonBeen bean = BaseJsonParse.parseBaseJson(arg0.result);
			if (bean != null && "true".equals(bean.success)) {
				setNextNormalRequestTime();
			} else {
				setNextErrorRequestTime();
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHAT) {
				sendRequest();
			} else {
				stopSelf();
			}
		};
	};

	private void sendRequest() {
		HttpTool.sendRequest(httpUtils, HttpMethod.GET, UrlMy.KEEP_ALIVE_URL, null, requestCallBack, 0);
	}

	private void setNextErrorRequestTime() {
		setRequestTime(ERROR_RETRY_TIME);
	}

	private void setNextNormalRequestTime() {
		setRequestTime(TIME_REPEAT);
	}

	private void setRequestTime(final int time) {
		handler.removeMessages(WHAT);
		handler.sendEmptyMessageDelayed(WHAT, time);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		httpUtils = new HttpUtils();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String action = intent.getStringExtra("action");
		if ("stop".equals(action)) {
			handler.sendEmptyMessageDelayed(STOP, 2 * 1000);
		} else {
			setNextNormalRequestTime();
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		handler.removeMessages(WHAT);
		handler = null;
		super.onDestroy();
	}

}
