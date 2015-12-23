package com.xyt.ygcf.adpter.my;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.my.CollectionRestaurantBeen;

public class CollectionRestaurantAdapter extends MyCommonListAdapter<CollectionRestaurantBeen> {

	public CollectionRestaurantAdapter(Context context, List<CollectionRestaurantBeen> list, OnClickListener listener,
			BitmapUtils bitmapUtils) {
		super(context, list, listener, bitmapUtils);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.collection_restaurant_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.collection_restaurant_item_img);
			holder.delete = convertView.findViewById(R.id.common_my_list_item_delete);
			holder.restaurant = (TextView) convertView.findViewById(R.id.collection_restaurant_item_restaurant);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.collection_restaurant_item_score);
			holder.type = (TextView) convertView.findViewById(R.id.collection_restaurant_item_type);
			holder.address = (TextView) convertView.findViewById(R.id.collection_restaurant_item_address);
			holder.distance = (TextView) convertView.findViewById(R.id.collection_restaurant_item_distance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CollectionRestaurantBeen been = (CollectionRestaurantBeen) list.get(position);
		showDeleteView(holder.delete, been.showDelete, position);

		bitmapUtils.display(holder.image, been.photoUrl);
		holder.restaurant.setText(been.restaurant);
		holder.ratingBar.setRating(been.score);
		holder.type.setText(been.type);
		holder.address.setText(been.address);
//		holder.distance.setText(been.distance);
		setDistance(holder.distance, been.distance);
		been = null;
		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView restaurant, type, address, distance;
		public RatingBar ratingBar;
		public View delete;
	}
}
