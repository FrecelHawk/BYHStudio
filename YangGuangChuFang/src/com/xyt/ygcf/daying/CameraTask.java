package com.xyt.ygcf.daying;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.xyt.ygcf.daying.MediaFetchWrap.MonitorTask;

import android.os.Bundle;

//import com.dyne.homeca.demo.MediaFetchWrap.MonitorTask;
//import com.dyne.homeca.newtask.GenericTask;
//import com.dyne.homeca.newtask.TaskListener;
//import com.dyne.homeca.newtask.TaskResult;

public abstract class CameraTask extends GenericTask {
	private static Map<String, CameraTask> map = new HashMap<String, CameraTask>();

	public static final String MONITORTASK = MonitorTask.class.getName();

	private static final String TAG = "CameraTask";
	protected MediaFetchWrap mMediaFetchWrap;
	protected Boolean mAutoRelease;
	protected boolean mOpenWithVideo = false;

	public boolean openWithVideo() {
		return mOpenWithVideo;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		map.put(getClass().getName(), null);
	}

	public CameraTask(TaskListener taskListener, MediaFetchWrap mediaFetchWrap,
			Boolean autoRelease) {
		super(mediaFetchWrap.context, taskListener);
		mMediaFetchWrap = mediaFetchWrap;
		mAutoRelease = autoRelease;
	}

	public void init(String json) {

	}

	public static CameraTask singleTask(TaskListener taskListener,
			MediaFetchWrap mediaFetchWrap, String taskType,
			Boolean autoRelease, String params) {
		if (map.get(taskType) == null) {
			try {
				Constructor<?> c = Class.forName(taskType).getConstructor(
						new Class[] { TaskListener.class, MediaFetchWrap.class,
								Boolean.class });

				CameraTask cameraTask = (CameraTask) c.newInstance(
						taskListener, mediaFetchWrap, autoRelease);
				map.put(taskType, cameraTask);

				cameraTask.init(params);
				cameraTask.execute();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return map.get(taskType);
	}

	protected boolean checkMediaFetch() {
		if (mMediaFetchWrap.mMediaFetch == null) {
			taskResult.putSerializable(GenericTask.RESULT,
					TaskResult.DISCONNECTED);
			return false;
		} else
			return true;
	}

	@Override
	protected void onPostExecute(Bundle result) {
		super.onPostExecute(result);

		map.put(getClass().getName(), null);
	}

	protected TaskResult init() {
		return mMediaFetchWrap.checkConnect(mOpenWithVideo);
	}

	protected abstract void doWork() throws InterruptedException;

	@Override
	protected Bundle _doInBackground(Object... params) {

		TaskResult lTaskResult = init();
		if (lTaskResult == TaskResult.OK) {
			try {
				doWork();
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
			if (mAutoRelease)
				mMediaFetchWrap.closeMediaFetch(false);
		} else {
			taskResult.putSerializable(GenericTask.RESULT, lTaskResult);
		}

		return taskResult;
	}
}