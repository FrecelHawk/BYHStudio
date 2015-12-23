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
 * 商城/发票信息
 * @author Administrator
 *
 */
public class StoreInvoiceActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private TextView rigthtext;
	private ImageView mingxi;
	private ImageView bangong;
	private View diannao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_invoice_activity);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("发票信息");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		rigthtext = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthtext.setText("确定");
		rigthtext.setTextSize(14);
		rigthtext.setTextColor(getResources().getColor(R.color.white));
		rigthtext.setOnClickListener(this);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		mingxi = (ImageView) findViewById(R.id.mingxi);
		mingxi.setOnClickListener(this);
		
		bangong = (ImageView) findViewById(R.id.bangong);
		bangong.setOnClickListener(this);
		
		diannao = (ImageView) findViewById(R.id.diannao);
		diannao.setOnClickListener(this);

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
		case R.id.mingxi:
			mingxi.setBackgroundResource(R.drawable.checked1);
			bangong.setBackgroundResource(R.drawable.checked);
			diannao.setBackgroundResource(R.drawable.checked);
			break;
		case R.id.bangong:
			mingxi.setBackgroundResource(R.drawable.checked);
			bangong.setBackgroundResource(R.drawable.checked1);
			diannao.setBackgroundResource(R.drawable.checked);
			break;
		case R.id.diannao:
			mingxi.setBackgroundResource(R.drawable.checked);
			bangong.setBackgroundResource(R.drawable.checked);
			diannao.setBackgroundResource(R.drawable.checked1);
			break;
		default:
			break;
		}

	}

}
