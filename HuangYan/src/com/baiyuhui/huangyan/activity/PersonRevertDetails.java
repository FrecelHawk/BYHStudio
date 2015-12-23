package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
/**
 * 个人中心/回复通知/评论详情
 * @author Administrator
 *
 */
public class PersonRevertDetails extends Activity implements OnClickListener{

	
	private TextView title;
	private ImageView leftImg;
	private TextView RigthText;
	private LinearLayout layout;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.person_revert_details);
    	initViews();
    }
	
    private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("评论详情");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		RigthText = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		RigthText.setText("查看主题帖");
		RigthText.setTextSize(10);
		RigthText.setTextColor(getResources().getColor(R.color.white));
		RigthText.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth_text:
			finish();
			break;
		default:
			break;
		}
	}
}
