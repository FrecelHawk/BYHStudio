package com.xyt.ygcf.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.more.CommentDetailsItem;

public class CommentFeedBackAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public List<CommentDetailsItem> detailsItems;
	private boolean isShow;
	private BitmapUtils bitmapUtils;
	
	public CommentFeedBackAdapter (Context context, List<CommentDetailsItem> arrayList, boolean isShow){
		this.inflater = LayoutInflater.from(context);
		this.detailsItems=arrayList;
		this.isShow = isShow;
		this.bitmapUtils = new BitmapUtils(context);
		this.bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
		this.bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		if(isShow == true){
			if(detailsItems.size() > 3){
				return 3;
			}else {
				return detailsItems.size();
			}
		}else {
			return detailsItems.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		return detailsItems.get(arg0);
	}
	

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
			if (arg1 == null) {
				holder = new ViewHolder();
				arg1 =  inflater.inflate(R.layout.comment_feedback, null);
				holder.feedBackImage = (ImageView) arg1.findViewById(R.id.comment_feedback_user_image);
				holder.feedBackName = (TextView) arg1.findViewById(R.id.comment_feedback_user_name);
				holder.feedBackPraise = (ImageView) arg1.findViewById(R.id.comment_feedback_praise);
				holder.feedBackContent=(TextView) arg1.findViewById(R.id.comment_feedback_content);
				holder.feedBackTime = (TextView) arg1.findViewById(R.id.comment_feedback_time);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			CommentDetailsItem detailsItem = detailsItems.get(arg0);
			if(detailsItem != null){
				bitmapUtils.display(holder.feedBackImage, detailsItem.imageView);
				holder.feedBackName.setText(detailsItem.commentName);
				holder.feedBackContent.setText(detailsItem.commentContent);
				holder.feedBackTime.setText("("+detailsItem.commentTime+")");
				if(detailsItem.commentPraise == 0){
					holder.feedBackPraise.setImageResource(R.drawable.some_collapse);
				}else {
					holder.feedBackPraise.setImageResource(R.drawable.some_praise);
				}
			}
		return arg1;
	}
	class ViewHolder {
		ImageView feedBackImage, feedBackPraise;
		TextView feedBackContent, feedBackTime, feedBackName;
	}
}
