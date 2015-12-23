package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.logic.my.VerifyCodeHandler;
import com.xyt.ygcf.logic.my.VerifyCodeHandler.VerifyPhoneListener;
import com.xyt.ygcf.urls.UrlMy;

public class ModifyPhoneActivity extends BaseActivity {

	private static final int REQUEST_COMMIT = 0;

	private EditText etVertify;

	private VerifyCodeHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_phone);
		findViews();
		initViewData();
	}

	private void findViews() {
		etVertify = (EditText) findViewById(R.id.activity_mofify_phone_vertifycode_et);
		findViewById(R.id.activity_mofify_phone_btn).setOnClickListener(this);
	}

	private void initViewData() {
		setTitle("更换手机");
		final TextView vertifyView = (TextView) findViewById(R.id.activity_mofify_phone_vertifycode_btn);
		final EditText etPhone = (EditText) findViewById(R.id.activity_mofify_phone);
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
		final int id = v.getId();
		if (id == R.id.activity_mofify_phone_btn) {
			requestCommit();
		}
	}

	private void requestCommit() {
		if (!isRequesting[REQUEST_COMMIT]) {
			final EditText etPhone = handler.getPhoneEditText();
			final String phone = etPhone.getText().toString().trim();
			if (!handler.getVerifyPhone().equals(phone)) {
				showToast("请输入正确的电话号码");
				return;
			}
			if (TextUtils.isEmpty(etVertify.getText().toString().trim())) {
				showToast("验证码不能为空");
				return;
			}
			RequestParams params = new RequestParams();
			sendRequest(HttpMethod.GET, UrlMy.COLLECTION_RESTAURANT, params, REQUEST_COMMIT, true);
		}
	}

	@Override
	public void handleJson(String json, int which) {
		LoginHelper.user.telephone = handler.getVerifyPhone();
		setResult(RESULT_OK);
		handler = null;
		finish();
	}

}
