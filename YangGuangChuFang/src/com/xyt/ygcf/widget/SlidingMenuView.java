package com.xyt.ygcf.widget;

import android.content.Context;

import android.graphics.Canvas;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import android.widget.Scroller;

public class SlidingMenuView extends ViewGroup {

	private View mLeftView;
	private View mContainer;
	private static final int INVALID_SCREEN = -1;

	private static final int SNAP_VELOCITY = 1000;

	private int mDefaultScreen = 0;

	public int mCurrentScreen;
	private int mNextScreen = INVALID_SCREEN;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker; //

	private float mLastMotionX;
	private float mLastMotionY;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	public int mTouchState = TOUCH_STATE_REST;

	private boolean mAllowLongPress;
	private boolean mLocked;

	private int mTouchSlop;

	public SlidingMenuView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWorkspace();
		postDelayed(new Runnable() {
			@Override
			public void run() {
				// scrollTo(mLeftView.getWidth(), 0);
			}
		}, 50);
	}

	public View getLeftView() {
		return mLeftView;
	}

	public void setLeftView(View mLeftView) {
		this.mLeftView = mLeftView;
	}

	public View getmContainer() {
		return mContainer;
	}

	public void setContainer(View mContainer) {
		this.mContainer = mContainer;
	}

	private void initWorkspace() {
		mScroller = new Scroller(getContext());
		mCurrentScreen = mDefaultScreen;

		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	boolean isDefaultScreenShowing() {
		return mCurrentScreen == mDefaultScreen;
	}

	int getCurrentScreen() {
		return mCurrentScreen;
	}

	public void setCurrentScreen(int currentScreen) {
		mCurrentScreen = Math.max(0,
				Math.min(currentScreen, getChildCount() - 1));

		invalidate();
	}

	void showDefaultScreen() {
		setCurrentScreen(mDefaultScreen);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
		} else if (mNextScreen != INVALID_SCREEN) {
			mCurrentScreen = Math.max(0,
					Math.min(mNextScreen, getChildCount() - 1));
			mNextScreen = INVALID_SCREEN;
			clearChildrenCache();
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		final int scrollX = getScrollX();
		super.dispatchDraw(canvas);
		canvas.translate(scrollX, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureViews(widthMeasureSpec, heightMeasureSpec);

	}

	public void measureViews(int widthMeasureSpec, int heightMeasureSpec) {
		mLeftView.measure(
				mLeftView.getLayoutParams().width + mLeftView.getLeft()
						+ mLeftView.getRight(), heightMeasureSpec);
		mContainer.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int childLeft = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (direction == View.FOCUS_LEFT) {
			if (getCurrentScreen() > 0) {
				snapToScreen(getCurrentScreen() - 1);
				return true;
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (getCurrentScreen() < getChildCount() - 1) {
				snapToScreen(getCurrentScreen() + 1);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}

//	 @Override
//	 public boolean onInterceptTouchEvent(MotionEvent ev) {
//	 if (mLocked) {
//	 return true;
//	 }
//	
//	 final int action = ev.getAction();
//	 if ((action == MotionEvent.ACTION_MOVE) && (mTouchState !=
//	 TOUCH_STATE_REST)) {
//	 return true;
//	 }
//	
//	 final float x = ev.getX();
//	 final float y = ev.getY();
//	
//	 switch (action) {
//	 case MotionEvent.ACTION_MOVE:
//	
//	 final int xDiff = (int) Math.abs(x - mLastMotionX);
//	 final int yDiff = (int) Math.abs(y - mLastMotionY);
//	
//	 final int touchSlop = mTouchSlop;
//	 boolean xMoved = xDiff > touchSlop;
//	 boolean yMoved = yDiff > touchSlop;
//	
//	 if (xMoved || yMoved) {
//	
//	 if (xMoved) {
//	 // Scroll if the user moved far enough along the X axis
//	 mTouchState = TOUCH_STATE_SCROLLING;
//	 enableChildrenCache();
//	 }
//	 // Either way, cancel any pending longpress
//	 if (mAllowLongPress) {
//	 mAllowLongPress = false;
//	 // Try canceling the long press. It could also have been scheduled
//	 // by a distant descendant, so use the mAllowLongPress flag to block
//	 // everything
//	 final View currentScreen = getChildAt(mCurrentScreen);
//	 currentScreen.cancelLongPress();
//	 }
//	 }
//	 break;
//	
//	 case MotionEvent.ACTION_DOWN:
//	 // Remember location of down touch
//	 mLastMotionX = x;
//	 mLastMotionY = y;
//	 mAllowLongPress = true;
//	
//	 mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST :
//	 TOUCH_STATE_SCROLLING;
//	
//	 break;
//	
//	 case MotionEvent.ACTION_CANCEL:
//	 case MotionEvent.ACTION_UP:
//	 // Release the drag
//	 clearChildrenCache();
//	 mTouchState = TOUCH_STATE_REST;
//	 mAllowLongPress = false;
//	 break;
//	 }
//	
//	 /*
//	 * The only time we want to intercept motion events is if we are in the
//	 * drag mode.
//	 */
//	 return mTouchState != TOUCH_STATE_REST;
//	 }

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLocked) {
			return true;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			// Remember where the motion event started
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				// Scroll to follow the motion event
				final int deltaX = (int) (mLastMotionX - x);
				mLastMotionX = x;

				if (deltaX < 0) {
					if (getScrollX() > 0) {

						scrollBy(Math.max(-getScrollX(), deltaX), 0);
					}
				} else if (deltaX > 0) {
					final int availableToScroll = getChildAt(
							getChildCount() - 1).getRight()
							- getScrollX() - getWidth();
					if (availableToScroll > 0) {

						scrollBy(Math.min(availableToScroll, deltaX), 0);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();

				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					// Fling hard enough to move left
					snapToScreen(mCurrentScreen - 1);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurrentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					snapToScreen(mCurrentScreen + 1);
				} else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
		}

		return true;
	}

	protected void snapToDestination() {
		final int screenWidth = mLeftView.getWidth();
		final int whichScreen = (getScrollX() + (screenWidth / 2))
				/ screenWidth;

		snapToScreen(whichScreen);
	}

	protected void snapToScreen(int whichScreen) {

		enableChildrenCache();

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		boolean changingScreens = whichScreen != mCurrentScreen;

		mNextScreen = whichScreen;

		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens
				&& focusedChild == getChildAt(mCurrentScreen)) {
			focusedChild.clearFocus();
		}

		final int newX = whichScreen * mLeftView.getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
		invalidate();
	}

	public void scrollLeft() {
		if (mNextScreen == INVALID_SCREEN && mCurrentScreen > 0
				&& mScroller.isFinished()) {
			snapToScreen(mCurrentScreen - 1);
		}
	}

	public void scrollRight() {
		if (mNextScreen == INVALID_SCREEN
				&& mCurrentScreen < getChildCount() - 1
				&& mScroller.isFinished()) {
			snapToScreen(mCurrentScreen + 1);
		}
	}

	public int getScreenForView(View v) {
		int result = -1;
		if (v != null) {
			ViewParent vp = v.getParent();
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (vp == getChildAt(i)) {
					return i;
				}
			}
		}
		return result;
	}

	public void unlock() {
		mLocked = false;
	}

	public void lock() {
		mLocked = true;
	}

	void moveToDefaultScreen() {
		snapToScreen(mDefaultScreen);
		getChildAt(mDefaultScreen).requestFocus();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		View child;
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			child.setFocusable(true);
			child.setClickable(true);
		}
	}
}