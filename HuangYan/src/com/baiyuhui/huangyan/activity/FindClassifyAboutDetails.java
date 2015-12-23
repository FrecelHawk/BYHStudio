package com.baiyuhui.huangyan.activity;

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
import com.baiyuhui.huangyan.adapter.BranchAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.utils.UILManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 圈子/圈子分类/版主详情
 * 
 * @author Administrator
 * 
 */
public class FindClassifyAboutDetails extends Activity implements OnClickListener{

	private TextView title;
	private ImageView leftImg;
	
	private ListView mListView;
	
	private LinearLayout layout;

	ImageView circle_list_item_img;
	TextView text1,View2;
	TextView cicle_introduce;//圈子介绍
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_classfiy_about);
		initViews();
		HttpGet();
	}


	private void initViews() {
		
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("版主详情");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		cicle_introduce = (TextView)findViewById(R.id.cicle_introduce);
		circle_list_item_img = (ImageView)findViewById(R.id.circle_list_item_img);
		text1 = (TextView)findViewById(R.id.text1);
		View2 = (TextView)findViewById(R.id.View2);
		
		mListView = (ListView) findViewById(R.id.adout_list);		
	}

	@Override
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

		client.get(MyUrl.CIRCLE_ONE + getIntent().getIntExtra("mId",0), new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				BaseJsonBeen lists = JsonParse.parseCircleClassifySomePerson(arg2);
				BranchAdapter adapter = new BranchAdapter(FindClassifyAboutDetails.this, lists);
		
				text1.setText(lists.getName());
				View2.setText(lists.getPostnum());
				cicle_introduce.setText(lists.getInfo());
				UILManager.with(FindClassifyAboutDetails.this).load(lists.getIco())
				.into(circle_list_item_img);
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
