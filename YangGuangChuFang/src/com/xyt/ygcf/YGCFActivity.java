package com.xyt.ygcf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.ui.Main;

public class YGCFActivity extends Activity {
	private boolean isFirstIn = false;

//	private View view;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟2秒
	private static final long SPLASH_DELAY_MILLIS = 2000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				/**debug版本，先关掉，浪费时间*/
				startActivity(new Intent(YGCFActivity.this, Main.class));
				finish();
				break;
			case GO_GUIDE:
				Intent intent=new Intent(getApplicationContext(), GuideActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
//		view = View.inflate(this, R.layout.start, null);
		setContentView(R.layout.start);
		LoginHelper.autoLogin(this);
		init();
	}

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

//		isFirstIn = true;
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
//			mHandler.sendEmptyMessage(GO_HOME);
		} else {
//			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			mHandler.sendEmptyMessage(GO_GUIDE);
		}

	}

	/**
	 * 跳转首页
	 */
	private void redirectTo() {
//		 //渐变展示启动屏
//		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
//		aa.setDuration(2000);
//		aa.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				startActivity(new Intent(YGCFActivity.this, Main.class));
//				finish();
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//
//		});
//		view.startAnimation(aa);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ygc, menu);
		return true;
	}

	
	
}
