package com.baiyuhui.huangyan.activity;

import java.util.List;

import org.apache.http.Header;

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
import com.baiyuhui.huangyan.adapter.ManagementBankAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 侧边栏/财务管理/选择银行
 * @author Administrator
 *
 */
public class SidebarManagementBank extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;

	private ListView mListView;
	private List<BaseJsonBeen> listDatas;
	private TextView rigthText;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management_bank);
		initViews();
		HttpGet();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("选择银行");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		rigthText = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthText.setText("添加");
		rigthText.setTextSize(14);
		rigthText.setTextColor(getResources().getColor(R.color.white));
		rigthText.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.management_bank);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left: //返回
			finish();
			break;
			
		case R.id.aciont_bar_rigth_text: //添加银行卡
			Intent intent = new Intent();
			intent.setClass(SidebarManagementBank.this, SidebarManagementAddBank.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.NEWS, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				listDatas = JsonParse.parseNews(arg2);
				ManagementBankAdapter adapter = new ManagementBankAdapter(
						SidebarManagementBank.this, listDatas);
				mListView.setAdapter(adapter);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				System.out.println("请求异常----->");
			}
		});

	}

}
