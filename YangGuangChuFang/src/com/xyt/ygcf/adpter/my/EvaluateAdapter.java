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
import com.xyt.ygcf.entity.my.EvaluateBeen;

public class EvaluateAdapter extends MyCommonListAdapter<EvaluateBeen> {

	public EvaluateAdapter(Context context, List<EvaluateBeen> list, OnClickListener listener, BitmapUtils bitmapUtils) {
		super(context, list, listener, bitmapUtils);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.evaluate_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.evaluate_item_img);
			holder.name = (TextView) convertView.findViewById(R.id.evaluate_item_name);
			holder.delete = convertView.findViewById(R.id.common_my_list_item_delete);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.evaluate_item_score);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		EvaluateBeen been = (EvaluateBeen) list.get(position);
		showDeleteView(holder.delete, been.showDelete, position);

		bitmapUtils.display(holder.image, been.photoUrl);
		holder.name.setText(been.name);
		holder.ratingBar.setRating(Float.valueOf(been.recommendLevel));

		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView name;
		public RatingBar ratingBar;
		public View delete;
	}

}
