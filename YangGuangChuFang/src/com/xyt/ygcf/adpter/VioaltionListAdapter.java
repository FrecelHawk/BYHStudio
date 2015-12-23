package com.xyt.ygcf.adpter;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.entity.VioaltionListBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 违规公示Adapter
 * @author lenovo hexiangyang
 *
 */
public class VioaltionListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<VioaltionListBean> vioaltionListBeans;
	
	public VioaltionListAdapter(Context context,
			List<VioaltionListBean> vioaltionListBeans) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.vioaltionListBeans = vioaltionListBeans;
	}

	@Override
	public int getCount() {
		return vioaltionListBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return vioaltionListBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.activity_vioaltion_list_item, null);
			holder.nameText = (TextView) arg1.findViewById(R.id.vioaltion_name);
			holder.messageText = (TextView) arg1.findViewById(R.id.vioaltion_message);
			holder.dateText = (TextView) arg1.findViewById(R.id.vioaltion_date);
			arg1.setTag(holder);
		}else {
			holder = (ViewHolder) arg1.getTag();
		}
		VioaltionListBean listBean = vioaltionListBeans.get(arg0);
		holder.nameText.setText(listBean.getName());
		holder.messageText.setText(listBean.getMessage());
		holder.dateText.setText(listBean.getDate());
		return arg1;
	}

	class ViewHolder{
		TextView nameText, messageText, dateText;
	}
}
