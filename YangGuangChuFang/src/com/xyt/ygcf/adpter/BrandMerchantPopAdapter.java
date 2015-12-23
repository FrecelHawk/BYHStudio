package com.xyt.ygcf.adpter;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.FoodTypeBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BrandMerchantPopAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<FoodTypeBean> typeBeans;
	

	public BrandMerchantPopAdapter(Context context, List<FoodTypeBean> typeBeans) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.typeBeans = typeBeans;
	}

	@Override
	public int getCount() {
		return typeBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return typeBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.layout_popwindow_item, null);
			holder.popText = (TextView) arg1.findViewById(R.id.pop_item_text);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		FoodTypeBean typeBean = typeBeans.get(arg0);
		holder.popText.setText(typeBean.getFoodName());
		return arg1;
	}

	public class ViewHolder {
		TextView popText;
	}
}
