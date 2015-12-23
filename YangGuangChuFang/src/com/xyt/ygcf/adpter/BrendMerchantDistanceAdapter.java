package com.xyt.ygcf.adpter;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.BrandMerchantPopAdapter.ViewHolder;
import com.xyt.ygcf.entity.FoodTypeBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BrendMerchantDistanceAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private String[] data;
	

	public BrendMerchantDistanceAdapter(Context context, String[] data) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		return data[arg0];
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
		holder.popText.setText(data[arg0]);
		return arg1;
	}

	public class ViewHolder {
		TextView popText;
	}

}
