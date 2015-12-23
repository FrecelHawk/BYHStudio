package com.baiyuhui.huangyan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.FindAntifakeActivity;
import com.baiyuhui.huangyan.activity.FindCircleActivity;
import com.baiyuhui.huangyan.activity.FindNewbieActivity;
import com.baiyuhui.huangyan.activity.FindPrepActivity;
import com.baiyuhui.huangyan.activity.FindRadioActivity;
import com.baiyuhui.huangyan.activity.FindStoryActivity;

/**
 * 
 * @author Administrator
 * 
 *         :发现
 */
public class FindFragment extends Fragment implements OnClickListener {
	// 六个btn
	private RadioButton circlebtn, storybtn, radiobtn, antifakebtn, prepbtn,
			newbiebtn;

	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_find, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();

		circlebtn = (RadioButton) view.findViewById(R.id.circle_btn);
		storybtn = (RadioButton) view.findViewById(R.id.story_btn);
		radiobtn = (RadioButton) view.findViewById(R.id.radio_btn);
		antifakebtn = (RadioButton) view.findViewById(R.id.antifake_btn);
		prepbtn = (RadioButton) view.findViewById(R.id.prep_btn);
		newbiebtn = (RadioButton) view.findViewById(R.id.newbie_btn);

		circlebtn.setOnClickListener(this);
		storybtn.setOnClickListener(this);
		radiobtn.setOnClickListener(this);
		antifakebtn.setOnClickListener(this);
		prepbtn.setOnClickListener(this);
		newbiebtn.setOnClickListener(this);
	}

	private void initViews() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.circle_btn: // 圈子

			startActivity(new Intent(getActivity(), FindCircleActivity.class));
			break;
		case R.id.story_btn:// 故事
			startActivity(new Intent(getActivity(), FindStoryActivity.class));
			break;
		case R.id.radio_btn: // 电台
			startActivity(new Intent(getActivity(), FindRadioActivity.class));
			break;
		case R.id.antifake_btn: // 防伪
			startActivity(new Intent(getActivity(), FindAntifakeActivity.class));
			break;
		case R.id.prep_btn: // 关于我们
			startActivity(new Intent(getActivity(), FindPrepActivity.class));
			break;
		case R.id.newbie_btn: // 新手指南
			startActivity(new Intent(getActivity(), FindNewbieActivity.class));
			break;
		}
	}
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
