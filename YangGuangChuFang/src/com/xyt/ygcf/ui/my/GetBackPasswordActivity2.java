package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.logic.my.VerifyCodeHandler;
import com.xyt.ygcf.logic.my.VerifyCodeHandler.VerifyPhoneListener;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.PatternUtil;

/**
 * 该类暂时放弃
 * @author freesonfish
 *
 */
public class GetBackPasswordActivity2 extends BaseActivity {

	private static final int COMMIT_CODE = 0;

	private EditText etVerify, etPassword, etPassword2;

	private VerifyCodeHandler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getback_password);
		findViewsByid();
		initViewData();
	}

	private void findViewsByid() {
		etVerify = (EditText) findViewById(R.id.activity_getback_password_et_verify);
		etPassword = (EditText) findViewById(R.id.activity_getback_password_et_password);
		etPassword2 = (EditText) findViewById(R.id.activity_getback_password_et_password_n);
		findViewById(R.id.activity_getback_password_btn_commit).setOnClickListener(this);
	}

	private void initViewData() {
		setTitle("找回密码");
		final TextView vertifyView = (TextView) findViewById(R.id.activity_getback_password_btn_verify);
		final EditText etPhone = (EditText) findViewById(R.id.activity_getback_password_et_phone);
		handler = new VerifyCodeHandler(this, vertifyView, etPhone, httpUtils, new VerifyPhoneListener() {

			@Override
			public void startRequest() {
				showProgressDialog();
			}

			@Override
			public void finishedRequest() {
				dissmissProgressDialog();
			};
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_getback_password_btn_commit) {
			if (!isRequesting[COMMIT_CODE]) {
				sendRequest();
			}
		}
	}

	private void sendRequest() {
		final EditText etPhone = handler.getPhoneEditText();
		final String phone = etPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone) || !PatternUtil.isMobile(phone)) {
			showToast("请输入手机号码");
			return;
		}

		final String verifyCode = etVerify.getText().toString().trim();
		if (TextUtils.isEmpty(verifyCode)) {
			showToast("请输入验证码");
			return;
		}

		final String nowPassword = etPassword.getText().toString().trim();
		if (TextUtils.isEmpty(nowPassword)) {
			showToast("请输入新密码");
			return;
		}
		if (nowPassword.length() < Constants.PASSWORD_MIN_LENGHT) {
			showToast("密码长度最少 " + Constants.PASSWORD_MIN_LENGHT + "位");
			return;
		}

		if (nowPassword.length() > Constants.PASSWORD_MAX_LENGHT) {
			showToast("密码长度最长20位");
			return;
		}
		final String surePassword = etPassword2.getText().toString().trim();
		if (TextUtils.isEmpty(surePassword)) {
			showToast("请输入确认密码");
			return;
		}
		if (surePassword.length() < Constants.PASSWORD_MIN_LENGHT) {
			showToast("密码长度最少 " + Constants.PASSWORD_MIN_LENGHT + "位");
			return;
		}
		if (surePassword.length() > Constants.PASSWORD_MAX_LENGHT) {
			showToast("密码长度最长20位");
			return;
		}
		if (!nowPassword.equals(surePassword)) {
			showToast("两次密码不一致");
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("password", surePassword);
//		params.addQueryStringParameter("verifyCode", verifyCode);
		params.addQueryStringParameter("memberNo", LoginHelper.user.memberNo);
		sendRequest(UrlMy.GETBACK_PASSWORD, params, COMMIT_CODE, true);
	}

	@Override
	public void handleJson(String json, int which) {
		Toast.makeText(this, "密码找回成功", Toast.LENGTH_SHORT).show();
	}
}
