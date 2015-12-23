package com.baiyuhui.huangyan.activity;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.SchoolAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.views.SingleLayoutListView;
import com.baiyuhui.huangyan.views.SingleLayoutListView.OnLoadMoreListener;
import com.baiyuhui.huangyan.views.SingleLayoutListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 首页/学堂
 * 
 * @author Administrator
 * 
 */
public class HomeSchoolActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private List<BaseJsonBeen> listDatas;

	private LinearLayout layout;

	private SingleLayoutListView mListView;
	private SchoolAdapter adapter;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	private int mCount = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_school);
		initViews();
		HttpGet(1, 3);
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("学堂");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		mListView = (SingleLayoutListView) findViewById(R.id.school_list);

		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		// mListView.setMoveToFirstItemAfterRefresh(true);
		// mListView.setDoRefreshOnUIChanged(true);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(HomeSchoolActivity.this,
						HomeSchoolDetails.class);
				it.putExtra("id", listDatas.get(position - 1).id);
				it.putExtra("title", listDatas.get(position - 1).tit);
				it.putExtra("cdate", listDatas.get(position - 1).cdate);
				HomeSchoolActivity.this.startActivity(it);
			}
		});

		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				mCount = 1;
				HttpGet(mCount, 0);
			}
		});

		mListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				mCount += 1;
				HttpGet(mCount, 1);
			}
		});

	}

	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				listDatas = (List<BaseJsonBeen>) msg.obj;
				adapter = new SchoolAdapter(HomeSchoolActivity.this, listDatas);
				mListView.setAdapter(adapter);
				break;
			case REFRESH_DATA_FINISH:
				if (adapter != null) {
					listDatas = (List<BaseJsonBeen>) msg.obj;
					adapter.setLists(listDatas);
					adapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (adapter != null) {
					listDatas.addAll((List<BaseJsonBeen>) msg.obj);
					adapter.setLists(listDatas);
					// adapter.addAllLists((List<BaseJsonBeen>) msg.obj);
					adapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				break;
			}
		};
	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		default:
			break;
		}
	}

	private void HttpGet(int num, final int type) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.SCHOOL + "?page=" + num + "&contenttype=6",
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("请求回来的参数----->学堂" + arg2);
						List<BaseJsonBeen> listData = JsonParse.parseNews(arg2);

						if (type == 0) { // 下拉刷新
							// Collections.reverse(mList); //逆序
							Message _Msg = mHandler.obtainMessage(
									REFRESH_DATA_FINISH, listData);
							mHandler.sendMessage(_Msg);
						} else if (type == 1) {
							Message _Msg = mHandler.obtainMessage(
									LOAD_DATA_FINISH, listData);
							mHandler.sendMessage(_Msg);
						} else if (type == 3) {
							Message _Msg = mHandler.obtainMessage(1, listData);
							mHandler.sendMessage(_Msg);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						System.out.println("请求异常----->");
					}
				});

	}

}
