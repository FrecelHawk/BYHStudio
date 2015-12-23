package com.baiyuhui.huangyan.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomizeViewpageView extends ViewPager {

	private float mLastMotionX = 0;

	public CustomizeViewpageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
	}

	public CustomizeViewpageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		boolean actionPost = true;
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = arg0.getX();
			if (getCurrentItem() != 0) {
				super.requestDisallowInterceptTouchEvent(true);
			} else {

				super.requestDisallowInterceptTouchEvent(false);
			}

			break;

		case MotionEvent.ACTION_MOVE:

			if ((arg0.getX() - mLastMotionX) > 0 && getCurrentItem() > 0) {
				super.requestDisallowInterceptTouchEvent(false);
				actionPost = true;
			}

			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			super.requestDisallowInterceptTouchEvent(false);
			break;

		default:

			break;
		}

		return super.onTouchEvent(arg0) || actionPost;
	}

}
