package com.baiyuhui.huangyan.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.FindViewPagerAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.fragment.GoodsDetailFragment;
import com.baiyuhui.huangyan.fragment.GoodsEvaluateFragment;
import com.baiyuhui.huangyan.fragment.GoodsInventoryFragment;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.view.CustomizeViewpageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 商城/商品详情
 * 
 * @author Administrator
 * 
 */
public class StoreDetailsActivity extends FragmentActivity implements
		OnClickListener {

	private TextView title;
	private ImageView leftImg;

	private CustomizeViewpageView mViewPager;
	private LinearLayout mDotLayout;
	private FindViewPagerAdapter pagerAdapter;
	private Runnable mRunnable;
	private Handler mHandler;
	private boolean isTouched;
	private final static int AD_SWITCH_TIME = 4 * 1000; // 5S
	private final static int mDotResId = R.drawable.dot;
	// private String[] imgs;
	private View view1;
	private View view2;
	private View view3;
	private Button xiangqing;
	private Button pingjia;
	private Button qingdan;
	private Button kefu;
	private Button gouwu;
	private Button jiaru;
	private LinearLayout content;
	private LinearLayout layout;
	

	private static final String GOODS_DETAIL = "detail";
	private static final String GOODS_EVALUATE = "evaluate";
	private static final String GOODS_INVENTORY = "inventory";

	private TextView sanzhuang;
	private TextView hezhuang;
	private TextView shangcheng;
	private TextView shichang;
	private TextView name;

	private List<String> lists = new ArrayList<String>();

	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_details_activity);
		getIntentDate();
		// initBanner();
		initViews();
	}

	private void getIntentDate() {
		id = getIntent().getIntExtra("id", 0);
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("商品详情");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		xiangqing = (Button) findViewById(R.id.xiangqing);
		xiangqing.setOnClickListener(this);

		pingjia = (Button) findViewById(R.id.pingjia);
		pingjia.setOnClickListener(this);

		qingdan = (Button) findViewById(R.id.qingdan);
		qingdan.setOnClickListener(this);

		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);
		view3 = (View) findViewById(R.id.view3);

		kefu = (Button) findViewById(R.id.kefu);
		kefu.setOnClickListener(this);

		gouwu = (Button) findViewById(R.id.gouwu);
		gouwu.setOnClickListener(this);

		jiaru = (Button) findViewById(R.id.jiaru);
		jiaru.setOnClickListener(this);

		sanzhuang = (TextView) findViewById(R.id.sanzhuang);
		sanzhuang.setOnClickListener(this);

		hezhuang = (TextView) findViewById(R.id.hezhuang);
		hezhuang.setOnClickListener(this);

		shangcheng = (TextView) findViewById(R.id.shangcheng);

		shichang = (TextView) findViewById(R.id.shichang);

		content = (LinearLayout) findViewById(R.id.beijin);
		content.getBackground().setAlpha(220);

		name = (TextView) findViewById(R.id.store_detail_name);

		setDefaultFragment();

		getData();
		getShopCartNum();
	}
	
	private void setDefaultFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		GoodsDetailFragment fragment = new GoodsDetailFragment();
		transaction.replace(R.id.frament_content, fragment);
		transaction.commit();
	}

	@Override
	// public void onScroll(int scrollY) {
	// int mBuyLayout2ParentTop = Math.max(scrollY, mBuyLayout.getTop());
	// mTopBuyLayout.layout(0, mBuyLayout2ParentTop, mTopBuyLayout.getWidth(),
	// mBuyLayout2ParentTop + mTopBuyLayout.getHeight());
	// }
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.sanzhuang:
			sanzhuang.setBackgroundResource(R.drawable.leixing_hong_biankuan);
			hezhuang.setBackgroundResource(R.drawable.leixing_biankuan);
			shangcheng.setText("克");
			shichang.setText("克");
			break;
		case R.id.hezhuang:
			sanzhuang.setBackgroundResource(R.drawable.leixing_biankuan);
			hezhuang.setBackgroundResource(R.drawable.leixing_hong_biankuan);
			shangcheng.setText("盒");
			shichang.setText("盒");
			break;
		case R.id.xiangqing:
			xiangqing.setTextColor(getResources().getColor(R.color.gamboge));
			view1.setVisibility(View.VISIBLE);
			pingjia.setTextColor(getResources().getColor(R.color.black));
			view2.setVisibility(View.GONE);
			qingdan.setTextColor(getResources().getColor(R.color.black));
			view3.setVisibility(View.GONE);
			change(GOODS_DETAIL);
			break;
		case R.id.pingjia:
			xiangqing.setTextColor(getResources().getColor(R.color.black));
			view1.setVisibility(View.GONE);
			qingdan.setTextColor(getResources().getColor(R.color.black));
			view3.setVisibility(View.GONE);
			pingjia.setTextColor(getResources().getColor(R.color.gamboge));
			view2.setVisibility(View.VISIBLE);

			// Intent it = new Intent(StoreDetailsActivity.this,
			// StoreReviews.class);
			// StoreDetailsActivity.this.startActivity(it);
			change(GOODS_EVALUATE);
			break;
		case R.id.qingdan:
			xiangqing.setTextColor(getResources().getColor(R.color.black));
			view1.setVisibility(View.GONE);
			pingjia.setTextColor(getResources().getColor(R.color.black));
			view2.setVisibility(View.GONE);
			qingdan.setTextColor(getResources().getColor(R.color.gamboge));
			view3.setVisibility(View.VISIBLE);

			// Intent it1 = new Intent(StoreDetailsActivity.this,
			// StoreInventory.class);
			// StoreDetailsActivity.this.startActivity(it1);
			change(GOODS_INVENTORY);
			break;
		case R.id.gouwu:
			Intent it2 = new Intent(StoreDetailsActivity.this, StoreCart.class);
			StoreDetailsActivity.this.startActivity(it2);
			break;
		default:
			break;
		}
	}

	// CustomDialog dialog = new CustomDialog(this);
	// dialog.createCallDialog();
	// dialog.show();

	// 切换fragment
	private void change(String nowTag) {

			Fragment fragment = null;
			if (nowTag == GOODS_DETAIL) {
				fragment = new GoodsDetailFragment();
			} else if (nowTag == GOODS_EVALUATE) {
				fragment = new GoodsEvaluateFragment();
			} else if (nowTag == GOODS_INVENTORY) {
				fragment = new GoodsInventoryFragment();
				
			}
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.replace(R.id.frament_content, fragment);
			transaction.commit();
	}

	/*
	 * public void setData() { if (lists.size() <= 0) { lists.add(1);
	 * lists.add(1); lists.add(1); } }
	 */

	/**
	 * ��ʼ��Banner
	 */
	private void initBanner() {
		mDotLayout = (LinearLayout) findViewById(R.id.store_dot_layout);
		mViewPager = (CustomizeViewpageView) findViewById(R.id.store_viewpage);
		pagerAdapter = new FindViewPagerAdapter(this, lists, null);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int position = arg0 % lists.size();
				setDotsState(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (isTouched) {
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, AD_SWITCH_TIME);
					return;
				}
				int index = mViewPager.getCurrentItem();
				int count = lists.size();

				index++;

				// if (index >= count) {
				// index = 0;
				// }
				if (count > 1) {
					mViewPager.setCurrentItem(index);
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, AD_SWITCH_TIME);
				}

			}
		};

		mHandler = new Handler();
		updateDotImage();
		// pagerAdapter.notifyDataSetChanged();

		if (lists.size() > 1) {
			mHandler.removeCallbacks(mRunnable);
			mHandler.postDelayed(mRunnable, AD_SWITCH_TIME);

		}
	}

	private void getData() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("id", id);
		client.get(this, MyUrl.GODDS_DETAILS, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						BaseJsonBeen been = JsonParse.parseGoodsDetail(arg2);
						setViewsData(been);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}
	
	private void getShopCartNum(){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(this, MyUrl.SHOP_CART_GET_NUM, new RequestParams(),
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("arg2");

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	public void setViewsData(BaseJsonBeen been) {
		name.setText(been.getName());
		String[] imgs = been.getImg().split(",");
		for (int i = 0; i < imgs.length; i++) {
			lists.add(imgs[i]);
		}
		initBanner();

	}

	public void setDotsState(int position) {
		int count = mDotLayout.getChildCount();

		for (int i = 0; i < count; i++) {

			boolean state = false;

			ImageView dotImgeImageView = (ImageView) mDotLayout.getChildAt(i);

			if (i == position) {
				state = true;
			}
			dotImgeImageView.setEnabled(state);
		}

	}

	public void updateDotImage() {

		int padding = (int) (getResources().getDisplayMetrics().density * 2);

		mDotLayout.removeAllViews();
		for (int i = 0; i < lists.size(); i++) {
			ImageView dotImageView = new ImageView(this);
			dotImageView.setImageResource(mDotResId);
			dotImageView.setPadding(padding, 0, padding, 0);
			mDotLayout.addView(dotImageView);
		}
		setDotsState(0);
	}

}
