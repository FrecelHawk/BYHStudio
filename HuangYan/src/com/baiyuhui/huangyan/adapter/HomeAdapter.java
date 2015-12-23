package com.baiyuhui.huangyan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baiyuhui.huangyan.R;

public class HomeAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> lists = new ArrayList<String>();

	public HomeAdapter(Context mContext, List<String> lists) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.home_item_list,
					null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.home_list_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource(R.drawable.moren1);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
	}
}
