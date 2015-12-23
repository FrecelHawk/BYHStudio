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
import com.xyt.ygcf.entity.my.CollectionMenuBeen;

public class CollectionMenuAdapter extends MyCommonListAdapter<CollectionMenuBeen> {

	public CollectionMenuAdapter(Context context, List<CollectionMenuBeen> list, OnClickListener listener,
			BitmapUtils bitmapUtils) {
		super(context, list, listener, bitmapUtils);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.collection_menu_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.collection_menu_item_img);
			holder.delete = convertView.findViewById(R.id.common_my_list_item_delete);
			holder.name = (TextView) convertView.findViewById(R.id.collection_menu_item_name);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.collection_menu_item_score);
			holder.price = (TextView) convertView.findViewById(R.id.collection_menu_item_price);
			holder.shopName = (TextView) convertView.findViewById(R.id.collection_menu_item_shopname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CollectionMenuBeen been = (CollectionMenuBeen) list.get(position);
		showDeleteView(holder.delete, been.showDelete, position);

		bitmapUtils.display(holder.image, been.photoUrl);
		holder.name.setText(been.name);
		holder.ratingBar.setRating(Float.valueOf(been.recommendLevel));
		holder.price.setText(been.price);
		holder.shopName.setText(been.shopName);
		been = null;
		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView name, price, shopName;
		public RatingBar ratingBar;
		public View delete;
	}
}
