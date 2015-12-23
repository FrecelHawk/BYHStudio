package com.xyt.ygcf.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
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
 * 应用程序Activity的基类
 * 
 * 
 */
public abstract class BaseActivity extends Activity implements OnClickListener, IHandleHttpResult {

	protected HttpUtils httpUtils = new HttpUtils();
	protected BitmapUtils bitmapUtils = null;

	/** 请求图片的大小 */
	protected final String itemBitmapSize = Variable.itemBitmapSize;
	protected final String detailBitmapSize = Variable.detailBitmapSize;

	// 一个页面有6个网络请求已经够多的了
	protected boolean[] isRequesting = { false, false, false, false, false, false };

	private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {

		@Override
		public void onFailure(HttpException arg0, String arg1, final int which) {
			finishedRequest(which);
			setRequestTagFalse(which);
			handleNetError(arg0, arg1, which);
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0, final int which) {
			finishedRequest(which);
			setRequestTagFalse(which);
			handleResult(arg0.result, which);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		// 初始化校验
		initTitleBackBtn();
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		// 初始化校验
		initTitleBackBtn();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		// 初始化校验
		initTitleBackBtn();
	}

	@Override
	public void onClick(View v) {
	}

	/**
	 * 标题栏左按钮被按下
	 */
	protected void onBackBtnPressed() {
		super.onBackPressed();
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	protected void setTitle(String text) {
		((TextView) findViewById(R.id.desktop_userID)).setText(text);
	}

	/**
	 * 设置返回按钮事件
	 */
	private void initTitleBackBtn(int... resid) {
		if (resid.length != 0) {
			((ImageView) findViewById(R.id.back_btn)).setBackgroundResource(resid[0]);
		}
		((ImageView) findViewById(R.id.back_btn)).setVisibility(View.VISIBLE);
		((ImageView) findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackBtnPressed();
			}
		});
	}

	/**
	 * 使用资源设置返回按钮背景
	 * 
	 * @param resource
	 *            资源
	 */
	protected void setBackBtnImage(int resource) {
		initTitleBackBtn(resource);
	}

	protected void setRequestTagFalse(final int which) {
		isRequesting[which] = false;
	}

	protected void showToast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	protected void initBitmapVariable() {
		bitmapUtils = new BitmapUtils(this, BaseUtil.getDiskCacheDir(this, "img").getAbsolutePath());
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_load_defalut);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_load_defalut);
	}

	/**
	 * view finish事件
	 * 
	 * @param v
	 */
	public void finish(View v) {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (bitmapUtils != null) {
			bitmapUtils.resumeTasks();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (bitmapUtils != null) {
			bitmapUtils.pauseTasks();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		if (bitmapUtils != null) {
			bitmapUtils.stopTasks();
			bitmapUtils.clearMemoryCache();
		}
	}

	protected void showProgressDialog() {
		BaseUtil.showProgressDialog(this, true);
	}

	protected void showProgressDialog(boolean cancel) {
		BaseUtil.showProgressDialog(this, cancel);
	}

	protected void dissmissProgressDialog() {
		BaseUtil.dissmissProgressDialog();
	}

	/**
	 * 里面可以操作一些逻辑，比如要关掉对话框等等
	 * 
	 * @param which
	 */
	protected void finishedRequest(final int which) {
		System.out.println("-----QueryURL------>" + requestCallBack.getRequestUrl());
		dissmissProgressDialog();
	}

	/**
	 * 服务器烦忙，需要的可override
	 * 
	 * @param message
	 * @param which
	 */
	protected void handleServerBusy(String message, final int which) {
		Toast.makeText(this, "服务器繁忙，请稍后...", Toast.LENGTH_LONG).show();
	}

	private void handleNetError(HttpException exception, String message, final int which) {
		final int exceptionCode = exception.getExceptionCode();
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
		System.out.println("-------handleNetError--------->" + message);
		Toast.makeText(this, "网络错误，请确认网络是否连接", Toast.LENGTH_LONG).show();
	}

	@Override
	public void handleError(String message, int which) {
		System.out.println("----message-->" + message);
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/***
	 * 请大家在子类重写
	 */
	@Override
	public void handleJson(String json, int which) {

	}

	@Override
	public void handleParseJsonException(int which) {
		handleServerBusy("", which);
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

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in,
		// R.anim.push_right_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		// overridePendingTransition(R.anim.push_left_in,
		// R.anim.push_right_out);
	}
}
