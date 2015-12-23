package com.baiyuhui.huangyan.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.CircleDetailsAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.utils.ShareUtil;
import com.baiyuhui.huangyan.utils.UILManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 圈子/帖子详情
 * 
 * @author Administrator
 * 
 */
public class FindCircleDetails extends Activity implements OnClickListener {

	private TextView title;
	private WebView mWebView;

	private int mId;
	private ImageView leftImg;
	private ImageView rightImg;
	private LinearLayout layout;
	
	private List<BaseJsonBeen> listDatas;

	private ListView circle_detail_list;
	EditText edt;
	ImageView fashong;
	private ImageView sex, circle_details_iv;
	private TextView biaoti, riqi;

	public static final String DESCRIPTOR = "com.umeng.share";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_circle_details);
		initViews();
		HttpGet();
		getCommentList();
	}


	private void initViews() {
		/* 获取上个Activity传过来的参数 */
		mId = getIntent().getIntExtra("id", 0);

		/* 获取ID更改属性 */
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("帖子详情");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		rightImg = (ImageView) findViewById(R.id.aciont_bar_rigth);
		rightImg.setBackgroundResource(R.drawable.fenxiang);
		rightImg.setOnClickListener(this);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		// 定义自己的图片显示
		sex = (ImageView) findViewById(R.id.sex);
		circle_details_iv = (ImageView) findViewById(R.id.circle_details_iv);

		fashong = (ImageView) findViewById(R.id.fashong);
		edt = (EditText) findViewById(R.id.edt);

		biaoti = (TextView) findViewById(R.id.biaoti);
		riqi = (TextView) findViewById(R.id.riqi);
		
		circle_detail_list = (ListView) findViewById(R.id.circle_detail_list);
		CircleDetailsAdapter adapter = new CircleDetailsAdapter(
				FindCircleDetails.this, listDatas);
		circle_detail_list.setAdapter(adapter);

		// /* 加载一个WEB界面 */
		mWebView = (WebView) findViewById(R.id.circle_detail_webview);
		WebSettings webSettings = mWebView.getSettings();

		
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本

	    webSettings.setLoadWithOverviewMode(true); 
	        
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		
		mWebView.setWebChromeClient(new WebChromeClient());


		// 实例化对象
		leftImg.setOnClickListener(this);
		rightImg.setOnClickListener(this);

	}

	/* title点击事件 */
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.lay_left:
			finish(); // 结束当前Activity
			break;
		case R.id.fashong:
			// 回复楼主
			if (edt.getText().toString().equals("")) {
				Toast.makeText(FindCircleDetails.this, "回复楼主内容不能为空!", 2000)
						.show();
			} else {

			}
			break;
		case R.id.aciont_bar_rigth:
			/* 分享按钮 */
			new ShareUtil(FindCircleDetails.this, mController, "", "", "");
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
			mController.openShare(FindCircleDetails.this, false);
			break;
		default:
			break;
		}
	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("id", mId);
		client.get(MyUrl.GET_POST_TOP, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, org.apache.http.Header[] arg1,
					String arg2, Throwable arg3) {
				System.out.println("请求异常----->");
			}

			@Override
			public void onSuccess(int arg0, org.apache.http.Header[] arg1,
					String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				BaseJsonBeen jsonBean = JsonParse.parseSomeCircleDetail(arg2);

				sex = (ImageView) findViewById(R.id.sex);
				circle_details_iv = (ImageView) findViewById(R.id.circle_details_iv);

				biaoti = (TextView) findViewById(R.id.biaoti);
				riqi = (TextView) findViewById(R.id.riqi);

				biaoti.setText(jsonBean.getTit());
				riqi.setText(jsonBean.getCdate());
				if (jsonBean.getSex() == 1) {
					sex.setBackgroundResource(R.drawable.boy);
				} else if (jsonBean.getSex() == 2) {
					sex.setBackgroundResource(R.drawable.girl);
				}

				UILManager.with(FindCircleDetails.this).load(jsonBean.getImg())
						.into(circle_details_iv);

				StringBuilder sb = new StringBuilder();
				  sb.append("<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\">");
				    sb.append("<head>");
				       sb.append("<style type=text/css>");
				          sb.append("html{margin:0px;padding:0px;}");
				       sb.append("body {margn:0px;padding:0px;font-size: 13px; font-family: ; color: ;line-height: 24px}");
				     sb.append("<metaname='viewport'content='width=device-width,maximum-scale=2.5'>");
				 sb.append("</style> ");
				 sb.append("</head> ");
				 sb.append("<body>"+jsonBean.getContent()+"<script type=text/javascript>var imgs=document.getElementsByTagName('img');for(var i=0;i<imgs.length;i++){imgs[i].style.maxWidth='100%%';}var ifms=document.getElementsByTagName('iframe');for(var i=0;i<ifms.length;i++){var ifm=ifms[i],w=parseFloat(ifm.getAttribute('width')),h=parseFloat(ifm.getAttribute('height')),p=h/w;ifm.removeAttribute('width');ifm.removeAttribute('height');varbdw=document.body.offsetWidth;ifm.width=bdw;ifm.height=parseFloat(bdw)*p;}</script></body> ");

				String str=sb.toString();
				
				mWebView.loadDataWithBaseURL(null, sb.toString(),
						"text/html", "utf-8", null);
			}
		});

	}

	private void getCommentList() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("postid", mId);
		params.put("page", String.valueOf(1));
		client.get(MyUrl.CIRCLE_CLASS_DETAILS, params,
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0,
							org.apache.http.Header[] arg1, String arg2,
							Throwable arg3) {
						System.out.println("请求异常----->");
					}

					@Override
					public void onSuccess(int arg0,
							org.apache.http.Header[] arg1, String arg2) {
						System.out.println("请求回来的参数----->" + arg2);
						
						List<BaseJsonBeen> getpost_replys = JsonParse
								.parseCommentList(arg2);
						for (int i = 0; i < getpost_replys.size(); i++) {
							getpost_replys.get(i).getId();
							List<BaseJsonBeen> beens = getpost_replys.get(i)
									.getList();
						}
					}
				});

	}

}
