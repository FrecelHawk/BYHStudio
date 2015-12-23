package com.baiyuhui.huangyan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.BranchAdapter;
/**
 * 首页/分支
 * 
 * @author Administrator
 * 
 */
public class HomeBranchActivity extends Activity implements OnClickListener{

	private TextView title;
	private ImageView leftImg; 
	private LinearLayout layout;
	
	private ListView mListView;
	private List<String> lists = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_branch);
		setData();
		initViews();
	}
	
	private void setData(){
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
	}
	
	private void initViews(){
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("分支");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.branch_list);
//		BranchAdapter adapter = new BranchAdapter(this, lists);
//		mListView.setAdapter(adapter);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		default:
			break;
		}
	}

}
