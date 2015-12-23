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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.my.AddressBeen;
import com.xyt.ygcf.entity.my.User;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.urls.UrlMy;

public class ModifyAddressActivity extends BaseActivity implements OnItemClickListener {

	private static final int REQUEST_PROVINCE = 0;
	private static final int REQUEST_CITY = 1;
	private static final int REQUEST_COUNTRY = 2;
	private static final int REQUEST_COMMIT = 3;

	private EditText name, phone, address, postCode;

	private TextView province, city, county;

	private PopupWindow window;

	private List<AddressBeen> list;

	private int popHeight = 0;

	private int currentType = REQUEST_PROVINCE;

	private String provinceId, cityId, countryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_address);
		initVariable();
		findViews();
		setViewsData();
	}

	private void initVariable() {
		popHeight = getResources().getDisplayMetrics().heightPixels / 3;
		list = new ArrayList<AddressBeen>();
	}

	private void findViews() {
		name = (EditText) findViewById(R.id.activity_modify_name);
		phone = (EditText) findViewById(R.id.activity_modify_phone);
		province = (TextView) findViewById(R.id.activity_modify_province);
		city = (TextView) findViewById(R.id.activity_modify_city);
		county = (TextView) findViewById(R.id.activity_modify_country);
		address = (EditText) findViewById(R.id.activity_modify_address);
		postCode = (EditText) findViewById(R.id.activity_modify_postcode);
		findViewById(R.id.activity_modify_province_layout).setOnClickListener(this);
		findViewById(R.id.activity_modify_city_layout).setOnClickListener(this);
		findViewById(R.id.activity_modify_country_layout).setOnClickListener(this);
		findViewById(R.id.activity_mofify_address_btn).setOnClickListener(this);
	}

	private void setViewsData() {
		setTitle("创建地址");
		User user = LoginHelper.user;
		name.setText(user.name);
		phone.setText(user.telephone);
		// province.setText("");
		// name.setText(user.name);
		// name.setText(user.name);
		address.setText(user.address);
		postCode.setText(user.zip);

		user = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.activity_modify_province_layout:
				requestProvince();
				break;
			case R.id.activity_modify_city_layout:
				requestCity();
				break;
			case R.id.activity_modify_country_layout:
				requestCountry();
				break;
			case R.id.activity_mofify_address_btn:
				break;
			default:
				break;
		}
	}

	private void requestProvince() {
		if (!isRequesting[REQUEST_PROVINCE]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("type", "1");
			params.addQueryStringParameter("level", "1");
			sendRequest(UrlMy.REGION, params, REQUEST_PROVINCE, true);
		}
	}

	private void requestCity() {
		if (!isRequesting[REQUEST_PROVINCE] && !TextUtils.isEmpty(province.getText().toString())) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("type", "3");
			params.addQueryStringParameter("regionId", provinceId);
			params.addQueryStringParameter("level", "2");
			sendRequest(UrlMy.REGION, params, REQUEST_CITY, true);
		}
	}

	private void requestCountry() {
		if (!isRequesting[REQUEST_PROVINCE] && !TextUtils.isEmpty(city.getText().toString())) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("type", "3");
			params.addQueryStringParameter("level", "3");
			params.addQueryStringParameter("regionId", cityId);
			sendRequest(UrlMy.REGION, params, REQUEST_COUNTRY, true);
		}
	}

	private void requestCommit() {
		if (!isRequesting[REQUEST_COMMIT]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("", name.getText().toString().trim());
			params.addQueryStringParameter("", phone.getText().toString().trim());
			final String provinceString = province.getText().toString().trim();
			params.addQueryStringParameter("", provinceString);
			params.addQueryStringParameter("", city.getText().toString().trim());
			params.addQueryStringParameter("", county.getText().toString().trim());
			params.addQueryStringParameter("", address.getText().toString().trim());
			params.addQueryStringParameter("", postCode.getText().toString().trim());
			sendRequest(UrlMy.REGION, params, REQUEST_COMMIT, true);
		}
	}

	@Override
	public void handleJson(String json, int which) {
		try {
			list = MyJsonParse.parseAddressList(json);
			currentType = which;
			switch (which) {
				case REQUEST_PROVINCE:
					showPopWindow(province);
					break;
				case REQUEST_CITY:
					showPopWindow(city);
					break;
				case REQUEST_COUNTRY:
					showPopWindow(county);
					break;
				default:
					setResult(RESULT_OK);
					finish();
					break;
			}
		} catch (JSONException e) {
			handleParseJsonException(which);
			e.printStackTrace();
		}

	}

	private void showPopWindow(View view) {
		window = new PopupWindow(view.getWidth(), popHeight);
		window.setOutsideTouchable(true);
		window.setFocusable(true);
		window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		ListView listview = (ListView) LayoutInflater.from(this).inflate(R.layout.fragment_my_common_list, null);
		listview.setPadding(1, 1, 1, 1);
		listview.setDivider(new ColorDrawable(Color.WHITE));
		listview.setDividerHeight(1);
		listview.setAdapter(new AddressAdapter());
		listview.setOnItemClickListener(this);
		window.setContentView(listview);
		window.showAsDropDown(view);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (currentType) {
			case REQUEST_PROVINCE:
				provinceId = list.get(position).code;
				province.setText(list.get(position).name);
				city.setText("");
				county.setText("");
				break;
			case REQUEST_CITY:
				cityId = list.get(position).code;
				city.setText(list.get(position).name);
				county.setText("");
				break;
			case REQUEST_COUNTRY:
				countryId = list.get(position).code;
				county.setText(list.get(position).name);
				break;
		}

		if (window != null) {
			window.dismiss();
		}
	}

	private class AddressAdapter extends BaseAdapter {

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
			TextView textView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_pop_item, null);
			}
			textView = (TextView) convertView;
			textView.setText(list.get(position).name);
			return convertView;
		}

	}

}
