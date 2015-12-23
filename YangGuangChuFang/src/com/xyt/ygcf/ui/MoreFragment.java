package com.xyt.ygcf.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseFragment;
import com.xyt.ygcf.more.AcquiesceCity;
import com.xyt.ygcf.more.FeedBackActivity;
import com.xyt.ygcf.more.MoreAboutUs;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.BaseUtil;
import com.xyt.ygcf.util.DataCleanManager;
import com.xyt.ygcf.util.DeviceHelper;
import com.xyt.ygcf.util.DialogUtil;
import com.xyt.ygcf.util.ToastUtil;

public class MoreFragment extends BaseFragment implements OnClickListener {
	private RelativeLayout logout_Btn;
	private LinearLayout Telephonenamber;
	private FragmentActivity mActivity;
	private LinearLayout Dissident, Update, Aboutus, Clearcache, Acquiescecity;
	private View view;

	/** 检查更新 */
	private final static int VERSIONCHECK = 1;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_more, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		Dissident = (LinearLayout) view.findViewById(R.id.dissident);
		Update = (LinearLayout) view.findViewById(R.id.update);
		Aboutus = (LinearLayout) view.findViewById(R.id.aboutus);
		Telephonenamber = (LinearLayout) view.findViewById(R.id.telephonenamber);
		Clearcache = (LinearLayout) view.findViewById(R.id.clearcache);
		Acquiescecity = (LinearLayout) view.findViewById(R.id.acquiescecity);
		Dissident.setOnClickListener(this);
		Update.setOnClickListener(this);
		Telephonenamber.setOnClickListener(this);
		Clearcache.setOnClickListener(this);
		Acquiescecity.setOnClickListener(this);
		Aboutus.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.dissident: //意见反馈
			startActivity(new Intent(mActivity, FeedBackActivity.class));
			break;
		case R.id.update: //版本更新
			checkVersionRequst();
			break;
		case R.id.aboutus: //关于我们
			Intent intent2 = new Intent(mActivity, MoreAboutUs.class);
			startActivity(intent2);
			break;
		case R.id.telephonenamber: 
			Uri uri = Uri.parse("tel:4008302727");
			Intent intent1 = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent1);
			break;
		case R.id.clearcache: //清除缓存
			DataCleanManager.cleanApplicationData(mActivity);
			break;
		case R.id.acquiescecity: //默认城市
			// 默认城市
			Intent intent3 = new Intent(mActivity, AcquiesceCity.class);
			startActivity(intent3);
			break;
		}
	}

	// 检测新版本
	private void checkVersionRequst() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("versionNo", DeviceHelper.getVersionName(mActivity));
		params.addQueryStringParameter("clientType", "1");
		sendRequest(HttpMethod.GET, UrlMy.VERSION_CHECK, params, VERSIONCHECK, true);
	}

	@Override
	public void handleJson(String json, int which) {
		// 版本更新
		try {
			if (which == VERSIONCHECK) {
				parseVersion(json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * 解析版本更新
	 * @param json
	 * @throws JSONException 
	 */
	private void parseVersion(String json) throws JSONException {
		JSONObject object = new JSONObject(json);
		if (object != null) {
			String content = object.optString("content");
			String updateUrl = object.optString("updateUrl");
			boolean isUpdate = object.optBoolean("isUpdate");
			String versionNo = object.optString("versionNo");
			String isForce = object.optString("isForce");
			
			if(isUpdate == true){
				DialogUtil.showVersionUpdataDialog(mActivity, content, updateUrl, isForce);
			}else {
				ToastUtil.getInstance(mContext).showPosition("当前已经是最新版本");
			}
		}
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return null;
	}

	@Override
	public void handleParseJsonException(int which) {
		
	}

}
