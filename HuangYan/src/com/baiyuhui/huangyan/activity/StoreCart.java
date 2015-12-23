package com.baiyuhui.huangyan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.StoreCartAdapter;

/**
 * 商城/商品详情/购物车
 * 
 * @author Administrator
 * 
 */
public class StoreCart extends Activity implements OnClickListener {

	private ListView mListView;
	private List<String> lists = new ArrayList<String>();
	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private LinearLayout beijin;
	private Button jiesuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_cart);
		setData();
		initViews();
	}

	private void setData() {
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("购物车");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		beijin = (LinearLayout) findViewById(R.id.content);
		beijin.getBackground().setAlpha(220);

		jiesuan = (Button) findViewById(R.id.jiesuan);
		jiesuan.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.cart_list);
		StoreCartAdapter adapter = new StoreCartAdapter(this, lists);
		mListView.setAdapter(adapter);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.jiesuan:
			Intent it = new Intent(StoreCart.this, StoreCartSettlement.class);
			StoreCart.this.startActivity(it);
			break;
		default:
			break;
		}
	}
}