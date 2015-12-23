package com.xyt.ygcf.more;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.mapcore.s;
import com.amap.api.mapcore.w;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.BaseUtil;

/***
 * 食堂反馈
 * 
 * @author Administrator
 * 
 */
public class RefectoryFeedBackActivity extends BaseActivity {
	private TextView Refectoryname; /*PutUpText, PutDownText;*/
	private ImageButton PutUp, PutDown;
	private Button submitBtn;
	private EditText refectoryEdit;
	private RadioButton putUp, putDown;
	
	private String name;
	private String id;
	
	private int isGood;
	
	/** 食堂反馈 */
	public final static int REFECTORY_FEEDBACK = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refectory_feedback);
		
		getIntentData();
		initWidget();
	}
	
	private void getIntentData(){
		name = getIntent().getStringExtra("name");
		id = getIntent().getStringExtra("id");
	}

	private void initWidget() {
		submitBtn = (Button) findViewById(R.id.refectory_submit_btn);
		putUp = (RadioButton) findViewById(R.id.put_up);
		putDown = (RadioButton) findViewById(R.id.put_down);
		
		refectoryEdit = (EditText) findViewById(R.id.refectory_edit);
		Refectoryname = (TextView) findViewById(R.id.refectory_name);
		
		putDown.setOnClickListener(this);
		putUp.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		Refectoryname.setText(name);
		setTitle("反馈");
	}
	
	/**
	 * 是否选中
	 * @return
	 */
	public void isChecked(){
		if(putUp.isChecked()){
			isGood = 1;
		}else if(putDown.isChecked()){
			isGood = 0;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.put_up:
			putUp.setText("已赞");
			putDown.setText("踩一下");
			break;
		case R.id.put_down:
			putDown.setText("已踩");
			putUp.setText("赞一下");
			break;
		case R.id.refectory_submit_btn:
			isChecked();
			if(putUp.isChecked() || putDown.isChecked()){
				if(!TextUtils.isEmpty(getEtFeedBackBoxText())){
					refectoryFeedBackRequest(id, getEtFeedBackBoxText(), isGood);
				}else {
					Toast.makeText(this, "反馈内容不能为空!", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(this, "您还没有点赞呢！", Toast.LENGTH_SHORT).show();
			}
			break;

		}
	}
	
	/**
	 * 获取编辑框的文本  
	 * 
	 * @return 返回编辑框中的文本
	 */
	private String getEtFeedBackBoxText() {
		return refectoryEdit.getText().toString().trim();
	}
	
	/**
	 * 提交食堂反馈
	 * @param merchantId
	 * @param sessionId
	 * @param content
	 * @param isGood
	 */
	public void refectoryFeedBackRequest(String merchantId, String content, int isGood){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("merchantId", merchantId);
		params.addQueryStringParameter("content", content);
		params.addQueryStringParameter("isGood", String.valueOf(isGood));
		sendRequest(HttpMethod.GET, UrlMy.REFECTORY_FEEDBACK, params, REFECTORY_FEEDBACK, true);
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		if(which == REFECTORY_FEEDBACK){
			parseFeedBack(json);
		}
	}

	private void parseFeedBack(String json) {
		Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
		setResult(RESULT_CANCELED);
		finish();
	}

}
