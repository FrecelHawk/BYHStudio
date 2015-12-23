package org.android.dialog.test;

import org.android.dialog.R;
import org.android.dialog.fragment.CustomDialog;
import org.android.dialog.fragment.LoadingDialog;
import org.android.dialog.fragment.SingleButtonDialog;
import org.android.dialog.fragment.SingleButtonDialog.SingleButtonDialogBuilder;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class TestActivity extends FragmentActivity{

	
	private android.support.v4.app.DialogFragment dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	
	public void test(View view){
//		customDialog();
//		singleDialog();
//		   View mView = getLayoutInflater().inflate(R.layout.dialog_loading, null);
//		   LoadPopupWindown windown = new LoadPopupWindown(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		   windown.initialPopupWindow(mView);
		dialog = LoadingDialog.createBuilder(TestActivity.this, getSupportFragmentManager())
				.setDismessListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(getApplication(), "测试", 0).show();
					}
				})
				.show();
	}


	private void singleDialog() {
		SingleButtonDialog.createBuilder(TestActivity.this, getSupportFragmentManager())
		  .setTitle("拨打收货人电话")
		  .setMessage("13789203659")
		  .setMessageColor(getResources().getColor(R.color.oranger))
		  .setSingleButtonText("取消")
		  .setMessageButtonListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TestActivity.this, "信息", 0).show();
			}
		 })
		  .setSingleButtonListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                Toast.makeText(TestActivity.this, "点击", 0).show();				
			}
		})
		  .show();
	}

	
	

	public void customDialog() {
		CustomDialog.
		        createBuilder(TestActivity.this, getSupportFragmentManager())
		        .setTitle("测试")
		        .setMessage("this test message!")
		        .setNegativeButtonText("取消")
		        .setPositiveButtonText("确定")
		        .setNegativeTextColor(getResources().getColor(R.color.gray))
		        .setPositiveTextColor(getResources().getColor(R.color.oranger))
		        .setPositiveButtonListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
                            Toast.makeText(TestActivity.this, "测试", 0).show();
                            CustomDialog.getInstance().dismiss();
					}
				})
		        .show();
	}
}
