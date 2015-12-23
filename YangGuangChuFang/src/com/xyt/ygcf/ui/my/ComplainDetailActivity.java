package com.xyt.ygcf.ui.my;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.urls.UrlMy;

public class ComplainDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain_detail);
		findViewById(R.id.complaint_header_detail_container).setVisibility(View.GONE);
		setViewsData();
	}

	private void setViewsData() {
		setTitle("投诉详情");
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", getIntent().getStringExtra("id"));
		sendRequest(UrlMy.COMPLAINT_DETAIL, params, 0, true);
	}

	@Override
	public void handleJson(String json, int which) {
		JSONObject object = null;
		try {
			object = new JSONObject(json);
		} catch (JSONException e) {
			super.handleParseJsonException(which);
			e.printStackTrace();
		}

		if (object != null) {
			final TextView tvOrder = (TextView) findViewById(R.id.complaint_header_detail_order);
			final TextView tvTime = (TextView) findViewById(R.id.complaint_header_detail_time);
			final TextView tvShopName = (TextView) findViewById(R.id.complaint_header_detail_shopname);
			final TextView tvSubject = (TextView) findViewById(R.id.complaint_header_detail_theme);
			final TextView tvContent = (TextView) findViewById(R.id.complaint_header_detail_content);
			final TextView tvReplayTime = (TextView) findViewById(R.id.complain_detail_item_time);
			final TextView tvReplyId = (TextView) findViewById(R.id.complain_detail_item_id);
			final TextView tvReplyContent = (TextView) findViewById(R.id.complain_detail_item_content);

			tvOrder.setText(object.optString("no"));
			tvTime.setText(object.optString("createDt"));
			tvShopName.setText(object.optString("shopName"));
			tvSubject.setText(object.optString("subject"));
			tvContent.setText(object.optString("content"));

			JSONArray array = object.optJSONArray("comReverts");
			if (array != null && array.length() > 0) {
				JSONObject item = array.optJSONObject(0);
				tvReplayTime.setText(item.optString("createDt"));
				tvReplyId.setText(item.optString("qindf"));
				tvReplyContent.setText(item.optString("content"));
			} else {
				findViewById(R.id.complaint_header_detail_reply_layout1).setVisibility(View.GONE);
				findViewById(R.id.complaint_header_detail_reply_layout2).setVisibility(View.GONE);
			}

			findViewById(R.id.complaint_header_detail_container).setVisibility(View.VISIBLE);
		}

	}

}
