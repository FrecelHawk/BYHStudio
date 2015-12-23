package com.baiyuhui.huangyan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.BranchAdapter;
import com.baiyuhui.huangyan.adapter.SiteAdapter;
/**
 * 商城/收货地址
 * @author Administrator
 *
 */
public class StoreSiteActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private LinearLayout layout;
	private TextView rigthtext;

	private ListView mListView;
	private List<String> lists = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_site_activity);
		initViews();
		setData();
	}

	private void setData() {
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("收货地址");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		rigthtext = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthtext.setText("新增");
		rigthtext.setTextSize(14);
		rigthtext.setTextColor(getResources().getColor(R.color.white));
		rigthtext.setOnClickListener(this);

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.site_list);
		SiteAdapter adapter = new SiteAdapter(this, lists);
		mListView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth_text:
			Intent it1 = new Intent(StoreSiteActivity.this,
					StoreCompileSiteActivity.class);
			StoreSiteActivity.this.startActivity(it1);
			break;

		default:
			break;
		}

	}

}
