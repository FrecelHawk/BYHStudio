package com.xyt.ygcf.daying;

import java.util.Observable;
import java.util.Observer;

public class TaskManager extends Observable {

	public static final Integer CANCEL_ALL = 1;

	public void cancelAll() {
		setChanged();
		notifyObservers(CANCEL_ALL);
	}

	public void addTask(Observer task) {
		if (task == null)
			return;
		super.addObserver(task);
	}

	public void deleteTask(Observer task) {
		if (task == null)
			return;
		super.deleteObserver(task);
	}
}