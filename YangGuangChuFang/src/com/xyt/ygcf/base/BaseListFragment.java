package com.xyt.ygcf.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.BaseListBeen;

/**
 * 
 * @author yuyangming 用于分页列表
 * @param <T>
 */
public abstract class BaseListFragment<T> extends BaseFragment implements OnItemClickListener {

	/** 请求第一页 */
	protected static final int REQUEST_FIRST_PAGE = 0;
	/** 请求下一页 */
	protected static final int REQUEST_NEXT_PAGE = 1;
	/** 其他请求 */
	protected static final int REQUEST_OTHER_DATA = 2;

	protected static final String STRING_IS_LOADING = "正在加载中...";
	protected static final String STRING_IS_END = "已到最后";
	protected static final String STRING_IS_ERROR = "网络错误，请重试";
	protected static final String STRING_IS_NONE = "暂时没有数据";
	protected static final String STRING_IS_MORE = "更多";
	protected static final String STRING_IS_BUSY = "服务器烦忙，请稍候";

	protected static final int INT_IS_LOADING = 0;
	protected static final int INT_IS_END = 1;
	protected static final int INT_IS_ERROR = 2;
	protected static final int INT_IS_NONE = 3;
	protected static final int INT_IS_MORE = 4;
	protected static final int INT_IS_GONE = 5;
	protected static final int INT_IS_BUSY = 6;

	/** 每次请求20条 */
	protected static final int REQUEST_COUNT = 20;

	protected ListView listView;

	/** 底部布局 */
	private View footerLayout;

	/** 滚动条 */
	private ProgressBar footerProgressBar;

	/** 底部显示文字 */
	private TextView footerTextView;

	/** 底部显示的类型 */
	protected int loadingType = INT_IS_LOADING;

	protected int currentPage = 1;

	private boolean hasScrollToLast = false;

	private boolean hasCaculatedFooterShouldShow = false;

	protected BaseAdapter adapter = null;

	/** 数据list */
	protected List<T> list = new ArrayList<T>();

	private long previousErrorTime = 0L;

	/** 该方法用于自动加载更多内容，直接实现即可 */
	protected abstract void loadNextPageData(Object... objects);

	/** 初始化adapter */
	protected abstract void initListAdapter();

	protected abstract void findViewsById();

	/**
	 * 解析数据
	 * 
	 * @throws JSONException
	 */
	protected abstract BaseListBeen<T> parseListData(String json, int which) throws JSONException;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initListAdapter();
		findViewsById();
	}

	/**
	 * 
	 * @param listener
	 *            监听滑动的OnScrollListener，一般情况可以直接传null
	 */
	protected void initListFooterView(final OnScrollListener listener) {
		footerLayout = LayoutInflater.from(mContext).inflate(R.layout.list_footer_layout, null);
		footerProgressBar = (ProgressBar) footerLayout.findViewById(R.id.list_footer_progress);
		footerTextView = (TextView) footerLayout.findViewById(R.id.list_footer_text);
		footerLayout.setOnClickListener(this);
		listView.addFooterView(footerLayout);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (listener != null) {
					listener.onScrollStateChanged(view, scrollState);
				}
				if (loadingType != INT_IS_END && scrollState == OnScrollListener.SCROLL_STATE_IDLE && hasScrollToLast
						&& list.size() > 0) {
					if (!isRequesting[REQUEST_NEXT_PAGE]) {
						requestNextPageData();
					}
				}
				if (bitmapUtils != null && scrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					bitmapUtils.resumeTasks();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (listener != null) {
					listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				}
				hasScrollToLast = (firstVisibleItem + visibleItemCount) >= totalItemCount - 1;
				if (bitmapUtils != null) {
					bitmapUtils.pauseTasks();
				}

				if (loadingType == INT_IS_END && !hasCaculatedFooterShouldShow) {
					hasCaculatedFooterShouldShow = true;
					final int childCount = view.getChildCount();
					if (childCount < list.size() + 1) {
						showFooterType(INT_IS_END);
					} else {
						showFooterType(INT_IS_GONE);
					}
				}
			}
		});
	}

	private void interceptRequest() {
		if (this.loadingType == INT_IS_ERROR) {
			final long offsetTime = System.currentTimeMillis() - previousErrorTime;
			if (offsetTime > 10 * 1000) {// 大于10s
				requestNextPageData();
			}
		} else {
			requestNextPageData();
		}

	}

	private void requestNextPageData() {
		showFooterType(INT_IS_LOADING);
		isRequesting[REQUEST_NEXT_PAGE] = true;
		loadNextPageData();
	}

	/**
	 * 显示footer的提示
	 * 
	 * @param type
	 *            {@link #TYPE_IS_LOADING， #TYPE_IS_END, #TYPE_IS_ERROR,
	 *            #TYPE_IS_MORE}</br> 在每次请求网络结束后根据具体情况手动调用
	 */
	protected void showFooterType(int type) {
		switch (type) {     
			case INT_IS_LOADING:
				footerProgressBar.setVisibility(View.VISIBLE);
				footerTextView.setText(STRING_IS_LOADING);
				break;
			case INT_IS_END:
				footerProgressBar.setVisibility(View.GONE);
				footerTextView.setText(STRING_IS_END);
				// footerLayout.setVisibility(View.GONE);
				break;
			case INT_IS_ERROR:
				footerProgressBar.setVisibility(View.GONE);
				footerTextView.setText(STRING_IS_ERROR);
				previousErrorTime = System.currentTimeMillis();
				break;
			case INT_IS_NONE:
				footerProgressBar.setVisibility(View.GONE);
				footerTextView.setText(STRING_IS_NONE);
				break;
			case INT_IS_MORE:
				footerProgressBar.setVisibility(View.GONE);
				footerTextView.setText(STRING_IS_MORE);
				break;
			case INT_IS_GONE:
				footerLayout.setVisibility(View.GONE);
				// footerProgressBar.setVisibility(View.GONE);
				// footerTextView.setVisibility(View.GONE);
				break;
			case INT_IS_BUSY:
				footerProgressBar.setVisibility(View.GONE);
				footerTextView.setText(STRING_IS_BUSY);
				break;
			default:
				break;
		}
		this.isRequesting[REQUEST_NEXT_PAGE] = false;
		this.loadingType = type;
	}

	@Override
	public void onClick(View v) {
		if (v == footerLayout) {
			if (!isRequesting[REQUEST_NEXT_PAGE] && this.loadingType != INT_IS_END) {
				interceptRequest();
			}
		}
	}

	@Override
	public void handleJson(String json, int which) {
		try {
			BaseListBeen<T> been = parseListData(json, which);
			if (been != null) {
				list.addAll(been.list);
				adapter.notifyDataSetChanged();
				if (list.size() == 0) {
					showFooterType(INT_IS_NONE);
				} else if (been.currentPage == been.totalPages) {
					showFooterType(INT_IS_END);
				} else {
					showFooterType(INT_IS_MORE);
				}
			}
			been = null;
		} catch (JSONException e) {
			handleParseJsonException(which);
			e.printStackTrace();
		}
	}

	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
		setErrorMessage(which, INT_IS_BUSY);
	}

	@Override
	public void handleParseJsonException(int which) {
		setErrorMessage(which, INT_IS_BUSY);
	}

	@Override
	public void handleNetError(String message, int which) {
		super.handleNetError(message, which);
		setErrorMessage(which, INT_IS_ERROR);
	}

	@Override
	protected void handleServerBusy(String message, int which) {
		super.handleServerBusy(message, which);
		if (which != REQUEST_OTHER_DATA) {
			showFooterType(INT_IS_BUSY);
		}
	}

	private void setErrorMessage(int which, int type) {
		if (which == REQUEST_NEXT_PAGE) {
			showFooterType(type);
			currentPage--;
		} else if (which == REQUEST_FIRST_PAGE) {
			showFooterType(type);
		}
	}

	/***
	 * 获取固定的参数列表
	 * 
	 * @return
	 */
	protected RequestParams getRequestParams() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("needPage", "true");
		params.addQueryStringParameter("currentPage", String.valueOf(currentPage));
		params.addQueryStringParameter("pageSize", String.valueOf(REQUEST_COUNT));
		return params;
	}

	@Override
	protected void beforeRequest(final int which) {
		if (which == REQUEST_FIRST_PAGE) {
			listView.setVisibility(View.GONE);

		}
	}

	@Override
	protected void finishedRequest(int which) {
		super.finishedRequest(which);
		if (which == REQUEST_FIRST_PAGE) {
			listView.setVisibility(View.VISIBLE);
		}
	}

}
