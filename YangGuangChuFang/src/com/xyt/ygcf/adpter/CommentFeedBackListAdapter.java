package com.xyt.ygcf.adpter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.more.CommentDetailsItem;

public class CommentFeedBackListAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	public List<CommentDetailsItem> detailsItems;
	public int mark;
	private BitmapUtils bitmapUtils;
	
	public CommentFeedBackListAdapter(Context context,List<CommentDetailsItem> detailsItems, int mark){
		this.detailsItems = detailsItems;
		this.inflater = LayoutInflater.from(context);
		this.mark = mark;
		this.bitmapUtils = new BitmapUtils(context);
		this.bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
		this.bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		return detailsItems ==null?0:detailsItems.size();
	}

	@Override
	public Object getItem(int position) {
		return detailsItems.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Hondler hondler = null;
		if(view == null){
			view =  inflater.inflate(R.layout.comment_or_feedback_list_item, null);
			hondler = new Hondler();
			hondler.commentIcon = (ImageView) view.findViewById(R.id.comment_icon);
			hondler.commentUserName = (TextView) view.findViewById(R.id.comment_username);
			hondler.commentTime = (TextView) view.findViewById(R.id.comment_time);
			hondler.commentZan = (ImageView) view.findViewById(R.id.comment_zan);
			hondler.rlDuiJian = (RelativeLayout) view.findViewById(R.id.rl_tuijian);
			hondler.ratBar = (RatingBar) view.findViewById(R.id.comment_ratbar);
			hondler.commentContext = (TextView) view.findViewById(R.id.comment_conttext);
			view.setTag(hondler);
		}else {
			hondler = (Hondler) view.getTag();
		}
		if(mark == 2){
			hondler.commentZan.setVisibility(View.VISIBLE);
			hondler.rlDuiJian.setVisibility(View.GONE);
		}
		CommentDetailsItem commentBean= detailsItems.get(position);
		bitmapUtils.display(hondler.commentIcon, commentBean.imageView);
		hondler.commentUserName.setText(commentBean.commentName);
		hondler.commentTime.setText(commentBean.commentTime);
		hondler.commentContext.setText(commentBean.commentContent);
		if(!TextUtils.isEmpty(commentBean.commentGrade)){
			hondler.ratBar.setRating(Float.valueOf(commentBean.commentGrade));
		}
		if(commentBean.commentPraise == 0){
			hondler.commentZan.setImageResource(R.drawable.icon_weak);
		}else {
			hondler.commentZan.setImageResource(R.drawable.icon_zan);
		}
		return view;
	}
	
	private class Hondler{
		ImageView commentIcon;
		TextView commentUserName;
		TextView commentTime;
		ImageView commentZan;
		RelativeLayout rlDuiJian;
		RatingBar ratBar;
		TextView commentContext;
		
	}


}
