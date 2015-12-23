package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseFragmentActivity;

/**
 * 
 * @author yuyangming 评价
 * 
 */
public class EvaluateActivity extends BaseFragmentActivity {

	private View tab1, tab2, tab3;
	private Fragment fragment1, fragment2, fragment3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_evaluate);
		findViewsByid();
		initViewData();
	}

	private void findViewsByid() {
		tab1 = findViewById(R.id.activity_my_pager_radio_1);
		tab2 = findViewById(R.id.activity_my_pager_radio_2);
		tab3 = findViewById(R.id.activity_my_pager_radio_3);
		tab1.setSelected(true);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
	}

	private void initViewData() {
		setTitle("评价管理");
		fragment1 = EvaluateFragment.getInstance(0);
		fragment2 = EvaluateFragment.getInstance(1);
		fragment3 = new EvaluateFeedBackFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.activity_my_evaluate_container, fragment1, "fragment1");
		transaction.add(R.id.activity_my_evaluate_container, fragment2, "fragment2");
		transaction.add(R.id.activity_my_evaluate_container, fragment3, "fragment3");
		transaction.hide(fragment2);
		transaction.hide(fragment3);
		transaction.commit();
		transaction = null;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.activity_my_pager_radio_1:
				tab1.setSelected(true);
				tab2.setSelected(false);
				tab3.setSelected(false);
				getSupportFragmentManager().beginTransaction().show(fragment1).hide(fragment2).hide(fragment3).commit();
				break;
			case R.id.activity_my_pager_radio_2:
				tab1.setSelected(false);
				tab2.setSelected(true);
				tab3.setSelected(false);
				getSupportFragmentManager().beginTransaction().show(fragment2).hide(fragment1).hide(fragment3).commit();
				break;

			case R.id.activity_my_pager_radio_3:
				tab1.setSelected(false);
				tab2.setSelected(false);
				tab3.setSelected(true);
				getSupportFragmentManager().beginTransaction().show(fragment3).hide(fragment1).hide(fragment2).commit();
				break;

			default:
				break;
		}
	}
}
