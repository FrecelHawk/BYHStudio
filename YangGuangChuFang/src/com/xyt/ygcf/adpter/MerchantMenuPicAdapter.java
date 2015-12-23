package com.xyt.ygcf.adpter;

import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.entity.GoodsListBean;
import com.xyt.ygcf.entity.MerchantMenuBean;

public class MerchantMenuPicAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<MerchantMenuBean> menuBeans;
	private BitmapUtils bitmapUtils;
	private String mark;

	public MerchantMenuPicAdapter(Context context, List<MerchantMenuBean> menuBeans, String mark) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.menuBeans = menuBeans;
		this.bitmapUtils = new BitmapUtils(context);
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
			arg1 = inflater.inflate(R.layout.activity_merchant_menu_pic_item, null);
			holder.MenuPicImg = (ImageView)arg1.findViewById(R.id.menu_pic_img);
			holder.MenuPicName = (TextView)arg1.findViewById(R.id.menu_pic_name);
			holder.MenuPicGrade = (RatingBar)arg1.findViewById(R.id.menu_pic_grade);
			holder.MenuPicCurrentPrice = (TextView)arg1.findViewById(R.id.menu_pic_current_price);
			holder.layout = (LinearLayout)arg1.findViewById(R.id.menu_groad_layout);
			arg1.setTag(holder);
		}else {
			holder = (ViewHolder) arg1.getTag();
		}
		if(mark.equals(Constants.INDUSTRY_SCHOOL_MAKER) || mark.equals(Constants.INDUSTRY_COMPANY_MAKER)){
			holder.layout.setVisibility(View.GONE);
		}
		MerchantMenuBean menuBean = menuBeans.get(arg0);
		holder.MenuPicName.setText(menuBean.getName());
		bitmapUtils.display(holder.MenuPicImg, menuBean.getIcon());
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.img_load_defalut);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_load_defalut);
		holder.MenuPicCurrentPrice.setText("￥" + menuBean.getPrice());
		if(TextUtils.isEmpty(menuBean.getGrade())){
			holder.MenuPicGrade.setRating(0);
		}else {
			holder.MenuPicGrade.setRating((float)Integer.valueOf(menuBean.getGrade()));
		}
		return arg1;
	}

	public class ViewHolder{
		ImageView MenuPicImg;
		RatingBar MenuPicGrade;
		TextView MenuPicName, MenuPicCurrentPrice;
		LinearLayout layout;
	}

}
