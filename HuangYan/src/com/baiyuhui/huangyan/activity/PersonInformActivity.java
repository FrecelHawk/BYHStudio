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
 * ：个人中心/通知
 * 
 * @author Administrator
 * 
 */

public class PersonInformActivity extends Activity implements OnClickListener{

	private TextView title;
	private ImageView leftImg;
	private LinearLayout lay1;
	private LinearLayout lay2;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_inform);
		initViews();
	}
	
	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("通知");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		lay1 = (LinearLayout)findViewById(R.id.lay1);
		lay1.setOnClickListener(this);
		
		lay2 = (LinearLayout)findViewById(R.id.lay2);
		lay2.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.lay1:
			Intent intent = new Intent();
			intent.setClass(PersonInformActivity.this, PersonInformRevert.class);
			startActivity(intent);
			break;
		case R.id.lay2:
			Intent intent1 = new Intent();
			intent1.setClass(PersonInformActivity.this, PersonInfromIogistics.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}
	
}
