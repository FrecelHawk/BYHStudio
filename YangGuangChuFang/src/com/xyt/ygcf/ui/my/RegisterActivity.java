package com.xyt.ygcf.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.HashAlgorithm;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.PatternUtil;

/**
 * 
 * @author yuyangming 注册(邮箱注册)
 * 
 */
public class RegisterActivity extends BaseActivity {

	private static final int REQUEST_COMMIT = 0;

	private EditText etVerfify, etEmial, etNickName, etPassword, etPassword2;
	private CheckBox checkBox;

	// private VerifyCodeHandler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findViewsById();
		initViewData();
	}

	private void findViewsById() {
		etVerfify = (EditText) findViewById(R.id.activity_register_verify);
		etEmial = (EditText) findViewById(R.id.activity_register_email);
		etNickName = (EditText) findViewById(R.id.activity_register_nickname);
		etPassword = (EditText) findViewById(R.id.activity_register_password);
		etPassword2 = (EditText) findViewById(R.id.activity_register_password_2);
		checkBox = (CheckBox) findViewById(R.id.activity_register_checkbox);
		findViewById(R.id.activity_register_protocol).setOnClickListener(this);
		findViewById(R.id.activity_register_commit).setOnClickListener(this);
		findViewById(R.id.activity_register_protocol_yes).setOnClickListener(this);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				findViewById(R.id.activity_register_commit).setEnabled(isChecked);
			}
		});

	}

	private void initViewData() {
		setTitle("注册");
		// final EditText etPhone = (EditText)
		// findViewById(R.id.activity_register_phone);
		// final TextView vertifyView = (TextView)
		// findViewById(R.id.activity_register_verify_btn);
		// handler = new VerifyCodeHandler(this, vertifyView, etPhone,
		// httpUtils, new VerifyPhoneListener() {
		//
		// @Override
		// public void startRequest() {
		// showProgressDialog();
		// }
		//
		// @Override
		// public void finishedRequest() {
		// dissmissProgressDialog();
		// };
		// });
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
			case R.id.activity_register_commit:
				requestToRegisterAcount();
				break;
			case R.id.activity_register_protocol:
				startActivity(new Intent(this, RegisterProtocolActivity.class));
				break;
			case R.id.activity_register_protocol_yes:
				checkBox.setChecked(!checkBox.isChecked());
				break;
			default:
				break;
		}
	}

	private String account_1;

	private void requestToRegisterAcount() {
		if (!isRequesting[REQUEST_COMMIT]) {
			// final EditText etPhone = handler.getPhoneEditText();
			// account_1 = etPhone.getText().toString().trim();
			// if (!handler.getVerifyPhone().equals(account_1)) {
			// showToast("请输入正确的电话号码");
			// return;
			// }
			//
			// final String verifyCode = etVerfify.getText().toString().trim();
			// if (TextUtils.isEmpty(verifyCode)) {
			// showToast("请输入验证码");
			// return;
			// }

			account_1 = etEmial.getText().toString().trim();
			if (TextUtils.isEmpty(account_1) || !PatternUtil.isEmail(account_1)) {
				showToast("请输入正确的邮箱地址");
				return;
			}

			final String password_1 = etPassword.getText().toString().trim();
			if (TextUtils.isEmpty(password_1)) {
				showToast("请输入密码");
				return;
			}

			if (password_1.length() < Constants.PASSWORD_MIN_LENGHT) {
				showToast("密码长度最少 " + Constants.PASSWORD_MIN_LENGHT + "位");
				return;
			}

			if (password_1.length() > Constants.PASSWORD_MAX_LENGHT) {
				showToast("密码长度最长20位");
				return;
			}

			final String password_2 = etPassword2.getText().toString().trim();
			if (TextUtils.isEmpty(password_2)) {
				showToast("请输入确认密码");
				return;
			}

			if (password_2.length() < Constants.PASSWORD_MIN_LENGHT) {
				showToast("密码长度最少 " + Constants.PASSWORD_MIN_LENGHT + "位");
				return;
			}
			if (password_2.length() > Constants.PASSWORD_MAX_LENGHT) {
				showToast("密码长度最长20位");
				return;
			}

			if (!password_1.equals(password_2)) {
				showToast("两次密码不一致");
				return;
			}

			requestRegister(password_1);

		}
	}

	private void requestRegister(String password) {
		try {
			final RequestParams params = new RequestParams();
			password = HashAlgorithm.getHashText(password);
			params.addQueryStringParameter("memberNo", account_1);
			params.addQueryStringParameter("email", account_1);
			params.addQueryStringParameter("nickname", etNickName.getText().toString().trim());
			params.addQueryStringParameter("password", password);
			sendRequest(HttpMethod.GET, UrlMy.REGISTER, params, REQUEST_COMMIT, true);
		} catch (Exception e) {
			Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@Override
	public void handleJson(String json, int which) {
		Intent intent = new Intent();
		Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
		intent.putExtra("acount", account_1);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

}
