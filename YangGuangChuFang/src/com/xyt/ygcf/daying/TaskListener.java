package com.xyt.ygcf.daying;

import android.os.Bundle;

public interface TaskListener {
	void onPreExecute(GenericTask task);

	void onPostExecute(GenericTask task, Bundle result);

	void onProgressUpdate(GenericTask task, Bundle param);

	void onCancelled(GenericTask task, Bundle result);
}
