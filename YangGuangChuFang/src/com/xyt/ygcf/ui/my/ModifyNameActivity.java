package com.xyt.ygcf.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.urls.UrlMy;

public class ModifyNameActivity extends BaseActivity {

	private static final int REQUEST_NAME = 0;
	private static final int REQUEST_EMAIL = 1;

	private EditText editText;
	private TextView textView;

	/** 0表示修改用户名，1表示修改邮箱 */
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_username);
		findViews();
		setViewData();
	}

	private void findViews() {
		editText = (EditText) findViewById(R.id.activity_mofify_user_name);
		textView = (TextView) findViewById(R.id.activity_mofify_user_name_tv);
		findViewById(R.id.activity_mofify_user_btn).setOnClickListener(this);
	}

	private void setViewData() {
		final Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if (type == 1) {
			textView.setText("邮箱");
			setTitle("修改邮箱");
		} else {
			setTitle("修改用户名");
		}
	}

	private String inputText = "";

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_mofify_user_btn) {
			if (type == 0) {
				if (!isRequesting[REQUEST_NAME]) {
					commitUserName();
				}
			} else {
				if (!isRequesting[REQUEST_EMAIL]) {
					commitUserEmail();
				}
			}
		}
	}

	private void commitUserName() {
		inputText = editText.getText().toString();
		final String userName = LoginHelper.user.nickname;
		if (TextUtils.isEmpty(inputText)) {
			showToast("请输入昵称");
		}
		if (inputText.equals(userName)) {
			showToast("与原昵称一致，无需修改");
			return;
		}
		if (inputText.length() > 10) {
			showToast("请控制在10字以内");
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("nickname", inputText);
		sendRequest(HttpMethod.GET, UrlMy.MODIFY_MESSAGE, params, REQUEST_NAME, true);
	}

	private void commitUserEmail() {
		inputText = editText.getText().toString();
		final String email = LoginHelper.user.email;
		if (TextUtils.isEmpty(inputText)) {
			showToast("输入邮箱地址");
			return;
		}
		if (email.equals(inputText)) {
			showToast("与原邮箱地址一致，无需修改");
			return;
		}
		if (!com.xyt.ygcf.util.StringUtils.isEmail(inputText)) {
			showToast("请输入正确的邮箱地址");
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("email", inputText);
		sendRequest(HttpMethod.GET, UrlMy.MODIFY_MESSAGE, params, REQUEST_EMAIL, true);
	}

	@Override
	public void handleJson(String json, int which) {
		Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
		if (which == REQUEST_NAME) {
			LoginHelper.user.nickname = inputText;
		} else {
			LoginHelper.user.email = inputText;
		}
		setResult(Activity.RESULT_OK);
		finish();
	}

}
