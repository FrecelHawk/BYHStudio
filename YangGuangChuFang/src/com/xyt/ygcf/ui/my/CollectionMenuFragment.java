package com.xyt.ygcf.ui.my;

import org.json.JSONException;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.ygcf.adpter.my.CollectionMenuAdapter;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.CollectionMenuBeen;
import com.xyt.ygcf.home.GoodsDetailsActivity;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;

/**
 * 
 * @author yuyangming 收藏的菜式
 * 
 */
public class CollectionMenuFragment extends MyCommonListFragment<CollectionMenuBeen> {

	@Override
	protected void sendRequestData(final int which, final boolean showProgressDialog) {
		final RequestParams params = getRequestParams();
		params.addQueryStringParameter("proPitureSize", itemBitmapSize);
		sendRequest(UrlMy.COLLECTION_MENU, params, which, showProgressDialog);
	}

	@Override
	protected void deleteSelect(int deletePostion) {
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("productId", list.get(deletePostion).id);
		sendRequest(UrlMy.DELETE_COLLECTION_MENU, params, REQUEST_OTHER_DATA, true);
	}

	@Override
	protected void initListAdapter() {
		super.initListAdapter();
		adapter = new CollectionMenuAdapter(mContext, list, this, bitmapUtils);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
		intent.putExtra("id", list.get(position).id);
		intent.putExtra("mark", list.get(position).industry);
		startActivity(intent);
	}

	@Override
	protected BaseListBeen<CollectionMenuBeen> parseListData(String json, int which) throws JSONException {
		return MyJsonParse.parseCollectionMenuList(json);
	}
}
