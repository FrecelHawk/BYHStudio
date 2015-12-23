package com.xyt.ygcf.ui.my;

import android.os.Bundle;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseFragmentActivity;

/**
 * 
 * @author yuyangming 投诉管理
 * 
 */
public class ComplainListActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_common);
		findViewsByid();
	}

	private void findViewsByid() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_my_common_container, new ComplainListFragment()).commit();
		setTitle("投诉管理");
	}
}
