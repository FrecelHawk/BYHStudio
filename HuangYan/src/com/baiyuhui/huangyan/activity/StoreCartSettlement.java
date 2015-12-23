package com.baiyuhui.huangyan.activity;

import com.baiyuhui.huangyan.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 商城/确认订单
 * 
 * @author Administrator
 *
 */
public class StoreCartSettlement extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private Button tijiao;
	private RelativeLayout coupon;
	private RelativeLayout distribution;
	private RelativeLayout site;
	private RelativeLayout invoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_cart_settlement);
		initViews();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("确认订单");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		tijiao = (Button) findViewById(R.id.tijiao);
		tijiao.setOnClickListener(this);
		
		coupon = (RelativeLayout) findViewById(R.id.coupon);
		coupon.setOnClickListener(this);
		
		distribution = (RelativeLayout) findViewById(R.id.distribution);
		distribution.setOnClickListener(this);
		
		site = (RelativeLayout) findViewById(R.id.site);
		site.setOnClickListener(this);
		
		invoice = (RelativeLayout) findViewById(R.id.invoice);
		invoice.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.tijiao:
			Intent it = new Intent(StoreCartSettlement.this,
					StoreCheckstand.class);
			StoreCartSettlement.this.startActivity(it);
			break;
		case R.id.coupon://优惠卷
			Intent it1 = new Intent(StoreCartSettlement.this,
					SidebarCouponActivity.class);
			StoreCartSettlement.this.startActivity(it1);
			break;
		case R.id.distribution://配送方式
			Intent it2 = new Intent(StoreCartSettlement.this,
					StoreDistributionActivity.class);
			StoreCartSettlement.this.startActivity(it2);
			break;
			
		case R.id.site://收货地址
			Intent it3 = new Intent(StoreCartSettlement.this,
					StoreSiteActivity.class);
			StoreCartSettlement.this.startActivity(it3);
			break;
		case R.id.invoice://发票信息
			Intent it4 = new Intent(StoreCartSettlement.this,
					StoreInvoiceActivity.class);
			StoreCartSettlement.this.startActivity(it4);
			break;

		default:
			break;
		}

	}

}
