package org.android.dialog.fragment;

import org.android.dialog.R;
import org.android.dialog.core.BaseDialogBuilder;
import org.android.dialog.core.BaseDialogFragment;
import org.android.dialog.iface.MessageButtonDailogListener;
import org.android.dialog.iface.SingleButtonDialogListener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


@SuppressLint({ "HandlerLeak", "InflateParams" })
public class SingleButtonDialog extends BaseDialogFragment{

	protected final static String ARG_MESSAGE = "message";
    protected final static String ARG_MESSAGE_COLOR ="message_color";
	protected final static String ARG_TITLE = "title";
	protected final static String ARG_TITLE_COLOR="title_color";
	protected final static String ARG_SINGLE_BUTTON="single_button";
	protected final static String ARG_SINGLE_BUTTON_COLOR="single_button_color";
	protected final static String ARG_GRAVITY="gravity";
	
	private static SingleButtonDialog  instance;
	
	
	private static TextView title;
	
	private static TextView message;
	
	private static Button  singleButton;
	
	
	private static Handler execute;
	
	
	
	@Override
	public void onShow(DialogInterface dialog) {
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog  dialog = new Dialog(getActivity(),R.style.singleDialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnShowListener(this);
		return dialog;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected View initView() {
		instance = this;
		View view = getLayoutInflater(getArguments()).inflate(R.layout.dialog_single_button_layout, null);
		title = (TextView) view.findViewById(R.id.title);
		message = (TextView) view.findViewById(R.id.message);
		singleButton = (Button) view.findViewById(R.id.button);
		configGravity();
		configTitle(title);
		configMessage(message);
		configSingle(singleButton);
		execute.sendEmptyMessage(0);
		return view;
	}
	
	public void configGravity(){
		if(null == getArguments()) return;
		
		if(0!=getArguments().getInt(ARG_GRAVITY))
			   gravity(getArguments().getInt(ARG_GRAVITY));
		   else
			   gravity(Gravity.BOTTOM); 
			
	}

	private void configSingle(Button but) {
		 if(null == getArguments())  return;
		 
		
		 if(null!=getArguments().getCharSequence(ARG_SINGLE_BUTTON))
			   but.setText(getArguments().getCharSequence(ARG_SINGLE_BUTTON));
		 
		 if(0!=getArguments().getInt(ARG_SINGLE_BUTTON_COLOR))
			   but.setTextColor(getArguments().getInt(ARG_SINGLE_BUTTON_COLOR));
	}

	private void configMessage(TextView tv) {
		if(null == getArguments()) return; 
		
		if(null!=getArguments().getCharSequence(ARG_MESSAGE))
			tv.setText(getArguments().getCharSequence(ARG_MESSAGE));
	    	
		if(0!=getArguments().getInt(ARG_MESSAGE_COLOR))
			tv.setTextColor(getArguments().getInt(ARG_MESSAGE_COLOR));
		tv.setGravity(Gravity.CENTER);
	}

	private void configTitle(TextView tv) {
		if(null==getArguments()) return ;
		
		 if(null==getArguments().getCharSequence(ARG_TITLE))
			   tv.setVisibility(View.GONE);
		
		if(null!=getArguments().getCharSequence(ARG_TITLE))
			 tv.setText(getArguments().getCharSequence(ARG_TITLE));
		
		if(0!=getArguments().getInt(ARG_TITLE_COLOR))
			 tv.setTextColor(getArguments().getInt(ARG_TITLE_COLOR));
	}

	public void gravity(int gravity) {
		final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.gravity = gravity;
		getDialog().getWindow().setAttributes(layoutParams);
	}
	
	
	
	public  static SingleButtonDialogBuilder  createBuilder(Context context,FragmentManager manager){
		return new SingleButtonDialogBuilder(context,manager, SingleButtonDialog.class);
	}

	
	public static class SingleButtonDialogBuilder  extends BaseDialogBuilder<SingleButtonDialogBuilder> implements SingleButtonDialogListener,MessageButtonDailogListener{

		private CharSequence  mTitle;
		
		private int mTitleColor;
		
		private CharSequence  mMessage;
		
		private int mMessageColor;
		
		private CharSequence  mSingleButton;
		
		private int mSingeButtonColor;
		
		private int mGravity;
		
	    private View.OnClickListener messageListener;
		
		private View.OnClickListener singleListener;

		private View.OnClickListener titleListener;
		
		public SingleButtonDialogBuilder(Context context,
				FragmentManager fragmentManager,
				Class<? extends BaseDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}
		
		
		public SingleButtonDialogBuilder  setTitle(CharSequence title){
			  mTitle = title;
			  return  self();
		}
		
		
		public SingleButtonDialogBuilder  setTtileColor(int color){
			mTitleColor = color;
			return self();
		}

		public SingleButtonDialogBuilder  setTitleListener(View.OnClickListener listener){
			titleListener = listener;
			return self();
		}
		
		public SingleButtonDialogBuilder setMessage(CharSequence message){
			mMessage = message;
			return self();
		}
		
		public SingleButtonDialogBuilder setMessageColor(int color){
			mMessageColor = color;
			return self();
		}
		
		public SingleButtonDialogBuilder setSingleButtonText(CharSequence singleButtonTxt){
			mSingleButton = singleButtonTxt;
			return self();
		}
		
		public SingleButtonDialogBuilder setSingleButtonColor(int butColor){
			mSingeButtonColor = butColor;
			return self();
		}
		
		public SingleButtonDialogBuilder setSingleButtonListener(View.OnClickListener listener){
			singleListener = listener;
			return self();
		}
		
		public SingleButtonDialogBuilder setMessageButtonListener(View.OnClickListener listener){
			messageListener = listener;
			return self();
		}
		
	    public SingleButtonDialogBuilder setGravity(final int gravity) {
	    	mGravity = gravity;
	    	return self();
	    	
	    }
		

		@Override
		protected SingleButtonDialogBuilder self() {
			return this;
		}

		@Override
		protected Bundle prepareArguments() {
			Bundle args = new Bundle();
			args.putCharSequence(ARG_TITLE, mTitle);
			args.putInt(ARG_TITLE_COLOR, mTitleColor);
			args.putCharSequence(ARG_MESSAGE, mMessage);
			args.putInt(ARG_MESSAGE_COLOR, mMessageColor);
			args.putCharSequence(ARG_SINGLE_BUTTON, mSingleButton);
			args.putInt(ARG_SINGLE_BUTTON_COLOR, mSingeButtonColor);
			args.putInt(ARG_GRAVITY, mGravity);
			return args;
		}

		@Override
		protected void configView() {
			execute = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					onTitleClicked(titleListener);
					onMessageClicked(messageListener);
					onSingleButtonClicked(singleListener);
				}
			};
		}

		@Override
		public void onMessageClicked(OnClickListener clickListener) {
			if(null==clickListener){
				message.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						instance.dismiss();
					}
				});
			}else
				 message.setOnClickListener(clickListener);
		}

		@Override
		public void onSingleButtonClicked(OnClickListener clickListener) {
			if(null==clickListener){
				 singleButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						instance.dismiss();
					}
				});
			}else
				singleButton.setOnClickListener(clickListener);
		}

		public void onTitleClicked(OnClickListener clickListener){
			if(null!=clickListener)
				  title.setOnClickListener(titleListener);
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	
	

}
