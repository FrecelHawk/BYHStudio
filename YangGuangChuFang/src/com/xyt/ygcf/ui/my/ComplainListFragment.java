package com.xyt.ygcf.ui.my;

import org.json.JSONException;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.my.ComplainListAdapter;
import com.xyt.ygcf.base.BaseListFragment;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.ComplainBeen;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;

/**
 * 
 * @author yuyangming 我的投诉
 * 
 */
public class ComplainListFragment extends BaseListFragment<ComplainBeen> {

	private int cancelPosition = 0;

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_my_common_list, null);
	}

	@Override
	protected void findViewsById() {
		listView = (ListView) getView().findViewById(R.id.frament_my_list_view);
		initListFooterView(null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		sendRequestData(REQUEST_FIRST_PAGE, true);
	}

	protected void sendRequestData(final int which, boolean showProgressBar) {
		sendRequest(UrlMy.MY_COMPLAINT, getRequestParams(), which, showProgressBar);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, ComplainDetailActivity.class);
		intent.putExtra("id", list.get(position).id);
		startActivity(intent);
	}

	@Override
	protected void loadNextPageData(Object... objects) {
		currentPage++;
		sendRequestData(REQUEST_NEXT_PAGE, false);
	}

	@Override
	protected void initListAdapter() {
		adapter = new ComplainListAdapter(mContext, list, this);
	}

	@Override
	protected BaseListBeen<ComplainBeen> parseListData(String json, int which) throws JSONException {
		return MyJsonParse.parseComplainList(json);
	}

	@Override
	public void handleJson(String json, int which) {
		if (which == REQUEST_OTHER_DATA) {
			list.remove(cancelPosition);
			adapter.notifyDataSetChanged();
			if (list.isEmpty()) {
				showFooterType(INT_IS_GONE);
			}
			Toast.makeText(mContext, "成功取消投诉！", Toast.LENGTH_SHORT).show();
		} else {
			super.handleJson(json, which);
		}
	}

	private void sendCancelComplain() {
		if (!isRequesting[REQUEST_OTHER_DATA]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("id", list.get(cancelPosition).id);
			sendRequest(UrlMy.DELETE_COMPLAINT, params, REQUEST_OTHER_DATA, true);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {
			case R.id.complain_list_item_no:
				cancelPosition = (Integer) v.getTag();
				sendCancelComplain();
				break;
			default:
				break;
		}
	}

}
