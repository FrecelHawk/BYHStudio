package com.xyt.ygcf.daying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.xyt.yangguangchufang.R;
//import com.dyne.homeca.bean.CameraInfo;
//import com.dyne.homeca.demo.MediaFetchWrap.Callback;
//import com.dyne.homeca.newtask.GenericTask;
//import com.dyne.homeca.newtask.TaskListener;
import com.xyt.ygcf.daying.MediaFetchWrap.Callback;

public class VideoActivity2 extends Activity implements TaskListener {
	private static final String TAG = "VideoActivity2";
	private MediaFetchWrap mMediaFetchWrap;

	private CameraInfo cameraInfo = null;

	private String sN;
	private String ip;
	Handler delayHandler = new Handler();

	private SurfaceView mWindow;
	private MediaFetchWrap.Callback mCallback = new Callback() {
		@Override
		public void errMsg(String msg) {
			Toast.makeText(VideoActivity2.this, msg, Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		public void infoMsg(String msg) {
			// Toast.makeText(VideoActivity2.this, msg,
			// Toast.LENGTH_SHORT).show();
		}
	};

	private void init() {
		mWindow = (SurfaceView) findViewById(R.id.window);

		cameraInfo = new CameraInfo();
		// cameraInfo.setCamerain("DYNE6C0363AMKNJ");
		// cameraInfo.setCameraserverurl("ddns1200.homca.com");
		cameraInfo.setCamerain(sN);
		cameraInfo.setCameraserverurl(ip);
		cameraInfo.setCamerasn("aa");

		mMediaFetchWrap = new MediaFetchWrap(this, cameraInfo, this, mWindow);
		mMediaFetchWrap.showWaitDialog("loading.......");
		mMediaFetchWrap.setmCallback(mCallback);

		mMediaFetchWrap.monitor();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video2);
		// 让屏幕保持不暗不关闭
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getIntentData();
		init();
	}

	public void getIntentData() {
		ip = getIntent().getStringExtra("ip");
		sN = getIntent().getStringExtra("sN");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMediaFetchWrap.closeWaitDoalog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {

			mMediaFetchWrap.release();
		}
		
	}

	boolean isMonitoring;

	@Override
	public void onProgressUpdate(GenericTask task, Bundle param) {
		if (task instanceof MediaFetchWrap.MonitorTask) {
			if (mMediaFetchWrap.isMonitoring()) {
				Integer first = param.getInt(GenericTask.INFO);
				if (first != null && first == 1) {
					isMonitoring = true;
				}
			}
		}
	}

	@Override
	public void onPreExecute(GenericTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancelled(GenericTask task, Bundle result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostExecute(GenericTask task, Bundle result) {
		// TODO Auto-generated method stub

	}
}