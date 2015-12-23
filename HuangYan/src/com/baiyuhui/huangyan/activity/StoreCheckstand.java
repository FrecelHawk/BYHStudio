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
 * 商城/收银台
 * 
 * @author Administrator
 * 
 */
public class StoreCheckstand extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private Button but;
	private ImageView yue;
	private ImageView zhifubao;
	private TextView textyue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_checkstand);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("收银台");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		textyue = (TextView) findViewById(R.id.textyue);

		yue = (ImageView) findViewById(R.id.yue);
		yue.setOnClickListener(this);

		zhifubao = (ImageView) findViewById(R.id.zhifubao);
		zhifubao.setOnClickListener(this);

		but = (Button) findViewById(R.id.fukuan);
		but.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.fukuan:
			finish();
			break;
		case R.id.yue:
			yue.setBackgroundResource(R.drawable.checked1);

			break;
		case R.id.zhifubao:
			zhifubao.setBackgroundResource(R.drawable.checked1);
			break;
		default:
			break;
		}

	}

}
