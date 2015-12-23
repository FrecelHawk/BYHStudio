package com.xyt.ygcf.adpter;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.BrandMerchantPopAdapter.ViewHolder;
import com.xyt.ygcf.entity.CityObjBean;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BrandMerchantAreaAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CityObjBean> objBeans;

	public BrandMerchantAreaAdapter(Context context, List<CityObjBean> objBeans) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.objBeans = objBeans;
	}
	
	@Override
	public int getCount() {
		return objBeans == null?0:objBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return objBeans.get(arg0);
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
//			arg1 = inflater.inflate(R.layout.layout_area_popupwindow_item, null);
//			holder.popText = (TextView) arg1.findViewById(R.id.aera_pop_item_text);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
//		CityObjBean objBean = objBeans.get(arg0);
		holder.popText.setText(objBeans.get(arg0).name);
		return arg1;
	}

	public class ViewHolder {
		TextView popText;
	}
}
