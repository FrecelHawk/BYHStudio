package com.xyt.ygcf.adpter;

import java.util.ArrayList;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.more.FoodSourceBean;
import com.xyt.ygcf.util.BaseUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodSourceAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater inflater;
	private ArrayList<FoodSourceBean> sourceBeans;
	private boolean isShow;

	public FoodSourceAdapter(Context context, ArrayList<FoodSourceBean> sourceBeans, boolean isShow) {
		this.mcontext = context;
		this.sourceBeans = sourceBeans;
		this.isShow = isShow;
	}

	@Override
	public int getCount() {
		if(isShow == true){
			if(sourceBeans.size() > 3){
				return 3;
			}else {
				return this.sourceBeans.size();
			}
		}else {
			return sourceBeans.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		return sourceBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mcontext).inflate(R.layout.food_source_item, null);
			holder.foodName = (TextView) arg1.findViewById(R.id.food_name);
			holder.Quality = (TextView) arg1.findViewById(R.id.quality);
			holder.foodFrom = (TextView) arg1.findViewById(R.id.food_from);
			holder.foodTime = (TextView) arg1.findViewById(R.id.food_time);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		FoodSourceBean item = sourceBeans.get(arg0);
		if (item != null) {
				holder.foodName.setText(item.getFoodName());
				holder.Quality.setText(item.getQuality()+item.getUnitName());
				holder.foodFrom.setText(item.getFoodFrom());
				holder.foodTime.setText(item.getFoodTime());
		}
		return arg1;
	}

	private static class ViewHolder {
		TextView foodName, Quality, foodFrom, foodTime;
	}
}
