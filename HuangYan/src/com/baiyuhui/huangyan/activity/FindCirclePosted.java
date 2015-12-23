package com.baiyuhui.huangyan.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.application.HomeInterfaceApplication;
import com.baiyuhui.huangyan.entity.ImageBean;
import com.baiyuhui.huangyan.network.MyUrl;
import com.baiyuhui.huangyan.util.MyCookieStore;
import com.baiyuhui.huangyan.utils.BitmapUtil;
import com.baiyuhui.huangyan.utils.HLog;
import com.baiyuhui.huangyan.utils.PhoneDeviceInfo;
import com.baiyuhui.huangyan.utils.httpUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 圈子/发帖
 * 
 * @author Administrator
 * 
 */
public class FindCirclePosted extends Activity implements OnClickListener {

	private TextView title;
	private ImageView leftImg;
	private ImageView rigthImg;
	private View parentView;
	private LinearLayout layout;
	public static Bitmap bimap;
	private EditText titleText;
	private EditText contentText;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;

	private static final int SCALE = 5;// 照片缩小比例

	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private List<Bitmap> listBitmap = new ArrayList<Bitmap>();

	private StringBuffer buffer = new StringBuffer();
	private int count = 0;

	private int image_num = 0;
	HomeInterfaceApplication ia;
	
	private int mId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.find_circle_posted,
				null);
		setContentView(parentView);
		mId = getIntent().getIntExtra("mId", 0);
		ia = (HomeInterfaceApplication) getApplication();
		initViews();
		showPopupWindow();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.aciont_bar_title);
		title.setText("发帖");
		title.setTextSize(17);
		title.setTextColor(getResources().getColor(R.color.white));

		leftImg = (ImageView) findViewById(R.id.aciont_bar_back);
		leftImg.setBackgroundResource(R.drawable.fanhui);

		layout = (LinearLayout) findViewById(R.id.lay_left);
		layout.setOnClickListener(this);

		rigthImg = (ImageView) findViewById(R.id.aciont_bar_rigth);
		rigthImg.setBackgroundResource(R.drawable.send);
		rigthImg.setOnClickListener(this);

		titleText = (EditText) findViewById(R.id.biaoti);
		contentText = (EditText) findViewById(R.id.neirong);

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);

		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == BitmapUtil.tempSelectBitmap.size()) {
					ll_popup.startAnimation(AnimationUtils
							.loadAnimation(FindCirclePosted.this,
									R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {

				}
			}
		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_left:
			finish();
			break;
		case R.id.aciont_bar_rigth:
			// sondPost();
			if (!PhoneDeviceInfo.isLogin()) {
				// 未登录
				Intent intent = new Intent(FindCirclePosted.this,
						LoginActivity.class);
				startActivityForResult(intent, 0x100);
			} else {
				sondImg();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				adapter.notifyDataSetChanged();
				// // 将保存在本地的图片取出并缩小后显示在界面上

				String path = Environment.getExternalStorageDirectory()
						+ "/image_" + image_num + ".jpg";

				String srcPath_ii = httpUtil.compressBitmap(path, 480, 320,
						false);

				Bitmap newBitmap = BitmapFactory.decodeFile(srcPath_ii);
				ImageBean takePhoto = new ImageBean();
				takePhoto.setBitmap(newBitmap);
				takePhoto.setImagePath(srcPath_ii);
				BitmapUtil.tempSelectBitmap.add(takePhoto);
				image_num++;
				// bitmap.recycle();
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();

				String srcPath = getRealFilePath(ia.getContext(), originalUri);

				String originalUri_path = httpUtil.compressBitmap(srcPath, 480,
						320, false);
				Bitmap originalUri_Bitmap = BitmapFactory
						.decodeFile(originalUri_path);

				ImageBean takePhoto2 = new ImageBean();
				takePhoto2.setBitmap(originalUri_Bitmap);
				takePhoto2.setImagePath(originalUri_path);

				BitmapUtil.tempSelectBitmap.add(takePhoto2);

				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}

	private void showPopupWindow() {
		pop = new PopupWindow(this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();

				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				Uri imageUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "/image_" + image_num
						+ ".jpg"));
				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
		});

		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();

				Intent openAlbumIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}

	private void sondImg() {
		count = 0;
		final String title = titleText.getText().toString();
		final String content = contentText.getText().toString();
		if (title == null || title.equals("")) {
			Toast.makeText(this, "帖子标题不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (content == null || content.equals("")) {
			Toast.makeText(this, "帖子内容不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}

		if (BitmapUtil.tempSelectBitmap.size() == 0) {
			Toast.makeText(this, "至少选择一张图片！", Toast.LENGTH_SHORT).show();
			return;
		}
		for (int i = 0; i < BitmapUtil.tempSelectBitmap.size(); i++) {

			ImageBean bean = BitmapUtil.tempSelectBitmap.get(i);

			File file = new File(bean.imagePath);

			RequestParams params = new RequestParams();
			params.addBodyParameter("f", file);
			HLog.e("图片请求前MyCookieStore.cookieStore=="
					+ MyCookieStore.cookieStore);
			final HttpUtils http = new HttpUtils(10 * 1000);
			http.configCookieStore(MyCookieStore.cookieStore);
			http.send(HttpMethod.POST, MyUrl.SOND_POST_IMG, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(
								com.lidroid.xutils.exception.HttpException arg0,
								String arg1) {

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub

							HLog.e("图片请求成功MyCookieStore.cookieStore=="
									+ MyCookieStore.cookieStore);
							JSONObject object;
							int status = -1;
							String img = "";
							try {
								object = new JSONObject(arg0.result.toString());
								status = object.getInt("status");
								img = object.getString("imgUrl");
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (status == 0) {
								count++;
								buffer.append(img);

								if (BitmapUtil.tempSelectBitmap.size() == count) {

									sondPost(title, content);
								}
							} else {

							}
						}

					});
		}
	}

	private void sondPost(String title, String content) {

		RequestParams params = new RequestParams();
		params.addBodyParameter("tit", title);
		params.addBodyParameter("content", content + buffer.toString());
		params.addBodyParameter("forumid", String.valueOf(mId));
		final HttpUtils http = new HttpUtils(10 * 1000);
		http.configCookieStore(MyCookieStore.cookieStore);

		HLog.e("发帖前MyCookieStore.cookieStore==" + MyCookieStore.cookieStore);
		http.send(HttpMethod.POST, MyUrl.SOND_POST, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(
							com.lidroid.xutils.exception.HttpException arg0,
							String arg1) {
						// TODO Auto-generated method stub
						System.out.println("");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						HLog.e("发帖成功MyCookieStore.cookieStore=="
								+ MyCookieStore.cookieStore);
						int code = -1;
						try {
							JSONObject jsonObject = new JSONObject(arg0.result
									.toString());
							code = jsonObject.getInt("err");
							HLog.e("code====" + code);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (code == 0) {
							Toast.makeText(FindCirclePosted.this, "发帖成功",
									Toast.LENGTH_SHORT).show();
							finish();
						} else if (code == 400) {
							Intent intent = new Intent(FindCirclePosted.this,
									LoginActivity.class);
							startActivityForResult(intent, 0x100);
						}
					}

				});
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (BitmapUtil.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (BitmapUtil.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.delete = (ImageView) convertView
						.findViewById(R.id.delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == BitmapUtil.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				holder.delete.setVisibility(View.GONE);

				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.delete.setVisibility(View.VISIBLE);
				holder.image.setImageBitmap(BitmapUtil.tempSelectBitmap.get(
						position).getBitmap());
			}
			holder.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					BitmapUtil.tempSelectBitmap.remove(position);
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
			ImageView delete;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (BitmapUtil.max == BitmapUtil.tempSelectBitmap
								.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							BitmapUtil.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
