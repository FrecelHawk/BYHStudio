package com.baiyuhui.huangyan.activity;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.adapter.InfromIogisticsAdapter;
import com.baiyuhui.huangyan.adapter.NewsAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 个人中心/物流通知
 * @author Administrator
 *
 */
public class PersonInfromIogistics extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;

	private List<BaseJsonBeen> listDatas;
	private ListView mListView;
	private ListView lisview;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_infrom_iogistics);
		initViews();
		HttpGet();
		
		lisview = (ListView) this.findViewById(R.id.infrom_iogistics);
		lisview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent it = new Intent(PersonInfromIogistics.this,
						PersonIogisticsDetails.class);

				it.putExtra("id", listDatas.get(position).id);
				PersonInfromIogistics.this.startActivity(it);
			}
		});

	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("物流通知");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));
		
		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);
		
		layout = (LinearLayout)findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.infrom_iogistics);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		default:
			break;
		}
	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.NEWS, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				listDatas = JsonParse.parseInfromIogistics(arg2);
				InfromIogisticsAdapter adapter = new InfromIogisticsAdapter(
						PersonInfromIogistics.this, listDatas);
				mListView.setAdapter(adapter);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				System.out.println("请求异常----->");
			}
		});

	}

}
