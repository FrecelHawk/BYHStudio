package com.baiyuhui.huangyan.activity;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.ManagementIncomeAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 侧边栏/财务管理/分支明细
 * @author Administrator
 *
 */
public class SidebarManagementIncome extends Activity implements OnClickListener{

	
	private TextView title;
	private View leftImg;
	private ImageView titleImg;
	
	private List<BaseJsonBeen> listDatas;
	private ListView mListView;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_management_income);
		initViews();
		HttpGet();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("全部");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		title.setOnClickListener(this);

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		titleImg = (ImageView) findViewById(R.id.aciont_bar_title_img);
		titleImg.setBackgroundResource(R.drawable.down);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.management_income);
		
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_title:
			finish();
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
				listDatas = JsonParse.parseInfromIogistics(arg2);
				ManagementIncomeAdapter adapter = new ManagementIncomeAdapter(
						SidebarManagementIncome.this, listDatas);
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
