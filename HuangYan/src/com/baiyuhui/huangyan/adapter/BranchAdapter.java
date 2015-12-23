package com.baiyuhui.huangyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.FindClassifyAboutDetails;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.utils.UILManager;

/**
 * 首页/分支
 * 
 * @author Administrator
 * 
 */
public class BranchAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private BaseJsonBeen lists = new BaseJsonBeen();

	public BranchAdapter(Context mContext, BaseJsonBeen lists) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.getList().size();
	}

	@Override
	public Object getItem(int position) {
		return lists.getList().get(position);
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
			convertView = mInflater.inflate(R.layout.home_item_list_branch,
					null);
			holder.branch_list_item_img = (ImageView) convertView
					.findViewById(R.id.branch_list_item_img);
			holder.branch_list_item_tv = (TextView) convertView
					.findViewById(R.id.branch_list_item_tv);
			holder.branch_list_item_img_sex = (ImageView) convertView
					.findViewById(R.id.branch_list_item_img_sex);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UILManager.with(mContext).load(lists.getList().get(position).img)
		.into(holder.branch_list_item_img);
		
		if(lists.getList().get(position).sex == 1)
		{
			holder.branch_list_item_img_sex.setBackgroundResource(R.drawable.boy);		
		}
		else if(lists.getList().get(position).sex == 2)
		{
			holder.branch_list_item_img_sex.setBackgroundResource(R.drawable.girl);
		}
		
		holder.branch_list_item_tv.setText(lists.getList().get(position).uname);
		return convertView;
	}

	class ViewHolder {
		ImageView branch_list_item_img;
		TextView branch_list_item_tv;
		ImageView branch_list_item_img_sex;
	}
}
