package com.xyt.ygcf.util;

import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aps.s;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.DefaultCityAdapter;
import com.xyt.ygcf.entity.CityObjBean;

public class MyDialog extends Dialog {
	private Context mContext;

	public MyDialog(Context context) {
		super(context);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 创建一个自定义对话框
	 * @author lenovo
	 *
	 */
	public static class Builder {
		private Context context; 
		private String title; //对话框标题
		private String content; //对话框内容
		private String confirmButtonText; // 对话框确定文本
		private String confirmButtonText2; // 对话框确定文本2
		private String backButtonText2; //对话框返回文本
		private View contentView;
		private int size;
		private List<CityObjBean> objBeans;
		
		// 对话框按钮监听事件
		private DialogInterface.OnClickListener backButtonClickListener, backButtonClickListener2,
			confirmButtonClickListener, confirmButtonClickListener2;
		//ListView Itme的监听事件
		private OnItemClickListener clickListener;

		public Builder(Context context, int size) {
			this.context = context;
			this.size = size;
		}
		public Builder(Context context, List<CityObjBean> objBeans) {
			this.context = context;
			this.objBeans = objBeans;
		}
		/**
		 * 使用字符串设置对话框标题
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title){
			this.title = title;
			return this;
		}
		
		/**
		 *  使用资源设置话框标题
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title){
			this.title = (String) context.getText(title);
			return this;
		}
		
		/**
		 * 使用字符串设置对话框消息
		 * 
		 * @param title
		 * @return
		 */
		public Builder setContent(String content) {
			this.content = content;
			return this;
		}

		/**
		 * 使用资源设置对话框消息
		 * 
		 * @param title
		 * @return
		 */
		public Builder setContent(int content) {
			this.content = (String) context.getText(content);
			return this;
		}
		
		/**
		 * 设置自定义的对话框内容
		 * 
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}
		
		/**
		 * 设置ListView Item的点击事件
		 * @param clickListener
		 * @return
		 */
		public Builder setListListener(OnItemClickListener clickListener){
			this.clickListener = clickListener;
			return this;
		}
		
		/**
		 * 设置back按钮的事件
		 * 
		 * @param backButtonText
		 * @param listener
		 * @return
		 */
		public Builder setBackButton(DialogInterface.OnClickListener listener) {
			this.backButtonClickListener = listener;
			return this;
		}
		
		/**
		 * 设置back按钮2的事件和文本
		 * 
		 * @param backButtonText
		 * @param listener
		 * @return
		 */
		public Builder setBackButton2(int backButtonText,
				DialogInterface.OnClickListener listener) {
			this.backButtonText2 = (String) context.getText(backButtonText);
			this.backButtonClickListener2 = listener;
			return this;
		}
		
		/**
		 * 设置back按钮2的事件和文本
		 * 
		 * @param backButtonText
		 * @param listener
		 * @return
		 */
		public Builder setBackButton2(String backButton,
				DialogInterface.OnClickListener listener) {
			this.backButtonText2 = backButton;
			this.backButtonClickListener2 = listener;
			return this;
		}

		/**
		 * 设置确定按钮事件和文本
		 * 
		 * @param confirmButtonText
		 * @param listener
		 * @return
		 */
		public Builder setConfirmButton(int confirmButtonText,
				DialogInterface.OnClickListener listener) {
			this.confirmButtonText = (String) context.getText(confirmButtonText);
			this.confirmButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置确定按钮事件和文本
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setConfirmButton(String confirmButtonText,
				DialogInterface.OnClickListener listener) {
			this.confirmButtonText = confirmButtonText;
			this.confirmButtonClickListener = listener;
			return this;
		}
		
		/**
		 * 设置确定按钮2事件和文本
		 * 
		 * @param confirmButtonText
		 * @param listener
		 * @return
		 */
		public Builder setConfirmButton2(int confirmButtonText,
				DialogInterface.OnClickListener listener) {
			this.confirmButtonText2 = (String) context
					.getText(confirmButtonText);
			this.confirmButtonClickListener2 = listener;
			return this;
		}

		/**
		 * 设置确定按钮2事件和文本
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setConfirmButton2(String confirmButtonText,
				DialogInterface.OnClickListener listener) {
			this.confirmButtonText2 = confirmButtonText;
			this.confirmButtonClickListener2 = listener;
			return this;
		}
		
		public void defaultCityDialog(){
			LayoutInflater inflater = LayoutInflater.from(context);
			// 实例化自定义的对话框主题
			final Dialog dialog = new AlertDialog.Builder(context).create();
			View layout = inflater.inflate(R.layout.default_city_dialog, null);
			LayoutParams params = dialog.getWindow().getAttributes();
			dialog.show();
			dialog.setCancelable(false);
			params.width = LayoutParams.MATCH_PARENT;
			params.height =  ((Activity) context).getWindowManager().getDefaultDisplay().getHeight() / 2;
			dialog.getWindow().setContentView(layout, params);
			// 设置对话框标题
			((TextView) layout.findViewById(R.id.default_dialog_title)).setText(title);
			ListView listView = (ListView) layout.findViewById(R.id.default_dialog_list);
			listView.setTag(dialog);
			DefaultCityAdapter cityAdapter = new DefaultCityAdapter(context, objBeans);
			listView.setAdapter(cityAdapter);
			listView.setOnItemClickListener(clickListener);
			
		}
		
		public void create(){
			LayoutInflater inflater = LayoutInflater.from(context);
			// 实例化自定义的对话框主题
			final Dialog dialog = new AlertDialog.Builder(context).create();
			View layout = inflater.inflate(R.layout.mydialog, null);
			dialog.show();
			dialog.setCancelable(false);
			LayoutParams params = dialog.getWindow().getAttributes();
			params.width = LayoutParams.WRAP_CONTENT;
			params.height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight() / size;
			dialog.getWindow().setContentView(layout, params);
			
			// 设置对话框标题
			if (title != null) {
				((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
			}else {
				((TextView) layout.findViewById(R.id.dialog_title)).setVisibility(View.GONE);
				((View) layout.findViewById(R.id.line)).setVisibility(View.GONE);
			}
			
			// 设置对话框内容
			if (content != null) {
				TextView dlgMsg = (TextView) layout.findViewById(R.id.dialog_cotent);
				if(size == 3){
					dlgMsg.setGravity(Gravity.LEFT);
					dlgMsg.setLineSpacing(0, 1.5f);
					dlgMsg.setPadding(40, 0, 0, 0);
					content = content.replace("；", "\n").replace(";", "\n");
				}
				
				dlgMsg.setText(content);
			} else if (contentView != null) {
				// if no message set
				// 如果没有设置对话框内容，添加contentView到对话框主体
//				((LinearLayout) layout.findViewById(R.id.dialog_cotent)).removeAllViews();
//				((LinearLayout) layout.findViewById(R.id.dialog_cotent))
//						.addView(contentView, new LayoutParams(
//						LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			}
			
			// 设置返回按钮事件
			if (backButtonClickListener != null) {
				ImageButton bckButton = ((ImageButton) layout.findViewById(R.id.dialog_delete));
				bckButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						backButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
					}
				});
			}else {
				layout.findViewById(R.id.dialog_delete).setVisibility(View.GONE);
			}
			
			// 设置确定按钮事件和文本
			if (confirmButtonText != null) {
				Button cfmButton = ((Button) layout.findViewById(R.id.dialog_confirm));
				cfmButton.setText(confirmButtonText);

				if (confirmButtonClickListener != null) {
					cfmButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							confirmButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_NEUTRAL);
						}
					});
				}
			} else {
				layout.findViewById(R.id.dialog_confirm).setBackgroundColor(context.getResources().getColor(R.color.orange));
			}
			
			//设置返回按钮2事件
			if (backButtonText2 != null) {
				Button bckButton = ((Button) layout.findViewById(R.id.dialog_delete2));
				bckButton.setText(backButtonText2);
				if(backButtonClickListener2 != null){
					bckButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							backButtonClickListener2.onClick(dialog,
									DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			}else {
				layout.findViewById(R.id.dialog_delete2).setBackgroundColor(context.getResources().getColor(R.color.orange));
			}
			
			//设置确认按钮2事件
			if (confirmButtonText2 != null) {
				Button cfmButton = ((Button) layout.findViewById(R.id.dialog_confirm2));
				cfmButton.setText(confirmButtonText2);

				if (confirmButtonClickListener2 != null) {
					cfmButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							confirmButtonClickListener2.onClick(dialog,
									DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			} else {
				layout.findViewById(R.id.dialog_confirm2).setBackgroundColor(context.getResources().getColor(R.color.orange));
			}
		}
		
	}
}
