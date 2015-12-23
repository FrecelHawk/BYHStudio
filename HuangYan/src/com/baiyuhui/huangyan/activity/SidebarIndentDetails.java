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
 * 侧边栏/订单管理/订单详情
 * 
 * @author Administrator
 * 
 */
public class SidebarIndentDetails extends Activity implements OnClickListener {

	private ImageView leftImg;
	private TextView title;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_indent_details);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("订单详情");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
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
