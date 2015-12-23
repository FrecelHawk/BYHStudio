package com.xyt.ygcf.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Variable;
import com.xyt.ygcf.entity.BaseJsonBeen;
import com.xyt.ygcf.impl.IHandleHttpResult;
import com.xyt.ygcf.logic.BaseJsonParse;
import com.xyt.ygcf.util.BaseUtil;
import com.xyt.ygcf.util.HttpTool;

/**
 * 
 * @author yuyangming 网络请求Fragment的基类，有关于用到不是列表的网络请求，继承其即可，如果是列表的Fragment请继承
 *         {@link #BaseListFragment}
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener, IHandleHttpResult {

	protected Context mContext;

	/** 加载显示框 */
	protected View progressBar = null;

	protected HttpUtils httpUtils = new HttpUtils();
	protected BitmapUtils bitmapUtils = null;

	/** 请求图片的大小 */
	protected final String itemBitmapSize = Variable.itemBitmapSize;
	protected final String detailBitmapSize = Variable.detailBitmapSize;

	// 一个页面有6个网络请求已经够多的了
	protected boolean[] isRequesting = { false, false, false, false, false, false };

	protected RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {

		@Override
		public void onFailure(HttpException arg0, String arg1, final int which) {
			finishedRequest(which);
			isRequesting[which] = false;
			handleNetError(arg0, arg1, which);
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0, final int which) {
			finishedRequest(which);
			isRequesting[which] = false;
			handleResponse(arg0, which);
			handleResult(arg0.result, which);
		}
	};

	/** 目的是获取cookies */
	protected void handleResponse(ResponseInfo<String> arg0, final int which) {

	}

	/**
	 * 服务器烦忙，需要的可override
	 * 
	 * @param message
	 * @param which
	 */
	protected void handleServerBusy(String message, final int which) {
		Toast.makeText(mContext, "服务器繁忙，请稍后...", Toast.LENGTH_LONG).show();
	}

	private void handleNetError(HttpException exception, String message, final int which) {
		final int exceptionCode = exception.getExceptionCode();
		System.out.println("---------exceptionCode--------->" + exceptionCode);
		switch (exceptionCode) {
			case 404:
			case 500:
				handleServerBusy(message, which);
				break;
			default:
				handleNetError(message, which);
				break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_base_layout, null);
		FrameLayout layout = (FrameLayout) view.findViewById(R.id.base_framgent_content);
		progressBar = view.findViewById(R.id.frament_base_progress_layout);
		progressBar.bringToFront();
		layout.addView(onCreateContentView(inflater), 0, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		return view;
	}  
	protected abstract View onCreateContentView(LayoutInflater inflater);

	/**
	 * 里面可以操作一些逻辑，比如要关掉对话框等等
	 * 
	 * @param which
	 */
	protected void finishedRequest(final int which) {
		dissmissProgressDialog();
		System.out.println("-----QueryURL------>" + requestCallBack.getRequestUrl());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mContext == null) {
			mContext = getActivity();
		}
		initVariable();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (bitmapUtils != null) {
			bitmapUtils.resumeTasks();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (bitmapUtils != null) {
			bitmapUtils.pauseTasks();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (bitmapUtils != null) {
			bitmapUtils.stopTasks();
			bitmapUtils.clearMemoryCache();
		}
	}

	protected void initVariable() {
	}

	protected void initBitmapVariable() {
		bitmapUtils = new BitmapUtils(mContext, BaseUtil.getDiskCacheDir(mContext, "img").getAbsolutePath());
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_load_defalut);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_load_defalut);
	}

	protected void showToast(final String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	final public void handleResult(String json, int which) {
		BaseJsonBeen been = BaseJsonParse.parseBaseJson(json);
		if (been == null) {
			handleParseJsonException(which);
		} else {
			if ("true".equals(been.success)) {
				handleJson(been.data, which);
			} else {
				handleError(been.message, which);
			}
		}

	}

	@Override
	public void handleNetError(String message, int which) {
		Toast.makeText(mContext, "网络错误，请确认网络是否连接", Toast.LENGTH_LONG).show();
	}

	@Override
	public void handleError(String message, int which) {
		System.out.println("----message-->" + message);
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void handleParseJsonException(int which) {
		this.handleServerBusy("", which);
	}
	
	protected void showProgressDialog() {
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}

	}

	protected void dissmissProgressDialog() {
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
	}

	/**
	 * 网络请求方法，请大家统一调用其方法来进行网络请求,需自己传请求方式({@link HttpMethod.GET}或者
	 * {@link HttpMethod.POST})
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @param which
	 * @param showProgressDialog
	 *            是否显示加载框
	 */
	protected void sendRequest(HttpMethod method, String url, RequestParams params, final int which,
			boolean showProgressDialog) {
		if (showProgressDialog) {
			showProgressDialog();
		}
		this.beforeRequest(which);
		isRequesting[which] = true;
		HttpTool.sendRequest(httpUtils, method, url, params, requestCallBack, which);
	}

	/**
	 * 网络请求方法，请大家统一调用其方法来进行网络请求,默认用get方法
	 * 
	 * @param url
	 * @param params
	 * @param which
	 * @param showProgressDialog
	 *            是否显示加载框
	 */
	protected void sendRequest(String url, RequestParams params, final int which, boolean showProgressDialog) {
		this.sendRequest(HttpMethod.GET, url, params, which, showProgressDialog);
	}

	protected void beforeRequest(final int which) {
	}
}
