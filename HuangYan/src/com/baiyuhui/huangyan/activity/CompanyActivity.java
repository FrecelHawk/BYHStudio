package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;

public class CompanyActivity extends Activity implements OnClickListener {

	private TextView title;
	private LinearLayout layout;
	private TextView login;
	private TextView leftText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		findViews();
	}

	private void findViews() {

		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("登录");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftText = (TextView) findViewById(R.id.aciont_bar_left_text);
		leftText.setText("关闭");
		leftText.setTextSize(14);
		leftText.setTextColor(getResources().getColor(R.color.white));

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		login = (TextView) findViewById(R.id.login);
		login.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.login:
			finish();
			break;

		default:
			break;
		}
	}

}
