package com.baiyuhui.huangyan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.ReviewsAdapter;
/**
 * 商城/商品详情/评价
 * @author Administrator
 *
 */
public class StoreReviews extends Activity implements OnClickListener {

	private ListView mListView;
	private List<String> lists = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_reviews);
		setData();
		initViews();
	}

	private void setData() {
		lists.add("");
		lists.add("");
		lists.add("");
		lists.add("");
	}

	private void initViews() {

		mListView = (ListView) findViewById(R.id.reviews_list);
		ReviewsAdapter adapter = new ReviewsAdapter(this, lists);
		mListView.setAdapter(adapter);

	}

	public void onClick(View v) {
		
	}

}
