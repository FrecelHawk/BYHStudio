package com.xyt.ygcf.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huamaitel.api.HMDefines;
import com.huamaitel.api.HMDefines.UserInfo;
import com.huamaitel.api.HMJniInterface;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.CommentDetailsAdapter;
import com.xyt.ygcf.adpter.CommentFeedBackAdapter;
import com.xyt.ygcf.adpter.FoodSourceAdapter;
import com.xyt.ygcf.base.AppContext;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.DatailsBean;
import com.xyt.ygcf.entity.VideoItem;
import com.xyt.ygcf.huamai.DeviceActivity;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.more.CommentAllActivity;
import com.xyt.ygcf.more.CommentDetailsItem;
import com.xyt.ygcf.more.FoodSourceBean;
import com.xyt.ygcf.more.MoreFood;
import com.xyt.ygcf.more.RefectoryFeedBackActivity;
import com.xyt.ygcf.ui.CommentActivity;
import com.xyt.ygcf.ui.PingyouActivity;
import com.xyt.ygcf.ui.RouteSearchActivity;
import com.xyt.ygcf.ui.my.ComplainActivity;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.DeviceHelper;
import com.xyt.ygcf.util.DialogUtil;
import com.xyt.ygcf.widget.MyListView;

/***
 * 食堂详情
 * 
 * @author zfx
 * 
 */
public class MerchantDatails extends BaseActivity {

//	private ImageView shopImage;
	private TextView shopName, shopTheme,  phoneNumber;
	private TextView moreComment, moreFoodfrom, navigComment;
	private MyListView foodSourceList, commentList;
	private TextView Tocomment;
	private ImageView navigCommentImg, shopMedal;
//	private RatingBar shopRating;
	private LinearLayout  shortcut;
	private ScrollView scrollView;

	private FoodSourceAdapter adapter;
	private CommentDetailsAdapter detailsAdapter;
	private CommentFeedBackAdapter feedBackAdapter;

	private ArrayList<FoodSourceBean> sourceBeans = new ArrayList<FoodSourceBean>();
	private List<CommentDetailsItem> detailsItems = new ArrayList<CommentDetailsItem>();
	private DatailsBean datailsBean;

	private Intent intent;
	/** 商家详情 */
	private final static int MERCHANT = 0;
	/** 商家评论 */
	private final static int MERCHANT_COMMENT = 1;
	/** 食材来源 */
	private final static int FOOD_SOURCE = 2;
	/** 收藏商家 */
	public final static int COLLEC_MERCHANT = 3;
	/** 食堂反馈列表 */
	public final static int MERCHANT_FEEDBACK = 4;
	/** 视频 */
	public final static int VIDEO = 5;

	// 餐厅标识 RST:餐厅; SCH：学校食堂 ; ENT：企业食堂
	private String mark;
	// 商家id
	private String id;
	private Double longitude;
	private Double latitude;

	// 华迈视频
	private static final int EVENT_LOGIN_SUCCESS = 11;
	private static final int EVENT_LOGIN_FAIL = 12;
	private static final String SERVER_ADDR = "www.huamaiyun.com";
	private static final short SERVER_PORT = 80;
//	private String ADDR;
//	private short PORT;
//	private String USER;
//	private String PASSWARD;
	private ProgressDialog loginProcessDialog;
	// private Handler handler;
	private Thread mServerThread = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MERCHANT:
				ImageView shopImage = (ImageView) findViewById(R.id.shopImage);
				TextView shopAddress = (TextView) findViewById(R.id.address);
				RatingBar shopRating = (RatingBar) findViewById(R.id.shop_datails_rating);
				shopAddress.setOnClickListener(MerchantDatails.this);
				if (TextUtils.isEmpty(datailsBean.getGrade())) {
					shopRating.setRating(0);
				} else {
					shopRating.setRating(Float.valueOf(datailsBean.getGrade()));
				}
				shopName.setText(datailsBean.getMerchantName());
				bitmapUtils.display(shopImage, datailsBean.getImage());
				shopTheme.setText(datailsBean.getTheme());
				shopAddress.setText(datailsBean.getAddress());
				phoneNumber.setText(datailsBean.getNumber());

				foodSourceRequest(id);
				if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
					merchantCommentRequest(id);
				} else {
					merchantFeedBackRequst(id, mark);
				}
				scrollView.setVisibility(View.VISIBLE);
				shortcut.setVisibility(View.VISIBLE);
				break;
			case FOOD_SOURCE:
				sourceBeans.addAll((List<FoodSourceBean>) msg.obj);
				adapter.notifyDataSetChanged();
				break;
			case MERCHANT_COMMENT:
				detailsItems.clear();
				detailsItems.addAll((List<CommentDetailsItem>) msg.obj);
				if (detailsItems.size() <= 3) {
					moreComment.setVisibility(View.GONE);
				} else {
					moreComment.setVisibility(View.VISIBLE);
				}
				detailsAdapter.notifyDataSetChanged();
				break;
			case MERCHANT_FEEDBACK:
				detailsItems.clear();
				detailsItems.addAll((List<CommentDetailsItem>) msg.obj);
				if (detailsItems.size() <= 3) {
					moreComment.setVisibility(View.GONE);
				} else {
					moreComment.setVisibility(View.VISIBLE);
				}
				feedBackAdapter.notifyDataSetChanged();
				break;
			// 华迈视频
			case EVENT_LOGIN_SUCCESS:
				closeWaitDoalog();
				Intent intent = new Intent();
				intent.setClass(MerchantDatails.this, DeviceActivity.class);
				intent.putExtra("id", id);
				// Bundle bundle = new Bundle();
				// bundle.putParcelableArrayList(key, value)

				startActivity(intent);
				break;
			case EVENT_LOGIN_FAIL:
				closeWaitDoalog();
				AppContext.getJni().disconnectServer(AppContext.serverId);
				Toast.makeText(MerchantDatails.this, "连接失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case VIDEO:
				break;

			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_datails);
		// 华迈
		// AppContext.getJni().init();
		initBitmapVariable();
		getIntentData();
		getWidget();
		isRefectory();
		merchantRequest(id);
		videoRequest(id);
	}

	public void getIntentData() {
		mark = getIntent().getExtras().getString("mark");
		id = getIntent().getStringExtra("id");
	}

	/**
	 * 判断是餐厅还是食堂
	 */
	private void isRefectory() {
		TextView userComment = (TextView) findViewById(R.id.userComment);
		LinearLayout layoutRating = (LinearLayout) findViewById(R.id.shop_datail_layout_rating);
		if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
			setTitle("餐厅详情");
		} else if (mark.equals(Constants.INDUSTRY_SCHOOL_MAKER)) {
			setTitle("学校食堂详情");
			userComment.setText("家长反馈");
			layoutRating.setVisibility(View.GONE);
			shopTheme.setVisibility(View.GONE);
		} else if (mark.equals(Constants.INDUSTRY_COMPANY_MAKER)) {
			setTitle("企业食堂详情");
			userComment.setText("员工反馈");
			layoutRating.setVisibility(View.GONE);
			shopTheme.setVisibility(View.GONE);
		}
	}

	public void getWidget() {
		/** 头部 */
		ImageView shareBtn = (ImageView) findViewById(R.id.left_btn);
		ImageView collentBtn = (ImageView) findViewById(R.id.right_btn);
		shareBtn.setVisibility(View.VISIBLE);
		collentBtn.setVisibility(View.VISIBLE);
		collentBtn.setImageResource(R.drawable.collect_icon);
		shareBtn.setImageResource(R.drawable.share_icon);

		scrollView = (ScrollView) findViewById(R.id.menu_datails_scroll);
		scrollView.setVisibility(View.GONE);
		shortcut = (LinearLayout) findViewById(R.id.details_shortcut);
		shortcut.setVisibility(View.GONE);
//		shopImage = (ImageView) findViewById(R.id.shopImage);
		shopName = (TextView) findViewById(R.id.shop_datails_name);
		shopTheme = (TextView) findViewById(R.id.shop_datails_theme);
//		shopRating = (RatingBar) findViewById(R.id.shop_datails_rating);
		shopMedal = (ImageView) findViewById(R.id.shop_datails_medal);
		

		
//		shopAddress = (TextView) findViewById(R.id.address);
		phoneNumber = (TextView) findViewById(R.id.telephone);
		moreFoodfrom = (TextView) findViewById(R.id.moreFoodFrom);
		Tocomment = (TextView) findViewById(R.id.tocomment);
		moreComment = (TextView) findViewById(R.id.moreComment);
		navigComment = (TextView) findViewById(R.id.navigComment);
		navigCommentImg = (ImageView) findViewById(R.id.avigCommentImg);

		foodSourceList = (MyListView) findViewById(R.id.foodSourceList);
		commentList = (MyListView) findViewById(R.id.commentList);
		// 导航栏
		((LinearLayout) findViewById(R.id.menuBtn)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.videoBtn)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.complainBtn))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.commentBtn)).setOnClickListener(this);

		collentBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);

//		shopAddress.setOnClickListener(this);
		phoneNumber.setOnClickListener(this);
		moreComment.setOnClickListener(this);
		Tocomment.setOnClickListener(this);
		moreFoodfrom.setOnClickListener(this);
		changeView();

		adapter = new FoodSourceAdapter(this, sourceBeans, true);
		foodSourceList.setAdapter(adapter);

		feedBackAdapter = new CommentFeedBackAdapter(this, detailsItems, true);
		commentList.setAdapter(feedBackAdapter);

		detailsAdapter = new CommentDetailsAdapter(this, detailsItems, true);
		commentList.setAdapter(detailsAdapter);
	}

	/**
	 * 改变View
	 */
	public void changeView() {
		if (!mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
			moreComment.setText("查看全部反馈");
			navigComment.setText("反馈");
			Tocomment.setVisibility(View.GONE);
			shopMedal.setVisibility(View.GONE);
			navigCommentImg.setImageResource(R.drawable.btu_feedback_selector);
		}
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.left_btn: // 分享菜品
			DeviceHelper.showShareMore(this, "阳光厨房");
			break;
		case R.id.right_btn: // 收藏菜品
			if (LoginHelper.isLogin()) {
				collecMerchantRequst(id);
			} else {
				DialogUtil.toLoginDialog(this);
			}
			break;
		case R.id.menuBtn: // 菜单
			intent = new Intent(this, MerchantMenuActivity.class);
			intent.putExtra("mark", mark);
			intent.putExtra("id", id);
			intent.putExtra("name", datailsBean.getMerchantName());
			startActivity(intent);
			break;
		case R.id.videoBtn: // 厨房视频
			// startActivity(new Intent(this, VideoActivity.class));
			// if (LoginHelper.isLogin()) {
			handler.sendEmptyMessage(EVENT_LOGIN_SUCCESS);
			// } else {
			// DialogUtil.toLoginDialog(this);
			// }

			// LoginDevice();
			break;
		case R.id.complainBtn: // 投诉
			if (LoginHelper.isLogin()) {
				intent = new Intent(this, ComplainActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", datailsBean.getMerchantName());
				startActivity(intent);
			} else {
				DialogUtil.toLoginDialog(this);
			}
			break;
		case R.id.commentBtn: // 反馈
			if (!LoginHelper.isLogin()) {
				DialogUtil.toLoginDialog(this);
				return;
			}
			if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {//
				intent = new Intent(this, PingyouActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			} else {
				intent = new Intent(this, RefectoryFeedBackActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", datailsBean.getMerchantName());
				startActivityForResult(intent, 2);
			}
			break;
		case R.id.tocomment: // 我要评论
			if (!LoginHelper.isLogin()) {
				DialogUtil.toLoginDialog(this);
				return;
			}
			if (mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)) {
				intent = new Intent(this, CommentActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("eatery", 1);
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.moreComment: // 查看全部评论
			intent = new Intent(this, CommentAllActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("maker", mark);
			startActivity(intent);
			break;
		case R.id.moreFoodFrom: // 更多食材来源
			if (sourceBeans == null || sourceBeans.size() == 0) {
				showToast("该商家暂无食材来源数据");
				return;
			}
			intent = new Intent(this, MoreFood.class);
			intent.putExtra("food", (Serializable) sourceBeans);
			startActivity(intent);
			break;
		case R.id.address:
			if (longitude == 0.0 || latitude == 0.0) {
				showToast("暂无此路线提供");
				return;
			}
			intent = new Intent(getApplicationContext(),
					RouteSearchActivity.class);
			Bundle bundle = new Bundle();
			bundle.putDouble("longitude", longitude);
			bundle.putDouble("latitude", latitude);
			bundle.putString("name", datailsBean.getMerchantName());
			intent.putExtra("bundle", bundle);
			startActivity(intent);
			break;
		case R.id.telephone:
			Uri telUri = Uri.parse("tel:" + datailsBean.getNumber());
			startActivity(new Intent(Intent.ACTION_DIAL, telUri));
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				merchantCommentRequest(id);
			}
		} else if (requestCode == 2) {
			if (resultCode == RESULT_CANCELED) {
				merchantFeedBackRequst(id, mark);
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 商家详情
	 * 
	 * @param merchantId
	 */
	public void merchantRequest(String merchantId) {
		if (!isRequesting[MERCHANT]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("id", merchantId);
			params.addQueryStringParameter("merPitureSize", detailBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.MERCHANT_DETAILS, params,
					MERCHANT, true);
		}
	}

	/**
	 * 视频请求
	 * 
	 * @param merchantId
	 */
	public void videoRequest(String merchantId) {
		if (!isRequesting[VIDEO]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			sendRequest(HttpMethod.GET, UrlMy.VIDEO_DETAILS, params, VIDEO,
					false);
		}
	}

	/**
	 * 商家评论
	 * 
	 * @param Id
	 */
	public void merchantCommentRequest(String merchantId) {
		if (!isRequesting[MERCHANT_COMMENT]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			params.addQueryStringParameter("needPage", "true");
			params.addQueryStringParameter("vipPitureSize", itemBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.INQUIRE_MER_EVALUATION, params,
					MERCHANT_COMMENT, false);
		}
	}

	/**
	 * 商家食材来源
	 * 
	 * @param merchantId
	 */
	public void foodSourceRequest(String merchantId) {
		if (!isRequesting[FOOD_SOURCE]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merId", merchantId);
			params.addQueryStringParameter("needPage", "false");
			sendRequest(HttpMethod.GET, UrlMy.FOOD_SOURCE, params, FOOD_SOURCE,
					false);
		}
	}

	/**
	 * 收藏商家
	 * 
	 * @param merchantId
	 * @param sessionId
	 */
	public void collecMerchantRequst(String merchantId) {
		if (!isRequesting[COLLEC_MERCHANT]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			sendRequest(HttpMethod.GET, UrlMy.COLLECTION_SHOP_ID, params,
					COLLEC_MERCHANT, true);
		}
	}

	/**
	 * 食堂反馈列表
	 * 
	 * @param merchantId
	 */
	public void merchantFeedBackRequst(String merchantId, String mark) {
		if (!isRequesting[MERCHANT_FEEDBACK]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", merchantId);
			params.addQueryStringParameter("needPage", "true");
			params.addQueryStringParameter("industry", mark);
			params.addQueryStringParameter("vipPitureSize", itemBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.MER_FEED_BACK, params,
					MERCHANT_FEEDBACK, false);
		}
	}

	@Override
	protected void finishedRequest(int which) {
		if (which == MERCHANT) {
			super.finishedRequest(which);
		} else if (which == COLLEC_MERCHANT) {
			super.finishedRequest(which);
		}
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		try {
			if (which == MERCHANT) {
				parseMerchant(json);
			} else if (which == MERCHANT_COMMENT) {
				parseMerchantComment(json);
			} else if (which == FOOD_SOURCE) {
				parseFoodSource(json);
			} else if (which == COLLEC_MERCHANT) {
				parseCollecMerchant(json);
			} else if (which == MERCHANT_FEEDBACK) {
				parsemerchantFeedBack(json);
			} else if (which == VIDEO) {
				parseVideo(json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	/**
	 * 解析视频列表
	 */
	private void parseVideo(String json) throws JSONException {
		System.out.println("json122>>" + json);
		JSONArray jsonArray = new JSONArray(json);
		if (jsonArray != null) {
			List<VideoItem> videoList = new ArrayList<VideoItem>();
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				VideoItem videoItem = new VideoItem();
				videoItem.id = jsonObject.optString("id");
				videoItem.name = jsonObject.optString("name");
				// videoItem.ip = jsonObject.optString("ip");
				// videoItem.port = jsonObject.optString("port");
				// videoItem.user = jsonObject.optString("user");
				// videoItem.passward = jsonObject.optString("passward");
				videoItem.sn = jsonObject.optString("sn");
				videoItem.devName = jsonObject.optString("devName");
				videoItem.status = jsonObject.optString("status");
				videoList.add(videoItem);
			}
			// if(videoList.size()>0){
			// ADDR = videoList.get(0).getIp();
			// PORT = (short) Integer.parseInt(videoList.get(0).getPort());
			// USER = videoList.get(0).getUser();
			// PASSWARD = videoList.get(0).getPassward();
			// }

			if (Constants.videoListAll.size() > 0) {
				Constants.videoListAll.clear();
			}
			Constants.videoListAll = videoList;

		}
	}

	/**
	 * 解析食堂反馈列表
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parsemerchantFeedBack(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject != null) {
			List<CommentDetailsItem> commentDetailsItems = new ArrayList<CommentDetailsItem>();
			
			JSONArray array = jsonObject.getJSONArray("beanList");
			int length =  array.length();
			for (int i = 0; i <length; i++) {
				JSONObject object = array.getJSONObject(i);
				CommentDetailsItem detailsItem = new CommentDetailsItem();
				String id = object.optString("id");
				detailsItem.commentContent = object.optString("content");
				detailsItem.commentName = object.optString("memberName");
				detailsItem.imageView = object.optString("memberPhotoUrl");
				detailsItem.commentPraise = Integer.valueOf(object
						.optString("isGood"));
				detailsItem.commentTime = object.optString("createDt");
				commentDetailsItems.add(detailsItem);
			}
			sendMessage(MERCHANT_FEEDBACK, commentDetailsItems);
		}
		if (detailsItems.size() <= 3) {
			moreComment.setVisibility(View.GONE);
		} else {
			moreComment.setVisibility(View.VISIBLE);
		}
		feedBackAdapter = new CommentFeedBackAdapter(this, detailsItems, true);
		commentList.setAdapter(feedBackAdapter);
		feedBackAdapter.notifyDataSetChanged();
	}

	/**
	 * 解析商家收藏
	 * 
	 * @param json
	 */
	private void parseCollecMerchant(String json) {
		showToast("收藏成功");
	}

	/**
	 * 食材来源
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseFoodSource(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		if (array != null) {
			List<FoodSourceBean> foodSourceBeans = new ArrayList<FoodSourceBean>();
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				FoodSourceBean sourceBean = new FoodSourceBean();
				sourceBean.foodName = object.optString("name");
				sourceBean.quality = object.optString("quantity");
				sourceBean.foodFrom = object.optString("supplierName");
				sourceBean.foodTime = object.optString("inDt");
				sourceBean.unitName = object.optString("unitName");
				foodSourceBeans.add(sourceBean);
			}

			sendMessage(FOOD_SOURCE, foodSourceBeans);
		}
	}

	/**
	 * 解析商家评论
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseMerchantComment(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject != null) {
			List<CommentDetailsItem> commentDetailsItems = new ArrayList<CommentDetailsItem>();
			JSONArray array = jsonObject.getJSONArray("beanList");
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				CommentDetailsItem detailsItem = new CommentDetailsItem();
				String id = object.optString("id");
				detailsItem.imageView = object.optString("memberUrl");
				detailsItem.commentContent = object.optString("content");
				detailsItem.commentName = object.optString("nickName");
				detailsItem.commentGrade = object.optString("recommendLevel");
				detailsItem.commentTime = object.optString("createDt");

				commentDetailsItems.add(detailsItem);
			}
			sendMessage(MERCHANT_COMMENT, commentDetailsItems);
		}
	}

	/**
	 * 解析商家
	 * 
	 * @throws JSONException
	 */
	public void parseMerchant(String json) throws JSONException {
		JSONObject object = new JSONObject(json);
		if (object != null) {
			datailsBean = new DatailsBean();
			datailsBean.id = object.optString("id");
			datailsBean.image = object.optString("logo");
			datailsBean.merchantName = object.optString("shopName");
			datailsBean.Number = object.optString("serviceTel");
			datailsBean.Address = object.optString("address");
			datailsBean.grade = object.optString("grade");
			datailsBean.theme = object.optString("foodType");

			longitude = object.optDouble("longitude");
			latitude = object.optDouble("latitude");

			handler.sendEmptyMessage(MERCHANT);
		}
	}

	// 华迈视频
	public void LoginDevice() {
		showWaitDialog("logining...");
		mServerThread = new Thread() {
			@Override
			public void run() {
				HMJniInterface jni = AppContext.getJni();
				int result = 0;

				// 平台相关
				HMDefines.LoginServerInfo info = new HMDefines.LoginServerInfo();
				info.ip = SERVER_ADDR; // 平台地址
				info.port = SERVER_PORT; // 平台端口
				info.user = "xyttest"; // 用户名
				info.password = "123"; // 密码
				// info.ip = ADDR; // 平台地址
				// info.port = PORT; // 平台端口
				// info.user = USER; // 用户名
				// info.password = PASSWARD; // 密码
				info.model = android.os.Build.MODEL; // 手机型号
				info.version = android.os.Build.VERSION.RELEASE; // 手机系统版本号

				// 添加登录信息返回的错误码。
				// step 1: Connect the server.
				int serverId = jni.connectServer(info
						);
				if (serverId != 0) {
					AppContext.serverId = serverId;
					result = jni.getDeviceList(serverId);
					if (result != HMDefines.HMEC_OK) {
						jni.disconnectServer(serverId);
						sendEmptyMessage(EVENT_LOGIN_FAIL);
						return;
					}

					// step 2: Get user information.
					UserInfo userInfo = jni.getUserInfo(serverId);
					if (userInfo == null) {
						jni.disconnectServer(serverId);
						sendEmptyMessage(EVENT_LOGIN_FAIL);
						return;
					}

					// step 3: Get transfer service.
					if (userInfo.useTransferService != 0
							&& userInfo.useTransferService != 8) {
						result = jni.getTransferInfo(serverId);
						if (result != HMDefines.HMEC_OK) {
							jni.disconnectServer(serverId);
							sendEmptyMessage(EVENT_LOGIN_FAIL);
							return;
						}
					}

					// step 4: Get tree id.
					AppContext.treeId = jni.getTree(serverId);
					handler.sendEmptyMessage(EVENT_LOGIN_SUCCESS);
				} else {
					handler.sendEmptyMessage(EVENT_LOGIN_FAIL);
				}
			}
		};
		mServerThread.start();
	}

	private void showWaitDialog(String str) {
		loginProcessDialog = ProgressDialog.show(this, null, str);
		loginProcessDialog.setCancelable(true);
	}

	private void sendEmptyMessage(int what) {
		if (handler != null) {
			handler.sendEmptyMessage(what);
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

	@Override
	protected void handleServerBusy(String message, int which) {

	}

	private void closeWaitDoalog() {
		if (loginProcessDialog != null) {
			if (loginProcessDialog.isShowing()) {
				loginProcessDialog.dismiss();
				loginProcessDialog = null;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		if(mServerThread != null){
			mServerThread.stop();
		}
		super.onDestroy();
	}
}
