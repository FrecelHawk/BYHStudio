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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.CircleClassifyAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.utils.HLog;
import com.baiyuhui.huangyan.utils.UILManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 发现/圈子分类
 * 
 * @author Administrator
 * 
 */
public class FindCircleClassify extends Activity implements OnClickListener {

	private TextView title;
	private View leftImg;
	private List<BaseJsonBeen> listDatas;
	private ListView mListView;

	private int mId;
	private String mTitle;
	private TextView tit;
	private String postnum;
	private TextView View2;
	private TextView View4;
	private ImageView rigthImg;
	private ImageView circle_list_item_img;
	private ListView lisview;
	private RelativeLayout fenlei;
	private LinearLayout layout;
	private String ico;
	private ImageView img;
	private String adminuids;
	private String cuid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_circle_classfiy);
		initViews();
		HttpGet();

		lisview = (ListView) this.findViewById(R.id.classify_list);
		lisview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(FindCircleClassify.this,
						FindCircleDetails.class);
				it.putExtra("id", listDatas.get(position).id);
				FindCircleClassify.this.startActivity(it);
			}
		});

	}

	private void initViews() {
		mId = getIntent().getIntExtra("id", 0);
		mTitle = getIntent().getStringExtra("name");
		postnum = getIntent().getStringExtra("postnum").toString();
		adminuids = getIntent().getStringExtra("adminuids");
		ico = getIntent().getStringExtra("ico");
		cuid = getIntent().getStringExtra("cuid");
		
		
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText(mTitle);
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		rigthImg = (ImageView) findViewById(R.id.aciont_bar_rigth);
		rigthImg.setBackgroundResource(R.drawable.modify33);

		tit = (TextView) findViewById(R.id.text1);
		tit.setText(mTitle);
		
		circle_list_item_img = (ImageView) findViewById(R.id.circle_list_item_img);		
		UILManager.with(this).load(ico)
		.into(circle_list_item_img);

		View2 = (TextView) findViewById(R.id.View2);
		View2.setText(cuid);
		View4 = (TextView) findViewById(R.id.View4);
		View4.setText(postnum);

		fenlei = (RelativeLayout) findViewById(R.id.fenlei);

		mListView = (ListView) findViewById(R.id.classify_list);

		rigthImg.setOnClickListener(this);
		fenlei.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth:
			Intent intent = new Intent();				
			intent.setClass(FindCircleClassify.this, FindCirclePosted.class);
			intent.putExtra("mId", mId);		
			startActivity(intent);
			break;
		case R.id.fenlei:
			Intent intent1 = new Intent();
			intent1.setClass(FindCircleClassify.this, FindClassifyAboutDetails.class);
			intent1.putExtra("mId", mId);
			intent1.putExtra("cuid", cuid);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.CIRCLE_CLASS + mId, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				listDatas = JsonParse.parseCircleClassify(arg2);
				CircleClassifyAdapter adapter = new CircleClassifyAdapter(
						FindCircleClassify.this, listDatas);
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
