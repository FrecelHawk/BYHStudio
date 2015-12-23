package org.android.dialog.fragment;


import org.android.dialog.R;
import org.android.dialog.core.BaseDialogBuilder;
import org.android.dialog.core.BaseDialogFragment;
import org.android.dialog.iface.CustomDialogListener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomDialog extends BaseDialogFragment {

	protected final static String ARG_MESSAGE = "message";
    protected final static String ARG_MESSAGE_COLOR ="message_color";
	protected final static String ARG_TITLE = "title";
	protected final static String ARG_TITLE_COLOR="title_color";
	protected final static String ARG_POSITIVE_BUTTON = "positive_button";
	protected final static String ARG_POSITIVE_BUTTON_COLOR = "positive_button_color";
	protected final static String ARG_NEGATIVE_BUTTON = "negative_button";
	protected final static String ARG_NEGATIVE_BUTTON_COLOR = "negative_button_color";
	

	private static Button negativeButton;

	private static TextView message;
	
	private static TextView title;

	private static View line;
	
	private static Button positiveButton;

	private static CustomDialog  instance;
	
	
	static Handler  execute;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.customDialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnShowListener(this);
		return dialog;
	}
	
	@Override
	protected View initView() {
		instance = this;
		View inflate = getLayoutInflater(getArguments()).inflate(
				R.layout.dialog_custom, null);
		negativeButton = (Button) inflate.findViewById(R.id.left_button);
		positiveButton = (Button) inflate.findViewById(R.id.right_button);
		title = (TextView) inflate.findViewById(R.id.title);
		message = (TextView) inflate.findViewById(R.id.message);
		line = (View) inflate.findViewById(R.id.line);
		configTitle(title,line);
		configMessage(message);
		configPositiveBut(positiveButton);
		configNegativeBut(negativeButton);
		execute.sendEmptyMessage(0);
		return inflate;
	}
	
	private void configNegativeBut(Button but) {
         if(null==getArguments()) return;
         
         if(null!=getArguments().getCharSequence(ARG_NEGATIVE_BUTTON))
        	 but.setText(getArguments().getCharSequence(ARG_NEGATIVE_BUTTON));
         
         if(0!=getArguments().getInt(ARG_NEGATIVE_BUTTON_COLOR))
        	 but.setTextColor(getArguments().getInt(ARG_NEGATIVE_BUTTON_COLOR));
	}

	private void configPositiveBut(Button but) {
		if(null==getArguments()) return;
		
		if(null!=getArguments().getCharSequence(ARG_POSITIVE_BUTTON))
			but.setText(getArguments().getCharSequence(ARG_POSITIVE_BUTTON));
		
		if(0!=getArguments().getInt(ARG_POSITIVE_BUTTON_COLOR))
			but.setTextColor(getArguments().getInt(ARG_POSITIVE_BUTTON_COLOR));
		
	}

	private void configMessage(TextView tv) {
		if(null==getArguments()) return;
		tv.setText(getArguments().getCharSequence(ARG_MESSAGE));
		
		if(0!=getArguments().getInt(ARG_MESSAGE_COLOR))
			 tv.setTextColor(getArguments().getInt(ARG_MESSAGE_COLOR));
		tv.setGravity(Gravity.CENTER);
	}

	private void configTitle(TextView tv,View line){
		if(null==getArguments()) return;
		if(null==getArguments().getCharSequence(ARG_TITLE)) {
			tv.setVisibility(View.GONE);
			line.setVisibility(View.INVISIBLE);
			return;
		}
		tv.setText(getArguments().getCharSequence(ARG_TITLE));
		if(0!=getArguments().getInt(ARG_TITLE_COLOR)){
			tv.setTextColor(getArguments().getInt(ARG_TITLE_COLOR));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();

	}
	
	
	public static CustomDialogBuilder createBuilder(Context context,FragmentManager fragmentManager){
		return  new CustomDialogBuilder(context, fragmentManager, CustomDialog.class);
	}

	public static class CustomDialogBuilder extends
			BaseDialogBuilder<CustomDialogBuilder> implements  CustomDialogListener{

		private CharSequence mTitle;
		
		private int mTitleColor;
		
		private CharSequence mMessage;
		
		private int mMessageColor;
		
		private CharSequence mNegativeText;
		
		private int mNegativeTextColor;
		
		private CharSequence mPositiveText;
		
		private int mPositiveTextColor;
		
		private View.OnClickListener mNegativeButton;
		
		private View.OnClickListener mPositiveButton;
		
		
		public CustomDialogBuilder(Context context,
				FragmentManager fragmentManager,
				Class<? extends BaseDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}

		@Override
		protected CustomDialogBuilder self() {
			return this;
		}

		public CustomDialogBuilder setTitle(CharSequence title) {
			mTitle = title;
			return this;
		}
		
		public CustomDialogBuilder setTitleColor(int color){
			mTitleColor = color;
			return this;
		}

		/**
		 * Allow to set resource string with HTML formatting and bind %s,%i.
		 * This is workaround for
		 * https://code.google.com/p/android/issues/detail?id=2923
		 */
		public CustomDialogBuilder setMessage(int resourceId,
				Object... formatArgs) {
			mMessage = Html.fromHtml(String.format(Html
					.toHtml(new SpannedString(mContext.getText(resourceId))),
					formatArgs));
			return this;
		}

		public CustomDialogBuilder setMessage(CharSequence message) {
			mMessage = message;
			return this;
		}
		
		public CustomDialogBuilder setMessageColor(int color){
			mMessageColor = color;
			return this;
		}

		public CustomDialogBuilder setPositiveButtonText(CharSequence text) {
			mPositiveText = text;
			return this;
		}


		public CustomDialogBuilder setNegativeButtonText(CharSequence text) {
			mNegativeText = text;
			return this;
		}
		
		public CustomDialogBuilder setPositiveTextColor(int color){
			mPositiveTextColor = color;
			return this;
		}
		
		public CustomDialogBuilder setPositiveButtonListener(View.OnClickListener onClickListener){
			mPositiveButton = onClickListener;
			return this;
		}
		
		
		public CustomDialogBuilder setNegativeButtonListener(View.OnClickListener onClickListener){
			mNegativeButton = onClickListener;
			return this;
		}
		
		public CustomDialogBuilder  setNegativeTextColor(int color){
			mNegativeTextColor = color;
			return this;
		}
		

		@Override
		protected Bundle prepareArguments() {
			Bundle args = new Bundle();
			args.putCharSequence(ARG_TITLE, mTitle);
			args.putInt(ARG_TITLE_COLOR, mTitleColor);
			args.putCharSequence(ARG_MESSAGE, mMessage);
			args.putInt(ARG_MESSAGE_COLOR, mMessageColor);
			args.putCharSequence(ARG_NEGATIVE_BUTTON, mNegativeText);
			args.putInt(ARG_NEGATIVE_BUTTON_COLOR, mNegativeTextColor);
			args.putCharSequence(ARG_POSITIVE_BUTTON, mPositiveText);
			args.putInt(ARG_POSITIVE_BUTTON_COLOR, mPositiveTextColor);
			
			return args;
		}

		@SuppressLint("HandlerLeak")
		@Override
		protected void configView() {
			execute = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					onNegativeButtonClicked(mNegativeButton);
					onPositiveButtonClicked(mPositiveButton);
				}
			};
		}


		@Override
		public void onNegativeButtonClicked(OnClickListener clickListener) {
			if(null==clickListener){
				negativeButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
							instance.dismiss();
							return;
					}
				});
			}else 
				negativeButton.setOnClickListener(clickListener);
			
		}

		@Override
		public void onPositiveButtonClicked(OnClickListener clickListener) {
			if(null==clickListener){
				positiveButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						  instance.dismiss();
					}
				});
			}else
		        positiveButton.setOnClickListener(clickListener);
		}
		
		
	}


	@Override
	public void onShow(DialogInterface dialog) {
		
	}
	
	
	public static CustomDialog  getInstance(){
		return instance;
	}

}
