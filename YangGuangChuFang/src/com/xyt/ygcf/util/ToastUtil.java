package com.xyt.ygcf.util;

import com.xyt.yangguangchufang.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Toast 封装
 * 
 * 
 */
public class ToastUtil extends Toast {
	private final Context context;
	private Toast toast;
	private static ToastUtil toastUtil;

	public ToastUtil(Context context) {
		super(context);
		this.context = context;
	}

	public static ToastUtil getInstance(Context context) {
		if (toastUtil == null) {
			toastUtil = new ToastUtil(context);
		}
		return toastUtil;
	}

	@Override
	public void show() {
		super.show();
	}

	/**
	 * 只允许show一次的 Toast
	 * 
	 * @param msg
	 * @param isSound
	 */
	public void show(String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	/**
	 * 自定义位置的 Toast
	 * 
	 * @param msg
	 * @param isSound
	 *            是否声音提示
	 */
	public void showPosition(String msg, boolean isSound) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();

		View v = inflate.inflate(R.layout.new_data_toast, null);
		v.setMinimumWidth(dm.widthPixels);// 设置控件最小宽度为手机屏幕宽度
		TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
		tv.setText(msg);
		toast.setView(v);
		toast.setDuration(300);
		toast.setGravity(Gravity.BOTTOM, 0, (int) (dm.density * 50));
		toast.show();

	}

	/**
	 * 自定义位置的 Toast
	 * 
	 * @param msg
	 */
	public void showPosition(String msg) {
		showPosition(msg, false);
	}

	public void show(String Msg, int drawable) {
		Toast t = Toast.makeText(context, " " + Msg, Toast.LENGTH_SHORT);
		LinearLayout toastView = (LinearLayout) t.getView();
		toastView.setOrientation(LinearLayout.HORIZONTAL);
		toastView.setGravity(Gravity.CENTER_VERTICAL);
		ImageView imageView = new ImageView(context);
		imageView.setMaxHeight(15);
		imageView.setMaxWidth(15);
		imageView.setImageDrawable(context.getResources().getDrawable(drawable));
		toastView.addView(imageView, 0);
		t.show();
	}
}
