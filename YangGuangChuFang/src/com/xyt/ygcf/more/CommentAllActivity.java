package com.xyt.ygcf.more;

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
import android.widget.ListView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.CommentDetailsAdapter;
import com.xyt.ygcf.adpter.CommentFeedBackAdapter;
import com.xyt.ygcf.adpter.CommentFeedBackListAdapter;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.ui.my.EvaluateDetailActivity;
import com.xyt.ygcf.ui.my.EvaluateFeedBackActivity;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.view.CustomListView;
import com.xyt.ygcf.view.CustomListView.OnLoadMoreListener;

/**
 * 
 * 全部评论
 * 
 * @author wjj
 * 
 */
public class CommentAllActivity extends BaseActivity implements OnClickListener {
	
	/** 商家评论 */
	private final static int MERCHANT_COMMENT = 1;
	/** 商品评论 */
	private final static int PRODUCT_COMMENT = 2;
	/** 反馈列表 */
	private final static int MERCHANT_FEEDBACK = 3;
	
//	private CommentFeedBackListAdapter mAdapter;
	private CommentDetailsAdapter detailsAdapter;
	private CommentFeedBackAdapter backAdapter;
	
	private CustomListView mListView;
	private List<CommentDetailsItem> commentAllList = new ArrayList<CommentDetailsItem>();
	private int currenPage = 1; //当前页
	//餐厅标识  RST:餐厅; SCH：学校食堂  ; ENT：企业食堂 
	private String maker;
	private String id;
	private Intent intent;

	
	
	private Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what == MERCHANT_COMMENT){
				if(currenPage == 1){
					commentAllList.clear();
				}
				BaseListBeen<CommentDetailsItem> resultBean = (BaseListBeen<CommentDetailsItem>) msg.obj;
				if(resultBean.list.size() !=0){
					commentAllList.addAll(resultBean.list);
					detailsAdapter.notifyDataSetChanged();
					mListView.onLoadMoreComplete();
				}
				if(resultBean.currentPage == resultBean.totalPages){
					mListView.setCanLoadMore(false);
				}
			}else if(msg.what == MERCHANT_FEEDBACK){
				if(currenPage == 1){
					commentAllList.clear();
				}
				BaseListBeen<CommentDetailsItem> resultBean = (BaseListBeen<CommentDetailsItem>) msg.obj;
				if(resultBean.list.size() !=0){
					commentAllList.addAll(resultBean.list);
					backAdapter.notifyDataSetChanged();
					mListView.onLoadMoreComplete();
				}
				if(resultBean.currentPage == resultBean.totalPages){
					mListView.setCanLoadMore(false);
				}
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_all);
		
		getIntentValue();
		getListView();
	}

	private void getIntentValue(){
		intent = getIntent();
		maker = intent.getStringExtra("maker");
		id = intent.getStringExtra("id");
		if(maker.equals(Constants.INDUSTRY_PRODUCT_MAKER)){
			setTitle("全部评论");
			productCommentRequest(id);
		}else if(maker.equals(Constants.INDUSTRY_RESTAURANT_MAKER)){
			setTitle("全部评论");
			merchantCommentRequest(id);
		}else {
			setTitle("全部反馈");
			merchantFeedBackRequst(id, maker);
		}
		
	}
	
	private void getListView() {
		
		mListView = (CustomListView) findViewById(R.id.comment_all_details);
		if(maker.equals(Constants.INDUSTRY_PRODUCT_MAKER) || maker.equals(Constants.INDUSTRY_RESTAURANT_MAKER)){
			detailsAdapter = new CommentDetailsAdapter(this, commentAllList, false);
			mListView.setAdapter(detailsAdapter);
//			mAdapter = new CommentFeedBackListAdapter(this,commentAllList, 1);
//			mListView.setAdapter(mAdapter);
		}else {
			backAdapter = new CommentFeedBackAdapter(this, commentAllList, false);
			mListView.setAdapter(backAdapter);
//			mAdapter = new CommentFeedBackListAdapter(this,commentAllList, 2);
//			mListView.setAdapter(mAdapter);
		}
		mListView.setCanRefresh(false);
		mListView.setCanLoadMore(true);
		
//		mListView.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				// TODO 下拉刷新
//				
//			}
//		});

		mListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				currenPage ++;
				merchantCommentRequest(id);
				
			}
		});
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView lvItem = (ListView) parent;
				CommentDetailsItem item = (CommentDetailsItem) lvItem.getItemAtPosition(position);
				if(maker.equals(Constants.INDUSTRY_PRODUCT_MAKER) || maker.equals(Constants.INDUSTRY_RESTAURANT_MAKER)){ //评论详情
					intent.putExtra("rating", item.commentGrade);
					intent.putExtra("content", item.commentContent);
					intent.putExtra("time", item.commentTime);
					intent.setClass(CommentAllActivity.this, EvaluateDetailActivity.class);
				}else { //反馈详情
					intent.putExtra("id", item.id);
					intent.setClass(CommentAllActivity.this, EvaluateFeedBackActivity.class);
				}
				startActivity(intent);
			}
		});

	}


	/**
	 * 商家评论
	 * @param merchantId
	 */
	public void merchantCommentRequest(String merchantId){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("merchantId", merchantId);
		params.addQueryStringParameter("needPage", "true");
		params.addQueryStringParameter("currentPage", String.valueOf(currenPage));
		params.addQueryStringParameter("vipPitureSize", itemBitmapSize);
		sendRequest(HttpMethod.GET, UrlMy.INQUIRE_MER_EVALUATION, params, MERCHANT_COMMENT, false);
	}
	
	/**
	 * 商品评论
	 * @param merchantId
	 */
	public void productCommentRequest(String productId){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("productId", productId);
		params.addQueryStringParameter("needPage", "true");
		params.addQueryStringParameter("currentPage", String.valueOf(currenPage));
		params.addQueryStringParameter("vipPitureSize", itemBitmapSize);
		sendRequest(HttpMethod.GET, UrlMy.INQULRE_PRO_EVALUATION, params, PRODUCT_COMMENT, false);
	}
	
	/**
	 * 食堂反馈列表
	 * @param merchantId
	 */
	public void merchantFeedBackRequst(String merchantId, String mark){
		RequestParams params = new RequestParams();	
		params.addQueryStringParameter("merchantId", merchantId);
		params.addQueryStringParameter("needPage", "true");
		params.addQueryStringParameter("industry", mark);
		params.addQueryStringParameter("vipPitureSize", itemBitmapSize);
		sendRequest(HttpMethod.GET, UrlMy.MER_FEED_BACK, params, MERCHANT_FEEDBACK, false);
	}
	
	@Override
	public void handleJson(String json, int which) {
		try {
			if(which == PRODUCT_COMMENT || which == MERCHANT_COMMENT){
				parserCommentJson(json);
			}else if(which == MERCHANT_FEEDBACK){
				parsemerchantFeedBack(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
	}
	
	/**
	 * 
	 * @param json
	 * @throws JSONException 
	 */
	public void parserCommentJson(String json) throws JSONException{
		BaseListBeen<CommentDetailsItem> baseListBeens = new BaseListBeen<CommentDetailsItem>();
		JSONObject jsonObject = new JSONObject(json);
		baseListBeens.currentPage = jsonObject.optInt("currentPage");
		baseListBeens.totalPages = jsonObject.optInt("totalPages");
		JSONArray jsonArray = jsonObject.getJSONArray("beanList");
		int lenght = jsonArray.length();
		List<CommentDetailsItem> listData = new ArrayList<CommentDetailsItem>();
		for(int i=0;i<lenght;i++){
			CommentDetailsItem itemBean = new CommentDetailsItem();
			JSONObject itemJson = jsonArray.getJSONObject(i);
			itemBean.imageView = itemJson.getString("memberUrl");
			itemBean.commentName = itemJson.getString("nickName");
			itemBean.commentTime =itemJson.getString("createDt");
			itemBean.commentGrade = itemJson.getString("recommendLevel");
			itemBean.commentContent =itemJson.getString("content");
			listData.add(itemBean);
		}
		baseListBeens.list =listData;
		
		Message msg = Message.obtain();
		msg.what = MERCHANT_COMMENT;
		msg.obj = baseListBeens;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 解析食堂反馈列表
	 * @param json
	 * @throws JSONException 
	 */
	private void parsemerchantFeedBack(String json) throws JSONException {
		BaseListBeen<CommentDetailsItem> baseListBeens = new BaseListBeen<CommentDetailsItem>();
		JSONObject jsonObject = new JSONObject(json);
		if(jsonObject != null){
			baseListBeens.currentPage = jsonObject.optInt("currentPage");
			baseListBeens.totalPages = jsonObject.optInt("totalPages");
			JSONArray array = jsonObject.getJSONArray("beanList");
			List<CommentDetailsItem> listData = new ArrayList<CommentDetailsItem>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				CommentDetailsItem detailsItem = new CommentDetailsItem();
				detailsItem.id = object.optString("id");
				detailsItem.commentContent = object.optString("content");
				detailsItem.commentName = object.optString("memberName");
				detailsItem.imageView = object.optString("memberPhotoUrl");
				detailsItem.commentPraise = Integer.valueOf(object.optString("isGood"));
				detailsItem.commentTime = object.optString("createDt");
				listData.add(detailsItem);
			}
			baseListBeens.list = listData;
		}
		
		Message msg = Message.obtain();
		msg.what = MERCHANT_FEEDBACK;
		msg.obj = baseListBeens;
		mHandler.sendMessage(msg);
		
	}
}
