package com.xyt.ygcf.ui.my;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.my.MyCommonListAdapter;
import com.xyt.ygcf.base.BaseListFragment;

public abstract class MyCommonListFragment<T> extends BaseListFragment<T> {

	private int deletePostion = 0;

	protected abstract void sendRequestData(final int which, boolean showProgressDialog);

	protected abstract void deleteSelect(final int deletePostion);

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_my_common_list, null);
	}

	@Override
	protected void loadNextPageData(Object... objects) {
		currentPage++;
		sendRequestData(REQUEST_NEXT_PAGE, false);
	}

	@Override
	protected void initListAdapter() {
		initBitmapVariable();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void findViewsById() {
		listView = (ListView) getView().findViewById(R.id.frament_my_list_view);
		initListFooterView(null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		((MyCommonListAdapter<T>) adapter).setListOnLongItemClick(listView);
		sendRequestData(REQUEST_FIRST_PAGE, true);
	}

	@Override
	public void handleJson(String json, int which) {
		if (which == REQUEST_OTHER_DATA) {
			list.remove(deletePostion);
			adapter.notifyDataSetChanged();
			if (list.isEmpty()) {
				showFooterType(INT_IS_GONE);
			}
			Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
		} else {
			super.handleJson(json, which);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.common_my_list_item_delete) {
			if (!isRequesting[REQUEST_OTHER_DATA]) {
				deletePostion = (Integer) v.getTag();
				deleteSelect(deletePostion);
			}
		} else {
			super.onClick(v);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		View delete = ((MyCommonListAdapter<T>) adapter).getPreviousDeleteView();
		if (delete != null && delete.getVisibility() == View.VISIBLE) {
			delete.setVisibility(View.GONE);
		}
		delete = null;
	}

}
