package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
/**
 * 
 * @author Administrator
 * 
 * @Overview:侧边栏菜单/个人信息
 */
public class SidebarInformationActivity extends Activity implements OnClickListener{
 

    private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_information);
		initViews();
	}
    private void initViews(){
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("个人信息");
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		default:
			break;
		}
	}
}
