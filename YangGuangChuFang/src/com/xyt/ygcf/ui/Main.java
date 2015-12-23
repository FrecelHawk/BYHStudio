package com.xyt.ygcf.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.base.AppManager;
import com.xyt.ygcf.base.BaseFragmentActivity;
import com.xyt.ygcf.impl.IActivityComunicationFragment;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.ui.my.LoginActivity;
import com.xyt.ygcf.util.DeviceHelper;
import com.xyt.ygcf.util.ToastUtil;

public class Main extends BaseFragmentActivity implements IActivityComunicationFragment {

	public static Fragment[] mFragments;
	private int mBeforeWhich = 0;
	private RelativeLayout rl;
	private LinearLayout ll;
	private BroadcastReceiver receiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setFragmentIndicator(0);
		if (!DeviceHelper.getNetworkState(this)) {
			initconnlessNetworkView();
			IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
			initBrocastReciever();
			registerReceiver(receiver, intentFilter);
		
		}

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initBrocastReciever() {
		receiver = new BroadcastReceiver() {

			public void onReceive(android.content.Context context, Intent intent) {

				ConnectivityManager connectivityManager =

				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

				if (connectivityManager != null) {

					NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

					for (int i = 0; i < networkInfos.length; i++) {

						State state = networkInfos[i].getState();

						if (NetworkInfo.State.CONNECTED == state) {
							ll.setVisibility(View.VISIBLE);
							rl.setVisibility(View.GONE);
							loactionCallBreak();
							return;

						}

					}

				}

			};
		};
	}

	private void initconnlessNetworkView() {
		rl = (RelativeLayout) findViewById(R.id.rl_no_network);
		ll = (LinearLayout) findViewById(R.id.ll_conn_networt);
		Button btnSetNetWork = (Button) findViewById(R.id.set_network);
		ll.setVisibility(View.GONE);
		rl.setVisibility(View.VISIBLE);
		btnSetNetWork.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// 跳转到系统设置界面
		Intent intent = new Intent(Settings.ACTION_SETTINGS);
		startActivity(intent);
		v.setVisibility(View.GONE);
		findViewById(R.id.ll_connting).setVisibility(View.VISIBLE);

	}

	/**
	 * 初始化fragment
	 */
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[5];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_nearby);
		mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_search);
		mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_my);
		mFragments[4] = getSupportFragmentManager().findFragmentById(R.id.fragment_more);
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2])
				.hide(mFragments[3]).hide(mFragments[4]).show(mFragments[whichIsDefault]).commit();

		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator.setOnIndicateListener(new com.xyt.ygcf.ui.FragmentIndicator.OnIndicateListener() {
			@Override
			public void onIndicate(View v, int beforeWhich, int which) {
				mBeforeWhich = beforeWhich;
				mCurrentTab = which;
				if (which == Constants.TAB_MY_CODE && !LoginHelper.isLogin()) {
					Intent intent = new Intent(Main.this, LoginActivity.class);
					startActivityForResult(intent, 0x100);
				} else {
					if (LoginHelper.isLogin() && which == Constants.TAB_MY_CODE) {
						((MyFragment) mFragments[Constants.TAB_MY_CODE]).finishedLoginInLoginActivity();
					}
					if (which == 0) {
						((HomeFragment) mFragments[0]).showCityDialog();
					}
					getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1])
							.hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]).show(mFragments[which])
							.commit();
				}
				// if (which == 1) {
				// ((NearbyFragment)mFragments[1]).isSelected();
				// }

			}
		});
	}

	public void loactionCallBreak() {
		if (mFragments[0] != null) {
			((HomeFragment) mFragments[0]).locationCallBreak();
		}
		
		if (mFragments[1] != null) {
			((NearbyFragment) mFragments[1]).netConnectCallBreak();
		}
	}

	static final long WAITTIME = 2000;
	long touchTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 监听返回按钮。2秒内连续点击则退出
		if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= WAITTIME) {
				Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
//				Toast toast=Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.TOP, 10, 50);
//				toast.show();
				
				touchTime = currentTime;
			} else {
				// finish();
				LoginHelper.loginOut();
				AppManager.getAppManager().finishAllActivity();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void framgentCallBack(int which) {
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2])
				.hide(mFragments[3]).hide(mFragments[4]).show(mFragments[which]).commit();
		FragmentIndicator.setIndicator(which);
		if (which == 1) {
			((NearbyFragment) mFragments[which]).showShopListView();
		} else if (which == 0) {
			mCurrentTab = 0;
			((HomeFragment) mFragments[0]).showCityDialog();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		if (requestCode == 0x100) {
			if (resultCode == Activity.RESULT_OK) {
				framgentCallBack(Constants.TAB_MY_CODE);
				((MyFragment) mFragments[Constants.TAB_MY_CODE]).finishedLoginInLoginActivity();
			} else {
				FragmentIndicator.setIndicator(mBeforeWhich);
				if (mBeforeWhich == 0) {
					mCurrentTab = 0;
					((HomeFragment) mFragments[0]).showCityDialog();
				}
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	private int mCurrentTab = 0;

	@Override
	public int getCurrentTab() {
		return mCurrentTab;
	}

}
