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
import com.xyt.ygcf.entity.my.EvaluateFeedBackBeen;

public class EvaluateFeedBackAdapter extends MyCommonListAdapter<EvaluateFeedBackBeen> {

	public EvaluateFeedBackAdapter(Context context, List<EvaluateFeedBackBeen> list, OnClickListener listener,
			BitmapUtils bitmapUtils) {
		super(context, list, listener, bitmapUtils);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.evaluate_feedback_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.evaluate_feedback_item_img);
			holder.name = (TextView) convertView.findViewById(R.id.evaluate_feedback_item_name);
			holder.type = (TextView) convertView.findViewById(R.id.evaluate_feedback_item_type);
			holder.intro = (TextView) convertView.findViewById(R.id.evaluate_feedback_item_intro);
			holder.delete = convertView.findViewById(R.id.common_my_list_item_delete);
			// holder.news = (ImageView)
			// convertView.findViewById(R.id.evaluate_feedback_item_new);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		EvaluateFeedBackBeen been = (EvaluateFeedBackBeen) list.get(position);
		showDeleteView(holder.delete, been.showDelete, position);
		bitmapUtils.display(holder.image, been.photoUrl);
		holder.name.setText(been.name);
		holder.type.setText(been.type);
		holder.intro.setText(been.content);

		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView name, type, intro;
		public View delete;
	}

}
