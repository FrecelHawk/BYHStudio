package com.xyt.ygcf.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.MerchantMenuListAdapter;
import com.xyt.ygcf.adpter.MerchantMenuPicAdapter;
import com.xyt.ygcf.adpter.MerchantMenuTypeAdapter;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.MerchantMenuBean;
import com.xyt.ygcf.entity.MerchantMenuTypeBean;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.TimeUtils;
import com.xyt.ygcf.view.CustomListView;
import com.xyt.ygcf.view.CustomListView.OnLoadMoreListener;

/**
 * 菜谱
 * 
 * @author lenovo hexiangyang
 * 
 */
public class MerchantMenuActivity extends BaseActivity implements OnClickListener {
	// private LeftSliderLayout leftSliderLayout;
	private CustomListView aboveList;
	private ListView belowList;
	private TextView noDate;

	private ImageView btnSetting;
	private TextView dataShow, weekShow, topTitle;
	private RelativeLayout dateLayout;
	private ImageButton beforeDay, afterDay;

	private List<MerchantMenuBean> menus = new ArrayList<MerchantMenuBean>();
	private List<MerchantMenuTypeBean> menuTypeBeans = new ArrayList<MerchantMenuTypeBean>();

	private MerchantMenuPicAdapter picAdapter;
	private MerchantMenuListAdapter listAdapter;
	private MerchantMenuTypeAdapter typeAdapter;

	private Intent intent;

	private boolean isShow = true;
	// 餐厅标识 RST:餐厅; SCH：学校食堂 ; ENT：企业食堂
	private String mark;
	// 商家id
	private String id;
	// 商家名称
	private String name;

	private int currentPage = 1; // 当前页数
	private View currentView;
	// 商家商品类型id
	private String typeid = "";
	// 日期
	private String data;
	private String week;
	/** 每日菜谱 */
	public final static int EVERYDAY_PRODUCTLIST = 1;
	/** 商家商品分类 */
	public final static int MERPROTYPE = 2;
	/** 搜索商家 */
	public final static int PRODUCTLIST = 3;
	/** 每日菜谱类型字典 */
	public final static int RECIPE_TYPE_DATA = 4;

	private final static int PRODUCT_TYPE = 5;
	private final static int DISH_STLY = 6;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case PRODUCT_TYPE:
				List<MerchantMenuTypeBean> typeBean = (List<MerchantMenuTypeBean>)msg.obj;
				menuTypeBeans.clear();
				menuTypeBeans.addAll(typeBean);
				typeAdapter.notifyDataSetChanged();
				break;

			case DISH_STLY:
				if (currentPage == 1) {
					aboveList.setCanLoadMore(true);
					if(isShow){
						aboveList.setAdapter(picAdapter);
					}else {
						aboveList.setAdapter(listAdapter);
					}
					menus.clear();
				}
				BaseListBeen<MerchantMenuBean> baseListBeen = (BaseListBeen<MerchantMenuBean>) msg.obj;
				if (baseListBeen.list.isEmpty()) {
					noDate.setVisibility(View.VISIBLE);
				} else {
					menus.addAll(baseListBeen.list);
					noDate.setVisibility(View.GONE);
					picAdapter.notifyDataSetChanged();
					listAdapter.notifyDataSetChanged();
					aboveList.onLoadMoreComplete();
				}

				if (baseListBeen.currentPage == baseListBeen.totalPages) {
					aboveList.setCanLoadMore(false);
				}

				break;
			}

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant_menu);

		getIntentData();
		initwidget();
		initSliderLayout();
		getCurrentTime();

		if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
			productlist(id, currentPage, "",true);
			MerProType(id);
		} else {
			everyDayProductlist(id, currentPage, data, "",true);
			dictionaryData(id, data);
		}

	}

	private void getIntentData() {
		mark = getIntent().getStringExtra("mark");
		id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
	}

	private void initwidget() {
		dateLayout = (RelativeLayout) findViewById(R.id.merchant_date_layout);
		beforeDay = (ImageButton) findViewById(R.id.merchant_before_day);
		afterDay = (ImageButton) findViewById(R.id.merchant_after_day);
		dataShow = (TextView) findViewById(R.id.merchant_date_show);
		weekShow = (TextView) findViewById(R.id.merchant_week_show);
		btnSetting = (ImageView) findViewById(R.id.right_btn);
		topTitle = (TextView)findViewById(R.id.desktop_userID);
		topTitle.setSelected(true);
		topTitle.setText(name + "菜谱");
		btnSetting.setVisibility(View.VISIBLE);
		btnSetting.setImageResource(R.drawable.menu_list_btn);
		btnSetting.setOnClickListener(this);
		beforeDay.setOnClickListener(this);
		afterDay.setOnClickListener(this);
		currentView = new View(this);
		if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
			dateLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化LeftSliderLayout
	 */
	private void initSliderLayout() {
		// leftSliderLayout = (LeftSliderLayout)
		// findViewById(R.id.main_slider_layout);
		aboveList = (CustomListView) findViewById(R.id.sliding_layout_above_list);
		belowList = (ListView) findViewById(R.id.sliding_layout_below_list);
		noDate = (TextView) findViewById(R.id.sliding_layout_above_no_date);
		// leftSliderLayout.setOnLeftSliderLayoutListener(this);
		// 展开leftSliderLayout
		// leftSliderLayout.open();
		aboveList.setCanRefresh(false);
		listAdapter = new MerchantMenuListAdapter(MerchantMenuActivity.this, menus, mark);
		picAdapter = new MerchantMenuPicAdapter(MerchantMenuActivity.this, menus, mark);
		
		
		typeAdapter = new MerchantMenuTypeAdapter(this, menuTypeBeans);
		belowList.setAdapter(typeAdapter);

		aboveList.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				data = dataShow.getText().toString();
				currentPage++;
				if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
					productlist(id, currentPage, typeid,false);
				} else {
					everyDayProductlist(id, currentPage, data, typeid,false);
				}
			}
		});
		belowList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				typeAdapter.setBackground(arg2);
				ListView lvItem = (ListView) arg0;
				MerchantMenuTypeBean menuTypeBean = (MerchantMenuTypeBean) lvItem
						.getItemAtPosition(arg2);
				typeid = menuTypeBean.getTypeId();
				if (currentView == arg1) {
					return;
				} else {
					currentView = arg1;
					currentPage = 1;
				}
				if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
					productlist(id, currentPage, typeid, false);
				} else {
					data = dataShow.getText().toString();
					everyDayProductlist(id, currentPage, data, typeid,false);
				}
				typeAdapter.notifyDataSetChanged();
			}
		});
		aboveList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)){
					MerchantMenuBean menuBean = menus.get(arg2 - 1);
					intent = new Intent(getApplicationContext(), GoodsDetailsActivity.class);
					intent.putExtra("name", menuBean.getName());
					intent.putExtra("id", menuBean.getId());
					intent.putExtra("mark", mark);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 获取当前时间
	 */
	public void getCurrentTime() {
		data = TimeUtils.getFormatTime(TimeUtils.YY_MM_DD_2, 0);
		week = TimeUtils.getDayOfWeek(System.currentTimeMillis());
		dataShow.setText(data);
		weekShow.setText(week);
	}

	public void notifyAdapter() {
		typeAdapter.setBackground(-1);
		typeAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.merchant_before_day: // 前一天菜式
			String foreData = TimeUtils.getSpecifiedDayBefore(dataShow.getText().toString());
			setView(foreData);
			notifyAdapter();
			dictionaryData(id, foreData);
			everyDayProductlist(id, currentPage, foreData, "",true);
			break;
		case R.id.merchant_after_day: // 后一天菜式
			String AfterData = TimeUtils.getSpecifiedDayAfter(dataShow.getText().toString());
			setView(AfterData);
			notifyAdapter();
			dictionaryData(id, AfterData);
			everyDayProductlist(id, currentPage, AfterData, "",true);
			break;
		case R.id.right_btn:
			if (isShow) {
				btnSetting.setImageResource(R.drawable.menu_pic_btn);
				aboveList.setAdapter(listAdapter);
				isShow = false;
			} else {
				btnSetting.setImageResource(R.drawable.menu_list_btn);
				aboveList.setAdapter(picAdapter);
				isShow = true;
			}
			break;
		default:
			break;
		}
		super.onClick(arg0);
	}

	/**
	 * 设置日期显示
	 * 
	 * @param data
	 */
	public void setView(String data) {
		dataShow.setText(data);
		try {
			weekShow.setText(TimeUtils.dayForWeek(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	/**
	 * 每日菜谱
	 */
	private void everyDayProductlist(String merchantId, int currentPage,
			String date, String type,Boolean isShow) {
		if (!isRequesting[EVERYDAY_PRODUCTLIST]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			params.addQueryStringParameter("dailyDt", date);
			params.addQueryStringParameter("type", type);
			params.addQueryStringParameter("needPage", "true");
			params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
			params.addQueryStringParameter("proPitureSize", itemBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.DAILY_RECIPE, params, EVERYDAY_PRODUCTLIST, isShow);
		}
	}

	/**
	 * 搜索商品
	 * 
	 * @param merchantId
	 * @param currentPage
	 * @param type
	 */
	private void productlist(String merchantId, int currentPage, String merProType,boolean isShow) {
		if (!isRequesting[PRODUCTLIST]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			params.addQueryStringParameter("merProType", merProType);
			params.addQueryStringParameter("needPage", "true");
			params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
			params.addQueryStringParameter("proPitureSize", itemBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.PRODUCT_LIST, params, PRODUCTLIST, isShow);
		}
	}

	/**
	 * 商家商品分类
	 */
	private void MerProType(String merchantId) {
		if (!isRequesting[MERPROTYPE]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			params.addQueryStringParameter("needPage", "false");
			sendRequest(HttpMethod.GET, UrlMy.MERPROTYPE, params, MERPROTYPE, false);
		}
	}

	/**
	 * 每日菜谱类型字典
	 */
	public void dictionaryData(String merchantId, String dailyDt) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("merchantId", merchantId);
		params.addQueryStringParameter("dailyDt", dailyDt);
		sendRequest(HttpMethod.GET, UrlMy.RECIPE_TYPE, params, RECIPE_TYPE_DATA, false);
	}
	
	

	@Override
	protected void finishedRequest(int which) {
		if(PRODUCTLIST == which || EVERYDAY_PRODUCTLIST == which){
			super.finishedRequest(which);
		}
		
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		try {
			if (which == EVERYDAY_PRODUCTLIST) {
				everyDayParseProductList(json);
			} else if (which == MERPROTYPE) {
				parseMerProType(json);
			} else if (which == PRODUCTLIST) {
				parseProductList(json);
			} else if (which == RECIPE_TYPE_DATA) {
				parseDictionaryData(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送信息
	 * 
	 * @param what
	 * @param obj
	 */
	private void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);

	}

	/**
	 * 解析每日菜谱类型字典
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseDictionaryData(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		List<MerchantMenuTypeBean> menuLists = new ArrayList<MerchantMenuTypeBean>();
		int length = array.length();
		for (int i = 0; i < length; i++) {
			JSONObject object = array.getJSONObject(i);
			MerchantMenuTypeBean menuTypeBean = new MerchantMenuTypeBean();
			menuTypeBean.typeId = object.optString("code");
			menuTypeBean.typeName = object.optString("name");
			menuLists.add(menuTypeBean);
		}

		sendMessage(PRODUCT_TYPE, menuLists);

	}

	/**
	 * 搜索商品
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseProductList(String json) throws JSONException {
		BaseListBeen<MerchantMenuBean> baseListBeen = new BaseListBeen<MerchantMenuBean>();
		JSONObject jsonObject = new JSONObject(json);
		baseListBeen.currentPage = Integer.valueOf(jsonObject.optString("currentPage"));
		currentPage = baseListBeen.currentPage;
		baseListBeen.totalPages = Integer.valueOf(jsonObject.optString("totalPages"));
		JSONArray array = jsonObject.getJSONArray("beanList");
		List<MerchantMenuBean> menuBeans = new ArrayList<MerchantMenuBean>();
		int length = array.length();
		for (int i = 0; i < length; i++) {
			JSONObject object = array.getJSONObject(i);
			MerchantMenuBean menuBean = new MerchantMenuBean();
			menuBean.id = object.optString("id");
			menuBean.name = object.optString("name");
			menuBean.Price = object.optString("actualPrice");
			menuBean.icon = object.optString("photoUrl");
			menuBean.grade = object.optString("recommendLevel");
			menuBeans.add(menuBean);
		}

		baseListBeen.list = menuBeans;
		sendMessage(DISH_STLY, baseListBeen);
	}

	/**
	 * 解析商家商品分类
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseMerProType(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		List<MerchantMenuTypeBean> menuLists = new ArrayList<MerchantMenuTypeBean>();
		if (array != null) {
			int lenth = array.length();
			for (int i = 0; i < lenth; i++) {
				JSONObject object = array.getJSONObject(i);
				MerchantMenuTypeBean menuTypeBean = new MerchantMenuTypeBean();
				menuTypeBean.typeId = object.optString("id");
				menuTypeBean.typeName = object.optString("name");
				menuLists.add(menuTypeBean);

			}
			sendMessage(PRODUCT_TYPE, menuLists);
		}
	}

	/**
	 * 解析每日菜谱
	 * 
	 * @param json
	 * @throws JSONException
	 */
	@SuppressWarnings("unused")
	private void everyDayParseProductList(String json) throws JSONException {
		BaseListBeen<MerchantMenuBean> baseListBeen = new BaseListBeen<MerchantMenuBean>();
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject != null) {
			baseListBeen.currentPage = Integer.valueOf(jsonObject.optString("currentPage"));
			baseListBeen.totalPages = Integer.valueOf(jsonObject.optString("totalPages"));
			List<MerchantMenuBean> menuBeans = new ArrayList<MerchantMenuBean>();
			JSONArray array = jsonObject.getJSONArray("beanList");
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				MerchantMenuBean menuBean = new MerchantMenuBean();
				menuBean.id = object.optString("productId");
				menuBean.name = object.optString("productName");
				menuBean.Price = object.optString("price");
				menuBean.icon = object.optString("photoUrl");
				menuBeans.add(menuBean);
			}

			baseListBeen.list = menuBeans;
			sendMessage(DISH_STLY, baseListBeen);
		}
	}
	
	
}
