package com.xyt.ygcf.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.yangguangchufang.R.id;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.urls.UrlMy;

public class VioaltionContentActivity extends BaseActivity {
	
	private TextView title;
	private TextView content;
	private TextView time;
	private TextView mode;
	private TextView deadline;
	private TextView createTime;
	
	//违规id
	private String id;
	//违规公示内容
	private final static int ANNOUNCEMENT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vioaltion_content);
		getIntentData();
		initWidget();
		announcementRequest(id);
	}
	
	public void initWidget(){
		title = (TextView) findViewById(R.id.publicity_content_title);
		content = (TextView) findViewById(R.id.publicity_content);
		time = (TextView) findViewById(R.id.publicity_content_time);
		mode = (TextView) findViewById(R.id.publicity_content_mode);
		deadline = (TextView) findViewById(R.id.publicity_content_deadline);
		createTime = (TextView) findViewById(R.id.publicity_content_create_time);
		setTitle("违规公示内容");
	}

	private void getIntentData(){
		id = getIntent().getStringExtra("id");
	}
	
	public void announcementRequest(String announcementId){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("announcementId", announcementId);
		sendRequest(HttpMethod.GET, UrlMy.INQUIRE_GOVERNMENT_NOTICE, params, ANNOUNCEMENT, true);
	}

	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		try {
			if(which == ANNOUNCEMENT){
			   JSONObject object = new JSONObject(json);
			   if(object != null){
				   title.setText(object.optString("title"));
				   content.setText(object.optString("违规类别："+"content"));
				   time.setText(object.optString("title"));
				   mode.setText(object.optString("title"));
				   deadline.setText(object.optString("updateDt"));
				   createTime.setText(object.optString("createDt"));
				   
			   }
			}
		} catch (JSONException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}

