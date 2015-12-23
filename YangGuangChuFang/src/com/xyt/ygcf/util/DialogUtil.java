package com.xyt.ygcf.util;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.CityObjBean;
import com.xyt.ygcf.more.DownloadManager;
import com.xyt.ygcf.ui.my.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;


public class DialogUtil {
	public DialogUtil() {

	}

	/**
	 * 显示Toast提示信息
	 * 
	 * @param context
	 *            上下文
	 * @param msg
	 *            消息内容
	 * @param time
	 *            取值1或0，1表示长时间显示，0表示短时间显示
	 */
	public static void toast(Context context, String msg, int time) {
		Toast.makeText(context, msg, time).show();
	}

	/**
	 * 确认对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param msg
	 *            消息内容
	 * @param listener
	 *            确定按钮事件
	 */
	public static AlertDialog alert(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setNeutralButton("确定", listener);
			return builder.show();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 确认对话框,加入取消事件
	 * 
	 * @param mContext
	 * @param title
	 *            标题
	 * @param msg
	 *            消息内容
	 * @param listener
	 *            确定按钮事件
	 * @param calcelListener
	 */
	public static AlertDialog alertWithClose(final Activity actv, String title, String msg) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(actv);
			builder.create();
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setNeutralButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					actv.finish();
				}
			});
			builder.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
					actv.finish();
				}
			});
			return builder.show();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 确认对话框,加入自己要做事事情
	 * 
	 * @param actv
	 * @param title
	 *            标题
	 * @param msg
	 *            消息内容
	 * @param listener
	 *            确定按钮事件
	 * @param calcelListener
	 */
	public static AlertDialog alertWithDo(Activity actv, String title, String msg,
			DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener calcelListener) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(actv);
			builder.create();
			builder.setTitle(title);
			builder.setMessage(msg);
			if (listener != null)
				builder.setNeutralButton("确定", listener);
			if (calcelListener != null)
				builder.setOnCancelListener(calcelListener);
			return builder.show();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @param context
	 * @param title
	 *            标题
	 * @param msg
	 *            消息内容
	 * @param okListener
	 *            确定按钮事件
	 * @param cancleListener
	 *            否定按钮事件
	 */
	public static AlertDialog confirm(Context context, String title, String msg,
			DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancleListener) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setNeutralButton("确定", okListener);
			builder.setNegativeButton("取消", cancleListener);
			return builder.show();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @param context
	 * @param title
	 *            标题
	 * @param msg
	 *            消息内容
	 * @param okListener
	 *            确定按钮事件
	 * @param cancleListener
	 *            否定按钮事件
	 */
	public static AlertDialog confirm(Context context, String title, String msg, String btnOkStr, String btnNoStr,
			DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancleListener) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setNeutralButton(btnOkStr, okListener);
			builder.setNegativeButton(btnNoStr, cancleListener);
			return builder.show();
		} catch (Exception e) {
		}
		return null;
	}

	public static AlertDialog confirm(Context context, String title, String msg,
			DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener noListener,
			DialogInterface.OnCancelListener cancleListener) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setNeutralButton("确定", okListener);
			builder.setNegativeButton("取消", noListener);
			builder.setOnCancelListener(cancleListener);
			return builder.show();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param msg
	 *            显示内容
	 * @param isCancel
	 *            是否可取消
	 * @param cancleListener
	 *            取消事件监听器
	 * @param clickListener
	 *            点击事件
	 * @param keyListener
	 *            按键事件
	 * @return
	 */
	public static ProgressDialog showProgressDialogWithCancel(Context context, CharSequence title, CharSequence msg,
			boolean isCancel, DialogInterface.OnCancelListener cancleListener,
			DialogInterface.OnClickListener clickListener, DialogInterface.OnKeyListener keyListener) {
		ProgressDialog localProgressDialog = new ProgressDialog(context) {
			@Override
			public void onAttachedToWindow() {
				// 去掉对主页键的屏蔽
				// this.getWindow().setType(
				// WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
				super.onAttachedToWindow();
			}
		};
		localProgressDialog.setTitle(title);
		localProgressDialog.setMessage(msg);
		localProgressDialog.setCancelable(isCancel);

		if (isCancel) {
			localProgressDialog.setOnCancelListener(cancleListener);
			localProgressDialog.setOnKeyListener(keyListener);
			localProgressDialog.setButton(-1, "取  消", clickListener);
		}
		return localProgressDialog;
	}

	/**
	 * 不可取消的进度条
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @return
	 */
	public static ProgressDialog showPrgbWithoutCancle(Context context, CharSequence title, CharSequence msg) {
		// ProgressDialog localProgressDialog = new ProgressDialog(context) {
		// @Override
		// public void onAttachedToWindow() {
		// // 去掉对主页键的屏蔽
		// this.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		// super.onAttachedToWindow();
		// }
		// };
		ProgressDialog localProgressDialog = new ProgressDialog(context);
		localProgressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				switch (keyCode) {
				// 屏蔽 搜索,主页,返回 键
				case KeyEvent.KEYCODE_HOME:
				case KeyEvent.KEYCODE_BACK:
				case KeyEvent.KEYCODE_SEARCH:
					return true;
				}
				return false;
			}
		});
		localProgressDialog.setTitle(title);
		localProgressDialog.setMessage(msg);
		return localProgressDialog;
	}

	/**
	 * 缓存清除对话框
	 * @param context
	 */
	public static void showDataCleanDialog(Context context){
		MyDialog.Builder builder = new MyDialog.Builder(context, 4);
		builder.setContent("缓存已清除");
		builder.setBackButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create();
	}
	
	/**
	 * 最新版本已更新对话框
	 * @param context
	 */
	public static void showVersionAlreadyUpdataDialog(Context context){
		MyDialog.Builder builder = new MyDialog.Builder(context, 4);
		builder.setContent("您使用的版本已经是最新版本");
		builder.setBackButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create();
	}
	
	/**
	 * 版本更新对话框
	 * @param context
	 */
	public static void showVersionUpdataDialog(final Context context, String content, final String url, final String isForce){
		MyDialog.Builder builder = new MyDialog.Builder(context, 3);
		builder.setTitle("更新提示");
		builder.setContent(content);
		/*builder.setBackButton(new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});*/
		builder.setBackButton2("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(isForce.equals("YES")){
					android.os.Process.killProcess(android.os.Process.myPid());
				}else if(isForce.equals("") || isForce.equals("NO")){
					dialog.dismiss();
				}else {
					dialog.dismiss();
				}
			}
		});
		builder.setConfirmButton2("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownloadManager activity = new DownloadManager(context, url);
				activity.startDownload();
				if(isForce.equals("") || isForce.equals("NO")){
					dialog.dismiss();
				}
			}
		});
		builder.create();
	}
	
	/**
	 * 会员专用对话框
	 * @param context
	 */
	public static void toLoginDialog(final Context context){
		MyDialog.Builder builder = new MyDialog.Builder(context, 4);
		builder.setTitle("阳光厨房温馨提醒");
		builder.setContent("此功能为会员用户专享，请登录后再进行操作？");
		builder.setBackButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setConfirmButton("登录", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.create();
	}
	
	/**
	 * 选择默认城市 
	 * @param context 
	 * @param objBeans 
	 * @param clickListener
	 */
	public static void DefaultCityDialog(Context context, List<CityObjBean> objBeans, OnItemClickListener clickListener){
		MyDialog.Builder builder = new MyDialog.Builder(context, objBeans);
		builder.setTitle("选择默认城市");
		builder.setListListener(clickListener);
		builder.defaultCityDialog();
	}
	
	/**
	 * 解析参数
	 */
	public static String parse(Context context, Object param) {
		if (param instanceof Integer) { // 如果是资源id
			return context.getString((Integer) param);
		} else if (param instanceof String) { // 如果是字符串
			return param.toString();
		}
		return null;
	}

	/**
	 * 对话框监听器
	 * 
	 * @author Administrator
	 */
	public interface DialogListener {
		public void onDialogButtonClick(AlertDialog dialog, int which, Button button);
	}
}
