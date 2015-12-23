package com.xyt.ygcf.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.VioaltionListAdapter;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.NearbyListItemBean;
import com.xyt.ygcf.entity.VioaltionListBean;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.BaseUtil;
import com.xyt.ygcf.view.CustomListView;
import com.xyt.ygcf.view.CustomListView.OnLoadMoreListener;

/**
 * 违规公示
 * 
 * @author lenovo hexiangyang
 * 
 */
public class ViolationListActivity extends BaseActivity {
	private CustomListView vioaltionList;
	private EditText boxSearch;
	private Button btnSearch;

	private int currentPage = 1;
	private Intent intent;
	private VioaltionListAdapter adapter;
	private List<VioaltionListBean> vioaltionLists = new ArrayList<VioaltionListBean>();

	/** 政府公告 */
	public final static int GOVERNMENT_NOTICE = 1;
	private RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_violation_list);
		initwidget();
		params = new RequestParams();
		sendQuery(params, currentPage);
	}

	private void initwidget() {
		setTitle("违规公示");

		boxSearch = (EditText) findViewById(R.id.box_search);
		btnSearch = (Button) findViewById(R.id.search_btn);
		vioaltionList = (CustomListView) findViewById(R.id.result_list);
		vioaltionList.setCanRefresh(false);
		vioaltionList.setCanLoadMore(true);
		btnSearch.setOnClickListener(this);
		adapter = new VioaltionListAdapter(this, vioaltionLists);
		vioaltionList.setAdapter(adapter);

		vioaltionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				VioaltionListBean listBean = vioaltionLists.get(arg2);
				intent = new Intent(ViolationListActivity.this, VioaltionContentActivity.class);
				intent.putExtra("id", listBean.getId());
				startActivity(intent);
			}
		});
		vioaltionList.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				currentPage++;
				sendQuery(params, currentPage);
			}
		});
	}

	/**
	 * 获取编辑框的文本
	 * 
	 * @return 返回编辑框中的文本
	 */
	private String getEtSearchBoxText() {
		return boxSearch.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.search_btn:
			if (!getEtSearchBoxText().equals("")) {
				currentPage = 1;
				params = titleAsparametes(getEtSearchBoxText());
				sendQuery(params, currentPage);
			} else {
				Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	/** 发送请求 */
	private void sendQuery(RequestParams params, int currentPage) {
		params.addQueryStringParameter("isPaging", "true");
		params.addQueryStringParameter("currentPage",
				String.valueOf(currentPage));
		sendRequest(HttpMethod.GET, UrlMy.GOVERNMENT_NOTICE_LISTS, params,
				GOVERNMENT_NOTICE, true);
	}

	/**
	 * 关键字作为参数
	 * 
	 * @param title
	 *            关键字
	 * @return
	 */
	private RequestParams titleAsparametes(String title) {
		RequestParams param = new RequestParams();
		param.addQueryStringParameter("title", title);
		return param;

	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == GOVERNMENT_NOTICE){
				if(currentPage ==1){
					vioaltionLists.clear();
				}
				BaseListBeen<VioaltionListBean> been = (BaseListBeen<VioaltionListBean>) msg.obj;
				if (been.currentPage == been.totalPages) {
					vioaltionList.setCanLoadMore(false);
				}
				
				if(been.list.size() !=0){
					vioaltionLists.addAll(been.list);
					adapter.notifyDataSetChanged();
					vioaltionList.onLoadMoreComplete();
				}
			}
		}
		
	};

	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		try {
			if (which == GOVERNMENT_NOTICE) {
				parseAnnouncement(json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void sendMsg(Object object){
		Message msg = Message.obtain();
		msg.what = GOVERNMENT_NOTICE;
		msg.obj = object;
		handler.sendMessage(msg);
		
	}

	/**
	 * 解析违规公示
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseAnnouncement(String json) throws JSONException {
		BaseListBeen<VioaltionListBean> baseListBeens = new BaseListBeen<VioaltionListBean>();
		JSONObject jsonObject = new JSONObject(json);
		baseListBeens.currentPage = jsonObject.optInt("currentPage");
		baseListBeens.totalPages = jsonObject.optInt("totalPages");
		List<VioaltionListBean> vioaltionListBeans = new ArrayList<VioaltionListBean>();
		JSONArray array = jsonObject.getJSONArray("beanList");
		int length = array.length();
		for (int i = 0; i < length; i++) {
			JSONObject object = array.getJSONObject(i);
			VioaltionListBean vioaltionListBean = new VioaltionListBean();
			vioaltionListBean.id = object.optString("id");
			vioaltionListBean.name = object.optString("title");
			vioaltionListBean.message = object.optString("content");
			vioaltionListBean.date = object.optString("createDt");
			vioaltionListBeans.add(vioaltionListBean);
		}
		
		baseListBeens.list = vioaltionListBeans;
		sendMsg(baseListBeens);
		
	}

}
