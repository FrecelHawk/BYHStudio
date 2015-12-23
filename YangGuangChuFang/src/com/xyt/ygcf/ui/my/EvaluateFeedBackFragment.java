package com.xyt.ygcf.ui.my;

import org.json.JSONException;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.ygcf.adpter.my.EvaluateFeedBackAdapter;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.EvaluateFeedBackBeen;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;

public class EvaluateFeedBackFragment extends MyCommonListFragment<EvaluateFeedBackBeen> {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(mContext, EvaluateFeedBackActivity.class);
		intent.putExtra("id", list.get(position).id);
		startActivity(intent);
	}

	@Override
	protected void initListAdapter() {
		super.initListAdapter();
		adapter = new EvaluateFeedBackAdapter(mContext, list, this, bitmapUtils);
	}

	@Override
	protected BaseListBeen<EvaluateFeedBackBeen> parseListData(String json, int which) throws JSONException {
		return MyJsonParse.parseEvaluateFeedbackList(json);
	}

	@Override
	protected void sendRequestData(int which, boolean showProgressDialog) {
		final RequestParams params = getRequestParams();
		params.addQueryStringParameter("merPitureSize", itemBitmapSize);
		sendRequest(UrlMy.MY_EVALUATE_FEEDBACK, params, which, showProgressDialog);
	}

	@Override
	protected void deleteSelect(int deletePostion) {
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", list.get(deletePostion).id);
		sendRequest(UrlMy.DELETE_EVALUATE_FEEDBACK, params, REQUEST_OTHER_DATA, true);
	}
}
