package com.xyt.ygcf.ui.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseFragmentActivity;

/**
 * 
 * @author yuyangming 我的收藏
 * 
 */
public class CollectionActivitiy extends BaseFragmentActivity {

	private View tab1, tab2, tab3;

	private Fragment tabFirstFragment, tabSecondFragment, tabThirdFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pager);
		findViewsById();
		initTitle();
		initViewData();
	}

	private void initTitle() {
		setTitle("我的收藏");
	}

	private void findViewsById() {
		tab1 = findViewById(R.id.activity_my_pager_radio_1);
		tab2 = findViewById(R.id.activity_my_pager_radio_2);
		tab3 = findViewById(R.id.activity_my_pager_radio_3);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab1.setSelected(true);
	}

	private void initViewData() {
		tabFirstFragment = new CollectionRestaurantFragment();
		tabSecondFragment = new CollectionCanteenFragment();
		tabThirdFragment = new CollectionMenuFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.activity_my_collection_container, tabFirstFragment);
		transaction.add(R.id.activity_my_collection_container, tabSecondFragment);
		transaction.add(R.id.activity_my_collection_container, tabThirdFragment);
		transaction.hide(tabSecondFragment);
		transaction.hide(tabThirdFragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.activity_my_pager_radio_1:
				tab1.setSelected(true);
				tab2.setSelected(false);
				tab3.setSelected(false);
				getSupportFragmentManager().beginTransaction().show(tabFirstFragment).hide(tabSecondFragment)
						.hide(tabThirdFragment).commit();
				break;
			case R.id.activity_my_pager_radio_2:
				tab1.setSelected(false);
				tab2.setSelected(true);
				tab3.setSelected(false);
				getSupportFragmentManager().beginTransaction().show(tabSecondFragment).hide(tabFirstFragment)
						.hide(tabThirdFragment).commit();
				break;
			case R.id.activity_my_pager_radio_3:
				tab1.setSelected(false);
				tab2.setSelected(false);
				tab3.setSelected(true);
				getSupportFragmentManager().beginTransaction().show(tabThirdFragment).hide(tabFirstFragment)
						.hide(tabSecondFragment).commit();
				break;
			default:
				break;
		}
	}

}
