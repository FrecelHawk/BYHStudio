package com.xyt.ygcf.ui.search;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.Variable;
import com.xyt.ygcf.adpter.NearbyListAdapter;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.NearbyListItemBean;
import com.xyt.ygcf.home.MerchantDatails;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.SearchShopTool;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.view.CustomListView;
import com.xyt.ygcf.view.CustomListView.OnLoadMoreListener;

/**
 * 搜索
 * 
 */

public class SearchResultActivity extends BaseActivity {


	//private static final String TAG = "SearchResultActivity";
	private final static int SEARCH_NEARBY_SHOP = 0;

	private EditText etSeachCont;
	private CustomListView resultList;
	private TextView tvNodata;
	private NearbyListAdapter adapter;
	private List<NearbyListItemBean> listDatas = new ArrayList<NearbyListItemBean>();

	private String keyword;
	private int currentPage = 1;// 当前页
	private RequestParams params;
	private String cityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		initTopView();
		RequestParams();
	}

	/** 顶栏 */
	private void initTopView() {
		setTitle("搜索");
		etSeachCont = (EditText) findViewById(R.id.box_search);
		resultList = (CustomListView) findViewById(R.id.result_list);
		tvNodata = (TextView) findViewById(R.id.no_data);
		findViewById(R.id.search_btn).setOnClickListener(this);
		resultList.setCanRefresh(false);
		adapter = new NearbyListAdapter(this, listDatas,Constants.INDUSTRY_RESTAURANT_MAKER);
		resultList.setOnLoadListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				currentPage++;
				sendReques(false);
			}
		});
		
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				ListView listItem = (ListView) arg0;
				NearbyListItemBean itemBean = (NearbyListItemBean) listItem
						.getItemAtPosition(position);
				Intent intent = new Intent(SearchResultActivity.this,
						MerchantDatails.class);
				intent.putExtra("mark", Constants.INDUSTRY_RESTAURANT_MAKER);
				intent.putExtra("id", itemBean.stopId);
				startActivity(intent);
				
			}
		});
	}

	private void RequestParams(){
		 keyword = getIntent().getStringExtra("keyword");
		 etSeachCont.setText(keyword);
	    cityId = SpHelper.init(this).getCurrentCityID();
		params = SearchShopTool.searchShopByWord(this,keyword,cityId, Constants.INDUSTRY_RESTAURANT_MAKER);
		sendReques(true);
	}
	
	private void sendReques(Boolean isShow){
		params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
		sendRequest(UrlMy.SEARCH_SHOP, params, SEARCH_NEARBY_SHOP, isShow);
	}
	

	@Override
	public void onClick(View v) {
		if (v.getId() ==R.id.search_btn) {
			keyword = etSeachCont.getText().toString();
			if(TextUtils.isEmpty(keyword)){
				Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		currentPage =1;
		params = SearchShopTool.searchShopByWord(this,keyword, cityId, Constants.INDUSTRY_RESTAURANT_MAKER);
		sendReques(true);
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SEARCH_NEARBY_SHOP){
				if (currentPage == 1) {
					resultList.setCanLoadMore(true);
					resultList.setAdapter(adapter);
					listDatas.clear();
				}
				
				BaseListBeen<NearbyListItemBean> been = (BaseListBeen<NearbyListItemBean>) msg.obj;
				if (been.list.size() != 0) {
					listDatas.addAll(been.list);
					adapter.notifyDataSetChanged();
					resultList.setVisibility(View.VISIBLE);
					tvNodata.setVisibility(View.GONE);
				}else {
					resultList.setVisibility(View.GONE);
					tvNodata.setVisibility(View.VISIBLE);
				}
				
				resultList.onLoadMoreComplete();
				if (been.currentPage == been.totalPages) {
					resultList.setCanLoadMore(false);
				}
				been = null;
			}
		}
		
	};
	@Override
	public void handleJson(String json, int which) {
		
		try {
			SearchShopTool.parserJson(json, handler, SEARCH_NEARBY_SHOP);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};


}
