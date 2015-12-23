package com.xyt.ygcf.adpter;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.BrandMerchantPopAdapter.ViewHolder;
import com.xyt.ygcf.entity.CityObjBean;
import com.xyt.ygcf.entity.FoodTypeBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DefaultCityAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<CityObjBean> objBeans;
	
	public DefaultCityAdapter(Context context, List<CityObjBean> objBeans) {
		this.context = context;
		this.objBeans = objBeans;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return objBeans.size();
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
			arg1 = inflater.inflate(R.layout.default_city_itme, null);
			holder.textView = (TextView) arg1.findViewById(R.id.default_city_item_text);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		CityObjBean typeBean = objBeans.get(arg0);
		holder.textView.setText(typeBean.name);
		return arg1;
	}

	public class ViewHolder {
		TextView textView;
	}
}
