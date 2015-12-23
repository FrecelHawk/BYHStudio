package com.xyt.ygcf.adpter.my;

import com.xyt.yangguangchufang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AvatrAdapter extends BaseAdapter {

	private static int[] drawablesId = { R.drawable.ic_my_avar_login, R.drawable.ic_my_avar_login,
			R.drawable.ic_my_avar_login, R.drawable.ic_my_avar_login, R.drawable.ic_my_avar_login,
			R.drawable.ic_my_avar_login, R.drawable.ic_my_avar_login, R.drawable.ic_my_avar_login,
			R.drawable.ic_my_avar_login };

	private LayoutInflater inflater;

	public AvatrAdapter(Context mContext) {
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return drawablesId.length;
	}

	@Override
	public Object getItem(int position) {
		return drawablesId[position];
	}

	@Override
	public long getItemId(int position) {
		return drawablesId[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_avatr_item, null);
		}
		final ImageView imageView = (ImageView) convertView;
		imageView.setImageResource(drawablesId[position]);
		return convertView;
	}

}
