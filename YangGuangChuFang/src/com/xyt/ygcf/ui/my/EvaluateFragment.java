package com.xyt.ygcf.ui.my;

import org.json.JSONException;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.my.EvaluateAdapter;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.EvaluateBeen;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;

public class EvaluateFragment extends MyCommonListFragment<EvaluateBeen> {

	/** 0：表示餐厅 1：表示菜式 */
	public int type = 0;

	public static Fragment getInstance(int type) {
		EvaluateFragment fragment = new EvaluateFragment();
		fragment.type = type;
		return fragment;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(mContext, EvaluateDetailActivity.class);
		final EvaluateBeen been = list.get(position);
		intent.putExtra("rating", been.recommendLevel);
		intent.putExtra("content", been.commentContent);
		intent.putExtra("time", been.commentTime);
		startActivity(intent);
	}

	@Override
	protected void initListAdapter() {
		super.initListAdapter();
		adapter = new EvaluateAdapter(mContext, list, this, bitmapUtils);
	}

	@Override
	protected BaseListBeen<EvaluateBeen> parseListData(String json, int which) throws JSONException {
		return MyJsonParse.parseEvaluateList(json, type);
	}

	@Override
	protected void sendRequestData(int which, boolean showProgressDialog) {
		final RequestParams params = getRequestParams();
		if (type == 0) {
			params.addQueryStringParameter("Industry", Constants.INDUSTRY_RESTAURANT_MAKER);
			params.addQueryStringParameter("merPitureSize", itemBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.MY_EVALUATE_RESTAURANT, params, which, showProgressDialog);
		} else {
			params.addQueryStringParameter("proPitureSize", itemBitmapSize);
			sendRequest(HttpMethod.GET, UrlMy.MY_EVALUATE_MENU, params, which, showProgressDialog);
		}

	}

	@Override
	protected void deleteSelect(int deletePostion) {
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", list.get(deletePostion).id);
		if (type == 0) {
			sendRequest(UrlMy.DELETE_EVALUATE_RESTAURANT, params, REQUEST_OTHER_DATA, true);
		} else {
			sendRequest(UrlMy.DELETE_EVALUATE_MENU, params, REQUEST_OTHER_DATA, true);
		}
	}

}
