package com.xyt.ygcf.util;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;

public class BaseUtil {
	public static Dialog mDialog;
	Context mcontext;

	/**
	 * 收起软键盘
	 * 
	 * @param activity
	 */
	public static void hideSoftInput(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(activity.getWindow().getDecorView()
					.getWindowToken(), 0);
		}
	}

	/**
	 * 弹出软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void showKeyboard(Context context, EditText editText) {
		showKeyboard(context, editText, 300);
	}

	/**
	 * 弹出软键盘
	 * 
	 * @param context
	 * @param editText
	 * @param delay
	 *            延时(毫秒)
	 */
	public static void showKeyboard(final Context context,
			final EditText editText, long delay) {
		editText.requestFocus();
		if (delay > 0) {
			// 如果是新打开Activity(onCreate),由于界面还没渲染完毕,必须设置一定的delay值,否则键盘会弹不出来
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
							InputMethodManager.HIDE_IMPLICIT_ONLY);
				}
			}, delay);
		} else {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	public static void LogI(Object iString) {
		Log.i("System.out", String.valueOf(iString));
	}

	public static void LogII(Object iString) {
		Log.i("System.outii", String.valueOf(iString));
	}

	public static void LogE(Object iString) {
		Log.e("System.out", String.valueOf(iString));
	}

	public static void print(String iString) {
//		System.out.print(iString);
	}

	public static void putKeyboard(Activity activity, EditText edit) {
		if (getInputMethodManager(activity) != null) {
			getInputMethodManager(activity).showSoftInput(edit,
					InputMethodManager.RESULT_SHOWN);
		}
	}

	public static InputMethodManager getInputMethodManager(Activity activity) {
		return (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * 判断包名是否存在,可用来判断是否安装了应用程序
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean AppIsExist(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}

	}

	/**
	 * 获取缓存路径 (如果有外部缓存目录则返回外部,否则返回内部[data/data/包名/cache]).
	 * 
	 * @param context
	 * @param uniqueName
	 *            二级目录名称
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		// 如果没有外部存储(Android 2.1或以下版本没有,设备没插SD卡也没有)
		if (context.getExternalCacheDir() == null) {
			return new File(Constants.ROOT_PATH + "cache/" + uniqueName);
		}
		final String cachePath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) 
				|| !isExternalStorageRemovable() ? context
				.getExternalCacheDir().getPath() : context.getCacheDir()
				.getPath();
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 检测外部存储是内置的还是可拆卸的
	 * 
	 * @return true=可卸载 false=内置
	 */
	@SuppressLint("NewApi")
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * 删除缓存
	 * 
	 * @param cacheDir
	 */
	public static void deleteCacheDir(File cacheDir) {
		if (cacheDir.exists()) {
			File[] cacheFiles = cacheDir.listFiles();
			if (cacheFiles == null) {
				return;
			}

			if (cacheFiles != null) {
				for (int j = 0; j < cacheFiles.length; j++) {
					cacheFiles[j].delete();
				}
			}
		}
	}

	public static void showProgressDialog(Context context) {
		makeProgressDialog(context, false);
	}

	public static void showProgressDialog(Context context, boolean cancel) {

		makeProgressDialog(context, cancel);
	}

	private static void makeProgressDialog(Context context, boolean cancel) {
		mDialog = new Dialog(context, R.style.DialogStyle);
		if (mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
			final View view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
			view.setVisibility(View.VISIBLE);
			mDialog.setContentView(view);
			WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
			lp.dimAmount = 0.0f;
			mDialog.getWindow().setAttributes(lp);
			mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setCancelable(cancel);
		}
	}
	
	public static void dissmissProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
}
