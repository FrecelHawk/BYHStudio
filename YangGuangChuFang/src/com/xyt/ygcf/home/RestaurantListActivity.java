package com.xyt.ygcf.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.ui.NearbyFragment;

public class RestaurantListActivity extends FragmentActivity {

	private Fragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);
		getFragment();
	}

	public void getFragment(){
		fragment = new NearbyFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.activity_restaurant_container, fragment);
		transaction.commit();
	}

}
