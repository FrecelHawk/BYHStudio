package com.xyt.ygcf.daying;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

//import com.dyne.homeca.module.Feedback;

public abstract class GenericTask extends AsyncTask<Object, Bundle, Bundle>
		implements Observer {
	private String TAG = "GenericTask";
	public static final String TASKNAME = "TASKNAME";
	public static final String MSG = "MSG";
	public static final String RESULT = "RESULT";
	public static final String INFO = "INFO";
	public static final String PERCENT = "PERCENT";

	private TaskListener mListener = null;
	private Feedback mFeedback = null;

	private boolean isCancelable = true;
	protected Bundle taskResult;
	protected Context context;

	public GenericTask() {
		super();
		TAG = this.getClass().getSimpleName();
	}

	public GenericTask(Context context) {
		this();
		this.context = context;
	}

	public GenericTask(Context context, TaskListener taskListener) {
		this(context);
		this.mListener = taskListener;
	}

	abstract protected Bundle _doInBackground(Object... params);

	public void setListener(TaskListener taskListener) {
		mListener = taskListener;
	}

	public TaskListener getListener() {
		return mListener;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		if (mListener != null) {
			mListener.onCancelled(this, taskResult);
		}

		if (mFeedback != null) {
			mFeedback.cancel("");
		}

		Log.d(TAG, "onCancelled");
	}

	@Override
	protected void onPostExecute(Bundle result) {
		super.onPostExecute(result);

		if (mListener != null) {
			mListener.onPostExecute(this, result);
		}

		if (mFeedback != null) {
			mFeedback.success("");
		}

		Log.d(TAG, "onPostExecute");
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mListener != null) {
			mListener.onPreExecute(this);
		}

		if (mFeedback != null) {
			mFeedback.start("");
		}

		Log.d(TAG, "onPreExecute");
	}

	@Override
	protected void onProgressUpdate(Bundle... values) {
		super.onProgressUpdate(values);

		if (values != null && values.length > 0) {
			if (mListener != null) {
				mListener.onProgressUpdate(this, values[0]);
			}

			if (mFeedback != null) {
				mFeedback.update(values[0]);
			}
		}

		Log.d(TAG, "onProgressUpdate");
	}

	@Override
	protected Bundle doInBackground(Object... params) {
		taskResult = new Bundle();
		taskResult.putString(TASKNAME, this.getClass().getName());
		taskResult.putSerializable(GenericTask.RESULT, TaskResult.OK);

		_doInBackground(params);
		if (mFeedback != null) {
			mFeedback.update(99);
		}
		return taskResult;
	}

	public void update(Observable o, Object arg) {
		if (TaskManager.CANCEL_ALL == (Integer) arg && isCancelable) {
			if (getStatus() == GenericTask.Status.RUNNING) {
				cancel(true);
			}
		}
	}

	public void setCancelable(boolean flag) {
		isCancelable = flag;
	}

	public void setFeedback(Feedback feedback) {
		mFeedback = feedback;
	}
}
