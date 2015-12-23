package com.xyt.ygcf.more;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.util.FileUtils;

/***
 * 版本更新下载新APK
 * 
 * @author Administrator
 * 
 */

public class DownloadManager {
	
	private String filePath; //本地存放的路径
	private String url; //下载地址
	private int totalSize = 0; //文件总大小
	private int alreadyDownloadSize = 0; //下载的大小
	private int downloadCount = 0; //下载计数
	private boolean isDownloading = false; //是否下载
	private String fileSize; //文件总 M数
	private String fileName; //文件名称
	private BigDecimal bigDecimal;
	private File targetDir;
	
	private Context context;
	private Notification notification;
	private NotificationManager notificationManager;
	private Intent notificationIntent;
	private PendingIntent pendingIntent;
	
	/** 开始下载 */
	private static final int DOWNLOAD_PREPARE = 0;
	/** 正在下载 */
	private static final int DOWNLOAD_WORK = 1;
	/** 下载完成 */
	private static final int DOWNLOAD_OK = 2;
	/** 下载错误 */
	private static final int DOWNLOAD_ERROR = 3;
	/** 下载取消 */
	private static final int DOWNLOAD_CANCEL = 4;
	
	private int NOTIFICATION_REF = 1;
	
	public DownloadManager() {
		
	}

	public DownloadManager(Context context, String url) {
		this.url = url;
		this.context = context;
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.notificationIntent = new Intent(context, context.getClass());
	    this.pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	}

	public void startDownload() {
		// 创建缓存目录
		targetDir = new File(FileUtils.getStorageDir(),
				"yangguangchufang/download/");
		if(!targetDir.exists()){
			targetDir.mkdirs();
		}
		fileName = url.substring(url.lastIndexOf("/") + 1);
		// 设置目标文件位置
		filePath = targetDir.getAbsolutePath()
				+ File.separator + fileName;
//		filePath = "/sdcard/";
//		filePath = filePath + fileName;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				downloadFile(url, filePath);
			}
		});
		thread.start();
	}

	private final Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_PREPARE:
				alreadyDownloadSize = 0;
				Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
				bigDecimal = new BigDecimal(totalSize * 1.0 / 1024 / 1024);
				fileSize =  bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M";
				
				int icon = R.drawable.ic_launcher; //通知图标
			    CharSequence tickerText = fileName + "开始下载..."; //状态栏(Status Bar)显示的通知文本提示
			    long when = System.currentTimeMillis(); //通知产生的时间，会在通知信息里显示
			    notification = new Notification(icon, tickerText, when);
			    notification.flags = Notification.FLAG_NO_CLEAR;
			    
			    notification.setLatestEventInfo(context, fileName, fileSize, pendingIntent);
			    notificationManager.notify(NOTIFICATION_REF, notification);
				break;
			case DOWNLOAD_WORK:
				int percent = (Integer) msg.obj;
				bigDecimal = new BigDecimal(alreadyDownloadSize * 1.0 / 1024 / 1024);
				String downSize = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "M";
				String content = "(" + fileSize +" / "+ downSize+") " + percent + "%";
				if(notification != null){
					notification.setLatestEventInfo(context, fileName, content, pendingIntent);
					notificationManager.notify(NOTIFICATION_REF, notification);
				}
				break;
			case DOWNLOAD_OK:
				isDownloading = false;
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
				context.startActivity(intent);
				notificationManager.cancel(NOTIFICATION_REF);
				Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_CANCEL:
				Toast.makeText(context, "下载被取消", Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_ERROR:
				if(notification != null){
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					notification.setLatestEventInfo(context, fileName, "文件下载错误", pendingIntent);
					notificationManager.notify(NOTIFICATION_REF, notification);
				}
				break;
			}
		}
	};
	
	/**
	 * @param what
	 */
	private void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}
	
	/**
	 * 发送信息
	 * 
	 * @param what
	 * @param obj
	 */
	private void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);

	}

	/**
	 * 
	 * @param 文件网络地址
	 * @param SD卡存放位置
	 */
	private void downloadFile(String url, String filePaths) {
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			InputStream is = conn.getInputStream();
			totalSize = conn.getContentLength();// 文件总大小
			if (totalSize < 1 || is == null) {
				sendMessage(DOWNLOAD_ERROR);
			} else {
				FileOutputStream fos = new FileOutputStream(filePaths);
				isDownloading = true;
				sendMessage(DOWNLOAD_PREPARE);

				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					if (!isDownloading) {
						// 下载被取消
						sendMessage(DOWNLOAD_CANCEL);
						break;
					}
					fos.write(bytes, 0, len);
					fos.flush();
					alreadyDownloadSize += len;
					int tmp = (int) (alreadyDownloadSize * 100 / totalSize);
					//为了防止频繁的通知导致应用吃紧，百分比增加5才通知一次
					if(downloadCount == 0 || tmp-5 > downloadCount){
						downloadCount += 5;
						sendMessage(DOWNLOAD_WORK, tmp);
					}
				}
				if (isDownloading) {
					sendMessage(DOWNLOAD_OK);
				}
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			sendMessage(DOWNLOAD_ERROR);
			e.printStackTrace();
		}
	}

}
