package com.xyt.ygcf.adpter;

import java.util.List;

import com.xyt.yangguangchufang.R;
import com.xyt.yangguangchufang.R.id;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.adpter.MerchantMenuPicAdapter.ViewHolder;
import com.xyt.ygcf.entity.MerchantMenuBean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MerchantMenuListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<MerchantMenuBean> menuBeans;
	private String mark;
	

	public MerchantMenuListAdapter(Context context, List<MerchantMenuBean> menuBeans, String mark) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.menuBeans = menuBeans;
		this.mark = mark;
	}

	@Override
	public int getCount() {
		return menuBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return menuBeans.get(arg0);
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
			arg1 = inflater.inflate(R.layout.activity_merchant_menu_list_item, null);
			holder.MenuListName = (TextView)arg1.findViewById(R.id.menu_list_name);
			holder.MenuListGrade = (RatingBar)arg1.findViewById(R.id.menu_list_grade);
			holder.MenuListCurrentPrice = (TextView)arg1.findViewById(R.id.menu_list_current_price);
			holder.groadText = (TextView)arg1.findViewById(R.id.groad_text);
			arg1.setTag(holder);
		}else {
			holder = (ViewHolder) arg1.getTag();
		}
		if(mark.equals(Constants.INDUSTRY_SCHOOL_MAKER) || mark.equals(Constants.INDUSTRY_COMPANY_MAKER)){
			holder.MenuListGrade.setVisibility(View.GONE);
			holder.groadText.setVisibility(View.GONE);
		}
		MerchantMenuBean menuBean = menuBeans.get(arg0);
		holder.MenuListName.setText(menuBean.getName());
		holder.MenuListCurrentPrice.setText("￥" + menuBean.getPrice());
		if(TextUtils.isEmpty(menuBean.getGrade())){
			holder.MenuListGrade.setRating(0);
		}else {
			holder.MenuListGrade.setRating((float)Integer.valueOf(menuBean.getGrade()));
		}
		return arg1;
	}

	public class ViewHolder{
		RatingBar MenuListGrade;
		TextView MenuListName, MenuListCurrentPrice, groadText;
	}
}
