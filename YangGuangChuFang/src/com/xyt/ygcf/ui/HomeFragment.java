package com.xyt.ygcf.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.HomeListAdapter;
import com.xyt.ygcf.base.BaseFragment;
import com.xyt.ygcf.entity.AdBean;
import com.xyt.ygcf.entity.CityObjBean;
import com.xyt.ygcf.home.BrandMerchantActivity;
import com.xyt.ygcf.home.RestaurantListActivity;
import com.xyt.ygcf.home.ViolationListActivity;
import com.xyt.ygcf.impl.IActivityComunicationFragment;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.ui.my.CollectionActivitiy;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.DeviceHelper;
import com.xyt.ygcf.util.DialogUtil;
import com.xyt.ygcf.util.MyDialog;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.util.StringUtils;
import com.xyt.ygcf.widget.AbScrollView;
import com.xyt.ygcf.widget.MyListView;

public class HomeFragment extends BaseFragment implements TextWatcher, OnClickListener {
	
	private final  String mPageName = "HomeFragment";

	private View view;
	private Context context;
	private AbScrollView homeLayout;
	// 抽屉控件
	private EditText mSearchBox;
	private ListView mSelectList;
	private Button mCity,mLoadCity;
	private TextView mLocationCity;
	private DrawerLayout mDrawerLayout;
	private List<CityObjBean> cityObjs; // 城市数据集
	// 广告
	private ViewPager adViewPager;
	private RelativeLayout pagerLayout;
	private List<View> pageViews;
	private ImageView[] imageViews;
	private ImageView imageView;
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private boolean isContinue = true;

	private List<AdBean> adBeans;
	/** 是否是第一次启动 */
	private boolean isFirstIn = true;
	/** 默认城市 */
	private final static String DEFAULT_CITY = "default_city";
	/** 地区 */
	private final static int REGION = 2;
	/** 广告 */
	private final static int ADVERTISING = 3;
	/** 版本更新 */
	private final static int VERSIONCHECK = 4;

	private HashMap<String, Integer> alphaIndexer; // 存放存在的汉语拼音首字母和与之对应的列表位置

	private SharedPreferences preferences;
	public String cityname;
	public String cityid;
    private boolean isOpent = false;

	private IActivityComunicationFragment listener = null;

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_home, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listener = (IActivityComunicationFragment) mContext;
		view = getView();
		initBitmapVariable();
		initwidget();
		initDrawerLayout();
		region();
	}

	private void initViewPager() {
		// 从布局文件中获取ViewPager父容器
		pagerLayout = (RelativeLayout) view.findViewById(R.id.view_pager_content);
		pagerLayout.removeAllViews();
		// 创建ViewPager
		adViewPager = new ViewPager(mContext);
		// 获取屏幕像素相关信息
//		DisplayMetrics dm = new DisplayMetrics();
//		dm = context.getResources().getDisplayMetrics();

		// 根据屏幕信息设置ViewPager广告容器的宽高
		// adViewPager.setLayoutParams(new LayoutParams(dm.widthPixels,
		// dm.heightPixels * 2 / 5));

		// 将ViewPager容器设置到布局文件父容器中
		pagerLayout.addView(adViewPager);
		initPageAdapter();
		initCirclePoint();
		AdPageAdapter adapter = new AdPageAdapter(pageViews);
		adViewPager.setAdapter(adapter);
		adViewPager.setOnPageChangeListener(new AdPageChangeListener());

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(atomicInteger.get());
						atomicOption();
					}
				}
			}
		}).start();

	}

	/** 抽屉里面的按钮和事件等 */
	private void initDrawerLayout() {
		
		mSearchBox = (EditText) view.findViewById(R.id.home_city_search);
		mSelectList = (ListView) view.findViewById(R.id.home_city_select);
		mCity = (Button) view.findViewById(R.id.home_location_city);
		mLoadCity = (Button) view.findViewById(R.id.home_load_location_city);
		mSearchBox.addTextChangedListener(this);
		mCity.setOnClickListener(this);
	}

	/**
	 * 获取编辑框的文本
	 * 
	 * @return 返回编辑框中的文本
	 */
	private String getEtSearchBoxText() {
		return mSearchBox.getText().toString().trim();
	}

	private void initwidget() {
		RelativeLayout mTitle = (RelativeLayout) view.findViewById(R.id.logout_btn);
		mLocationCity = (TextView) view.findViewById(R.id.city_text);
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.home_drawer_layout);
		homeLayout = (AbScrollView) view.findViewById(R.id.home_layout);
		mTitle.setVisibility(View.VISIBLE);
		homeLayout.setVisibility(View.GONE);
		cityname = SpHelper.init(mContext).getStringValue(Constants.DEFAULTCITY, null);
		cityid = SpHelper.init(mContext).getStringValue(Constants.DEFAULTCITY_ID, null);
		mLocationCity.setText(cityname);
		mLocationCity.setTag(cityid);
		// 禁止DrawerLayout的手势事件
//		 mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
		mDrawerLayout.closeDrawer(GravityCompat.START); // 关闭抽屉
		mTitle.setOnClickListener(this);
		/** 功能区 */
		 MyListView featureListView = (MyListView) view.findViewById(R.id.home_list);
		 HomeListAdapter featureAdapter = new HomeListAdapter(mContext);
		featureListView.setAdapter(featureAdapter);
		featureListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				switch (arg2) {
					case 0:
						intent.setClass(mContext, RestaurantListActivity.class);
						break;
					case 1:
						intent.setClass(mContext, BrandMerchantActivity.class);
						intent.putExtra("mark", Constants.INDUSTRY_SCHOOL_MAKER);
						break;
					case 2:
						intent.setClass(mContext, BrandMerchantActivity.class);
						intent.putExtra("mark", Constants.INDUSTRY_COMPANY_MAKER);
						break;
					case 3:
						if (LoginHelper.isLogin()) {
							intent.setClass(mContext, CollectionActivitiy.class);
						} else {
							Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 4:
						intent.setClass(mContext, ViolationListActivity.class);
						break;
					
				}
				startActivity(intent);
				intent = null;
			}
		});

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.logout_btn: // 头部右边按钮
				if (isOpent) {
					isOpent = false;
					mDrawerLayout.closeDrawer(GravityCompat.START);
				} else {
					// 将抽屉打开
					mDrawerLayout.openDrawer(GravityCompat.START);
					isOpent = true;
				}
			
				break;
			case R.id.home_location_city:
				String name = mCity.getText().toString();
				mLocationCity.setText(name.replace("市", ""));
				mLocationCity.setTag(SpHelper.init(mContext).getCityCode());
				advertising(SpHelper.init(mContext).getCityCode());
				mDrawerLayout.closeDrawer(GravityCompat.START);
				break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void atomicOption() {
		atomicInteger.incrementAndGet();
		if (atomicInteger.get() > imageViews.length - 1) {
			atomicInteger.getAndAdd(-5);
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
	}

	/*
	 * 每隔固定时间切换广告栏图片
	 */
	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			adViewPager.setCurrentItem(msg.what);
		}

	};

	private void initPageAdapter() {
		pageViews = new ArrayList<View>();
		// bitmapUtils = new BitmapUtils(mContext);
		if (bitmapUtils == null) {
			super.initBitmapVariable();
		}
		int size = adBeans.size();
		for (int i = 0; i < size; i++) {
			AdBean adBean = adBeans.get(i);
			ImageView img = new ImageView(mContext);
			img.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(img, adBean.getPhotoUrl());
			// img.setBackgroundResource(R.drawable.one);
			pageViews.add(img);
		}
		

	}

	private void initCirclePoint() {
		ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
		group.removeAllViews();
		// group.setBackgroundColor(Color.argb(200, 135, 135, 152));
		imageViews = new ImageView[pageViews.size()];
		// 广告栏的小圆点图标
		int size= pageViews.size();
		for (int i = 0; i < size; i++) {
			// 创建一个ImageView, 并设置宽高. 将该对象放入到数组中
			LinearLayout layout = new LinearLayout(mContext);
			layout.setPadding(20, 0, 0, 0);
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new LayoutParams(15, 15));
			imageViews[i] = imageView;

			// 初始值, 默认第0个选中2
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.feature_point_cur);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.feature_point);
			}
			// 将小圆点放入到布局中

			layout.addView(imageViews[i]);
			group.addView(layout);
		}
	}

	/**
	 * ViewPager 页面改变监听器
	 */
	private final class AdPageChangeListener implements OnPageChangeListener {

		/**
		 * 页面滚动状态发生改变的时候触发
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		/**
		 * 页面滚动的时候触发
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/**
		 * 页面选中的时候触发
		 */
		@Override
		public void onPageSelected(int arg0) {
			// 获取当前显示的页面是哪个页面
			atomicInteger.getAndSet(arg0);
			// 重新设置原点布局集合
			int length =  imageViews.length;
			for (int i = 0; i < length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.feature_point_cur);
				if (arg0 != i) {
					
					imageViews[i].setBackgroundResource(R.drawable.feature_point);
				}
			}
		}
	}

	private final class AdPageAdapter extends PagerAdapter {
		private List<View> views = null;

		/**
		 * 初始化数据源, 即View数组
		 */
		public AdPageAdapter(List<View> views) {
			this.views = views;
		}

		/**
		 * 从ViewPager中删除集合中对应索引的View对象
		 */
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		/**
		 * 获取ViewPager的个数
		 */
		@Override
		public int getCount() {
			return views.size();
		}

		/**
		 * 从View集合中获取对应索引的元素, 并添加到ViewPager中
		 */
		@Override
		public Object instantiateItem(View container, final int position) {
			((ViewPager) container).addView(views.get(position), 0);
			View currentView = views.get(position);
			currentView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String jumpUrl = adBeans.get(position).getJumpUrl();
					if ("".equals(jumpUrl)) {
						return;
					}
					Uri uri = Uri.parse(jumpUrl);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}
			});

			return views.get(position);
		}

		/**
		 * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联 这个方法是必须实现的
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	/**
	 * 城市选择Adapter
	 * 
	 */
	private class CityAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public CityAdapter() {
			this.mInflater = LayoutInflater.from(mContext);
			alphaIndexer = new HashMap<String, Integer>();
			int size = cityObjs.size();
			for (int i = 0; i < size; i++) {
				// 当前汉语拼音首字母
				String currentAlpha = cityObjs.get(i).alpha;
				// 上一个汉语拼音首字母，如果不存在为""
				String previewAlpha = (i - 1) >= 0 ? cityObjs.get(i - 1).alpha : " ";
				if (!previewAlpha.equals(currentAlpha)) {
					String alpha = cityObjs.get(i).alpha;
					alphaIndexer.put(alpha, i);
				}
			}
		}

		@Override
		public int getCount() {
			return cityObjs.size();
		}

		@Override
		public Object getItem(int position) {
			return cityObjs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.home_city_select_item, null);
				holder.mHotCity = (TextView) convertView.findViewById(R.id.home_not_city);
				holder.mAlpha = (TextView) convertView.findViewById(R.id.alpha_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final CityObjBean item = cityObjs.get(position);
			holder.mHotCity.setText(item.name);
			String currentAlpha = cityObjs.get(position).alpha;
			String previewAlpha = (position - 1) >= 0 ? cityObjs.get(position - 1).alpha : " ";
			if (!previewAlpha.equals(currentAlpha)) {
				holder.mAlpha.setVisibility(View.VISIBLE);
				holder.mAlpha.setText(currentAlpha);
			} else {
				holder.mAlpha.setVisibility(View.GONE);
			}
			holder.mHotCity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mLocationCity.setText(item.name);
					mLocationCity.setTag(item.id);
					advertising(item.id);
					// 将抽屉关闭
					mDrawerLayout.closeDrawer(Gravity.LEFT);
				}
			});
			return convertView;
		}

		public class ViewHolder {
			TextView mHotCity;
			TextView mAlpha;
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onPause() {
		super.onPause();
		SpHelper.init(mContext).setCurrentCity(mLocationCity.getText().toString());
		SpHelper.init(mContext).setCurrentCityID((String) mLocationCity.getTag());
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String alpae = null;
		if (arg0.length() > 0) {
			if (StringUtils.isChineseEnglish(getEtSearchBoxText().charAt(0)) == "minLetter"
					|| StringUtils.isChineseEnglish(getEtSearchBoxText().charAt(0)) == "maxLetter") {
				alpae = getEtSearchBoxText().toUpperCase();
			} else if (StringUtils.isChineseEnglish(getEtSearchBoxText().charAt(0)) == "hanzi") {
				alpae = PinyinHelper(getEtSearchBoxText());
			}
			if (alphaIndexer.get(alpae) != null) {
				int position = alphaIndexer.get(alpae);
				mSelectList.setSelection(position);
			}
		} else if (arg0.length() == 0) {

		}
	}

	/**
	 * 提取汉字的首字母
	 * 
	 * @param cityName
	 *            城市名称
	 * @return 城市名称的首字母
	 */
	private String PinyinHelper(String cityName) {
		String alpha = null;
		// 提取首字母
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 大写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不要声调
		try {
			if (cityName.startsWith("长") || cityName.startsWith("重")) {
				alpha = "C";
			} else {
				alpha = PinyinHelper.toHanyuPinyinStringArray(cityName.charAt(0), defaultFormat)[0].substring(0, 1);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return alpha;
	}

	/**
	 * 判断默认城市对话框是否是第一次显示
	 */
	public void isShowCityDialog() {
		// 使用SharedPreferences来记录程序的使用次数
		preferences = mContext.getSharedPreferences(DEFAULT_CITY, context.MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		if (isFirstIn) {
			DialogUtil.DefaultCityDialog(mContext, cityObjs, new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					ListView lv  =(ListView) arg0;
					Dialog dialog = (Dialog) lv.getTag();
					CityObjBean cityObjBean = cityObjs.get(arg2);
					SpHelper.init(mContext).setStringValue(Constants.DEFAULTCITY, cityObjBean.name);
					SpHelper.init(mContext).setStringValue(Constants.DEFAULTCITY_ID, cityObjBean.id);
					mLocationCity.setText(cityObjBean.name);
					mLocationCity.setTag(cityObjBean.id);
					advertising(cityObjBean.id);
					setCityDialogShow();
					dialog.dismiss();
				}
			});
		}
	}

	/**
	 * 城市确认对话框
	 * 
	 * @param context
	 */
	public void showLocationDialog(Context context) {
		MyDialog.Builder builder = new MyDialog.Builder(context, 4);
		builder.setTitle("阳光厨房定位提示");
		builder.setContent("您当前位于" +SpHelper.init(mContext).getCityName() + ",是否切换城市");
		builder.setBackButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mLocationCity.setText(cityname);
				mLocationCity.setTag(cityid);
				advertising(cityid);
				dialog.dismiss();
			}
		});
		builder.setConfirmButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = mCity.getText().toString();
				mLocationCity.setText(name);
				mLocationCity.setTag(SpHelper.init(mContext).getCityCode());
				dialog.dismiss();
			}
		});
		builder.create();
	}

	/**
	 * 设置默认城市已经显示
	 */
	private void setCityDialogShow() {
		preferences = mContext.getSharedPreferences(DEFAULT_CITY, context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", false);
		// 提交修改
		editor.commit();
	}

	/**
	 * 定位当前城市
	 * 
	 * @param location
	 *            当前城市
	 */
	public void locationCallBreak() {
			region();

	}

	/**
	 * 地区选择
	 */
	private void region() {
		if (!isRequesting[REGION]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("regionId", "44");
			params.addQueryStringParameter("type", "1");
 			params.addQueryStringParameter("level", "2");
			sendRequest(HttpMethod.GET, UrlMy.REGION, params, REGION, true);
		}
		preferences = mContext.getSharedPreferences(DEFAULT_CITY, context.MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
         String adCode = SpHelper.init(mContext).getCityCode();
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		if(!isFirstIn){
			if(!adCode.equals(cityid)){
				showLocationDialog(mContext);
			}
		}
	}
	
	/**
	 * 检测新版本
	 */
	private void checkVersionRequst() {
		RequestParams params = new RequestParams();
		String version = DeviceHelper.getVersionName(mContext);
		params.addQueryStringParameter("versionNo", version);
		params.addQueryStringParameter("clientType", "1");
		sendRequest(HttpMethod.GET, UrlMy.VERSION_CHECK, params, VERSIONCHECK, false);
	}

	/**
	 * 广告
	 */
	private void advertising(String regionId) {
		if (!isRequesting[ADVERTISING]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("regionId", regionId);
			params.addQueryStringParameter("typeNo", "AHML");
			sendRequest(HttpMethod.GET, UrlMy.ADV, params, ADVERTISING, true);
		}
	}

	@Override
	public void handleJson(String json, int which) {
		try {
			if (which == REGION) {
				parseRegion(json);
			} else if (which == ADVERTISING) {
				parseAdvertising(json);
			}else if(which == VERSIONCHECK){
				parseVersion(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析地区选择
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseRegion(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		if (array != null) {
			cityObjs = new ArrayList<CityObjBean>();
			final int lenght = array.length();
			for (int i = 0; i < lenght; i++) {
				JSONObject object = array.getJSONObject(i);
				CityObjBean cityObj = new CityObjBean();
				cityObj.id = object.optString("id");
				cityObj.name =object.optString("name").replace("市", "");
				cityObj.alpha = PinyinHelper(cityObj.name.trim());
				cityObj.parentId = object.optString("parentId");
				cityObjs.add(cityObj);
			}
			CityAdapter cityAdapter = new CityAdapter();
			mSelectList.setAdapter(cityAdapter);
			finishedRequset = true;
			showCityDialog();
			advertising(SpHelper.init(mContext).getCityCode());
			mCity.setText(SpHelper.init(mContext).getCityName());
			mCity.setVisibility(View.VISIBLE);
			mLoadCity.setVisibility(View.GONE);
		}
		homeLayout.setVisibility(View.VISIBLE);
//		checkVersionRequst();
	}
	
	private boolean finishedRequset = false;
	
	public void showCityDialog() {
		if (finishedRequset && listener.getCurrentTab() == 0) {
			isShowCityDialog();
		}
	}
	
	/**
	 * 解析广告数据
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseAdvertising(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		adBeans = new ArrayList<AdBean>();
		if (array != null) {
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				AdBean adBean = new AdBean();
				adBean.id = object.optString("id");
				adBean.photoUrl = object.optString("photoUrl");
				adBean.jumpUrl = object.optString("jumpUrl");
				adBeans.add(adBean);
			}
			initViewPager();
		}
	}
	
	/**
	 * 解析版本更新
	 * @param json
	 * @throws JSONException 
	 */
	private void parseVersion(String json) throws JSONException {
		JSONObject object = new JSONObject(json);
		if (object != null) {
			String content = object.optString("content");
			String updateUrl = object.optString("updateUrl");
			boolean isUpdate = object.optBoolean("isUpdate");
//			String versionNo = object.optString("versionNo");
			String isForce = object.optString("isForce");
			
			if(isUpdate == true){
				DialogUtil.showVersionUpdataDialog(mContext, content, updateUrl, isForce);
			}
		}
	}
	
	@Override
	protected void handleServerBusy(String message, int which) {
		
	}

	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
	}

	@Override
	public void handleParseJsonException(int which) {

	}

}
