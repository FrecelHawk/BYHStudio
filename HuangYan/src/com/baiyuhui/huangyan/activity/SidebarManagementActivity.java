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
 * 
 * @author Administrator
 * 
 * @Overview:侧边栏菜单/跳转布局/财务管理
 */
public class SidebarManagementActivity extends Activity implements
		OnClickListener {
	private TextView title;
	private ImageView leftImg;
	private Button tixian;
	// private Button chonzhi;
	private ImageView rigthImg;
	private LinearLayout xiugai;
	private LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("财务管理");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		rigthImg = (ImageView) findViewById(R.id.aciont_bar_rigth);
		rigthImg.setBackgroundResource(R.drawable.income);
		rigthImg.setOnClickListener(this);

		tixian = (Button) findViewById(R.id.tixian);// 找到你要设透明背景的Button的id
		// tixian.getBackground().setAlpha(220);// 0~255透明度值
		tixian.setOnClickListener(this);

		xiugai = (LinearLayout) findViewById(R.id.xiugai);
		xiugai.setOnClickListener(this);

		// chonzhi = (Button) findViewById(R.id.chonzhi);
		// chonzhi.getBackground().setAlpha(220);
		// chonzhi.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth: // 收支明细
			Intent intent = new Intent();
			intent.setClass(SidebarManagementActivity.this,
					SidebarManagementIncome.class);
			startActivity(intent);
			break;
		case R.id.xiugai: // 查看修改银行卡
			Intent intent1 = new Intent();
			intent1.setClass(SidebarManagementActivity.this,
					SidebarManagementBank.class);
			startActivity(intent1);
			break;
		case R.id.tixian: // 提现
			Intent intent2 = new Intent();
			intent2.setClass(SidebarManagementActivity.this,
					SidebarManagementWithdraw.class);
			startActivity(intent2);
			break;
		/*
		 * case R.id.chonzhi: // 充值 
		 * Intent intent3 = new Intent();
		 * intent3.setClass(SidebarManagementActivity.this,
		 * SidebarManagementRecharge.class); 
		 * startActivity(intent3); break;
		 */
		default:
			break;
		}
	}
}
