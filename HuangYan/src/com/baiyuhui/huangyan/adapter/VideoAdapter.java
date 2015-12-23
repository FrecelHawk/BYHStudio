package com.baiyuhui.huangyan.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.HomeVideoActivity;
import com.baiyuhui.huangyan.activity.HomeVideoDetails;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.utils.ShareUtil;
import com.baiyuhui.huangyan.utils.UILManager;
import com.baiyuhui.huangyan.view.MyWebView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 首页/视频
 * 
 * @author Administrator
 * 
 */
public class VideoAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Activity activity;
	private Context mContext;
	private List<BaseJsonBeen> lists ;
	
	public static final String DESCRIPTOR = "com.umeng.share";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

	public VideoAdapter(Activity activity, List<BaseJsonBeen> listDatas) {
		super();
		this.mInflater = LayoutInflater.from(activity);
		this.mContext = activity;
		this.activity = activity;
		this.lists = listDatas;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setLists(List<BaseJsonBeen> lists){
		this.lists = lists;
	}
	
	public void addAllLists(List<BaseJsonBeen> lists){
		this.lists.addAll(lists);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.home_item_list_video, null);
			holder.tit = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.texttiem);
			holder.web_img = (ImageView) convertView.findViewById(R.id.web_img);
			
//			holder.webView = (MyWebView) convertView.findViewById(R.id.web_flash);
//			
//			holder.webView.getSettings().setJavaScriptEnabled(true);
////			details_web.getSettings().setPluginState(PluginState.ON);
////			details_web.getSettings().setPluginsEnabled(true);//可以使用插件
//			holder.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//			holder.webView.getSettings().setAllowFileAccess(true);
//			holder.webView.getSettings().setDefaultTextEncodingName("UTF-8");
//			holder.webView.getSettings().setLoadWithOverviewMode(true);
//			holder.webView.getSettings().setUseWideViewPort(true);
//			holder.webView.setWebChromeClient(new WebChromeClient());
			
			
			holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
			holder.imageView2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new ShareUtil(activity , mController, "", "", "");
					mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
							SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
							SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
					mController.openShare(activity, false);
				}
			});
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UILManager.with(mContext).load(lists.get(position).getIco())
		.into(holder.web_img);
		
//		holder.webView.loadUrl(lists.get(position).getUrl());
		holder.tit.setText(lists.get(position).getTit());
		holder.time.setText(lists.get(position).getCdate());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(mContext, HomeVideoDetails.class);
				it.putExtra("id",lists.get(position).id);
				it.putExtra("title", lists.get(position).tit);
				it.putExtra("cdate", lists.get(position).cdate);
				it.putExtra("url", lists.get(position).url);
				mContext.startActivity(it);
			}
		});
		return convertView;
	}

	class ViewHolder {
//		MyWebView webView;
		ImageView web_img;
		ImageView imageView2;
		TextView tit;
		TextView time;
	}

}
