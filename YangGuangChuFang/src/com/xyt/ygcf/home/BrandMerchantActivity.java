package com.xyt.ygcf.home;

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
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.NearbyListAdapter;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.NearbyListItemBean;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.SearchShopTool;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.view.CustomListView;
import com.xyt.ygcf.view.CustomListView.OnLoadMoreListener;

/**
 * 食堂列表
 * 
 * @author lenovo hexiangyang
 * 
 */
public class BrandMerchantActivity extends BaseActivity {

	private final static int SEARCH_NEARBY_SHOP = 0;
	/** 筛选 */
	private CustomListView goodsLsit;
	private TextView tvNodata;
	/** 搜索 */
	private EditText searchBox;

	private NearbyListAdapter adapter;

	private List<NearbyListItemBean> listDatas = new ArrayList<NearbyListItemBean>();
	private RequestParams params;

	private int currentPage = 1; // 当前页数
	// SCH：学校食堂  ; ENT：企业食堂 
	private String mark;
	private String currentCityID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brand_goods);
		mark = getIntent().getExtras().getString("mark");
		currentCityID =SpHelper.init(this).getCurrentCityID();
		initwidget();
		isRefectory();
	}
	
	/**
	 * 判断是餐厅还是食堂
	 */
	private void isRefectory() {
		if (mark.equals(Constants.INDUSTRY_SCHOOL_MAKER)) {
			setTitle("学校食堂");
		} else if (mark.equals(Constants.INDUSTRY_COMPANY_MAKER)) {
			setTitle("企业食堂");
		}
		
		params = SearchShopTool.searchShopByArea(this,currentCityID, mark,null);
		
		sendParams(true);
	}

	/**
	 * 发送请求
	 */
	private void sendParams(Boolean isShowDialog) {
		params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
		sendRequest(UrlMy.SEARCH_SHOP, params, SEARCH_NEARBY_SHOP, isShowDialog);
	}

	private void initwidget() {
		/** 关键字搜索 */
		searchBox = (EditText) findViewById(R.id.box_search);
		goodsLsit = (CustomListView) findViewById(R.id.result_list);
		tvNodata = (TextView) findViewById(R.id.no_data);
		findViewById(R.id.search_btn).setOnClickListener(this);
		goodsLsit.setCanRefresh(false);

		adapter = new NearbyListAdapter(this, listDatas, mark);
		goodsLsit.setOnLoadListener(new OnLoadMoreListener() {
			// 加载更多
			@Override
			public void onLoadMore() {
				currentPage++;
				sendParams(false);
			}
		});

		goodsLsit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(BrandMerchantActivity.this,
						MerchantDatails.class);
				intent.putExtra("mark", mark);
				intent.putExtra("id", listDatas.get(arg2 - 1).stopId);
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取编辑框的文本
	 * 
	 * @return 返回编辑框中的文本
	 */
	private String getEtSearchBoxText() {
		return searchBox.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn:
			String keyWord = getEtSearchBoxText();
			if (TextUtils.isEmpty(keyWord)) {
				Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
				return;
			}

			currentPage = 1;
			params = SearchShopTool.searchShopByWord(this,keyWord,currentCityID, mark);
			sendParams(true);
			break;
		}
	}

	/**
	 * 此handler主要用于接收并处理信息操作
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SEARCH_NEARBY_SHOP:
				if(currentPage == 1){
					goodsLsit.setCanLoadMore(true);
					goodsLsit.setAdapter(adapter);
					listDatas.clear();
				}
				BaseListBeen<NearbyListItemBean> been = (BaseListBeen<NearbyListItemBean>) msg.obj;
				if (been.list.size() != 0) {
					listDatas.addAll(been.list);
					goodsLsit.setVisibility(View.VISIBLE);
					tvNodata.setVisibility(View.GONE);
				}else {
					goodsLsit.setVisibility(View.GONE);
					tvNodata.setVisibility(View.VISIBLE);
				}
				
				goodsLsit.onLoadMoreComplete();
				if (been.currentPage == been.totalPages) {
					goodsLsit.setCanLoadMore(false);
				}
				adapter.notifyDataSetChanged();
				been = null;
				break;
			}

		}

	};
	
	@Override
	public void handleJson(String json, int which) {
		System.out.println("----------"+json);
		try {
			SearchShopTool.parserJson(json, handler, SEARCH_NEARBY_SHOP);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	};
	

	
}
