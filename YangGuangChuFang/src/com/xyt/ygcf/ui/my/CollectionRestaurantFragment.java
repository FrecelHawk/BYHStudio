package com.xyt.ygcf.ui.my;

import org.json.JSONException;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.my.CollectionRestaurantAdapter;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.CollectionRestaurantBeen;
import com.xyt.ygcf.home.MerchantDatails;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.SpHelper;

/**
 * 
 * @author yuyangming 收藏的餐厅
 * 
 */
public class CollectionRestaurantFragment extends MyCommonListFragment<CollectionRestaurantBeen> {

	private String latitude , longitude;
	
	@Override
	protected void initVariable() {
		latitude = String.valueOf(SpHelper.init(mContext).getCityLatitude());
		longitude = String.valueOf(SpHelper.init(mContext).getCityLongitude());
	}
	
	@Override
	protected void sendRequestData(final int which, boolean showProgressDialog) {
		final RequestParams params = getRequestParams();
		params.addQueryStringParameter("merPitureSize", itemBitmapSize);
		params.addQueryStringParameter("Industry", Constants.INDUSTRY_RESTAURANT_MAKER);
		params.addQueryStringParameter("latitude", latitude);
		params.addQueryStringParameter("longitude", longitude);
		sendRequest(UrlMy.COLLECTION_RESTAURANT, params, which, showProgressDialog);
	}

	@Override
	protected void deleteSelect(int deletePostion) {
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("merchantId", list.get(deletePostion).id);
		sendRequest(UrlMy.DELETE_COLLECTION_RESTAURANT, params, REQUEST_OTHER_DATA, true);
	}

	@Override
	protected void initListAdapter() {
		super.initListAdapter();
		adapter = new CollectionRestaurantAdapter(mContext, list, this, bitmapUtils);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(mContext, MerchantDatails.class);
		intent.putExtra("id", list.get(position).id);
		intent.putExtra("mark", Constants.INDUSTRY_RESTAURANT_MAKER);
		startActivity(intent);
	}

	@Override
	protected BaseListBeen<CollectionRestaurantBeen> parseListData(String json, int which) throws JSONException {
		return MyJsonParse.parseCollectionRestaurantList(json);
	}

}
