package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
/**
 * 侧边栏/我的体验店/南山店
 * 
 * @author Administrator
 * 
 */
public class SidebarExperienceContent extends Activity implements
		OnClickListener {

	private ImageView leftImg;
	private TextView title;
	private Button yuding;
	private Button weiti;
	private Button zhenyi;
	private View view2;
	private View view1;
	private View view3;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_experience_content);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("南山店");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		yuding = (Button) findViewById(R.id.yuding);
		yuding.setOnClickListener(this);

		weiti = (Button) findViewById(R.id.weiti);
		weiti.setOnClickListener(this);

		zhenyi = (Button) findViewById(R.id.zhenyi);
		zhenyi.setOnClickListener(this);

		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);
		view3 = (View) findViewById(R.id.view3);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.yuding:
			yuding.setTextColor(getResources().getColor(R.color.gamboge));
			view1.setVisibility(View.VISIBLE);
			weiti.setTextColor(getResources().getColor(R.color.black));
			view2.setVisibility(View.GONE);
			zhenyi.setTextColor(getResources().getColor(R.color.black));
			view3.setVisibility(View.GONE);
			break;
		case R.id.weiti:
			yuding.setTextColor(getResources().getColor(R.color.black));
			view1.setVisibility(View.GONE);
			zhenyi.setTextColor(getResources().getColor(R.color.black));
			view3.setVisibility(View.GONE);
			weiti.setTextColor(getResources().getColor(R.color.gamboge));
			view2.setVisibility(View.VISIBLE);
			break;
		case R.id.zhenyi:
			yuding.setTextColor(getResources().getColor(R.color.black));
			view1.setVisibility(View.GONE);
			weiti.setTextColor(getResources().getColor(R.color.black));
			view2.setVisibility(View.GONE);
			zhenyi.setTextColor(getResources().getColor(R.color.gamboge));
			view3.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
