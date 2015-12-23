package com.baiyuhui.huangyan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baiyuhui.huangyan.R;

public class GoodsInventoryFragment extends Fragment {

	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_goods_inventory, container, false);
		return view;
	}
}