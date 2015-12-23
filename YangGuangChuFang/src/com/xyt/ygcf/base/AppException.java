package com.xyt.ygcf.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.http.HttpException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 * 
 */
public class AppException extends Exception implements UncaughtExceptionHandler {
	/** 定义异常类型 */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_SQL = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_RUN = 0x07;
	public final static byte TYPE_JSON = 0x08;
	public final static byte TYPE_XML = 0x09;

	private byte type;
	private int code;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private AppException() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		if (!AppContext.curApp().getMode()) {
			this.saveErrorLog(excp);
		}
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	public String getErrorMsg() {
		String msg = null;
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			msg = "网络异常，错误码：" + this.getCode();
			break;
		case TYPE_HTTP_ERROR:
			msg = "网络异常，请求超时";
			break;
		case TYPE_SOCKET:
			msg = "网络异常，读取数据超时";
			break;
		case TYPE_NETWORK:
			msg = "网络连接失败，请检查网络设置";
			break;
		case TYPE_SQL:
			msg = "数据库异常，错误码：";
			break;
		case TYPE_IO:
			msg = "文件流异常";
			break;
		case TYPE_RUN:
			msg = "应用程序运行时异常";
			break;
		case TYPE_JSON:
//			msg = "JSON数据转换异常";
			msg = "网络异常，请求超时";
			break;
		case TYPE_XML:
			msg = "XML解析异常";
			break;
		}
		return msg;
	}

	/**
	 * 保存异常日志
	 * 
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/Log/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			// 没有挂载SD卡，无法写文件
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + (new Date().toLocaleString())
					+ "---------------------");
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}

	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0, e);
	}

	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0, e);
	}

	public static AppException json(Exception e) {
		return new AppException(TYPE_JSON, 0, e);
	}

	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}

	public static AppException io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new AppException(TYPE_IO, 0, e);
		}
		return run(e);
	}

	public static AppException db(Exception e, int code) {
		return new AppException(TYPE_SQL, code, e);
	}

	public static AppException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof HttpException) {
			return http(e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param mContext
	 * @return
	 */
	public static AppException getAppExceptionHandler() {
		return new AppException();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}

	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * 
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		final Context context = AppManager.getAppManager().curActivity();

		if (context == null) {
			return false;
		}

		final String crashReport = getCrashReport(context, ex);
		// 显示异常信息&发送报告
		new Thread() {
			public void run() {
				Looper.prepare();
				sendAppCrashReport(context, crashReport);
				Looper.loop();
			}

		}.start();
		return true;
	}

	/**
	 * 获取APP崩溃异常报告
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((AppContext) context.getApplicationContext())
				.getPackageInfo();
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "("
				+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
				+ "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		Log.e("exception", exceptionStr.toString());
		return exceptionStr.toString();
	}

	/**
	 * 发送App异常崩溃报告
	 * 
	 * @param cont
	 * @param crashReport
	 */
	public void sendAppCrashReport(final Context cont, final String crashReport) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("应用程序错误");
		builder.setMessage("很抱歉，应用程序出现错误，即将退出。\n请提交错误报告，我们会尽快修复这个问题！");
		builder.setPositiveButton("提交报告",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 发送异常报告
						Intent i = new Intent(Intent.ACTION_SEND);
						// i.setType("text/plain"); //模拟器
						i.setType("message/rfc822"); // 真机
						i.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
						i.putExtra(Intent.EXTRA_SUBJECT, "Android客户端 - 错误报告");
						i.putExtra(Intent.EXTRA_TEXT, crashReport);
						cont.startActivity(Intent.createChooser(i, "发送错误报告"));
						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 退出
				AppManager.getAppManager().AppExit(cont);
			}
		});
		builder.show();
	}
}
