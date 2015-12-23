package com.baiyuhui.huangyan.view;

import com.baiyuhui.huangyan.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author Administrator
 * 商城拨打电话
 *
 */
public class CustomDialog extends Dialog{
	
	private Context mContext;
	private CustomDialog mDialog;
	
	
	public CustomDialog(Context context) {
		super(context, android.R.style.Theme_Dialog);
		this.mContext = context;
	}
	
	public void createCallDialog(){
		View root = LayoutInflater.from(mContext).inflate(R.layout.store_details_dialog, null);
		
	}
	

}
