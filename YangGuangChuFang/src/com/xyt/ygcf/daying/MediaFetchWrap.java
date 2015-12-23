package com.xyt.ygcf.daying;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

//import com.dyne.homeca.bean.CameraInfo;
//import com.dyne.homeca.bean.CameraInfo.CameraState;
//import com.dyne.homeca.bean.CameraInfo.CameraType;
//import com.dyne.homeca.newtask.GenericTask;
//import com.dyne.homeca.newtask.TaskListener;
//import com.dyne.homeca.newtask.TaskManager;
//import com.dyne.homeca.newtask.TaskResult;
import com.raycommtech.ipcam.Code;
import com.raycommtech.ipcam.MediaFetch;
import com.raycommtech.ipcam.MediaFetchFactory;
import com.raycommtech.ipcam.VideoInfo;
import com.xyt.ygcf.daying.CameraInfo.CameraState;
import com.xyt.ygcf.daying.CameraInfo.CameraType;

public class MediaFetchWrap {
	public static final String TAG = "MediaFetchWrap";
	MediaFetch mMediaFetch;
	private SurfaceView mSurfaceView;
	private TaskListener mListener;
	private CameraInfo mCameraInfo;

	public CameraInfo getmCameraInfo() {
		return mCameraInfo;
	}

	public void setmCameraInfo(CameraInfo cameraInfo) {
		this.mCameraInfo = cameraInfo;
	}

	private WorkHandler mWorkHandler;
	Context context;
	private boolean cameraOpen;

	public static enum Mode {
		NONE, MONITOR, PLAY
	};

	Mode mMode = Mode.NONE;

	public MediaFetchWrap(Context context, CameraInfo cameraInfo,
			TaskListener callback, SurfaceView surfaceView) {
		this.context = context;
		mCameraInfo = cameraInfo;
		mSurfaceView = surfaceView;
		mListener = callback;
		mWorkHandler = new WorkHandler(this);
	}

	public static interface Callback {
		public void errMsg(String msg);

		public void infoMsg(String msg);
	}

	private Callback mCallback;

	public static class WorkHandler extends Handler {
		WeakReference<MediaFetchWrap> mMediaFetchWrap;

		public WorkHandler(MediaFetchWrap mediaFetchWrap) {
			mMediaFetchWrap = new WeakReference<MediaFetchWrap>(mediaFetchWrap);
		}

		@Override
		public void handleMessage(Message msg) {
			MediaFetchWrap mediaFetchWrap = mMediaFetchWrap.get();
			if (mediaFetchWrap != null) {
				switch (msg.what) {
				case 1: // connectSuccess
				case Code.MSG_OPENCAMERA_SUCCESS:
					mediaFetchWrap.signalOpenCamera(true);
					break;
				case 2: // connectError
				case Code.MSG_OPENCAMERA_FAILURE:
					mediaFetchWrap.signalOpenCamera(false);
					break;
				case Code.MSG_INFO:
					if (mediaFetchWrap.mCallback != null) {
						String str = msg.obj.toString();

						mediaFetchWrap.mCallback.infoMsg(str);

					}
					break;
				case Code.MSG_ERROR:
					mediaFetchWrap.release();
					if (mediaFetchWrap.mCallback != null)
						mediaFetchWrap.mCallback.errMsg(msg.obj.toString());
					break;
				}
			}
		}
	}

	public static final String GROUPNAME = "GROUPNAME";
	public static final String FILEMAPLIST = "FILEMAPLIST";
	public static final String FETCHEDALL = "FETCHEDALL";
	public static final String PATH = "PATH";
	public static final String TIME = "TIME";
	public static final String SIZE = "SIZE";

	private Map<String, Map<String, Object>> vodMapList;

	public static class MonitorTask extends CameraTask {
		public MonitorTask(TaskListener taskListener,
				MediaFetchWrap mediaFetchWrap, Boolean autoRelease) {
			super(taskListener, mediaFetchWrap, autoRelease);
			mOpenWithVideo = true;
		}

		@Override
		protected void doWork() throws InterruptedException {
			WeakReference<MediaFetch> weakMediaFetch = null;
			mMediaFetchWrap.lockConnection.readLock().lock();
			try {
				if (checkMediaFetch()) {
					weakMediaFetch = new WeakReference<MediaFetch>(
							mMediaFetchWrap.mMediaFetch);

					// if (mMediaFetchWrap.getmCameraInfo().isOldDDns() ==
					// false) {
					mMediaFetchWrap.closeWaitDoalog();
					mMediaFetchWrap.mMediaFetch.StartRealPlay(0); //komorilee,打开实时监控
					
					// }

				}
			} finally {
				mMediaFetchWrap.lockConnection.readLock().unlock();
			}

			int tryCnt = 0;
			while (true) {
				Thread.sleep(1000);
				mMediaFetchWrap.lockConnection.readLock().lock();
				try {
					MediaFetch mediaFetch = null;

					if (weakMediaFetch != null
							&& (mediaFetch = weakMediaFetch.get()) != null) {
						taskResult.putInt(INFO, 0);
						if (mMediaFetchWrap.mMode == Mode.NONE) {
							if (mediaFetch.getSumSize() > 0) {
								mMediaFetchWrap.mMode = Mode.MONITOR;
								mMediaFetchWrap.mCameraDirection = mediaFetch
										.getCameraDirection();
								mMediaFetchWrap.snap();

								taskResult.putInt(INFO, 1);
							} else if (tryCnt > 60) {
								taskResult.putSerializable(GenericTask.RESULT,
										TaskResult.ERRMONITOROUTOFTIME);
								break;
							}
							tryCnt++;
						}
						taskResult.putSerializable(GenericTask.RESULT,
								TaskResult.PROCESS);
						publishProgress(taskResult);
					} else {
						break;
					}
				} finally {
					mMediaFetchWrap.lockConnection.readLock().unlock();
				}
			}
		}
	}
	
	private ProgressDialog loginProcessDialog;
	
	public void showWaitDialog(String str) {
		loginProcessDialog = ProgressDialog.show(context, null, str);
		loginProcessDialog.setCancelable(false);
	}
	
	public void closeWaitDoalog() {
		if (loginProcessDialog != null) {
			if (loginProcessDialog.isShowing()) {
				loginProcessDialog.dismiss();
				loginProcessDialog = null;
			}
		}
	}

	private TaskManager taskManager = new TaskManager();

	ReentrantReadWriteLock lockConnection = new ReentrantReadWriteLock();
	private Lock lockWaitConnectingResult = new ReentrantLock();
	private Condition conditionWaitConnectingResult = lockWaitConnectingResult
			.newCondition();
	boolean checkVideoStatus = false;

	TaskResult checkConnect(boolean openWidthVideo) {
		if (lockConnection.writeLock().tryLock() == false) {
			return TaskResult.CONNECT_BUSY;
		}

		lockWaitConnectingResult.lock();
		try {
			boolean onLine = false;
			if (WebServiceHelper.testNet("ban.homca.com", 5000))
				onLine = true;

			if (mMediaFetch == null || cameraOpen == false) {
				VideoInfo vi = mCameraInfo.getVideoInfo();
				if (vi == null) {
					vi = new VideoInfo();
					vi.setTitle(mCameraInfo.getCamerasn());
					vi.setDdnsname(mCameraInfo.getPureCamerain());
					vi.setDdnsServer(mCameraInfo.getCameraserverurl());
					mCameraInfo.setVideoInfo(vi);
				}

				if (onLine) {
					CameraFun.getStatus(mCameraInfo);
					if (mCameraInfo.isOldDDns() == false) {
						if (mCameraInfo.isValid() != CameraState.ONLINE)
							return TaskResult.OPENCAMERAFAILD;
					}
				}

				int cameraid = mCameraInfo.getNvrCnt() == -1 ? 0 : mCameraInfo
						.getNvrCnt();

				vi.setChannelId(cameraid);

				if (onLine) {
					if (mCameraInfo.getCameraType() == CameraType.AGENTSHARE) {
						vi.setConnectMode(false);
						vi.SetDistributeType(true);
					} else {
						vi.setConnectMode(false);
						vi.SetDistributeType(false);
					}
				} else {
					vi.setConnectMode(true);
					vi.SetDistributeType(false);
					vi.setUsername(mCameraInfo.getEquipadmin());
					vi.setPassword(mCameraInfo.getEquippass());
				}

				mMediaFetch = MediaFetchFactory.makeMeidaFetch(mWorkHandler,
						mSurfaceView, vi);
			}
			if (mMediaFetch == null)
				return TaskResult.OPENCAMERAFAILD;
			else if (cameraOpen == false) {
				// if (openWidthVideo && mCameraInfo.isOldDDns()) {
				// if (openWidthVideo) {
				// mMediaFetch.start();
				// Log.d(TAG, "mMediaFetch.start");
				// } else {
				// mMediaFetch.opencamera();
				// }
				mMediaFetch.opencamera();

				conditionWaitConnectingResult.await(60, TimeUnit.SECONDS);
			}

			if (cameraOpen) {
				return TaskResult.OK;
			} else {
				return TaskResult.OPENCAMERAOUTOFTIME;
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			return TaskResult.TASKINTERRUPTED;
		} finally {
			lockWaitConnectingResult.unlock();
			lockConnection.writeLock().unlock();
		}
	}

	public void signalOpenCamera(boolean success) {
		lockWaitConnectingResult.lock();
		try {
			cameraOpen = success;
			conditionWaitConnectingResult.signalAll();
		} finally {
			lockWaitConnectingResult.unlock();
		}
	}

	public void closeMediaFetch(boolean mandatory) {
		if (mandatory)
			lockConnection.writeLock().lock();
		else {
			if (lockConnection.writeLock().tryLock() == false)
				return;
		}

		try {
			if (mMediaFetch != null) {
				if (mMode == Mode.PLAY)
					mMediaFetch.VODStopPlayRecord();
				if (mMode == Mode.MONITOR)
					mMediaFetch.StopRealPlay();
				mMediaFetch.closecamera();
				mMediaFetch = null;
			}
			cameraOpen = false;
			mMode = Mode.NONE;
			mIsAudio = false;
			mIsTalk = false;
			mCameraDirection = 0;
			mIsRecording = false;
		} finally {
			lockConnection.writeLock().unlock();
		}
	}

	public void release() {
		taskManager.cancelAll();

		if (mDelayStop != null)
			mWorkHandler.removeCallbacks(mDelayStop);

		closeMediaFetch(true);
	}

	Date curDate = new Date();
	Date minDate = new Date();
	Date maxDate = new Date();
	private Map<String, Object> curMap = new HashMap<String, Object>();

	public Map<String, Object> getCurMap() {
		return curMap;
	}

	public void monitor() {
		taskManager.addTask(CameraTask.singleTask(mListener, this,
				CameraTask.MONITORTASK, true, null));
	}

	private boolean mCanControl = true;

	public boolean isPlaying() {
		return mMediaFetch != null && mMode == Mode.PLAY;
	}

	public boolean isMonitoring() {
		return mMediaFetch != null && mMode == Mode.MONITOR;
	}

	public Map<String, Map<String, Object>> getVodMapList() {
		return vodMapList;
	}

	public Callback getmCallback() {
		return mCallback;
	}

	public void setmCallback(Callback mCallback) {
		this.mCallback = mCallback;
	}

	public boolean ismCanControl() {
		return mCanControl;
	}

	public void setmCanControl(boolean mCanControl) {
		this.mCanControl = mCanControl;
	}

	public void preGo(int i) {
		if (isMonitoring() && ismCanControl()) {
			mMediaFetch.preGo(i);
		}
	}

	public void ptzGo(int i) {
		if (isMonitoring() && ismCanControl()) {
			mMediaFetch.ptzGo(i);
		}
	}

	public void ptzGoUp() {
		ptzGo(Code.UP);
	}

	public void ptzGoDown() {
		ptzGo(Code.DOWN);
	}

	public void ptzGoLeft() {
		ptzGo(Code.LEFT);
	}

	public void ptzGoRight() {
		ptzGo(Code.RIGHT);
	}

	public void ptzGoUpStop() {
		ptzGo(Code.UP);
		delayStop();
	}

	public void ptzGoDownStop() {
		ptzGo(Code.DOWN);
		delayStop();
	}

	public void ptzGoLeftStop() {
		ptzGo(Code.LEFT);
		delayStop();
	}

	public void ptzGoRightStop() {
		ptzGo(Code.RIGHT);
		delayStop();
	}

	public void ptzGoFar() {
		ptzGo(Code.FAR);
	}

	public void ptzGoNear() {
		ptzGo(Code.NEAR);
	}

	public void ptzGoStop() {
		ptzGo(Code.STOP);
	}

	public void startAudio() {
		if (isMonitoring() && ismCanControl() && mIsAudio == false) {
			mIsAudio = true;
			mMediaFetch.startAudio();
		}
	}

	public void endAudio() {
		if (isMonitoring() && ismCanControl() && mIsAudio == true) {
			mIsAudio = false;
			mMediaFetch.endAudio();
		}
	}

	public boolean switchAudio() {
		if (mIsAudio) {
			endAudio();
		} else {
			startAudio();
		}
		return mIsAudio;
	}

	public void startTalk() {
		if (isMonitoring() && ismCanControl() && mIsTalk == false) {
			mIsTalk = true;
			mMediaFetch.startTalk();
		}
	}

	public void endTalk() {
		if (isMonitoring() && ismCanControl() && mIsTalk == true) {
			mIsTalk = false;
			mMediaFetch.endTalk();
		}
	}

	public boolean switchTalk() {
		if (mIsTalk) {
			endTalk();
		} else {
			startTalk();
		}
		return mIsTalk;
	}

	public void setCameraDirection(int flag) {
		if (isMonitoring() && ismCanControl() && mCameraDirection != flag) {
			mCameraDirection = flag;
			mMediaFetch.setCameraDirection(flag);
		}
	}

	public boolean switchCameraDirection() {
		if (isScreenPositive()) {
			setCameraDirection(1);
		} else {
			setCameraDirection(0);
		}
		return isScreenPositive();
	}

	public Integer getSumSize() {
		if (isMonitoring()) {
			return mMediaFetch.getSumSize();
		}
		return 0;
	}

	private DelayStop mDelayStop;

	private class DelayStop implements Runnable {
		public void run() {
			ptzGoStop();
		}
	}

	private void delayStop() {
		if (mDelayStop != null)
			mWorkHandler.removeCallbacks(mDelayStop);
		mDelayStop = new DelayStop();
		mWorkHandler.postDelayed(mDelayStop, 1000);
	}

	private boolean mIsAudio;
	private boolean mIsTalk;
	private int mCameraDirection;
	private boolean mIsRecording;

	private boolean mIsTrueAudio;

	public boolean ismIsRecording() {
		return mIsRecording;
	}

	public boolean ismIsAudio() {
		Log.d(TAG, "audio" + String.valueOf(mIsAudio));
		return mIsAudio;
	}

	public boolean ismIsTalk() {
		Log.d(TAG, "talk" + String.valueOf(mIsTalk));
		return mIsTalk;
	}

	public boolean isScreenPositive() {
		return mCameraDirection == 0;
	}

	public void startRecord() {
		if (isMonitoring() && ismCanControl() && mIsRecording == false) {
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.CHINA);
			String desFile = FileUtils.VIDEOPATH + "/"
					+ mCameraInfo.getCamerain() + "_" + format.format(now);
			mIsRecording = true;
			mMediaFetch.startRecord(desFile + FileUtils.VIDEOEXT);
			new Snap(Snap.PHOTO_PIC, desFile + FileUtils.SNAPEXT).start();
		}
	}

	public void startRecord(String filePath) {
		if (isMonitoring() && ismCanControl() && mIsRecording == false) {
			mIsRecording = true;
			mMediaFetch.startRecord(filePath);
		}
	}

	public void stopRecord() {
		if (isMonitoring() && ismCanControl() && mIsRecording == true) {
			mIsRecording = false;
			mMediaFetch.stopRecord();
		}
	}

	public boolean switchRecord() {
		if (mIsRecording)
			stopRecord();
		else
			startRecord();
		return mIsRecording;
	}

	public void photo() {
		if (isMonitoring() && ismCanControl()) {
			new Snap(Snap.PHOTO_PIC, null).start();
		}
	}

	public void snap() {
		new Snap(Snap.PHOTO_SNAP, null).start();
	}

	public void snap(String filePath) {
		if (isMonitoring() && ismCanControl()) {
			mMediaFetch.snap(filePath);
		}
	}

	public boolean ismIsTrueAudio() {
		return mIsTrueAudio;
	}

	public void setmIsTrueAudio(boolean mIsTrueAudio) {
		this.mIsTrueAudio = mIsTrueAudio;
	}

	private class Snap extends Thread {
		public static final int PHOTO_SNAP = 0;
		public static final int PHOTO_PIC = 1;

		public Snap(int photoType, String fileName) {
			this.photoType = photoType;
			this.fileName = fileName;
		}

		private int photoType;
		private String fileName;

		@Override
		public void run() {
			String desFile = null;
			switch (photoType) {
			case PHOTO_SNAP:
				// ibHidePicMsg = true;
				desFile = FileUtils.SNAPPATH + "/" + mCameraInfo.getCamerain()
						+ FileUtils.SNAPEXT;

				FileUtils.deleteFile(desFile);

				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case PHOTO_PIC:
				if (fileName != null) {
					// ibHidePicMsg = true;
					desFile = fileName;
				} else {
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyyMMddHHmmss", Locale.CHINA);
					desFile = FileUtils.PHOTOPATH + "/"
							+ mCameraInfo.getCamerain() + "_"
							+ format.format(now) + FileUtils.SNAPEXT;
				}
				break;
			}

			lockConnection.readLock().lock();
			try {
				if (isMonitoring()) {
					mMediaFetch.snap(desFile);
				}
			} finally {
				lockConnection.readLock().unlock();
			}
		}
	};
}
