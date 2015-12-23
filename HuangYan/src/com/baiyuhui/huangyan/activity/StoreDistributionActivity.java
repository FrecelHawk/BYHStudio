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
 * 商城/配送方式
 * @author Administrator
 *
 */
public class StoreDistributionActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private ImageView putong;
	private ImageView jicun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_distribution_activity);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("配送方式");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		putong = (ImageView) findViewById(R.id.putong);
		putong.setOnClickListener(this);
		
		jicun = (ImageView) findViewById(R.id.jicun);
		jicun.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.putong:
			putong.setBackgroundResource(R.drawable.checked1);
			jicun.setBackgroundResource(R.drawable.checked);
			break;
		case R.id.jicun:
			putong.setBackgroundResource(R.drawable.checked);
			jicun.setBackgroundResource(R.drawable.checked1);
			break;
		default:
			break;
		}

	}

}
