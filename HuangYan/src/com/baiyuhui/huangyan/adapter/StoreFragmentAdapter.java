package com.baiyuhui.huangyan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.utils.UILManager;

public class StoreFragmentAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<BaseJsonBeen> lists = new ArrayList<BaseJsonBeen>();

	public StoreFragmentAdapter(Context mContext, List<BaseJsonBeen> lists) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		} else {
			return 0;
		}
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
			convertView = mInflater.inflate(R.layout.fragent_item_list_store, null);
			holder.img = (ImageView) convertView.findViewById(R.id.ItemImage);
			holder.name = (TextView) convertView.findViewById(R.id.ItemName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(lists.get(position).getName());
		UILManager.with(mContext).load(lists.get(position).getImg())
				.into(holder.img);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView name;
	}
}
