package com.xyt.ygcf.ui.my;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.my.User;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.util.FileUtils;
import com.xyt.ygcf.widget.CircleImageView;

/**
 * 
 * @author yuyangming 个人信息
 * 
 */
public class PersonMessageActivity extends BaseActivity implements OnClickListener {

	private static final int MODIFY_NICKNAME = 0x100;
	private static final int MODIFY_PASSWORD = 0x101;
	private static final int MODIFY_PHONE = 0x102;
	private static final int MODIFY_EMAIL = 0x103;
	private static final int MODIFY_ADDRESS = 0x104;
	private static final int MODIFY_AVATAR = 0x105;

	// private TextView tvLocation, tvPhone, tvEmail, tvAddress;

	private TextView tvName, tvNickName;
	private CircleImageView ivUser;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ivUser.setUseDefaultStyle(false);
			bitmapUtils.display(ivUser, (String) msg.obj);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_message);
		findViewsById();
		initBitmapVariable();
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_my_avar_login);
		fillViewsData();
	}

	private void findViewsById() {
		ivUser = (CircleImageView) findViewById(R.id.fragment_my_image_user);
		ivUser.setOnClickListener(this);
		tvName = (TextView) findViewById(R.id.activity_person_tv_name);
		tvNickName = (TextView) findViewById(R.id.fragment_my_user_name);
		tvNickName.setVisibility(View.VISIBLE);
		findViewById(R.id.activity_person_layout_name).setOnClickListener(this);
		findViewById(R.id.activity_person_layout_password).setOnClickListener(this);
		// tvPhone = (TextView) findViewById(R.id.activity_person_tv_phone);
		// tvEmail = (TextView) findViewById(R.id.activity_person_tv_emial);
		// tvAddress = (TextView) findViewById(R.id.activity_person_tv_address);
		// tvLocation = (TextView) findViewById(R.id.fragment_my_location);
		// tvLocation.setVisibility(View.VISIBLE);
		// findViewById(R.id.activity_person_layout_phone).setOnClickListener(this);
		// findViewById(R.id.activity_person_layout_emial).setOnClickListener(this);
		// findViewById(R.id.activity_person_layout_address).setOnClickListener(this);
	}

	private void fillViewsData() {
		setTitle("个人中心");
		final User user = LoginHelper.user;
		tvName.setText(TextUtils.isEmpty(user.nickname) ? user.memberNo : user.nickname);
		tvNickName.setText(TextUtils.isEmpty(user.nickname) ? user.memberNo : user.nickname);
		// tvPhone.setText(TextUtils.isEmpty(user.telephone) ? "绑定手机" : "已绑定手机 "
		// + user.telephone);
		// tvEmail.setText(TextUtils.isEmpty(user.email) ? "完善邮箱" : user.email);
		// tvAddress.setText(TextUtils.isEmpty(user.address) ? "完善地址" :
		// user.address);
		// if (TextUtils.isEmpty(user.address)) {
		// tvLocation.setVisibility(View.GONE);
		// } else {
		// tvLocation.setText(user.address);
		// }
		setAvatar();
	}

	private void sendDisplayAvatrMessage(String path) {
		bitmapUtils.clearMemoryCache(path);
		bitmapUtils.clearDiskCache(path);
		final Message message = Message.obtain();
		message.obj = path;
		handler.sendMessageDelayed(message, 200);
	}

	private void setAvatar() {
		bitmapUtils.display(ivUser, LoginHelper.user.avatr, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				ivUser.setUseDefaultStyle(false);
				ivUser.setImageBitmap(arg2);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				final File file = FileUtils.getExternalAvatarFile(PersonMessageActivity.this);
				if (file != null && file.exists()) {
					final String path = file.getAbsolutePath();
					sendDisplayAvatrMessage(path);
				} else {
					ivUser.setUseDefaultStyle(true);
					ivUser.setImageResource(R.drawable.ic_my_avar_login);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int requsetCode = 0;
		switch (v.getId()) {
			case R.id.fragment_my_image_user:
				startActivityForResult(new Intent(this, ModifyAvatarActivity.class), MODIFY_AVATAR);
				break;
			case R.id.activity_person_layout_name:
				requsetCode = MODIFY_NICKNAME;
				intent = new Intent(this, ModifyNameActivity.class);
				break;
			case R.id.activity_person_layout_password:
				requsetCode = MODIFY_PASSWORD;
				intent = new Intent(this, ModifyPasswordActivity.class);
				break;
			case R.id.activity_person_layout_phone:
				requsetCode = MODIFY_PHONE;
				intent = new Intent(this, ModifyPhoneActivity.class);
				break;
			case R.id.activity_person_layout_emial:
				requsetCode = MODIFY_EMAIL;
				intent = new Intent(this, ModifyNameActivity.class);
				intent.putExtra("type", 1);
				break;
			case R.id.activity_person_layout_address:
				requsetCode = MODIFY_ADDRESS;
				intent = new Intent(this, ModifyAddressActivity.class);
				break;
			default:
				break;
		}
		if (intent != null) {
			startActivityForResult(intent, requsetCode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case MODIFY_NICKNAME:
					tvName.setText(LoginHelper.user.nickname);
					tvNickName.setText(LoginHelper.user.nickname);
					setResult(RESULT_OK);
					break;
				case MODIFY_PASSWORD:
					break;
				case MODIFY_PHONE:
					// tvPhone.setText("已绑定手机 " + LoginHelper.user.telephone);
					break;
				case MODIFY_EMAIL:
					// tvEmail.setText(LoginHelper.user.email);
					break;
				case MODIFY_ADDRESS:
					// setResult(RESULT_OK);
					break;
				case MODIFY_AVATAR:
					final File file = FileUtils.getExternalAvatarFile(this);
					final String path = file.getAbsolutePath();
					sendDisplayAvatrMessage(path);
					Intent intent = new Intent();
					intent.putExtra("avatr", true);
					setResult(Activity.RESULT_OK, intent);
					break;
				default:
					break;
			}
		}
	}

}
