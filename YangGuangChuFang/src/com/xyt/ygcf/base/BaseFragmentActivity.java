package com.xyt.ygcf.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;

public class BaseFragmentActivity extends FragmentActivity implements OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initTitleBackBtn();
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		initTitleBackBtn();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		initTitleBackBtn();
	}

	protected void setTitle(String text) {
		((TextView) findViewById(R.id.desktop_userID)).setText(text);
	}

	/**
	 * 标题栏左按钮被按下
	 */
	protected void onBackBtnPressed() {
		super.onBackPressed();
	}

	/**
	 * 设置返回按钮事件
	 */
	private void initTitleBackBtn(int... resid) {
		if (resid.length != 0) {
			((ImageView) findViewById(R.id.back_btn)).setBackgroundResource(resid[0]);
		}
		((ImageView) findViewById(R.id.back_btn)).setVisibility(View.VISIBLE);
		((ImageView) findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackBtnPressed();
			}
		});
	}

	/**
	 * 使用资源设置返回按钮背景
	 * 
	 * @param resource
	 *            资源
	 */
	protected void setBackBtnImage(int resource) {
		initTitleBackBtn(resource);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in,
		// R.anim.push_right_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		// overridePendingTransition(R.anim.push_left_in,
		// R.anim.push_right_out);
	}

}
