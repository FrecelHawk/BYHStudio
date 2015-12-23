package com.xyt.ygcf.adpter.my;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.my.ComlainDetailBeen;

/**
 * 1.0版本废弃
 * @author freesonfish
 *
 */
public class ComplainDetailAdapter extends BaseAdapter {

	private List<ComlainDetailBeen> list;
	private LayoutInflater inflater;

	public ComplainDetailAdapter(Context context, List<ComlainDetailBeen> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.complain_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.complain_list_item_shop);
			holder.order = (TextView) convertView.findViewById(R.id.complain_list_item_order);
			holder.shop = (TextView) convertView.findViewById(R.id.complain_list_item_shop);
			holder.state = (TextView) convertView.findViewById(R.id.complain_list_item_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.see.setTag(position);
		
//		holder.title.setText("");
//		holder.order.setText("");
//		holder.shop.setText("");
//		holder.state.setText("");
		

		return convertView;
	}

	private static class ViewHolder {
		public TextView title, order, shop, state, see;
	}

}
