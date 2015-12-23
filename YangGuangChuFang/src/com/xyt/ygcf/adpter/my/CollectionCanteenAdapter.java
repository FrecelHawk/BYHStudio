package com.xyt.ygcf.adpter.my;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.my.CollectionCanteenBeen;

public class CollectionCanteenAdapter extends MyCommonListAdapter<CollectionCanteenBeen> {

	public CollectionCanteenAdapter(Context context, List<CollectionCanteenBeen> list, OnClickListener listener,
			BitmapUtils bitmapUtils) {
		super(context, list, listener, bitmapUtils);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.collection_canteen_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.collection_canteen_item_img);
			holder.delete = convertView.findViewById(R.id.common_my_list_item_delete);
			holder.name = (TextView) convertView.findViewById(R.id.collection_canteen_item_name);
			holder.type = (TextView) convertView.findViewById(R.id.collection_canteen_item_type);
			holder.address = (TextView) convertView.findViewById(R.id.collection_canteen_item_address);
			holder.distance = (TextView) convertView.findViewById(R.id.collection_canteen_item_distance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CollectionCanteenBeen been = (CollectionCanteenBeen) list.get(position);
		showDeleteView(holder.delete, been.showDelete, position);
		bitmapUtils.display(holder.image, been.photoUrl);
		holder.name.setText(been.name);
		holder.type.setText(been.type);
		holder.address.setText(been.address);
//		holder.distance.setText(been.distance);
		setDistance(holder.distance, been.distance);
		been = null;
		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView name, type, address, distance;
		public View delete;
	}

}
