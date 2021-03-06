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
/**
 * 个人中心/回复通知
 * 
 * @author Administrator
 * 
 */
public class InfromRevertAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<BaseJsonBeen> lists = new ArrayList<BaseJsonBeen>();
	
	public InfromRevertAdapter(Context mContext, List<BaseJsonBeen> lists) {
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
			convertView = mInflater.inflate(R.layout.person_item_infrom_revert, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder{
	}
}
