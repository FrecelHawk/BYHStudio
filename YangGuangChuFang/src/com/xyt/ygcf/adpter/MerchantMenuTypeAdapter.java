package com.xyt.ygcf.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.MerchantMenuTypeBean;

public class MerchantMenuTypeAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<MerchantMenuTypeBean> menuTypeBeans;
	private int position = -1;
	
	public MerchantMenuTypeAdapter(Context context, List<MerchantMenuTypeBean> menuTypeBeans) {
		this.inflater = LayoutInflater.from(context);
		this.menuTypeBeans = menuTypeBeans;
	}
	
	public void setBackground(int position){
		this.position = position;
	}

	@Override
	public int getCount() {
		return menuTypeBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return menuTypeBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.merchant_menu_item, null);
			holder.nameText = (TextView) arg1.findViewById(R.id.menu_type_name);
			arg1.setTag(holder);
		}else {
			holder = (ViewHolder) arg1.getTag();
		}
		MerchantMenuTypeBean menuTypeBean = menuTypeBeans.get(arg0);
		holder.nameText.setText(menuTypeBean.getTypeName());
		if(position == arg0){
			arg1.setBackgroundResource(R.color.aurantium);
		}else {
			arg1.setBackgroundResource(R.color.gray);
		}
		return arg1;
	}

	public class ViewHolder{
		TextView nameText;
	}
}
