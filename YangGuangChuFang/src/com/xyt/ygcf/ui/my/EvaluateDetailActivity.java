package com.xyt.ygcf.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;

public class EvaluateDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_detail);
		setViewData();
	}

	private void setViewData() {
		setTitle("评论详情");
		final RatingBar bar1 = (RatingBar) findViewById(R.id.activity_evaluate_detail_recommend_rb);
		final TextView tvContent = (TextView) findViewById(R.id.activity_evaluate_detail_content);
		final TextView tvTime = (TextView) findViewById(R.id.activity_evaluate_detail_comment_time);

		final Intent intent = getIntent();
		bar1.setRating(Float.valueOf(intent.getStringExtra("rating")));
		tvContent.setText(intent.getStringExtra("content"));
		tvTime.setText(intent.getStringExtra("time"));
		// tvReplyContent = (TextView)
		// findViewById(R.id.activity_evaluate_detail_reply_content);
		// tvReplyTime = (TextView)
		// findViewById(R.id.activity_evaluate_detail_reply_time);
	}

}
