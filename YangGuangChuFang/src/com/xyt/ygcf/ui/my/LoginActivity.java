package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseFragmentActivity;
import com.xyt.ygcf.ui.MyFragment;

public class LoginActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_common);
		findViewById(R.id.activity_my_common_title).setVisibility(View.GONE);

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.activity_my_common_container, MyFragment.getInstance("登录"));
		transaction.commit();
	}
}
