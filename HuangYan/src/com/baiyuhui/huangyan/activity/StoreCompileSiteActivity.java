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
 * 商城/新增收货地址
 * @author Administrator
 *
 */
public class StoreCompileSiteActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private TextView rigthtext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_compile_site_activity);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("编辑收货地址");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		rigthtext = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthtext.setText("保存");
		rigthtext.setTextSize(14);
		rigthtext.setTextColor(getResources().getColor(R.color.white));
		rigthtext.setOnClickListener(this);

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth_text:
			finish();
			break;

		default:
			break;
		}

	}

}
