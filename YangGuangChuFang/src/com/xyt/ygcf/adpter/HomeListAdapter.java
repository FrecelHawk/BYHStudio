package com.xyt.ygcf.adpter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyt.yangguangchufang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> mData;

	
	public HomeListAdapter(Context cxt) {
		this.mContext = cxt;
		mData = getData();
	}
	
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("theme", "阳光厨房");
//		map.put("content", "厨房卫生一看便知");
		map.put("img", R.drawable.i1);
		list.add(map);

		map = new HashMap<String, Object>();
//		map.put("theme", "学校食堂");
//		map.put("content", "孩子饮食您来监督");
		map.put("img", R.drawable.i2);
		list.add(map);

		map = new HashMap<String, Object>();
//		map.put("theme", "企业食堂");
//		map.put("content", "食堂卫生人人参与");
		map.put("img", R.drawable.i3);
		list.add(map);
		
		map = new HashMap<String, Object>();
//		map.put("theme", "我的收藏");
//		map.put("content", "喜欢的关注的都在这里");
		map.put("img", R.drawable.i4);
		list.add(map);
		
		/*map = new HashMap<String, Object>();
//		map.put("theme", "违规公示");
//		map.put("content", "黑名单曝光台");
		map.put("img", R.drawable.i5);
		list.add(map);*/
		
		return list;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = LinearLayout.inflate(mContext, R.layout.home_item, null);
			
//			holder.theme = (TextView) convertView.findViewById(R.id.store_name);
//			holder.content = (TextView) convertView.findViewById(R.id.coupon_item);
			holder.img = (ImageView) convertView.findViewById(R.id.goods_item_icon);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
//		holder.theme.setText((String) mData.get(position).get("theme"));
//		holder.content.setText((String) mData.get(position).get("content"));
		holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));

	

		return convertView;

	}

	public final class ViewHolder {
		public ImageView img;
//		public TextView theme;
//		public TextView content;
	}

}
