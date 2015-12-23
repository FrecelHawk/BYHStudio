package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.fragment.FindFragment;
import com.baiyuhui.huangyan.fragment.HomeFragment;
import com.baiyuhui.huangyan.fragment.PersonFragment;
import com.baiyuhui.huangyan.fragment.StoreFragment;
import com.baiyuhui.huangyan.impl.IActivityComunicationFragment;
import com.baiyuhui.huangyan.utils.PhoneDeviceInfo;
import com.baiyuhui.huangyan.view.FragmentIndicator;

/**
 * 首页/Main
 * 
 * @author Administrator
 * 
 */

public class MainTabActivity extends FragmentActivity implements
		OnClickListener, IActivityComunicationFragment {

	boolean istrue = false;
	private TextView title;
	private Fragment mContent;
	private FrameLayout tabFrameLayout;

	// 用于查找回退栈中的fragment，findFragmentByTag
	public static final String HOME_TAG = "home_button";
	public static final String STORE_TAG = "store_button";
	public static final String FIND_TAG = "find_btton";
	public static final String PERSON_TAG = "person_btton";

	// 底部菜单
	RadioGroup mTabMenu;

	// 保留当前的显示的fragment的标签
	String mLastFragmentTag;
	private ImageView rightImg;
	private ImageView leftImg;
	private LinearLayout lay;
	private RelativeLayout layrightimg;
	public static Fragment[] mFragments;
	private int mBeforeWhich = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniview();
		setFragmentIndicator(0);


	}

	/**
	 * 初始化fragment
	 */
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[5];
		mFragments[0] = getSupportFragmentManager().findFragmentById(
				R.id.fragment_home);
		mFragments[1] = getSupportFragmentManager().findFragmentById(
				R.id.fragment_store);
		mFragments[2] = getSupportFragmentManager().findFragmentById(
				R.id.fragment_find);
		mFragments[3] = getSupportFragmentManager().findFragmentById(
				R.id.fragment_person);
		getSupportFragmentManager().beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
				.show(mFragments[whichIsDefault]).commit();

		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator
				.setOnIndicateListener(new FragmentIndicator.OnIndicateListener() {
					@Override
					public void onIndicate(View v, int beforeWhich, int which) {
						mBeforeWhich = beforeWhich;
						mCurrentTab = which;
						if (which == 1 && !PhoneDeviceInfo.isLogin()) {
							Intent intent = new Intent(MainTabActivity.this,
									LoginActivity.class);
							startActivityForResult(intent, 0x100);
						} else if (which == 3 && !PhoneDeviceInfo.isLogin()) {
							Intent intent = new Intent(MainTabActivity.this,
									LoginActivity.class);
							startActivityForResult(intent, 0x100);
						} else {
							getSupportFragmentManager().beginTransaction()
									.hide(mFragments[0]).hide(mFragments[1])
									.hide(mFragments[2]).hide(mFragments[3])
									.show(mFragments[which]).commit();
							onCheckedChanged(mCurrentTab);
						}

					}
				});
	}

	private int mCurrentTab = 0;

	@Override
	protected void onResume() {
		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	private void iniview() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("首页");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setVisibility(View.GONE);

		rightImg = (ImageView) findViewById(R.id.aciont_bar_rigth);

		lay = (LinearLayout) findViewById(R.id.lay_left);
		lay.setOnClickListener(this);
		lay.setVisibility(View.GONE);

		layrightimg = (RelativeLayout) findViewById(R.id.lay_rigth);
		layrightimg.setOnClickListener(this);
		layrightimg.setVisibility(View.GONE);

	}

	// fragment_TAB点击事件
	public void onCheckedChanged(int which) {
		switch (which) {
		case 0:
			title.setText("首页");
			title.setTextSize(17);
			title.setTextColor(getResources().getColor(R.color.white));
			rightImg.setBackgroundResource(0);
			leftImg.setBackgroundResource(0);
			lay.setVisibility(View.GONE);
			layrightimg.setVisibility(View.GONE);
			title.setBackgroundResource(0);
			break;
		case 1:
			title.setText("商城");
			title.setTextSize(17);
			title.setTextColor(getResources().getColor(R.color.white));
			rightImg.setBackgroundResource(0);
			leftImg.setBackgroundResource(0);
			lay.setVisibility(View.GONE);
			layrightimg.setVisibility(View.GONE);
			title.setBackgroundResource(0);
			break;
		case 2:
			title.setText("发现");
			title.setTextSize(17);
			title.setTextColor(getResources().getColor(R.color.white));
			rightImg.setBackgroundResource(0);
			leftImg.setBackgroundResource(0);
			title.setBackgroundResource(0);
			layrightimg.setVisibility(View.GONE);
			lay.setVisibility(View.GONE);
			break;
		case 3:
			title.setText("个人中心");
			title.setTextSize(17);
			title.setTextColor(getResources().getColor(R.color.white));
			rightImg.setBackgroundResource(R.drawable.mail);
			leftImg.setBackgroundResource(R.drawable.more);
			lay.setVisibility(View.VISIBLE);
			layrightimg.setVisibility(View.VISIBLE);
			title.setBackgroundResource(0);
			break;
		}
	}

	// 切换fragment
	private void change(String nowTag) {

		if (nowTag != mLastFragmentTag) {
			Fragment lastFragment = getSupportFragmentManager()
					.findFragmentByTag(mLastFragmentTag);

			if (getSupportFragmentManager().findFragmentByTag(nowTag) != null) {
				Fragment fragmentNow = getSupportFragmentManager()
						.findFragmentByTag(nowTag);
				getSupportFragmentManager().beginTransaction()
						.show(fragmentNow).hide(lastFragment).commit();
			} else if (nowTag == HOME_TAG) {
				Fragment fragment = new HomeFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.frament_content, fragment, nowTag)
						.addToBackStack(null).hide(lastFragment).commit();
			} else if (nowTag == STORE_TAG) {
				Fragment fragment = new StoreFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.frament_content, fragment, nowTag)
						.addToBackStack(null).hide(lastFragment).commit();
			} else if (nowTag == FIND_TAG) {
				Fragment fragment = new FindFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.frament_content, fragment, nowTag)
						.addToBackStack(null).hide(lastFragment).commit();
			} else if (nowTag == PERSON_TAG) {
				Fragment fragment = new PersonFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.frament_content, fragment, nowTag)
						.addToBackStack(null).hide(lastFragment).commit();
			}
			mLastFragmentTag = nowTag;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
//			slidingMenu.showMenu();
			break;
		case R.id.lay_rigth:
			Intent intent = new Intent();
			intent.setClass(MainTabActivity.this, PersonInformActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		if (requestCode == 0x100) {
			if (resultCode == Activity.RESULT_OK) {
//				framgentCallBack(Constants.TAB_MY_CODE);
				framgentCallBack(mCurrentTab);
//				((MyFragment) mFragments[Constants.TAB_MY_CODE]).finishedLoginInLoginActivity();
			} else {
				FragmentIndicator.setIndicator(mBeforeWhich);
				/*if (mBeforeWhich == 0) {
					mCurrentTab = 0;
//					((HomeFragment) mFragments[0]).showCityDialog();
				}*/
			}
		}

	}
	
	@Override
	public void framgentCallBack(int which) {
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2])
				.hide(mFragments[3]).show(mFragments[which]).commit();
		FragmentIndicator.setIndicator(which);
		/*if (which == 1) {
			((NearbyFragment) mFragments[which]).showShopListView();
		} else if (which == 0) {
			mCurrentTab = 0;
			((HomeFragment) mFragments[0]).showCityDialog();
		}*/
	}
	
	@Override
	public int getCurrentTab() {
		// TODO Auto-generated method stub
		return mCurrentTab;
	}

	static final long WAITTIME = 2000;
	long touchTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 监听返回按钮。2秒内连续点击则退出
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= WAITTIME) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
