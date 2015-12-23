package com.xyt.ygcf.more;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.FoodSourceAdapter;
import com.xyt.ygcf.base.BaseActivity;

/**
 * 食材来源详情
 * @author lenovo
 *
 */
public class MoreFood extends BaseActivity {
	private ArrayList<FoodSourceBean> sourceBeans;
	private FoodSourceAdapter adapter;
	private ListView DetailsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_foodfrom);
		sourceBeans = new ArrayList<FoodSourceBean>();
		sourceBeans = (ArrayList<FoodSourceBean>) getIntent().getSerializableExtra("food");
		initWidget();
	}
	
	private void initWidget(){
		setTitle("更多食材来源");
		DetailsList = (ListView) findViewById(R.id.Rate);
		adapter = new FoodSourceAdapter(getApplicationContext(), sourceBeans, false);
		DetailsList.setAdapter(adapter);
	}
}
