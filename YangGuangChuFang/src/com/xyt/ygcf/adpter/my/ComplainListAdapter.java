package com.xyt.ygcf.adpter.my;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.my.ComplainBeen;

public class ComplainListAdapter extends BaseAdapter {

	private List<ComplainBeen> list;
	private LayoutInflater inflater;
	private OnClickListener listener;

	// private int color, color2;

	public ComplainListAdapter(Context context, List<ComplainBeen> list, OnClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.listener = listener;
		// this.color = context.getResources().getColor(R.color.fe3235);
		// this.color2 = context.getResources().getColor(R.color.yelllow_left);
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
			holder.order = (TextView) convertView.findViewById(R.id.complain_list_item_order);
			holder.shop = (TextView) convertView.findViewById(R.id.complain_list_item_shop);
			holder.content = (TextView) convertView.findViewById(R.id.complain_list_item_content);
			// holder.state = (TextView)
			// convertView.findViewById(R.id.complain_list_item_state);
			holder.cancle = (TextView) convertView.findViewById(R.id.complain_list_item_no);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cancle.setOnClickListener(listener);
		holder.cancle.setTag(position);
		ComplainBeen been = list.get(position);
		holder.order.setText(been.order);
		holder.shop.setText(been.shop);
		holder.content.setText(been.content);
		// holder.state.setText(been.state);
		// if (position % 2 == 0) {
		// // holder.state.setText("未回复");
		// holder.state.setTextColor(color);
		// } else {
		// // holder.state.setText("已回复");
		// holder.state.setTextColor(color2);
		// }

		return convertView;
	}

	private static class ViewHolder {
		public TextView order, shop, content, cancle;
	}

}
