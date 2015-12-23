package com.xyt.ygcf.adpter.my;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.my.MyBaseListBeen;
import com.xyt.ygcf.util.TimeUtils;

public abstract class MyCommonListAdapter<T> extends BaseAdapter implements OnItemLongClickListener {

	protected LayoutInflater inflater;
	private View previousDeleteView;

	protected OnClickListener listener;
	protected List<? extends MyBaseListBeen> list;

	protected BitmapUtils bitmapUtils;

	public MyCommonListAdapter(Context context, List<? extends MyBaseListBeen> list, OnClickListener listener,
			BitmapUtils bitmapUtils) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.listener = listener;
		this.bitmapUtils = bitmapUtils;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		View delete = view.findViewById(R.id.common_my_list_item_delete);
		if (delete != null) {
			if (delete.getVisibility() == View.VISIBLE) {
				delete.setVisibility(View.GONE);
				list.get(position).showDelete = false;
			} else {
				delete.setVisibility(View.VISIBLE);
				delete.setTag(position);
				list.get(position).showDelete = true;
				if (previousDeleteView != null && previousDeleteView != delete) {
					final int prePosition = (Integer) previousDeleteView.getTag();
					if (list.size() > prePosition) {
						previousDeleteView.setVisibility(View.GONE);
						list.get(prePosition).showDelete = false;
					}
				}
			}
			previousDeleteView = delete;
		}
		delete = null;
		return true;
	}

	public void setListOnLongItemClick(ListView listView) {
		listView.setOnItemLongClickListener(this);
	}

	public View getPreviousDeleteView() {
		return previousDeleteView;
	}

	protected void showDeleteView(View view, boolean show, final int position) {
		if (show) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
		view.setOnClickListener(listener);
		view.setTag(position);
	}

	// 计算两点间的距离
	protected int getDistance(double lat1, double lon1, double lat2, double lon2) {
		float[] results = new float[1];
		Location.distanceBetween(lat1 / 1e6, lon1 / 1e6, lat2 / 1e6, lon2 / 1e6, results);
		return (int) results[0];
	}

	/**
	 * 设置距离
	 * 
	 * @param textView
	 * @param distance
	 */
	protected void setDistance(TextView textView, String distance) {
		try {
			final int mDistance = Integer.valueOf(distance);
			textView.setText(TimeUtils.meterTransformKilometer(mDistance));
		} catch (Exception e) {
		}
	}
}
