package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.HashAlgorithm;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.SpHelper;

public class ModifyPasswordActivity extends BaseActivity {

	private EditText currentPassword, newPasswrod, surePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password);
		findViews();
	}

	private void findViews() {
		setTitle("修改密码");
		currentPassword = (EditText) findViewById(R.id.activity_mofify_password_current);
		newPasswrod = (EditText) findViewById(R.id.activity_mofify_password_new);
		surePassword = (EditText) findViewById(R.id.activity_mofify_password_new_2);
		findViewById(R.id.activity_mofify_password_btn).setOnClickListener(this);
	}

	private String password = "";

	@Override
	public void onClick(View v) {
		String password1 = currentPassword.getText().toString().trim();
		String password2 = newPasswrod.getText().toString().trim();
		String password3 = surePassword.getText().toString().trim();

		if (TextUtils.isEmpty(password1)) {
			showToast("请输入当前密码");
			return;
		}
		if (TextUtils.isEmpty(password2)) {
			showToast("请输入新密码");
			return;
		}
		if (password2.length() < Constants.PASSWORD_MIN_LENGHT) {
			showToast("密码长度最少 " + Constants.PASSWORD_MIN_LENGHT + "位");
			return;
		}

		if (password2.length() > Constants.PASSWORD_MAX_LENGHT) {
			showToast("密码长度最长20位");
			return;
		}
		if (TextUtils.isEmpty(password3)) {
			showToast("请输入确认密码");
			return;
		}

		if (password3.length() < Constants.PASSWORD_MIN_LENGHT) {
			showToast("密码长度最少 " + Constants.PASSWORD_MIN_LENGHT + "位");
			return;
		}
		if (password3.length() > Constants.PASSWORD_MAX_LENGHT) {
			showToast("密码长度最长20位");
			return;
		}
		if (!password2.equals(password3)) {
			showToast("两次密码不一致");
			return;
		}
		RequestParams params = new RequestParams();
		try {
			password1 = HashAlgorithm.getHashText(password1);
			password = HashAlgorithm.getHashText(password3);
			params.addQueryStringParameter("oldPwd", password1);
			params.addQueryStringParameter("newPwd", password);
			sendRequest(HttpMethod.GET, UrlMy.MODIFY_PASSWORD, params, 0, true);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "请重新登录", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void handleJson(String json, int which) {
		Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
		final boolean savePassword = SpHelper.init(this).getSavePassword();
		if (savePassword) {
			SpHelper.init(this).setUserPassword(password);
		}
		setResult(RESULT_OK);
		finish();
	}

}
