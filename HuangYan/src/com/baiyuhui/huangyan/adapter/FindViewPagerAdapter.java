package com.baiyuhui.huangyan.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.utils.UILManager;
/**
 * ViewPager图片滑动
 * @author Administrator
 *
 */
public class FindViewPagerAdapter extends PagerAdapter {

	private Context mContext;
	private List<String> lists;
	private int mHeight;
	private int mWidth;
	private OnClickListener mClickListener;

	public FindViewPagerAdapter(Context context, List<String> list,
			OnClickListener clickListener) {
		mContext = context;
		lists = list;
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mHeight = (int) (dm.density * 180 + 0.5f);
		mWidth = dm.widthPixels;
		mClickListener = clickListener;
	}
	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView view = new ImageView(mContext);
		view.setScaleType(ScaleType.CENTER_CROP);
		String str = lists.get(position % lists.size());
		UILManager.with(mContext).load(str).placeholder(R.drawable.moren1).into(view);
		container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		view.setOnClickListener(mClickListener);
		return view;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
		Log.v("AFMOBI_DEBUG", "destroyItem :" + position);
	}

	@Override
	public int getCount() {
		if (lists.size() > 1)
			return Integer.MAX_VALUE;
		return lists.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
}
