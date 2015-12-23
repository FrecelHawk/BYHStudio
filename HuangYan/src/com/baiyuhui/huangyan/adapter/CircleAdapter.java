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
/**
 * 圈子
 * @author Administrator
 *
 */
public class CircleAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<BaseJsonBeen> lists = new ArrayList<BaseJsonBeen>();
	
	public CircleAdapter(Context mContext, List<BaseJsonBeen> lists) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if(lists != null){
			return lists.size();
		}else{
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
			convertView = mInflater.inflate(R.layout.find_item_list_circle, null);
			holder.img = (ImageView) convertView.findViewById(R.id.circle_list_item_img);
			
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);
			holder.view2 = (TextView) convertView.findViewById(R.id.view2);
			holder.view4 = (TextView) convertView.findViewById(R.id.view4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UILManager.with(mContext).load(lists.get(position).getIco())
		.into(holder.img);
		holder.text1.setText(lists.get(position).getName());
		holder.text2.setText(lists.get(position).getInfo());
		holder.view2.setText(lists.get(position).getCuid());
		holder.view4.setText(lists.get(position).getPostnum());
		return convertView;
	}

	class ViewHolder{
		ImageView img;
		TextView text1;
		TextView text2;
		TextView view2;
		TextView view4;
	}
}
