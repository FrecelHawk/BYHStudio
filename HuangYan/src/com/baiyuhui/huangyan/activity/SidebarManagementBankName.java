package com.baiyuhui.huangyan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.BankNameAdapter;

/**
 * 
 * 侧边栏/财务管理/选择开户银行
 * 
 * @author Administrator
 * 
 */
public class SidebarManagementBankName extends Activity implements
		OnClickListener {

	private TextView title;
	private ImageView leftImg;
	
	private ListView mListView;
	private List<String> lists = new ArrayList<String>();
	private LinearLayout layout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management_bank_name);
		initViews();
		setData();
	}
	
	private void setData(){
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("选择开户银行");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.list_bank_name);
		BankNameAdapter adapter = new BankNameAdapter(this, lists);
		mListView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left: //返回
			finish();
			break;

		default:
			break;
		}
	}

}
