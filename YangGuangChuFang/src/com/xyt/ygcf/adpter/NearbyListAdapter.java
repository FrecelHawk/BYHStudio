package com.xyt.ygcf.adpter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.entity.NearbyListItemBean;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.util.SearchShopTool;
import com.xyt.ygcf.util.TimeUtils;

/**
 * 周边模块-listView的适配器(待续)
 * 
 */
public class NearbyListAdapter extends BaseAdapter{

	private Context context;
	private List<NearbyListItemBean> items;
	protected BitmapUtils bitmapUtils = null;
	private String mark;

	public NearbyListAdapter(Context context, List<NearbyListItemBean> items, String mark) {
		this.context = context;
		this.items = items;
		this.mark = mark;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_load_defalut);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_load_defalut);
	}

	@Override
	public int getCount() {
		return items.size();
		// return 6;
	}

	@Override
	public Object getItem(int posotion) {
		return items.get(posotion);
		// return posotion;
	}

	@Override
	public long getItemId(int posotion) {
		return posotion;
	}

	@Override
	public View getView(int posotion, View convertView, ViewGroup parent) {
		
		Honlder honlder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.nearby_list_item, parent, false);
			honlder = new Honlder();
			honlder.icon = (ImageView) convertView.findViewById(R.id.nearby_list_item_icon);
			honlder.name = (TextView) convertView.findViewById(R.id.nearby_list_item_name);
			honlder.rateTxt = (RatingBar) convertView.findViewById(R.id.nearby_list_item_ratetext);
			honlder.messTxt =  (TextView) convertView.findViewById(R.id.nearby_list_item_mess);
			honlder.themeTxt =(TextView) convertView.findViewById(R.id.nearby_list_item_themetext);
			honlder.distanceTxt =(TextView) convertView.findViewById(R.id.nearby_list_item_distance);
			honlder.ll_cecommendation_level = (RelativeLayout) convertView.findViewById(R.id.ll_cecommendation_level);
			honlder.highest_quality = (ImageView) convertView.findViewById(R.id.highest_quality);
			convertView.setTag(honlder);
		}else{
			honlder = (Honlder) convertView.getTag();
		}
		if(!mark.equals(Constants.INDUSTRY_RESTAURANT_MAKER)){
			honlder.ll_cecommendation_level.setVisibility(View.GONE);
			honlder.highest_quality.setVisibility(View.GONE);
			honlder.messTxt.setVisibility(View.GONE);
		}
		
		NearbyListItemBean listItemBean = items.get(posotion);
		if(listItemBean.praiseScore<1500){
			honlder.highest_quality.setVisibility(View.GONE);
				}
		honlder.name.setText(listItemBean.stopName);
		bitmapUtils.display(honlder.icon, listItemBean.logoUrl);
		honlder.messTxt.setText(listItemBean.theme);
		honlder.themeTxt.setText(listItemBean.address);
		honlder.distanceTxt.setText(TimeUtils.meterTransformKilometer(Integer.valueOf(listItemBean.distance)));
		if(TextUtils.isEmpty(listItemBean.rate)){
			honlder.rateTxt.setRating(0);
		}else {
			honlder.rateTxt.setRating((float)Integer.valueOf(listItemBean.rate));
		}
		honlder.messTxt.setText(listItemBean.theme);
		
		
		return convertView;
	}

	private class Honlder {

		ImageView icon;
		TextView name;
		RatingBar rateTxt;
		TextView themeTxt;
		TextView distanceTxt;
		TextView messTxt;
		ImageView highest_quality;
		RelativeLayout ll_cecommendation_level;

	}

}
