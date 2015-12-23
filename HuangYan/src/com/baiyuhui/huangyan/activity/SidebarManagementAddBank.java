package com.baiyuhui.huangyan.activity;

import com.baiyuhui.huangyan.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 侧边栏/财务管理/添加银行
 * 
 * @author Administrator
 * 
 */
public class SidebarManagementAddBank extends Activity implements
		OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout xuanze;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management_addbank);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("添加银行卡");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		xuanze = (LinearLayout) findViewById(R.id.xuanze);
		xuanze.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left: // 返回
			finish();
			break;

		case R.id.xuanze: // 选择开户银行
			Intent intent = new Intent();
			intent.setClass(SidebarManagementAddBank.this, SidebarManagementBankName.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
