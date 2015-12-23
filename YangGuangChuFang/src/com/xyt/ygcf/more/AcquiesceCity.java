package com.xyt.ygcf.more;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.base.AppManager;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.CityObjBean;
import com.xyt.ygcf.ui.Main;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.DialogUtil;
import com.xyt.ygcf.util.SpHelper;

/**
 * 选择默认城市
 * **/
public class AcquiesceCity extends BaseActivity {
	private TextView CityName;
	private Button ChooseCityButton;

	private List<CityObjBean> cityObjs;
	private boolean finishRequest = false;
	private boolean isExit = false;
	/** 地区 */
	private final static int REGION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acquiescecity_layout);
		init();
	}

	private void init() {
		CityName = (TextView) findViewById(R.id.city_name);
		String cityname = SpHelper.init(this).getStringValue(
				Constants.DEFAULTCITY, null);
		CityName.setText(cityname);
		ChooseCityButton = (Button) findViewById(R.id.choose_city);
		ChooseCityButton.setOnClickListener(this);
		setTitle("默认城市");

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.choose_city:
			region();
			break;

		}
	}

	private void showCityDialog() {
		DialogUtil.DefaultCityDialog(this, cityObjs, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ListView lv = (ListView) arg0;
				Dialog d = (Dialog) lv.getTag();
				CityObjBean cityObjBean = cityObjs.get(arg2);
				SpHelper.init(AcquiesceCity.this).setStringValue(
						Constants.DEFAULTCITY, cityObjBean.name);
				SpHelper.init(AcquiesceCity.this).setStringValue(
						Constants.DEFAULTCITY_ID, cityObjBean.id);
				CityName.setText(cityObjBean.name);
				d.dismiss();

			}
		});
	}

	/**
	 * 地区选择
	 */
	private void region() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("regionId", "4401");
		params.addQueryStringParameter("type", "1");
		params.addQueryStringParameter("level", "2");
		sendRequest(HttpMethod.GET, UrlMy.REGION, params, REGION, true);
	}

	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
	}

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		try {
			if (which == REGION) {
				parseRegion(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析地区选择
	 * 
	 * @param json
	 * @throws JSONException
	 */
	private void parseRegion(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		if (array != null) {
			cityObjs = new ArrayList<CityObjBean>();
			final int lenght = array.length();
			for (int i = 0; i < lenght; i++) {
				JSONObject object = array.getJSONObject(i);

				CityObjBean cityObj = new CityObjBean();
				cityObj.id = object.optString("id");
				cityObj.name =" " + object.optString("name").replace("市", "") + " ";
				cityObj.parentId = object.optString("parentId");
				cityObjs.add(cityObj);
			}
			finishRequest = true;
			showDialog();
		}
	}

	public void showDialog() {
		if(isExit){
			return;
		}
		if (finishRequest && this instanceof AcquiesceCity) {
			showCityDialog();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			isExit = true;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
