package com.baiyuhui.huangyan.activity;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.SidebarExperienceAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 侧边栏/我的体验店
 * @author Administrator
 *
 */
public class SidebarExperienceActivity extends Activity implements OnClickListener{
 

    private TextView title;
	private ImageView leftImg;
	
	private List<BaseJsonBeen> listDatas;
	private ListView mListView;
	private ListView lisview;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_experience);
		initViews();
		HttpGet();
		
		lisview = (ListView) this.findViewById(R.id.list_experience);
		lisview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(SidebarExperienceActivity.this,
						SidebarExperienceContent.class);
				it.putExtra("id", listDatas.get(position).id);
				it.putExtra("title", listDatas.get(position).tit);
				SidebarExperienceActivity.this.startActivity(it);
			}
		});
		
	}
    private void initViews(){
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("我的体验店");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		
		mListView = (ListView) findViewById(R.id.list_experience);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
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
				listDatas = JsonParse.parseNews(arg2);
				SidebarExperienceAdapter adapter = new SidebarExperienceAdapter(SidebarExperienceActivity.this,
						listDatas);
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