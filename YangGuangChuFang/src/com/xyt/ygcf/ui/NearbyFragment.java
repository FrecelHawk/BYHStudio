package com.xyt.ygcf.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.BrandMerchantAreaAdapter;
import com.xyt.ygcf.adpter.BrandMerchantPopAdapter;
import com.xyt.ygcf.adpter.BrendMerchantDistanceAdapter;
import com.xyt.ygcf.adpter.NearbyListAdapter;
import com.xyt.ygcf.base.BaseFragment;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.CityObjBean;
import com.xyt.ygcf.entity.FoodTypeBean;
import com.xyt.ygcf.entity.NearbyListItemBean;
import com.xyt.ygcf.home.MerchantDatails;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.DialogUtil;
import com.xyt.ygcf.util.SearchShopTool;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.view.CustomListView;
import com.xyt.ygcf.view.CustomListView.OnLoadMoreListener;
import com.xyt.ygcf.view.CustomListView.OnRefreshListener;

public class NearbyFragment extends BaseFragment implements OnItemClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
		OnMarkerClickListener, OnMapLoadedListener {


	/** 搜索商家 */
	private final static int SEARCH_NEARBY_SHOP = 0;
	private final static int QUREY_AREA =1;
	private final static int QUREY_FOOD_TYPE =2;

	private List<NearbyListItemBean> listDatas = new ArrayList<NearbyListItemBean>();

	private int currentPage = 1; // 当前页数

	private  View mView;
	private FragmentActivity mActivity;
	private AMap aMap;
	private MapView mapView = null;
	private LinearLayout ll_listdat_parent;
	private CustomListView lv_show;
	private ImageView btn_change;
	private LinearLayout ll_seacher;
	private ImageView ivSearch;
	private ImageView topBack;
	private Button btn_search;
	private EditText box_search;
	private TextView tvNoData;
	private TextView currentTV;
	private GeocodeSearch geocoderSearch;
	private boolean isShowMap = false; // 判断是否显示地图
	private boolean isLocaltionInfo = true;
	private NearbyListAdapter adapter;
	private RequestParams params;
	private String regionId = "";
	private String foodTypeId = "";
	private String orderBy = "";
	
	private PopupWindow mPopupWindow ;

	private BrandMerchantAreaAdapter areaAdapter;
	private BrandMerchantPopAdapter merchantPopAdapter;
	private BrendMerchantDistanceAdapter distanceAdapter;

	public String[] sortNames = { "默认", "最新餐厅" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_nearby, null);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mView = getView();
		mActivity = getActivity();
		initView(savedInstanceState);
		if (mActivity instanceof Main) {
			sendRequestByLocation();
			
		} else {
			showShopListView();
			geocoderSearch = new GeocodeSearch(mActivity);
			geocoderSearch.setOnGeocodeSearchListener(new Geocoder());
			geocodeQuery(SpHelper.init(mContext).getCurrentCity());
			
		}
		distanceAdapter = new BrendMerchantDistanceAdapter(
				mActivity, sortNames);
		//查询美食分类
//		sendRequest(UrlMy.FOODTYPE, new RequestParams(), QUREY_FOOD_TYPE, false);
	}
	
	/**
	 *监听有网络时回调
	 */
	public void netConnectCallBreak(){
		sendRequestByLocation();
//		SpHelper spHelper =SpHelper.init(mContext);
//		Double lat = spHelper.getCityLatitude();
//		Double log = spHelper.getCityLongitude();
//		moveToPosition(lat, log);
	}

	// 初始化控件，事件监听
	private void initView(Bundle savedInstanceState) {
		
		topBack = (ImageView) mView.findViewById(R.id.back_btn);
		btn_change = (ImageView) mView.findViewById(R.id.left_btn);
		mapView = (MapView) mView.findViewById(R.id.nearby_mapView);
		ivSearch = (ImageView) mView.findViewById(R.id.right_btn);
		ivSearch.setImageResource(R.drawable.search);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		tvNoData = (TextView) mView.findViewById(R.id.no_data);
		ll_listdat_parent = (LinearLayout) mView
				.findViewById(R.id.nearby_listData_parent);
		lv_show = (CustomListView) mView.findViewById(R.id.result_list);
		ll_seacher = (LinearLayout) mView
				.findViewById(R.id.ll_seacher);
		ll_seacher.setVisibility(View.GONE);
		btn_search = (Button) mView.findViewById(R.id.search_btn);
		box_search = (EditText) mView.findViewById(R.id.box_search);
		btn_change.setImageResource(R.drawable.nearby_list_image);
		btn_change.setVisibility(View.VISIBLE);
		lv_show.setCanRefresh(true);
		adapter = new NearbyListAdapter(mActivity, listDatas,Constants.INDUSTRY_RESTAURANT_MAKER);
		topBack.setOnClickListener(this);
		btn_change.setOnClickListener(this);
		ivSearch.setOnClickListener(this);
		mView.findViewById(R.id.city_filtration).setOnClickListener(this);
		mView.findViewById(R.id.cate_filtration).setOnClickListener(this);
		mView.findViewById(R.id.sort_filtration).setOnClickListener(this);
		btn_search.setOnClickListener(this);
		lv_show.setOnItemClickListener(this);

		lv_show.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				sendQuery(false);
				
			}
		});
		lv_show.setOnLoadListener(new OnLoadMoreListener() {
			// 加载更多
			@Override
			public void onLoadMore() {
				++currentPage;
				sendQuery(false);
			}
		});

		if (null == aMap) {
			aMap = mapView.getMap();
			setUpMap();
		}

	}

	private void setUpMap() {
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
		aMap.setOnMapLoadedListener(this);
	}

	/**
	 * 发送搜索商家的请求
	 */
	private void sendQuery(Boolean showProgressDialog) {
		params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
		sendRequest(UrlMy.SEARCH_SHOP, params, SEARCH_NEARBY_SHOP, showProgressDialog);
	}

	/**
	 * 地图加载完成时回调的方法
	 */
	@Override
	public void onMapLoaded() {
		
		if (mActivity instanceof Main) {
			SpHelper spHelper =SpHelper.init(mContext);
			Double lat = spHelper.getCityLatitude();
			Double log = spHelper.getCityLongitude();
			moveToPosition(lat, log);
		}
	}

	/**
	 * 地图上标志Marker的点击事件
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (!marker.isInfoWindowShown()) {
			marker.showInfoWindow();
		}
		return true;
	}

	@Override
	public View getInfoContents(Marker marker) {
		/**
		 * 这里不能放置有自动获取焦点的控件，否则点击弹出框会没反应
		 */
		TextView button = new TextView(mActivity);
		button.setText(marker.getTitle());
		button.setTextColor(Color.BLACK);
		return button;
	}

	/**
	 * 点击地图标志marker后弹出的窗体
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		/**
		 * 这里不能放置有自动获取焦点的控件，否则点击弹出框会没反应
		 */
		TextView button = new TextView(mActivity);
		button.setText(marker.getTitle());
		button.setTextColor(Color.BLACK);

		return button;
	}

	/**
	 * 地图标志marker后弹出的窗体的点击事件
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		if (null != marker) {
			marker.hideInfoWindow();
			startActivity(marker.getSnippet());
		}

	}
	
	/**
	 * 发送请求
	 */
	private final void sendRequestByLocation(){
		regionId = SpHelper.init(mContext).getCityCode();
		params = SearchShopTool.searchShopByArea(mContext, regionId, Constants.INDUSTRY_RESTAURANT_MAKER,"range");
		sendQuery(true);
	}

	/**
	 * 添加商家标志在地图上
	 * 
	 * @param listDatas
	 */
	private void addShopMarkersToMap(List<NearbyListItemBean> listDatas) {

		int size = listDatas.size();
		for (int i = 0; i < size; i++) {
			NearbyListItemBean itemData = listDatas.get(i);
			LatLng latLng = new LatLng(itemData.latitude,
					itemData.longitude);
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(latLng);
			markerOptions.title(itemData.stopName);
			markerOptions.snippet(itemData.stopId);
			markerOptions.perspective(false);
			markerOptions.draggable(false);
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding));
			aMap.addMarker(markerOptions);

		}

	}

//	/**
//	 * 自定义我的位置
//	 */
//	private void setMyLocationMarker() {
//		MyLocationStyle myLocationStyle = new MyLocationStyle();
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
//		aMap.setMyLocationStyle(myLocationStyle);
//		aMap.setMyLocationRotateAngle(180);
//		aMap.setLocationSource(locationLiner);// 设置定位监听
//		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//		aMap.setMyLocationEnabled(true);
//
//	}

	private void searchByWord() {
		String word = box_search.getText().toString();
		if (TextUtils.isEmpty(word)) {
			DialogUtil.toast(mActivity, "请输入关键字", 1000);
			return;
		}
		isLocaltionInfo = false;
		currentPage = 1;
		params = SearchShopTool.searchShopByWord(mContext,word, regionId, Constants.INDUSTRY_RESTAURANT_MAKER);
		sendQuery(true);

	}

	/**
	 * 移到中心点
	 * 
	 * @param latlng
	 */
	private void moveToPosition(Double lat,Double log) {
		// 纬度：23.122934进度：113.371914
//		aMap.clear();
		LatLng latlng = new LatLng(lat,log);
		aMap.addMarker(new MarkerOptions()
				.position(latlng)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_marker))
				.anchor(0.5f, 0.5f));

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 15);
		aMap.moveCamera(update);
	}

	/**
	 * 单击切换监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			mActivity.finish();
			break;
		case R.id.left_btn:
			if (!isShowMap) {
				btn_change.setImageResource(R.drawable.nearby_map_image);
				mapView.setVisibility(View.GONE);
				
				ivSearch.setVisibility(View.VISIBLE);
				ll_listdat_parent.setVisibility(View.VISIBLE);
				isShowMap = true;
			} else {

				btn_change.setImageResource(R.drawable.nearby_list_image);
				mapView.setVisibility(View.VISIBLE);
				ivSearch.setVisibility(View.GONE);
				ll_listdat_parent.setVisibility(View.GONE);
				isShowMap = false;

			}
			break;

		case R.id.right_btn:
			if (ll_seacher.isShown()) {
				ll_seacher.setVisibility(View.GONE);
			} else {
				ll_seacher.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.city_filtration:
			v.setSelected(true);
			currentTV = (TextView) v;
			if (areaAdapter == null) {
				sendRequest(UrlMy.REGION, SearchShopTool.regionParams(regionId), QUREY_AREA, false);
			}else {
				handler.sendEmptyMessage(QUREY_AREA);
			}
			
			break;
		case R.id.cate_filtration:
			v.setSelected(true);
			currentTV = (TextView) v;
			if (null == merchantPopAdapter) {
				sendRequest(UrlMy.FOODTYPE, new RequestParams(), QUREY_FOOD_TYPE, false);
			}else {
				handler.sendEmptyMessage(QUREY_FOOD_TYPE);
			}
			break;
		case R.id.sort_filtration:
			v.setSelected(true);
			currentTV = (TextView) v;
			
			showPopupwindow(distanceAdapter, currentTV);
			break;
		case R.id.search_btn:
			searchByWord();
			break;
		}
	}

	/**
	 * 监听listView的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		// 跳转到商家详情
		ListView listItem = (ListView) arg0;
		NearbyListItemBean itemBean = (NearbyListItemBean) listItem
				.getItemAtPosition(position);
		startActivity(itemBean.stopId);
	}

	/**
	 * 跳转到商家详情页
	 * 
	 * @param id
	 *            商家id
	 */
	private void startActivity(String id) {
		Intent intent = new Intent(mActivity, MerchantDatails.class);
		intent.putExtra("mark", Constants.INDUSTRY_RESTAURANT_MAKER);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	@Override
	protected void finishedRequest(int which) {
		if (which == SEARCH_NEARBY_SHOP) {
			super.finishedRequest(which);
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
					if (currentPage == 1) {
						lv_show.setCanLoadMore(true);
						lv_show.setAdapter(adapter);
						listDatas.clear();
					}
					BaseListBeen<NearbyListItemBean> been = (BaseListBeen<NearbyListItemBean>) msg.obj;
					if (been.list.size() != 0) {
						listDatas.addAll(been.list);
						lv_show.onRefreshComplete();
						if (isLocaltionInfo) {
							addShopMarkersToMap(listDatas);
						}
						tvNoData.setVisibility(View.GONE);
						lv_show.setVisibility(View.VISIBLE);
						adapter.notifyDataSetChanged();
					} else {
						tvNoData.setVisibility(View.VISIBLE);
						lv_show.setVisibility(View.GONE);
					}
					
					lv_show.onLoadMoreComplete();
					if (been.currentPage == been.totalPages) {
						lv_show.setCanLoadMore(false);
					}
				
				break;

			case QUREY_AREA:
				
				if (null == areaAdapter) {
					List<CityObjBean> cityObjBeans = (List<CityObjBean>) msg.obj;
					areaAdapter = new BrandMerchantAreaAdapter(
							mContext, cityObjBeans);
				}
				showPopupwindow(areaAdapter, currentTV);
				break;
			case QUREY_FOOD_TYPE:
				if (null==merchantPopAdapter) {
					List<FoodTypeBean> foodTypeBeans = (List<FoodTypeBean>) msg.obj;
					merchantPopAdapter = new BrandMerchantPopAdapter(mActivity,foodTypeBeans);
					
				}
				
				showPopupwindow(merchantPopAdapter,currentTV);
				break;
				
			}

		}

	};

	/**
	 * 根据城市查询城市中心坐标
	 * 
	 * @param cityName
	 *            城市名称
	 */
	private void geocodeQuery(String cityName) {
		GeocodeQuery query = new GeocodeQuery(cityName, cityName);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 地理编码与逆地理编码功能
	 */
	private class Geocoder implements OnGeocodeSearchListener {
		/**
		 * 地理编码查询回调 根据地理位置转化为地图坐标
		 */
		@Override
		public void onGeocodeSearched(GeocodeResult result, int rCode) {

			if (rCode == 0) {
				if (result != null && result.getGeocodeAddressList() != null
						&& result.getGeocodeAddressList().size() > 0) {
					GeocodeAddress address = result.getGeocodeAddressList()
							.get(0);
					LatLonPoint point =address.getLatLonPoint();
					moveToPosition(point.getLatitude(),point.getLongitude());
					

				} else {
				}
			} else if (rCode == 27) {
			} else if (rCode == 32) {
			} else {
			}

		}

		/** 逆地理编码查询回调 */
		@Override
		public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {

		}

	}

//	/** 定位源 */
//	private class LocationLiner implements LocationSource {
//		@Override
//		public void activate(OnLocationChangedListener listener) {
//			mListener = listener;
//			if (null == mAMapLocationManager) {
//				mAMapLocationManager = LocationManagerProxy
//						.getInstance(mActivity);
//				mAMapLocationManager.requestLocationUpdates(
//						LocationProviderProxy.AMapNetwork, 5000, 100,
//						amListener);
//			}
//		}
//
//		@Override
//		public void deactivate() {
//			if (mAMapLocationManager != null) {
//				mAMapLocationManager.removeUpdates(amListener);
//				mAMapLocationManager.destory();
//			}
//			mAMapLocationManager = null;
//
//		}
//
//	}

//	/** 监听定位回调 */
//	private class AmapLocationListener implements AMapLocationListener {
//
//		@Override
//		public void onLocationChanged(AMapLocation aLocation) {
//			
//
//			if (aLocation != null && mListener != null) {
//				mListener.onLocationChanged(aLocation);// 显示系统小蓝点
//				if (!isAgainRequest) {
//  					((Main) mActivity).loactionCallBreak(aLocation);// 将定位信息返回给首页
//					regionId = aLocation.getAdCode().substring(0, 4);
//					SpHelper spHelper = SpHelper.init(mActivity);
//					spHelper.setUerLatitude(aLocation.getLatitude());
//					spHelper.setUerLongitude(aLocation.getLongitude());
//					spHelper.setCityName(aLocation.getCity());
//					params = shopTool.searchShopByArea(regionId, Constants.INDUSTRY_RESTAURANT_MAKER,"range");
//					sendQuery(true);
//					sendRequest(UrlMy.REGION, shopTool.regionParams(regionId), QUREY_AREA, false);
////					locationParams = params;
//					isAgainRequest = true;
//				}
//
//				locationLiner.deactivate();
//			}
//
//		}
//
//		@Override
//		public void onLocationChanged(Location arg0) {
//
//		}
//
//		@Override
//		public void onProviderDisabled(String arg0) {
//
//		}
//
//		@Override
//		public void onProviderEnabled(String arg0) {
//
//		}
//
//		@Override
//		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//			// TODO Auto-generated method stub
//
//		}
//
//	}

	/**
	 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
	 */

	@Override
	public void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		mapView.onDestroy();
		super.onDestroy();
	}

	/**
	 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	 */
	@Override
	public void onPause() {
		mapView.onPause();
		super.onPause();
	}

	/**
	 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
	 */
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	public void isSelected(){
//		cityFiltration.setText("");
//		cateFiltration.setText("");
//		sortFiltration.setText("");
//		btn_change.setImageResource(R.drawable.nearby_list_image);
//		mapView.setVisibility(View.VISIBLE);
//		ivSearch.setVisibility(View.GONE);
//		ll_listdat_parent.setVisibility(View.GONE);
//		isShowMap = false;
//		moveToPosition(location.getLatitude(), location.getLongitude());
//		isLocaltionInfo =true;
//		params = locationParams;
//		sendQuery();
		
	}
	
	public void showShopListView() {
		
		// isLocaltionInfo = false;
		TextView topTitle = (TextView) mActivity.findViewById(R.id.desktop_userID);
		topTitle.setText("阳光餐厅");
		topBack.setVisibility(View.VISIBLE);
		btn_change.setImageResource(R.drawable.nearby_map_image);
		mapView.setVisibility(View.GONE);
		ivSearch.setVisibility(View.VISIBLE);
		ll_listdat_parent.setVisibility(View.VISIBLE);
		isShowMap = true;
		regionId = SpHelper.init(mContext).getCurrentCityID();
		params = SearchShopTool.searchShopByArea(mContext,regionId, Constants.INDUSTRY_RESTAURANT_MAKER,null);
		sendQuery(true);
//		sendRequest(UrlMy.REGION, SearchShopTool.regionParams(regionId), QUREY_AREA, false);
	}

	public void showPopupwindow(final BaseAdapter adapter,
			final TextView txtView) {
		View popupView = mActivity.getLayoutInflater().inflate(
				R.layout.layout_popupwindow, null);
		ListView listView = (ListView) popupView.findViewById(R.id.popList);
		// 创建一个popupwindow实例
		if(adapter instanceof BrendMerchantDistanceAdapter){
			mPopupWindow = new PopupWindow(popupView,
					txtView.getWidth(), LayoutParams.WRAP_CONTENT, true);
		}else {
			mPopupWindow = new PopupWindow(popupView,
					LayoutParams.MATCH_PARENT, mActivity.getWindowManager()
							.getDefaultDisplay().getHeight() / 2, true);
		}
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(mActivity
				.getResources(), (Bitmap) null));
		// 加上这个属性，PopupWindow的ListView的Item才可以点击
		mPopupWindow.setFocusable(true);
		mPopupWindow.showAsDropDown(txtView, 0, 0);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ListView listItem = (ListView) arg0;
				Object object = listItem.getItemAtPosition(arg2);
				if (object instanceof FoodTypeBean) { // 美食筛选
					FoodTypeBean foodTypeBean = (FoodTypeBean) object;
					txtView.setText(foodTypeBean.getFoodName());
					foodTypeId = foodTypeBean.getFoodId();
				} else if (object instanceof String) { // 排序筛选
//					String str = (String) object;
//					txtView.setText(str);
//					orderBy = str;

				} else { // 地区筛选
					CityObjBean cityObjBean = (CityObjBean) object;
					txtView.setText(cityObjBean.name);
					regionId = cityObjBean.id;
				}
				isLocaltionInfo = false;
				currentPage = 1;
				params = SearchShopTool.searchShopByCondition(mContext,regionId, foodTypeId,
						orderBy,Constants.INDUSTRY_RESTAURANT_MAKER);
				
				sendQuery(true);
				mPopupWindow.dismiss();
			}
		});

		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mPopupWindow !=null &&!mPopupWindow.isShowing()) {
					txtView.setSelected(false);
				}
				mPopupWindow = null;
			}
		});
	}

	@Override
	public void handleJson(String json, int which) {
		
		try {
			
			switch (which) {
			case SEARCH_NEARBY_SHOP:
				System.out.println("----------"+json);
				SearchShopTool.parserJson(json, handler, SEARCH_NEARBY_SHOP);
				break;
			case QUREY_AREA:
				SearchShopTool.parseRegion(json,handler,regionId,QUREY_AREA);
				break;
			case QUREY_FOOD_TYPE:
				SearchShopTool.parserFoodType(json, handler, QUREY_FOOD_TYPE);
				break;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void handleNetError(String message, int which) {
		
	}
	
}
