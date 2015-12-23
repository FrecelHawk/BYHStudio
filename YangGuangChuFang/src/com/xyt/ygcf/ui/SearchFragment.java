package com.xyt.ygcf.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.ui.search.SearchResultActivity;

/**
 * 主页面之搜索选项页面
 * 
 * @author wjj
 */
public class SearchFragment extends Fragment implements OnClickListener {

	private FragmentActivity mActivity;
	private View view;

	private ListView listView;
	private ArrayList<String> data, data2;
	private SharedPreferences sp;
	private Editor editor;
	private Button btClear;
	private ListAdapter adapter;
	private static final int MAX = 10;// 最多显示的搜索记录条数


	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();

		listView = (ListView) view.findViewById(R.id.LvKeyWord);

		// 获取SharedPreferences对象

		sp = mActivity.getSharedPreferences("ygcfKeyword",
				Activity.MODE_PRIVATE);
		// 存入数据
		editor = sp.edit();

		initView();

		initSearchView();

	}

	private void initListView() {

		data = getData();
		int leng2 = data.size();

		data2 = new ArrayList<String>();
		for (int i = 0; i < leng2; i++) {// 倒序
			data2.add(data.get(leng2 - 1 - i));
		}

		adapter = new ArrayAdapter<String>(mActivity,
				R.layout.simple__list_item, data2);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String keyword = data2.get(arg2).trim() + "";

				Intent intent = new Intent();
				intent.setClass(mActivity, SearchResultActivity.class);
				intent.putExtra("keyword", keyword + "");
				startActivity(intent);

			}
		});

	}

	private ArrayList<String> getData() {

		String newKeyWords = sp.getString("KeyWord", "");
		
		//Log.v("logcat","newKeyWords="+newKeyWords);
		data = new ArrayList<String>();

		if (TextUtils.isEmpty(newKeyWords)) {
			btClear.setVisibility(View.GONE);

		}

		else {
			btClear.setVisibility(View.VISIBLE);

			JSONArray arrays = null;

			try {
				arrays = new JSONArray(newKeyWords);
				int length = arrays.length();
				for (int i = 0; i < length; i++) {
					data.add(arrays.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				// Toast.makeText(mActivity, "找不到搜索记录",
				// Toast.LENGTH_SHORT).show();
			}

		}

		return data;
	}

	private JSONArray convert(ArrayList<String> data) throws JSONException {
		JSONArray array = new JSONArray();
		if (data.isEmpty())
			return array;

		int size = data.size();

		for (int i = 0; i < size; i++)
			array.put(i, data.get(i));

		return array;
	}

	private void initSearchView() {
		final EditText et = (EditText) view.findViewById(R.id.search_et);
		Button btn = (Button) view.findViewById(R.id.search_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (et.getText().toString().length() > 0) {
					// 搜索
					// Toast.makeText(getActivity(), "关键字:" + et.getText(),
					// Toast.LENGTH_LONG).show();
					String kWord = et.getText() + "";
					Intent intent = new Intent();
					intent.setClass(mActivity, SearchResultActivity.class);
					intent.putExtra("keyword", kWord);
					startActivity(intent);

					for (int i = 0; i < data.size(); i++) {

						if (kWord.equals(data.get(i))) {
							// 如果内容与记录的相同，则删除记录那条
							data.remove(i);
						}
					}

					data.add(kWord);
					int size = data.size();
					if (size > MAX) {// 限定只显示MAX条记录，多了就删了最旧的
						data.remove(0);
					} else {

					}

					String array1 = "";
					try {

						JSONArray argsArray = convert(data);

						array1 = argsArray.toString();

						// 储存提交搜索记录
						editor.putString("KeyWord", array1);
						editor.commit();

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getActivity(), "请输入关键字", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

	}

	private void initView() {

		btClear = (Button) view.findViewById(R.id.bt_clear);
		btClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editor.putString("KeyWord", "");
				editor.commit();
				data.clear();
				data2.clear();
				((BaseAdapter) adapter).notifyDataSetChanged();
				btClear.setVisibility(View.GONE);

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		initListView();

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();
			// Toast.makeText(getActivity(), "关键字:" +keyword,
			// Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(mActivity, SearchResultActivity.class);
			intent.putExtra("keyword", keyword + "");
			startActivity(intent);

		}

	}

}
