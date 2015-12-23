package com.baiyuhui.huangyan.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.utils.ShareUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 发现/故事详情
 * 
 * @author Administrator
 * 
 */
public class FindStoryDetails extends Activity implements OnClickListener {

	private TextView title;
	private int id;
	private WebView mWebView;
	private String wTitle;
	private String cDate;
	private TextView storyTitle;
	private TextView storyTime;
	private ImageView rightImg;
	private View leftImg;

	public static final String DESCRIPTOR = "com.umeng.share";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_story_details);
		initViews();
		HttpGet();
	}

	private void initViews() {
		id = getIntent().getIntExtra("id", 0);
		wTitle = getIntent().getStringExtra("title");
		cDate = getIntent().getStringExtra("cdate");

		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("故事详情");
		title.setTextColor(getResources().getColor(R.color.white));
		title.setTextSize(17);

		rightImg = (ImageView) findViewById(R.id.aciont_bar_rigth);
		rightImg.setBackgroundResource(R.drawable.fenxiang);
		rightImg.setOnClickListener(this);

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		storyTitle = (TextView) findViewById(R.id.story_title);
		storyTitle.setText(wTitle);

		storyTime = (TextView) findViewById(R.id.story_time);
		storyTime.setText(cDate);

		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		mWebView.setWebChromeClient(new WebChromeClient());
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本  
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth:
			new ShareUtil(FindStoryDetails.this, mController, "", "", "");
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
			mController.openShare(FindStoryDetails.this, false);
			break;
		default:
			break;
		}
	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.NEWS_DETAILS + id, new TextHttpResponseHandler() {

			private BaseJsonBeen listDatas;

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				listDatas = JsonParse.parseAlone(arg2);

				StringBuilder sb = new StringBuilder();
				  sb.append("<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\">");
				    sb.append("<head>");
				       sb.append("<style type=text/css>");
				          sb.append("html{margin:0px;padding:0px;}");
				       sb.append("body {margn:0px;padding:0px;font-size: 13px; font-family: ; color: ;line-height: 24px}");
				     sb.append("<metaname='viewport'content='width=device-width,maximum-scale=2.5'>");
				 sb.append("</style> ");
				 sb.append("</head> ");
				 sb.append("<body>"+listDatas.getContent()+"<script type=text/javascript>var imgs=document.getElementsByTagName('img');for(var i=0;i<imgs.length;i++){imgs[i].style.maxWidth='100%%';}var ifms=document.getElementsByTagName('iframe');for(var i=0;i<ifms.length;i++){var ifm=ifms[i],w=parseFloat(ifm.getAttribute('width')),h=parseFloat(ifm.getAttribute('height')),p=h/w;ifm.removeAttribute('width');ifm.removeAttribute('height');varbdw=document.body.offsetWidth;ifm.width=bdw;ifm.height=parseFloat(bdw)*p;}</script></body> ");

				String str=sb.toString();

				mWebView.loadDataWithBaseURL(null, str,
						"text/html", "utf-8", null);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				System.out.println("请求异常----->");
			}
		});

	}
}
