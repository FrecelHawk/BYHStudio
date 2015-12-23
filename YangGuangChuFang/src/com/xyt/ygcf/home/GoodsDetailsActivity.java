package com.xyt.ygcf.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.CommentDetailsAdapter;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.DatailsBean;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.more.CommentAllActivity;
import com.xyt.ygcf.more.CommentDetailsItem;
import com.xyt.ygcf.more.FoodSourceBean;
import com.xyt.ygcf.ui.CommentActivity;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.DeviceHelper;
import com.xyt.ygcf.util.DialogUtil;

/**
 * 商品详情
 * 
 * @author hexiangyang
 * 
 */
public class GoodsDetailsActivity extends BaseActivity {
	/** 头部 */
	private ImageView shareBtn;
	private ImageView collentBtn;

	private ImageView detailsImg; // 广告
	private TextView goodsName, goodsIntro, goodsIntroMore, moreComment, enterMerchantBtn;
	private TextView currentPrice; // 价格
	private TextView comment; // 评论商品
	private RatingBar goodsRating; // 商品评分
	private ListView commentList; // 用户评论
	private ScrollView scrollView;
	private LinearLayout gradeLayout; 

	private DatailsBean datailsBean;
	private List<CommentDetailsItem> commentItems = new ArrayList<CommentDetailsItem>();
	private CommentDetailsAdapter adapter;

	// 菜式名称
	private String name;
	// 菜式id
	private String productId;
	
	private String mark;
	/** 商品详情 */
	private final static int COMMODITY = 0;
	/** 商品评论 */
	private final static int PRO_EVALUATION = 1;
	/** 收藏商品 */
	private final static int COLLEC_PRODUCT = 2;
	
	private final static int PRODUCT_DATA = 3;
	// 商品介绍是否展开
	private Boolean flag = true;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PRODUCT_DATA:
				bitmapUtils.display(detailsImg, datailsBean.getImage());
				goodsName.setText(datailsBean.getProductname());
				currentPrice.setText("￥" + datailsBean.getPrice());
				goodsIntro.setText(datailsBean.getIntro());
				enterMerchantBtn.setText(datailsBean.getMerchantName());
				scrollView.setVisibility(View.VISIBLE);
				if(TextUtils.isEmpty(datailsBean.getGrade())){
					goodsRating.setRating(0);
				}else {
					goodsRating.setRating((float)Integer.valueOf(datailsBean.getGrade()));
				}
				
				MyOpenTask myOpenTask = new MyOpenTask();
				myOpenTask.start();
				ProEvaluationRequest(productId);
				break;
			case PRO_EVALUATION:
				commentItems.clear();
				commentItems.addAll((List<CommentDetailsItem>) msg.obj);
				if(commentItems.size() <= 3){
					moreComment.setVisibility(View.GONE);
				}else {
					moreComment.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_details);
		getIntentData();
		initWidget();
		initBitmapVariable();

		productDatailsRequest(productId);
	}

	private void getIntentData() {
		name = getIntent().getStringExtra("name");
		productId = getIntent().getStringExtra("id");
		mark = getIntent().getStringExtra("mark");
	}

	/**
	 * 初始化控件
	 */
	private void initWidget() {
		/** 头部 */
		shareBtn = (ImageView) findViewById(R.id.left_btn);
		collentBtn = (ImageView) findViewById(R.id.right_btn);
		shareBtn.setVisibility(View.VISIBLE);
		collentBtn.setVisibility(View.VISIBLE);
		collentBtn.setImageResource(R.drawable.collect_icon);
		shareBtn.setImageResource(R.drawable.share_icon);
		setTitle("菜式详情");

		scrollView = (ScrollView) findViewById(R.id.scrollView);
		scrollView.setVisibility(View.GONE);
		detailsImg = (ImageView) findViewById(R.id.goods_details_img);
		commentList = (ListView) findViewById(R.id.goods_details_comment_list);
		goodsIntro = (TextView) findViewById(R.id.goods_intro);
		goodsIntroMore = (TextView) findViewById(R.id.goods_intro_more);
		currentPrice = (TextView) findViewById(R.id.goods_current_price);
		moreComment = (TextView) findViewById(R.id.goods_more_comment);
		comment = (TextView) findViewById(R.id.goods_details_comment);
		goodsName = (TextView) findViewById(R.id.goods_name);
		goodsRating = (RatingBar) findViewById(R.id.goods_rating);
		enterMerchantBtn = (TextView)findViewById(R.id.enter_merchant_btn);
		gradeLayout = (LinearLayout)findViewById(R.id.grade_layout);

		goodsIntroMore.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		goodsIntroMore.setOnClickListener(this);
		moreComment.setOnClickListener(this);
		comment.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		collentBtn.setOnClickListener(this);
		enterMerchantBtn.setOnClickListener(this);
		
		adapter = new CommentDetailsAdapter(this, commentItems, true);
		commentList.setAdapter(adapter);
		
		if(mark.equals(Constants.INDUSTRY_SCHOOL_MAKER) || mark.equals(Constants.INDUSTRY_COMPANY_MAKER)){
			gradeLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.goods_intro_more:
			if (flag) {
				flag = false;
				goodsIntro.setEllipsize(null); // 展开
				goodsIntro.setSingleLine(flag);
				goodsIntroMore.setText("收起");
			} else {
				flag = true;
				goodsIntro.setLines(2);
				goodsIntro.setEllipsize(TruncateAt.END); // 收缩
				goodsIntroMore.setText("查看更多");
			}
			break;
		case R.id.goods_more_comment: // 用户评论列表
			if (commentItems == null || commentItems.size() == 0) {
				showToast("该商家暂无评论数据");
 				return;
			}
			Intent intent = new Intent(this, CommentAllActivity.class);
			intent.putExtra("id", productId);
			intent.putExtra("maker", Constants.INDUSTRY_PRODUCT_MAKER);
			startActivity(intent);
			break;
		case R.id.goods_details_comment: // 用户评论
			if(LoginHelper.isLogin()){
				intent = new Intent(this, CommentActivity.class);
				intent.putExtra("eatery", 2);
				intent.putExtra("id", productId);
				startActivityForResult(intent, 1);
			}else {
				DialogUtil.toLoginDialog(this);
			}
			break;
		case R.id.left_btn: //分享菜品
			DeviceHelper.showShareMore(this, "阳光厨房");
			break;
		case R.id.right_btn: //收藏菜品
			if(LoginHelper.isLogin()){
				collecProductRequest(productId);
			}else {
				DialogUtil.toLoginDialog(this);
			}
			break;
		case R.id.enter_merchant_btn:
			intent = new Intent(GoodsDetailsActivity.this, MerchantDatails.class);
			intent.putExtra("mark", mark);
			intent.putExtra("id", datailsBean.merchantId);
			startActivity(intent);
			break;
		default:
			break;
		}
		super.onClick(arg0);
	}
	
	private class MyOpenTask extends AsyncTask<Integer, Integer, Integer> {  
	       private int[] location = new int[2];  
	   
	       @Override 
	       protected void onCancelled() {  
	           super.onCancelled();  
	       }  
	   
	       public void start() {  
	           execute(0);  
	       }  
	   
	       @Override 
	       protected Integer doInBackground(Integer... params) {  
	           return 1;  
	       }  
	   
	       @Override 
	       protected void onPostExecute(Integer result) {  
	           super.onPostExecute(result);  
	           int linecount = goodsIntro.getLineCount();
				if(linecount >= 2){  
		        	   goodsIntroMore.setVisibility(View.VISIBLE);
		        }else{  
		        	   goodsIntroMore.setVisibility(View.GONE);
		        }  
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
	 * 商品详情
	 * 
	 * @param id
	 */
	private void productDatailsRequest(String productId) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", productId);
		params.addQueryStringParameter("proPitureSize", "B");
		sendRequest(HttpMethod.GET, UrlMy.COMMODITY_DETAILS, params, COMMODITY,true);
	}

	/**
	 * 查询商品评论
	 * 
	 * @param id
	 */
	private void ProEvaluationRequest(String productId) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("productId", productId);
		params.addQueryStringParameter("needPage", "true");
		params.addQueryStringParameter("vipPitureSize", itemBitmapSize);
		sendRequest(HttpMethod.GET, UrlMy.INQULRE_PRO_EVALUATION, params,PRO_EVALUATION, false);
	}
	
	/**
	 * 收藏商品
	 * @param productId
	 */
	private void collecProductRequest(String productId){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("productId", productId);
		params.addQueryStringParameter("isPaging", "false");
		sendRequest(HttpMethod.GET, UrlMy.COLLECTION_PRODUCT, params,COLLEC_PRODUCT, true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			productDatailsRequest(productId);
		}
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		try {
			if (which == COMMODITY) {
				parseCommodity(json);
			} else if (which == PRO_EVALUATION) {
				parseEvaluation(json);
			}else if(which == COLLEC_PRODUCT){
				parseCollecProduct(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析收藏商品
	 * @param json
	 */
	private void parseCollecProduct(String json) {
		showToast("收藏成功");
	}

	/**
	 * 解析商品评论
	 * @param json
	 * @throws JSONException 
	 */
	private void parseEvaluation(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject != null) {
			JSONArray array = jsonObject.getJSONArray("beanList");
			List<CommentDetailsItem> detailsItems = new ArrayList<CommentDetailsItem>();
			int lenth = array.length();
			for (int i = 0; i < lenth; i++) {
				JSONObject object = array.getJSONObject(i);
				CommentDetailsItem detailsItem = new CommentDetailsItem();
				detailsItem.imageView = object.optString("memberUrl");
				detailsItem.commentName = object.getString("nickname");
				detailsItem.commentGrade = object.getString("recommendLevel");
				detailsItem.commentTime = object.optString("createDt");
				detailsItem.commentContent = object.optString("content");
				detailsItems.add(detailsItem);
			}
			sendMessage(PRO_EVALUATION, detailsItems);
		}
	}

	/**
	 * 解析商品详情
	 * @param json
	 * @throws JSONException 
	 */
	private void parseCommodity(String json) throws JSONException {
		JSONObject object = new JSONObject(json);
		if (object != null) {
			datailsBean = new DatailsBean();
			datailsBean.id = object.optString("id");
			datailsBean.merchantId = object.optString("merchantId");
			datailsBean.image = object.optString("photoUrl");
			datailsBean.productname = object.optString("name");
			datailsBean.merchantName = object.optString("shopName");
			datailsBean.Price = object.optString("actualPrice");
			datailsBean.Intro = object.optString("remark");
			datailsBean.grade = object.optString("recommendLevel");

			handler.sendEmptyMessage(PRODUCT_DATA);
		}
	}

	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
	}

}
