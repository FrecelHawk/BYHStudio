package com.xyt.ygcf.ui.my;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.my.ComplainSubjectBean;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;

public class ComplainActivity extends BaseActivity implements OnItemClickListener {

	private static final int SUBJECT_CODE = 0;
	private static final int COMMIT_CODE = 1;

	// private static String[] themes = { "卫生", "收费", "服务", "食材来源" };

	private EditText content;
	private TextView shop, theme;
	private PopupWindow window;

	private String id;
	private List<ComplainSubjectBean> list = new ArrayList<ComplainSubjectBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_complain);
		findViews();
		initBitmapVariable();
		setViewsData();
	}

	private void findViews() {
		findViewById(R.id.fragment_complaint_img).setOnClickListener(this);
		findViewById(R.id.fragment_complaint_btn_commit).setOnClickListener(this);
		shop = (TextView) findViewById(R.id.fragment_complaint_name);
		theme = (TextView) findViewById(R.id.fragment_complaint_theme);
		theme.setOnClickListener(this);
		content = (EditText) findViewById(R.id.fragment_complaint_content);
	}

	private void setViewsData() {
		setTitle("投诉");
		id = getIntent().getStringExtra("id");
		shop.setText(getIntent().getStringExtra("name"));
		sendSubjectRequest();
	}

	private void sendSubjectRequest() {
		if (!isRequesting[SUBJECT_CODE]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("clsCode", "COMPLAIN_SUBJECT");
			sendRequest(UrlMy.COMLAIN_SUJECT, params, SUBJECT_CODE, true);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.fragment_complaint_theme:
			case R.id.fragment_complaint_img:
				if (list.size() > 0) {
					showPopWindow(theme);
				} else {
					sendSubjectRequest();
				}
				break;
			case R.id.fragment_complaint_btn_commit:
				sendRequest();
				break;
			default:
				break;
		}
	}

	private void showPopWindow(View view) {
		window = new PopupWindow(view.getWidth(), LayoutParams.WRAP_CONTENT);
		window.setOutsideTouchable(true);
		window.setFocusable(true);
		window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		ListView listview = (ListView) LayoutInflater.from(this).inflate(R.layout.fragment_my_common_list, null);
		listview.setBackgroundColor(Color.WHITE);
		listview.setPadding(2, 2, 2, 2);
		listview.setDivider(getResources().getDrawable(R.drawable.bg_my_input));
		listview.setDividerHeight(1);
		listview.setAdapter(new ThemeAdapter());
		listview.setOnItemClickListener(this);
		window.setContentView(listview);
		window.showAsDropDown(view);
	}

	private void sendRequest() {
		if (isRequesting[COMMIT_CODE]) {
			return;
		}
		final String shopName = shop.getText().toString();
		final String contentTheme = theme.getText().toString();
		final String contents = content.getText().toString();
		if (TextUtils.isEmpty(shopName)) {
			showToast("不能为空");
			return;
		}
		if (TextUtils.isEmpty(contentTheme)) {
			Toast.makeText(this, "请选择投诉类型", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(contents)) {
			showToast("不能为空");
			return;
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("merchantId", id);
		params.addBodyParameter("subject", subjectCode);
		params.addBodyParameter("content", contents);
		sendRequest(HttpMethod.POST, UrlMy.ADD_COMPLAINT, params, COMMIT_CODE, true);
	}

	@Override
	public void handleJson(String json, int which) {
		if (which == SUBJECT_CODE) {
			try {
				MyJsonParse.parseComlainSubjectList(list, json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "投诉成功", Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	@Override
	public void handleParseJsonException(int which) {
		Toast.makeText(this, "数据出错，请再提交一次", Toast.LENGTH_SHORT).show();
	}

	private String subjectCode = "1";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (window != null) {
			window.dismiss();
		}
		theme.setText(list.get(position).name);
		subjectCode = list.get(position).code;
	}

	private class ThemeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(ComplainActivity.this).inflate(R.layout.my_pop_item, null);
			}
			final TextView textView = (TextView) convertView;
			textView.setText(list.get(position).name);
			return convertView;
		}

	}

}
