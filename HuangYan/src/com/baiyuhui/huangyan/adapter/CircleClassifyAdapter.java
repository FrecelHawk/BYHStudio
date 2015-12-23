package com.baiyuhui.huangyan.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.FindCircleClassify;
import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.network.JsonParse;
import com.baiyuhui.huangyan.network.MyUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 圈子/版块分类
 * 
 * @author Administrator
 * 
 */
public class CircleClassifyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<BaseJsonBeen> lists = new ArrayList<BaseJsonBeen>();
	
	public CircleClassifyAdapter(Context mContext, List<BaseJsonBeen> lists) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if(lists != null){
			return lists.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.find_item_list_classify, null);
			
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text11 = (TextView) convertView.findViewById(R.id.text11);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);
			holder.text3 = (TextView) convertView.findViewById(R.id.text3);
			holder.text4 = (TextView) convertView.findViewById(R.id.text4);
			holder.text5 = (TextView) convertView.findViewById(R.id.text5);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(lists.get(position).idx == 0){
			holder.text11.setVisibility(View.GONE);
		}else if(lists.get(position).idx == 1){
			holder.text11.setVisibility(View.VISIBLE);
		}
		holder.text1.setText(lists.get(position).getTit());
		holder.text2.setText(lists.get(position).getInfo());
		holder.text3.setText(lists.get(position).getCdate());
		holder.text4.setText(String.valueOf(lists.get(position).getViewnum()));
		holder.text5.setText(String.valueOf(lists.get(position).getReplynum()));
//		holder.text2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				AsyncHttpClient client = new AsyncHttpClient();
//				client.get(MyUrl.SET_POST_TOP + lists.get(position).id, new TextHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, String arg2) {
//						
//					}
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, String arg2,
//							Throwable arg3) {
//						System.out.println("请求异常----->");
//					}
//				});
//
//			}
//		});

		return convertView;
	}

	class ViewHolder{
		TextView text1;
		TextView text11;
		TextView text2;
		TextView text3;
		TextView text4;
		TextView text5;
		
	}
	
/*	class CommentAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}*/
}
