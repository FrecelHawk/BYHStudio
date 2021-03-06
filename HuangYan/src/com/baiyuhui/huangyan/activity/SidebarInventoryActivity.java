package com.baiyuhui.huangyan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;

/**
 * 
 * @author Administrator
 * 
 * @Overview:侧边栏菜单/我的存货
 */
public class SidebarInventoryActivity extends Activity implements OnClickListener{
 

    private TextView title;
	private ImageView leftImg;
	private Button fatie;
	private Button huitie;
	private View view1;
	private TextView rigthText;
	private View view2;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_inventory);
		initViews();
	}
    private void initViews(){
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("我的存货");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);
		
		rigthText = (TextView) findViewById(R.id.aciont_bar_rigth_text);
		rigthText.setText("选择");
		rigthText.setTextSize(14);
		rigthText.setTextColor(getResources().getColor(R.color.white));
		rigthText.setOnClickListener(this);
		
		fatie = (Button) findViewById(R.id.fatie);
		fatie.setOnClickListener(this);

		huitie = (Button) findViewById(R.id.huitie);
		huitie.setOnClickListener(this);

		
		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
			
		case R.id.aciont_bar_rigth_text:
			finish();
			break;
		case R.id.fatie:
			fatie.setTextColor(getResources().getColor(R.color.gamboge));
			view1.setVisibility(View.VISIBLE);
			huitie.setTextColor(getResources().getColor(R.color.black));
			view2.setVisibility(View.GONE);
			break;
		case R.id.huitie:
			fatie.setTextColor(getResources().getColor(R.color.black));
			view1.setVisibility(View.GONE);
			huitie.setTextColor(getResources().getColor(R.color.gamboge));
			view2.setVisibility(View.VISIBLE);
			break;
			
		default:
			break;
		}
	}
}