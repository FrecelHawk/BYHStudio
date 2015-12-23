package com.xyt.ygcf.ui.my;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.urls.UrlMy;

public class EvaluateFeedBackActivity extends BaseActivity {

	private TextView tvName, tvContent, tvTime, tvReplyContent;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_feedback);
		findViewsById();
		setViewData();
	}

	private void findViewsById() {
		findViewById(R.id.activity_evaluate_feedback_name_layout).setVisibility(View.GONE);
		tvName = (TextView) findViewById(R.id.activity_evaluate_feedback_name);
		tvContent = (TextView) findViewById(R.id.activity_evaluate_feedback_content);
		tvTime = (TextView) findViewById(R.id.activity_evaluate_feedback_comment_time);
		tvReplyContent = (TextView) findViewById(R.id.activity_evaluate_feedback_reply_content);
		imageView = (ImageView) findViewById(R.id.activity_evaluate_feedback_img);
		// tvReplyTime = (TextView)
		// findViewById(R.id.activity_evaluate_feedback_reply_time);
	}

	private void setViewData() {
		setTitle("反馈详情");
		final RequestParams params = new RequestParams();
		final String id = getIntent().getStringExtra("id");
		params.addQueryStringParameter("id", id);
		sendRequest(UrlMy.EVALUATE_DETAIL, params, 0, true);
	}

	@Override
	public void handleJson(String json, int which) {
		JSONObject object = null;
		try {
			object = new JSONObject(json);
		} catch (JSONException e) {
			handleParseJsonException(which);
			super.onBackPressed();
			e.printStackTrace();
		}
		if (object != null) {
			tvName.setText(object.optString("merchantName"));
			final int isGood = object.optInt("isGood", 1);
			if (isGood == 0) {
				imageView.setImageResource(R.drawable.ic_evaluate_feedback_bad);
			}
			tvContent.setText(object.optString("content"));
			tvTime.setText(object.optString("createDt"));

			final String reply = object.optString("reply");
			if (TextUtils.isEmpty(reply)) {
				findViewById(R.id.activity_evaluate_feedback_reply_layout).setVisibility(View.GONE);
			} else {
				tvReplyContent.setText(reply);
			}
			findViewById(R.id.activity_evaluate_feedback_name_layout).setVisibility(View.VISIBLE);
		}
	}

}
