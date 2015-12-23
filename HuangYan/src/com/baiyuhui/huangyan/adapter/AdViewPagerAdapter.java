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

import com.baiyuhui.huangyan.R;
/**
 * ViewPager图片滑动
 * @author Administrator
 *
 */
public class AdViewPagerAdapter extends PagerAdapter {

	private Context mContext;
	private List<Integer> mList;
	private int mHeight;
	private int mWidth;
	private OnClickListener mClickListener;

	public AdViewPagerAdapter(Context context, List<Integer> list,
			OnClickListener clickListener) {
		mContext = context;
		mList = list;
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mHeight = (int) (dm.density * 180 + 0.5f);
		mWidth = dm.widthPixels;
		mClickListener = clickListener;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView view = new ImageView(mContext);
		view.setScaleType(ScaleType.CENTER_CROP);
		view.setBackgroundResource(R.drawable.home_cartoon1);
		container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
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
		if (mList.size() > 1)
			return Integer.MAX_VALUE;
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
}
