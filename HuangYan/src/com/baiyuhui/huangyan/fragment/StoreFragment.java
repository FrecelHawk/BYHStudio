package com.baiyuhui.huangyan.fragment;

import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.StoreDetailsActivity;
import com.baiyuhui.huangyan.adapter.StoreFragmentAdapter;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 商城
 * 
 * @author Administrator
 * 
 */
public class StoreFragment extends Fragment implements OnClickListener {

	private View view;

	private GridView mListView;
	private List<BaseJsonBeen> listDatas;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_store, container, false);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		HttpGet();
	}

	private void initViews() {

		mListView = (GridView) view.findViewById(R.id.GridView);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(getActivity(), StoreDetailsActivity.class);
				it.putExtra("id", listDatas.get(position).getId());
				getActivity().startActivity(it);
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	private void HttpGet() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MyUrl.FRAGMENT_STORE, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				System.out.println("请求回来的参数----->" + arg2);
				listDatas = JsonParse.parseStore(arg2);
				StoreFragmentAdapter adapter = new StoreFragmentAdapter(
						getActivity(), listDatas);
				mListView.setAdapter(adapter);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				System.out.println("请求异常----->");
			}
		});

	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
