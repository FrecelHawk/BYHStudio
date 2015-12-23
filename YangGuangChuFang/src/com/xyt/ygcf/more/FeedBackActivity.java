package com.xyt.ygcf.more;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.ui.my.RegisterActivity;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.BaseUtil;
import com.xyt.ygcf.util.HttpTool;
import com.xyt.ygcf.util.PatternUtil;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.util.StringUtils;

/**
 * 意见反馈
 * 
 * @author lenovo
 * 
 */
public class FeedBackActivity extends BaseActivity {
	public EditText contactMess, contentEidt;
	public Button submitFeedback;
	public ImageView setting_Btn;
	public Calendar calendar;

	/** 意见反馈 */
	private final static int FEEDBACK = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_feedback);
		getView();
	}

	public void getView() {
		contactMess = (EditText) findViewById(R.id.contact_mess);
		contentEidt = (EditText) findViewById(R.id.content_eidt);
		submitFeedback = (Button) findViewById(R.id.submit_feedback);
		setTitle("意见反馈");
		submitFeedback.setOnClickListener(this);
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.submit_feedback:
			if(!getEtFeedBackBoxText().equals("") && !getEtMessBoxText().equals("")){
				if(!PatternUtil.isEmail(getEtMessBoxText()) && 
						!PatternUtil.isMobile(getEtMessBoxText())){
					Toast.makeText(this, "输入的联系方式有误！", Toast.LENGTH_SHORT).show();
				}else {
					feedbackRequest(getEtFeedBackBoxText(), getEtMessBoxText());
				}
			}else {
				if(getEtFeedBackBoxText().equals("")){
					Toast.makeText(this, "反馈内容不能为空！", Toast.LENGTH_SHORT).show();
				}else if(getEtMessBoxText().equals("")){
					Toast.makeText(this, "联系方式不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	/**
	 * 获取联系编辑框的文本
	 * 
	 * @return 返回编辑框中的文本
	 */
	private String getEtMessBoxText() {
		return contactMess.getText().toString().trim();
	}
	
	/**
	 * 获取反馈编辑框的文本
	 * 
	 * @return 返回编辑框中的文本
	 */
	private String getEtFeedBackBoxText() {
		return contentEidt.getText().toString().trim();
	}
	
	/**
	 * 意见反馈
	 * @param content
	 */
	public void feedbackRequest(String content, String mess) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("content", content);
		if(StringUtils.isNumeric(mess)){
			params.addQueryStringParameter("mobile", mess);
		}else {
			params.addQueryStringParameter("email", mess);
		}
		sendRequest(HttpMethod.GET, UrlMy.FEED_BACK, params, FEEDBACK, true);
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		if (which == FEEDBACK) {
			System.out.println("========="+json);
			Toast.makeText(this, "反馈成功", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

}
