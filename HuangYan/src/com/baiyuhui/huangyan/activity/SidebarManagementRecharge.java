package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;

/**
 * 侧边栏/财务管理/充值
 * 
 * @author Administrator
 * 
 */
public class SidebarManagementRecharge extends Activity implements
		OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layouBank;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management_recharge);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("充值");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		layouBank = (LinearLayout)findViewById(R.id.layou_bank);
		layouBank.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.layou_bank:
			Intent intent = new Intent();
			intent.setClass(SidebarManagementRecharge.this, SidebarManagementBank.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
