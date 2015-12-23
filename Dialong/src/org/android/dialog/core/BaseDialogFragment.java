package org.android.dialog.core;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public abstract class BaseDialogFragment extends DialogFragment implements
		DialogInterface.OnShowListener {

	// @Override
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	//
	// Bundle args = getArguments();
	// Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
	// dialog.setOnShowListener(this);
	// return dialog;
	// }

	protected abstract View initView();

	@Override
	public void onShow(DialogInterface dialog) {
		
	}

	public void showAllowingStateLoss(FragmentManager manager, String tag) {
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.commitAllowingStateLoss();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// bug in the compatibility library
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}

}
