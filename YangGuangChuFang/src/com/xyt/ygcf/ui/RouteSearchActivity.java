package com.xyt.ygcf.ui;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
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
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.BusRouteListAdaper;
import com.xyt.ygcf.util.BaseUtil;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.util.TimeUtils;

/**
 * 路线规划： 包含： 自驾，公交，步行三种路线方式
 * 
 */
public class RouteSearchActivity extends Activity implements
		OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
		OnRouteSearchListener, OnMapLoadedListener, AMapLocationListener {

	private int[] normalImages = { R.drawable.taxi, R.drawable.bus,
			R.drawable.walk };
	private int[] pressedImages = { R.drawable.taxi_focus,
			R.drawable.bus_focus, R.drawable.walk_focus };
	private ImageView[] textViews = new ImageView[3];

	private int routeType = 1;// 1代表公交模式，2代表驾车模式，3代表步行模式

	/** 控件对象 */
	private TextView tvTitle;
	private ImageView ivCancle;
	private TextView btnRouteLine;
	private LinearLayout llMap;
	private MapView mapView;
	private RelativeLayout rlBottomBtn;
	private RelativeLayout rlDetail;
	private LinearLayout llDetail;
	private TextView tvBusLine;
	private TextView tvBusDetail;
	private TextView btnDetail;
	private ListView listBusLineDetail;
	private LinearLayout llBusProgram;
	private TextView tvEndStation;
	private ListView listBusProgram;
	private View currentView;
	/** 地图操作类 */
	private AMap aMap;

	/** 路径规划操作 */
	private RouteSearch routeSearch;
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	private BusRouteResult busRouteResult = null;
	private LocationManagerProxy mAMapLocationManager;
	private LatLng myLocationLat, destinationLat;

	private boolean isOnclickListItem = false;
	private String city = null;
	private String[] strs = new String[2];
	private String endName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routesearch);
		mapView = (MapView) findViewById(R.id.routesearch_mapView);
		mapView.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		endPoint = new LatLonPoint(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		destinationLat = new LatLng(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		endName = bundle.getString("name");
		initView();
		registerListenerByView();
		moveToPosition();
	}

	private void initView() {
		if (null == aMap) {
			aMap = mapView.getMap();
			registerListenerByMap();
		}

		tvTitle = (TextView) findViewById(R.id.top_title);
		ivCancle = (ImageView) findViewById(R.id.icon_cancel);
		btnRouteLine = (TextView) findViewById(R.id.btn_routeline);
		llMap = (LinearLayout) findViewById(R.id.ll_map);
		rlBottomBtn = (RelativeLayout) findViewById(R.id.rl_bottom_btn);
		ImageView ivTaxi = (ImageView) findViewById(R.id.iv_taxi);
		ImageView  ivBus= (ImageView) findViewById(R.id.iv_bus);
		ImageView ivWalk = (ImageView) findViewById(R.id.iv_walk);
		rlDetail = (RelativeLayout) findViewById(R.id.rl_detail);
		llDetail = (LinearLayout) findViewById(R.id.ll_detail);
		tvBusLine = (TextView) findViewById(R.id.tv_busline);
		tvBusDetail = (TextView) findViewById(R.id.tv_busdetail);
		btnDetail = (TextView) findViewById(R.id.btn_detail);
		listBusLineDetail = (ListView) findViewById(R.id.shou_bus_instruction);
		llBusProgram = (LinearLayout) findViewById(R.id.ll_bus_program);
		tvEndStation = (TextView) findViewById(R.id.tv_end);
		tvEndStation.setText(endName);
		listBusProgram = (ListView) findViewById(R.id.routesearch_bus_list);
		textViews[0] = ivTaxi;
		textViews[1] = ivBus;
		textViews[2] = ivWalk;

	}

	/**
	 * 改变头部点击控件的背景
	 * 
	 * @param v
	 */
	private void changeBackground(View v) {
		int length = textViews.length;
		for (int i = 0; i < length; i++) {
			if (textViews[i] == v) {
				textViews[i].setImageResource(pressedImages[i]);
			} else {
				textViews[i].setImageResource(normalImages[i]);
			}
		}
	}

	/** 注册地图所需的监听 */
	private void registerListenerByMap() {
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
//		startLocation();

	}

	/** 注册控件点击的事件监听 */
	private void registerListenerByView() {
		OnClick onClick = new OnClick();
		findViewById(R.id.top_back).setOnClickListener(onClick);
		btnRouteLine.setOnClickListener(onClick);
		ivCancle.setOnClickListener(onClick);
		int len = textViews.length;
		for (int i = 0; i < len; i++) {
			textViews[i].setOnClickListener(onClick);
		}
		btnDetail.setOnClickListener(onClick);
		listBusProgram.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				isOnclickListItem = true;
				hideorShowView(true);
				ListView lv = (ListView) arg0;
				BusPath busPath = (BusPath) lv.getItemAtPosition(position);
				drawBusLineInMap(busPath);

				TextView busLine = (TextView) arg1
						.findViewById(R.id.bus_route_item_busMode);
				TextView busDetail = (TextView) arg1
						.findViewById(R.id.bus_route_item_detail);

				strs[0] = busLine.getText().toString();
				strs[1] = busDetail.getText().toString();
				String strDetail = (String) busLine.getTag();
				routeLineDetail(strs, strDetail);

			}
		});
	}

	private void startLocation() {
		if (null == mAMapLocationManager) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 5000, 100, this);
		}
	}

	public void deactivate() {
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;

	}

	private class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == currentView) {
				return;
			} else if (v.getId() == R.id.top_back) {
				finish();
				return;
			} else if (v == btnRouteLine) {
				tvTitle.setVisibility(View.GONE);
				v.setVisibility(View.GONE);
				rlBottomBtn.setVisibility(View.VISIBLE);
				changeBackground(textViews[0]);
				seachRouteQuery(startPoint, endPoint, city);
				return;

			} else if (v == ivCancle) {
				v.setVisibility(View.GONE);
				tvTitle.setVisibility(View.GONE);
				rlBottomBtn.setVisibility(View.VISIBLE);
				btnDetail.setVisibility(View.VISIBLE);
				mapView.setVisibility(View.VISIBLE);
				llDetail.setVisibility(View.VISIBLE);
				listBusLineDetail.setVisibility(View.GONE);
				return;

			} else if (v == btnDetail) {
				v.setVisibility(View.GONE);
				rlBottomBtn.setVisibility(View.GONE);
				ivCancle.setVisibility(View.VISIBLE);
				tvTitle.setVisibility(View.VISIBLE);
				tvTitle.setText("详情");
				mapView.setVisibility(View.GONE);
				listBusLineDetail.setVisibility(View.VISIBLE);
				return;

			} else if (v == textViews[0]) {
				routeType = 1;
			} else if (v == textViews[1]) {
				routeType = 2;
			} else if (v == textViews[2]) {
				routeType = 3;
			}
			currentView = v;
			seachRouteQuery(startPoint, endPoint, city);
			changeBackground(v);
		}

	}

	/**
	 * 地图加载完成回调方法
	 */
	@Override
	public void onMapLoaded() {
	}

	 /**
	 * 显示进度框
	 */
	 private void showProgressDialog() {
	 BaseUtil.showProgressDialog(this);
	 }

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		BaseUtil.dissmissProgressDialog();
	}

	/**
	 * 开始查询路径规划的方案
	 * 
	 * @param startPoint
	 *            起点
	 * @param endPoint
	 *            终点
	 * @param city
	 *            城市
	 */
	private void seachRouteQuery(LatLonPoint startPoint, LatLonPoint endPoint,
			String city) {
		 showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);

		if (routeType == 1) {
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo,
					RouteSearch.DrivingDefault, null, null, "");
			routeSearch.calculateDriveRouteAsyn(query);
			
			
		} else if (routeType == 2) {
			BusRouteQuery busQuery = new BusRouteQuery(fromAndTo,
					RouteSearch.BusDefault, city, 0);
			routeSearch.calculateBusRouteAsyn(busQuery);
		} else if (routeType == 3) {
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo,
					RouteSearch.WalkDefault);
			routeSearch.calculateWalkRouteAsyn(query);
		}
	}

	/**
	 * 公交路线查询回调
	 */
	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				List<BusPath> busPaths = result.getPaths();
				BusRouteListAdaper adaper = new BusRouteListAdaper(this,
						busPaths);
				listBusProgram.setAdapter(adaper);
				hideorShowView(false);
			} else {
				 moveToPosition();
				 hideDetail();
				Toast.makeText(this, R.string.no_result, 1000).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(this, R.string.error_network, 1000).show();
		} else {
			Toast.makeText(this, R.string.error_nukwon, 1000).show();
		}

	}

	/**
	 * 自驾路线查询回调
	 */
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		dissmissProgressDialog();
		hideorShowView(true);
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {

				DrivePath drivePath = result.getPaths().get(0);// 获取第一条路线
				List<DriveStep> driveSteps = drivePath.getSteps();
				StringBuffer sb = new StringBuffer("从我的位置出发;");
				if (drivePath != null) {
					String costTime = TimeUtils.cal((int) drivePath
							.getDuration());
					String distance = TimeUtils
							.meterTransformKilometer((int) drivePath
									.getDistance());
					strs[0] = costTime + "/" + distance;
					strs[1] = "收费：" + drivePath.getTolls() + "元";
					int len = driveSteps.size();
					for (int i = 0; i < len; i++) {
						sb.append(driveSteps.get(i).getInstruction()).append(
								";");
					}

				}
				routeLineDetail(strs, sb.toString());

				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						this, aMap, drivePath, result.getStartPos(),
						result.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			} else {
				 moveToPosition();
				 hideDetail();
				Toast.makeText(this, R.string.no_result, 1000).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(this, R.string.error_network, 1000).show();
		} else {
			Toast.makeText(this, R.string.error_nukwon, 1000).show();
		}

	}

	/**
	 * 步行路线查询回调
	 */
	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		dissmissProgressDialog();

		hideorShowView(true);
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				WalkPath walkPath = result.getPaths().get(0);// 获取第一条路线
				StringBuffer sb = new StringBuffer("从我的位置出发;");
				strs[0] = "步行方案";
				if (walkPath != null) {
					String duration = TimeUtils.cal((int) walkPath
							.getDuration());
					String distance = TimeUtils
							.meterTransformKilometer((int) walkPath
									.getDistance());
					strs[1] = duration + "/" + distance;

					List<WalkStep> walkSteps = walkPath.getSteps();
					int len = walkSteps.size();
					for (int i = 0; i < len; i++) {
						sb.append(walkSteps.get(i).getInstruction())
								.append(";");
					}
				}

				routeLineDetail(strs, sb.toString());
				aMap.clear();// 清理地图上的所有覆盖物
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
						aMap, walkPath, result.getStartPos(),
						result.getTargetPos());
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
			} else {
				 moveToPosition();
				 hideDetail();
				Toast.makeText(this, R.string.no_result, 1000).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(this, R.string.error_network, 1000).show();
		} else {
			Toast.makeText(this, R.string.error_nukwon, 1000).show();
		}
	}

	/**
	 * 移到中心点
	 * 
	 */
	private void moveToPosition() {
		SpHelper sp = SpHelper.init(this);
		myLocationLat = new LatLng(sp.getCityLatitude(),
				sp.getCityLongitude());
		startPoint = new LatLonPoint(sp.getCityLatitude(),
				sp.getCityLongitude());
		city = sp.getCityName();

		aMap.clear();
		aMap.addMarker(new MarkerOptions()
				.position(myLocationLat)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_marker))
				.perspective(true).draggable(true));
		
		aMap.addMarker(new MarkerOptions()
		.position(destinationLat)
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding))
		.perspective(true).draggable(true));
		
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(destinationLat, 16);
		aMap.moveCamera(update);
	}

	/**
	 * 路线详情
	 * 
	 * @param strs
	 * @param str
	 */
	private void routeLineDetail(String[] strs, String str) {

		rlDetail.setVisibility(View.VISIBLE);
		tvBusLine.setText(strs[0]);
		tvBusDetail.setText(strs[1]);
		String[] strDetail = str.split(";");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.online_list_item, strDetail);
		listBusLineDetail.setAdapter(adapter);

	}

	/**
	 * 隐藏详情页
	 */
	private void hideDetail() {
		rlDetail.setVisibility(View.GONE);
	}

	/**
	 * 在地图上绘制一条公交路线
	 * 
	 * @param busPath
	 */
	private void drawBusLineInMap(BusPath busPath) {

		aMap.clear(); // 清理地图上的所有覆盖物
		// // 绘制一条公交路线
		BusRouteOverlay busOverlay = new BusRouteOverlay(this, aMap, busPath,
				busRouteResult.getStartPos(), busRouteResult.getTargetPos());
		busOverlay.removeFromMap();
		busOverlay.addToMap();
		busOverlay.zoomToSpan();
	}

	/** 切换地图与公交方案列表 */
	private void hideorShowView(boolean isShowBusrouteInMap) {

		if (isShowBusrouteInMap) {
			llBusProgram.setVisibility(View.GONE);
			llMap.setVisibility(View.VISIBLE);
		} else {

			llMap.setVisibility(View.GONE);
			llBusProgram.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		// if (keyCode == KeyEvent.KEYCODE_BACK) {
		// if (topTitle.isShown()) {
		// hideDetail();
		// return false;
		// }
		// }

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public View getInfoContents(Marker marker) {
		String str = marker.getTitle();
		if (str.contains("(")) {
			str = str.substring(0, str.indexOf("("));
			TextView button = new TextView(this);
			button.setText(str);
			button.setTextColor(Color.BLACK);
			return button;
		} else {
			return null;
		}
	}

	@Override
	public View getInfoWindow(Marker marker) {
		String str = marker.getTitle();
		if (str.contains("(")) {
			str = str.substring(0, str.indexOf("("));
			TextView button = new TextView(this);
			button.setText(str);
			button.setTextColor(Color.BLACK);
			return button;
		} else {
			return null;
		}

	}

	@Override
	public void onInfoWindowClick(Marker arg0) {

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			deactivate();
//			myLocationLat = new LatLng(location.getLatitude(),
//					location.getLongitude());
//			startPoint = new LatLonPoint(location.getLatitude(),
//					location.getLongitude());
//			city = location.getCity();
//			moveToPosition();
		}

	}

}
