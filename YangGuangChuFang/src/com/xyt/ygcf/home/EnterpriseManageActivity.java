package com.xyt.ygcf.home;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 企业登入
 * @author lenovo hexiangyang
 *
 */
public class EnterpriseManageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_enterprise);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		RelativeLayout mTitle = (RelativeLayout)this.findViewById(R.id.logout_btn);
		TextView mUser = (TextView)this.findViewById(R.id.desktop_userID);
		mTitle.setVisibility(View.GONE);
		mUser.setText("企业登入");
	}

	@Override
	public void onClick(View arg0) {
		
	}
}
