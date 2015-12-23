package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.urls.UrlMy;

/**
 * 
 * @author freesonfish 用邮箱找回密码
 */
public class GetBackPasswordActivity extends BaseActivity {

	private static final int REQUEST_EMAIL = 0;

	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getback_password_2);
		findViews();
		setViewData();
	}

	private void findViews() {
		editText = (EditText) findViewById(R.id.activity_mofify_user_email);
		findViewById(R.id.activity_mofify_user_btn).setOnClickListener(this);
	}

	private void setViewData() {
		setTitle("找回密码");
	}

	private String inputText = "";

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_mofify_user_btn) {
			if (!isRequesting[REQUEST_EMAIL]) {
				commitUserEmail();
			}
		}
	}

	private void commitUserEmail() {
		inputText = editText.getText().toString();
		if (TextUtils.isEmpty(inputText) || !com.xyt.ygcf.util.StringUtils.isEmail(inputText)) {
			showToast("请输入正确的邮箱地址");
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("memberNo", inputText);
		params.addQueryStringParameter("checkType", "1");
		sendRequest(HttpMethod.GET, UrlMy.SEND_EMIAL, params, REQUEST_EMAIL, true);
	}

	@Override
	public void handleJson(String json, int which) {
		Toast.makeText(this, "修改密码邮件已经发送到" + inputText + "邮箱，请注意查收", Toast.LENGTH_LONG).show();
		finish();
	}

}
