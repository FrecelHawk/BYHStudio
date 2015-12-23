package com.baiyuhui.huangyan.activity;

import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeaderElementIterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.application.HomeInterfaceApplication;
import com.baiyuhui.huangyan.entity.LoginBean;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.util.MyCookieStore;
import com.baiyuhui.huangyan.utils.HLog;
import com.baiyuhui.huangyan.utils.PhoneDeviceInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText nameEditText;
	private EditText pwdEditText;
	private Button loginBtn;
	private TextView title;
	private TextView leftText;
	private TextView rigthText;
	private LinearLayout layout;
	private TextView company;

	HomeInterfaceApplication ia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ia = (HomeInterfaceApplication) getApplication();
		findViews();
	}

	private void findViews() {
		nameEditText = (EditText) findViewById(R.id.login_name);
		pwdEditText = (EditText) findViewById(R.id.login_password);
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);

		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("登录");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftText = (TextView) findViewById(R.id.aciont_bar_left_text);
		leftText.setText("关闭");
		leftText.setTextSize(14);
		leftText.setTextColor(getResources().getColor(R.color.white));

		rigthText = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthText.setText("注册");
		rigthText.setTextSize(14);
		rigthText.setOnClickListener(this);
		rigthText.setTextColor(getResources().getColor(R.color.white));

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		company = (TextView) findViewById(R.id.company);
		company.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			login();
			break;
		case R.id.lay_left:
			finish();
			break;
		case R.id.company:
			Intent it1 = new Intent(LoginActivity.this, CompanyActivity.class);
			LoginActivity.this.startActivity(it1);
			break;
		case R.id.aciont_bar_rigth_text:
			Intent it2 = new Intent(LoginActivity.this, RegisterActivity.class);
			LoginActivity.this.startActivity(it2);
			break;

		default:
			break;
		}
	}

	private void login() {
		String name = nameEditText.getText().toString();
		String pwd = pwdEditText.getText().toString();
		if (name == null || name.equals("")) {
			Toast.makeText(this, "邀请码或手机号不能为空", Toast.LENGTH_SHORT).show();
		}
		if (pwd == null || pwd.equals("")) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("uidorphone", name);
		params.addBodyParameter("pwd", pwd);

		final HttpUtils http = new HttpUtils(10 * 1000);
		http.send(HttpMethod.POST, MyUrl.LOGIN, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						
						LoginBean bean = JsonParse.parseLogin(arg0.result
								.toString());

						if (bean.getErr() == 0) {
							// 保存新的cookie							
							
							DefaultHttpClient dh = (DefaultHttpClient) http.getHttpClient();
							MyCookieStore.cookieStore = dh.getCookieStore();
							
							HLog.e("登录MyCookieStore.cookieStore=="+MyCookieStore.cookieStore);
							
							
//							
//		
//							CookieStore cs = dh.getCookieStore();
//							List<Cookie> cookies = cs.getCookies();
//						
//							String aa = null;
//							for (int i = 0; i < cookies.size(); i++) {
//							
//								if ("JSESSIONID".equals(cookies.get(i)
//										.getName())) {
//									aa = cookies.get(i).getValue();
//									break;
//								}
//							}
//							HLog.e("jack", "比较sessionid" + aa);
							
							PhoneDeviceInfo.setUser(bean.getUname(),
									bean.getUid());
							setResult(Activity.RESULT_OK);
							finish();
						} else {

						}
					}
				});
	}
}
