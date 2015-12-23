package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;

/**
 * 侧边栏/财务管理/提现
 * 
 * @author Administrator
 * 
 */
public class SidebarManagementWithdraw extends Activity implements
		OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private TextView rigthText;
	private LinearLayout xuanze;
	private Button tixian_but;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management_withdraw);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("申请提现");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		rigthText = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthText.setText("注意事项");
		rigthText.setTextSize(14);
		rigthText.setTextColor(getResources().getColor(R.color.white));
		rigthText.setOnClickListener(this);
		
		xuanze = (LinearLayout)findViewById(R.id.xuanze);
		xuanze.setOnClickListener(this);
		
		tixian_but = (Button) findViewById(R.id.tixian_but);
		tixian_but.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth_text: //注意事项
			Intent intent = new Intent();
			intent.setClass(SidebarManagementWithdraw.this,
					SidebarManagementNotes.class);
			startActivity(intent);
			break;
		case R.id.xuanze:  //选择银行卡
			Intent intent1 = new Intent();
			intent1.setClass(SidebarManagementWithdraw.this,
					SidebarManagementBank.class);
			startActivity(intent1);
			break;
			
		case R.id.tixian_but:  //申请提现
			Intent intent2 = new Intent();
			intent2.setClass(SidebarManagementWithdraw.this,
					SidebarManagementResultDetails.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
}
