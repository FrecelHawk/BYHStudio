package com.xyt.ygcf.ui.my;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.my.ComplainDetailAdapter;
import com.xyt.ygcf.base.BaseListFragment;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.ComlainDetailBeen;
import com.xyt.ygcf.urls.UrlMy;

/**
 * 根据最新需求，暂时废弃这个类
 * @Date 2014.06.20
 * @author freesonfish
 *
 */
public class ComplainDetailFragment extends BaseListFragment<ComlainDetailBeen> {

	private TextView tvOrder, tvTime, tvShop, tvTheme, tvContent, tvUser, tvContacts;

	public String id = "";

	public static ComplainDetailFragment getInstance(String id) {
		ComplainDetailFragment fragment = new ComplainDetailFragment();
		fragment.id = id;
		return fragment;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_my_common_list, null);
	}

	@Override
	protected void findViewsById() {
		listView = (ListView) getView().findViewById(R.id.frament_my_list_view);
		initListFooterView(null);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View headView = inflater.inflate(R.layout.activity_complain_detail, null);
		listView.addHeaderView(headView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		findHeaderView(headView);
//		sendRequsetData(REQUEST_FIRST_PAGE, true);
		showFooterType(INT_IS_GONE);
	}

	private void findHeaderView(View headView) {
		tvOrder = (TextView) headView.findViewById(R.id.complaint_header_detail_order);
		tvTime = (TextView) headView.findViewById(R.id.complaint_header_detail_time);
		tvShop = (TextView) headView.findViewById(R.id.complaint_header_detail_shopname);
		tvTheme = (TextView) headView.findViewById(R.id.complaint_header_detail_theme);
		tvContent = (TextView) headView.findViewById(R.id.complaint_header_detail_content);
	}

	private void sendRequsetData(final int which, boolean showProgressDialog) {
		RequestParams params = getRequestParams();
		params.addQueryStringParameter("id", "id");
		sendRequest(UrlMy.COMPLAINT_DETAIL, params, which, showProgressDialog);
	}

	@Override
	protected void loadNextPageData(Object... objects) {
		currentPage++;
		sendRequsetData(REQUEST_NEXT_PAGE, false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	protected void initListAdapter() {
		adapter = new ComplainDetailAdapter(mContext, list);
	}

	@Override
	protected BaseListBeen<ComlainDetailBeen> parseListData(String json, int which) {
		return null;
	}

}
