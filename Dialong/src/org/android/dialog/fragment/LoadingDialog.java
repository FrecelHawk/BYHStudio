package org.android.dialog.fragment;

import org.android.dialog.R;
import org.android.dialog.core.BaseDialogBuilder;
import org.android.dialog.core.BaseDialogFragment;
import org.android.dialog.iface.DismissDialogListerner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * @ClassName: LoadingDialog
 * @Description: TODO  加载弹框
 * @author FH 
 * @date 2015年10月27日 上午11:40:31
 * 
*/

@SuppressLint("InflateParams")
public class LoadingDialog extends BaseDialogFragment{


	
	private static Handler execute;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.loadingDialog);
		dialog.setOnShowListener(this);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	@Override
	protected View initView() {
		View view = getLayoutInflater(getArguments()).inflate(R.layout.dialog_loading, null);
		execute.sendEmptyMessage(0);
		return view;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();
	}
	
	public static LoadingDialogBuilder createBuilder(Context context,FragmentManager manager){
		return new LoadingDialogBuilder(context, manager, LoadingDialog.class);
	}

	
	public static class LoadingDialogBuilder extends BaseDialogBuilder<LoadingDialogBuilder> implements DismissDialogListerner{
                                                 
		private View.OnClickListener  mDismissListener;
		
		public LoadingDialogBuilder(Context context,
				FragmentManager fragmentManager,
				Class<? extends BaseDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}

		@Override
		protected LoadingDialogBuilder self() {
			return this;
		}
		
		public LoadingDialogBuilder setDismessListener(View.OnClickListener dismissListener){
			mDismissListener = dismissListener;
			return self();
		}

		@Override
		protected Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			return bundle;
		}

		@SuppressLint("HandlerLeak")
		@Override
		protected void configView() {
			execute = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					onDismiss(mDismissListener);
				}
			};
		}

		@Override
		public void onDismiss(OnClickListener clickListener) {
			  if(clickListener!=null)
				  setDismessListener(clickListener);
		}
	}
}
