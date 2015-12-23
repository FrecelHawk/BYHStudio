package org.android.dialog.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * @ClassName: BaseDialogBuilder
 * @Description: TODO  Dialog构建基类
 * @author FH 
 * @date 2015年9月18日 上午11:11:17
 * 
*/

public abstract class BaseDialogBuilder<T> {

	
	  protected final Context mContext;
	  
	  public final static String ARG_REQUEST_CODE = "request_code";
	  
	  private int mRequestCode = DEFAULT_REQUEST_CODE;
	  
	  public final static String DEFAULT_TAG = "simple_dialog";
	  
	  private String mTag = DEFAULT_TAG;
	  
	  public final static int DEFAULT_REQUEST_CODE = -42;
	  
	  private boolean mCancelable = true;
	  
	  private Fragment mTargetFragment;
	  
	  protected final FragmentManager mFragmentManager;
	  
	  protected final Class<? extends BaseDialogFragment> mClass;
	
	 public BaseDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
	        mFragmentManager = fragmentManager;
	        mContext = context.getApplicationContext();
	        mClass = clazz;
	 }
	 
	 protected abstract T self();

	 protected abstract Bundle prepareArguments();
	 
	 protected abstract void configView();

     public T setTargetFragment(Fragment fragment, int requestCode) {
	        mTargetFragment = fragment;
	        mRequestCode = requestCode;
	        return self();
	 }
	
	 public T setRequestCode(int requestCode) {
	        mRequestCode = requestCode;
	        return self();
	 }
	
	 public T setTag(String tag) {
	        mTag = tag;
	        return self();
	 }
	 
	 public T setCancelable(boolean cancelable) {
	        mCancelable = cancelable;
	        return self();
	 }

	 
	 
	 private BaseDialogFragment create() {
		    configView();
	        final Bundle args = prepareArguments();

	        final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(mContext, mClass.getName(), args);

	        if (mTargetFragment != null) {
	            fragment.setTargetFragment(mTargetFragment, mRequestCode);
	        } else {
	            args.putInt(ARG_REQUEST_CODE, mRequestCode);
	        }
	        fragment.setCancelable(mCancelable);
	        return fragment;
	 }
	 
	 
	 public DialogFragment show() {
	        BaseDialogFragment fragment = create();
	        fragment.show(mFragmentManager, mTag);
	        return fragment;
	  }
	 
	 
	 public DialogFragment showAllowingStateLoss() {
	        BaseDialogFragment fragment = create();
	        fragment.showAllowingStateLoss(mFragmentManager, mTag);
	        return fragment;
	    }
}
