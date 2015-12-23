package com.xyt.ygcf.logic.my;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.ygcf.entity.BaseJsonBeen;
import com.xyt.ygcf.logic.BaseJsonParse;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.DeviceUniqueIDUtil;
import com.xyt.ygcf.util.HttpTool;
import com.xyt.ygcf.util.PatternUtil;

/**
 * 专门用于获取短信验证码的操作控制类
 * @author freesonfish
 *
 */
public class VerifyCodeHandler implements OnClickListener {

	private int totalTime = 60000;
	private int spitTime = 1000;

	private TextView verifyView;
	private EditText verifyPhone;

	private Context mContext;

	private CountDownTimer countDownTimer;
	private VerifyPhoneListener listener;
	private boolean isRequesting = false;

	private HttpUtils httpUtils;

	private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {

		@Override
		public void onFailure(HttpException arg0, String arg1, final int which) {
			isRequesting = false;
			if (listener != null) {
				listener.finishedRequest();
			}
			showToast(arg1);
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0, final int which) {
			isRequesting = false;
			if (listener != null) {
				listener.finishedRequest();
			}
			handleResult(arg0.result, which);
		}
	};

	public void handleResult(String json, int which) {
		BaseJsonBeen been = BaseJsonParse.parseBaseJson(json);
		if (been != null) {
			if ("true".equals(been.success)) {
				if (listener != null) {
					listener.finishedRequest();
				}
				showToast("验证码已经发送到到手机，请注意查收");
				startTimer();
			} else {
				showToast(been.message + "获取验证码失败，请重新获取");
			}
		} else {
			showToast("获取验证码失败，请重新获取");
		}

	}

	public interface VerifyPhoneListener {
		public void startRequest();

		public void finishedRequest();
	}

	public VerifyCodeHandler(Context mContext, TextView verifyView, EditText verifyPhone, HttpUtils httpUtils,
			VerifyPhoneListener listener) {
		init(mContext, verifyView, verifyPhone, listener, httpUtils);
		initTimer();
	}

	public VerifyCodeHandler(Context mContext, TextView verifyView, EditText verifyPhone, HttpUtils httpUtils,
			int totalTime, VerifyPhoneListener listener) {
		this.totalTime = totalTime;
		init(mContext, verifyView, verifyPhone, listener, httpUtils);
		initTimer();
	}

	public VerifyCodeHandler(Context mContext, TextView verifyView, EditText verifyPhone, HttpUtils httpUtils,
			int totalTime, int spitTime, VerifyPhoneListener listener) {
		this.totalTime = totalTime;
		this.spitTime = spitTime;
		init(mContext, verifyView, verifyPhone, listener, httpUtils);
		initTimer();
	}

	private void showToast(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
	}

	private void init(Context mContext, TextView verifyView, EditText verifyPhone, VerifyPhoneListener listener,
			HttpUtils httpUtils) {
		this.mContext = mContext;
		this.verifyView = verifyView;
		this.verifyPhone = verifyPhone;
		this.listener = listener;
		this.httpUtils = httpUtils;
		this.verifyView.setOnClickListener(this);
	}

	private void initTimer() {
		countDownTimer = new CountDownTimer(this.totalTime, this.spitTime) {

			@Override
			public void onTick(long millisUntilFinished) {
				verifyView.setText((millisUntilFinished / spitTime) + "秒后可重发");
			}

			@Override
			public void onFinish() {
				verifyView.setEnabled(true);
				verifyView.setText("获取验证码");
			}
		};
	}

	private void startTimer() {
		verifyView.setEnabled(false);
		countDownTimer.start();
	}

	@Override
	public void onClick(View v) {
		if (!isRequesting) {
			requestVertifyCode();
		}
	}

	public EditText getPhoneEditText() {
		return verifyPhone;
	}

	public String getVerifyPhone() {
		return verificationPhone;
	}

	private String verificationPhone = "";

	private void requestVertifyCode() {
		final String phone = verifyPhone.getText().toString().trim();
		if (!PatternUtil.isMobile(phone)) {
			showToast("请输入正确的电话号码");
			return;
		}
		isRequesting = true;
		verificationPhone = phone;
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("phone", phone);
		params.addQueryStringParameter("guid", DeviceUniqueIDUtil.getDeviceUniqueId(mContext, false));
		if (listener != null) {
			listener.startRequest();
		}
		HttpTool.sendRequest(httpUtils, HttpMethod.GET, UrlMy.SMS_VERIFICATION, params, requestCallBack, 0);
	}

}
