package com.baiyuhui.huangyan.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
 * 首页/视频详情
 * 
 * @author Administrator
 * 
 */
public class HomeVideoDetails extends Activity implements OnClickListener {

	private TextView title;

	private String tit;
	private String cdate;
	private TextView mTit;
	private TextView mTiem;
	private int id;
	private ImageView rightImg;
	private View leftImg;
	private LinearLayout layout;

	private BaseJsonBeen listDatas;

	public static final String DESCRIPTOR = "com.umeng.share";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

	private WebView mWebView;

	private WebView details_web;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_vido_details);
		initViews();
		HttpGet();
		
	}

	private void initViews() {
		id = getIntent().getIntExtra("id", 0);
		url = getIntent().getStringExtra("url");
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("视频详情");	
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		rightImg = (ImageView) findViewById(R.id.aciont_bar_rigth);
		rightImg.setBackgroundResource(R.drawable.fenxiang);
		rightImg.setOnClickListener(this);

		mTit = (TextView) findViewById(R.id.tit);
		mTit.setText(tit);
		mTiem = (TextView) findViewById(R.id.tiem);
		mTiem.setText(cdate);

		mWebView = (WebView) findViewById(R.id.webview);
		details_web = (WebView) findViewById(R.id.details_web);
		
		initWebView();

	}
	
	private void initWebView() {  
        WebSettings settings = details_web.getSettings();  
        settings.setJavaScriptEnabled(true);  
        settings.setJavaScriptCanOpenWindowsAutomatically(true);  
        settings.setPluginState(PluginState.ON);  
        // settings.setPluginsEnabled(true);  
        settings.setAllowFileAccess(true);  
        settings.setLoadWithOverviewMode(true);  
        details_web.setWebChromeClient(new WebChromeClient());
  
    }  
	
	class MyWebChromeClient extends WebChromeClient {  
		  
        private CustomViewCallback mCustomViewCallback;  
        private int mOriginalOrientation = 1;  
        


  
        @Override  
        public void onShowCustomView(View view, CustomViewCallback callback) {  
            // TODO Auto-generated method stub  
            
            super.onShowCustomView(view, callback);  
  
        }  
  
        public void onShowCustomView(View view, int requestedOrientation,  
                WebChromeClient.CustomViewCallback callback) {  
        	onShowCustomView(view, mOriginalOrientation, callback);  
            if (details_web != null) {  
                callback.onCustomViewHidden();  
                return;  
            }  
  
        }  
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			details_web.loadData("", "text/html; charset=UTF-8", null);
			finish();
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		details_web.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		details_web.onResume();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth:
			new ShareUtil(HomeVideoDetails.this, mController, "", "", "");
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
			mController.openShare(HomeVideoDetails.this, false);
			break;
		default:
			break;

		}
	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.NEWS_DETAILS + id, new TextHttpResponseHandler() {


			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				listDatas = JsonParse.parseAlone(arg2);
				mTit.setText(listDatas.getTit());
				mTiem.setText(listDatas.getCdate());
				
//				String vid = url.substring(url.lastIndexOf("/")+1);Uri.encode(vid);
//				"[domain]/playvideo.htm?target=youku&vid=[vid]&auto=1"
//				
//				"<html>" +
//				"<body>" +
//				<div>
//					<iframe src='url' style='width:100%;height:500px'><iframe>
//					</div>
//					<div>
//					[content]
//					</div>
//				"</body>" +
//				"</html>"
				
				details_web.loadUrl(url);
				mWebView.loadDataWithBaseURL(null, listDatas.getContent(),
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
