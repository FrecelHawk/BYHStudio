package com.baiyuhui.huangyan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.HomeBenefitActivity;
import com.baiyuhui.huangyan.activity.HomeBranchActivity;
import com.baiyuhui.huangyan.activity.HomeNewsActivity;
import com.baiyuhui.huangyan.activity.HomeSchoolActivity;
import com.baiyuhui.huangyan.activity.HomeVideoActivity;
import com.baiyuhui.huangyan.adapter.FindViewPagerAdapter;
import com.baiyuhui.huangyan.adapter.HomeAdapter;
import com.baiyuhui.huangyan.view.CustomizeViewpageView;

/**
 * @author Administrator
 * 
 * @author :首页
 */
public class HomeFragment extends Fragment implements OnClickListener {

	// 5个button
	private Button newsbtn, videobtn, benefitbtn, schoolbtn, branchbtn;

	private View view;
	private CustomizeViewpageView mViewPager;
	private LinearLayout mDotLayout;

	private FindViewPagerAdapter pagerAdapter;
	private Runnable mRunnable;
	private Handler mHandler;
	private boolean isTouched;
	private final static int AD_SWITCH_TIME = 4 * 1000; // 5S
	private final static int mDotResId = R.drawable.dot;

	private List<String> lists = new ArrayList<String>();

	private ListView mListView;
	private List<String> lists2 = new ArrayList<String>();

	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setData();
		initViews();
		initBanner();

		newsbtn = (Button) view.findViewById(R.id.news_btn);
		videobtn = (Button) view.findViewById(R.id.video_btn);
		benefitbtn = (Button) view.findViewById(R.id.benefit_btn);
		schoolbtn = (Button) view.findViewById(R.id.school_btn);
		branchbtn = (Button) view.findViewById(R.id.branch_btn);

		newsbtn.setOnClickListener(this);
		videobtn.setOnClickListener(this);
		benefitbtn.setOnClickListener(this);
		schoolbtn.setOnClickListener(this);
		branchbtn.setOnClickListener(this);

	}

	public void setData() {
		if (lists.size() <= 0) {
			lists.add(String.valueOf(R.drawable.boy));
			lists.add(String.valueOf(R.drawable.branch));
			lists2.add("");
			lists2.add("");
		}
	}
	

	private void initViews() {

		TextView textView = (TextView) view.findViewById(R.id.text_view);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		

		mListView = (ListView) view.findViewById(R.id.home_list);
		HomeAdapter adapter = new HomeAdapter(getActivity(), lists2);
		mListView.setAdapter(adapter);

	}

	/**
	 * ��ʼ��Banner
	 */
	private void initBanner() {
		mDotLayout = (LinearLayout) view.findViewById(R.id.home_dot_layout);
		mViewPager = (CustomizeViewpageView) view
				.findViewById(R.id.home_viewpage);
		pagerAdapter = new FindViewPagerAdapter(getActivity(), lists, null);
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
		pagerAdapter.notifyDataSetChanged();

		if (lists.size() > 1) {
			mHandler.removeCallbacks(mRunnable);
			mHandler.postDelayed(mRunnable, AD_SWITCH_TIME);

		}
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
			ImageView dotImageView = new ImageView(getActivity());
			dotImageView.setImageResource(mDotResId);
			dotImageView.setPadding(padding, 0, padding, 0);
			mDotLayout.addView(dotImageView);
		}
		setDotsState(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_btn: // 新闻
			startActivity(new Intent(getActivity(), HomeNewsActivity.class));
			break;
		case R.id.video_btn:// 视频
			startActivity(new Intent(getActivity(), HomeVideoActivity.class));
			break;
		case R.id.benefit_btn: // 公益
			startActivity(new Intent(getActivity(), HomeBenefitActivity.class));
			break;
		case R.id.school_btn: // 学堂
			startActivity(new Intent(getActivity(), HomeSchoolActivity.class));
			break;
		case R.id.branch_btn: // 分支
			startActivity(new Intent(getActivity(), HomeBranchActivity.class));
			break;
		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
