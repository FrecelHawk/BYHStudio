package com.xyt.ygcf.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.more.CommentDetailsItem;

public class CommentDetailsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	public List<CommentDetailsItem> detailsItems;
	private BitmapUtils bitmapUtils;
	private boolean isShow;
	
	public CommentDetailsAdapter (Context context, List<CommentDetailsItem> arrayList, boolean isShow){
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
				arg1 =  inflater.inflate(R.layout.comment_item, null);
				holder.userImage=(ImageView) arg1.findViewById(R.id.userimage);
				holder.userName = (TextView) arg1.findViewById(R.id.username);
				holder.commentCotent=(TextView) arg1.findViewById(R.id.comment_content);
				holder.commentGrade = (RatingBar) arg1.findViewById(R.id.comment_grade);
				holder.commentTime = (TextView) arg1.findViewById(R.id.comment_time);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			CommentDetailsItem detailsItem = detailsItems.get(arg0);
			if(detailsItem!=null){
				bitmapUtils.display(holder.userImage, detailsItem.imageView);
				holder.userName.setText(detailsItem.commentName);
				holder.commentCotent.setText(detailsItem.commentContent);
				holder.commentTime.setText("("+detailsItem.commentTime+")");
				if(TextUtils.isEmpty(detailsItem.commentGrade)){
					holder.commentGrade.setRating(0);
				}else {
					holder.commentGrade.setRating((float)Integer.valueOf(detailsItem.commentGrade));
				}
			}
		return arg1;
	}
	class ViewHolder {
		ImageView userImage;
		RatingBar commentGrade;
		TextView commentCotent, commentTime, userName;
	}
}
