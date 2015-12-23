package com.xyt.ygcf.ui;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.Constants;
import com.xyt.ygcf.base.BaseFragment;
import com.xyt.ygcf.entity.my.User;
import com.xyt.ygcf.impl.IActivityComunicationFragment;
import com.xyt.ygcf.logic.my.Cookies;
import com.xyt.ygcf.logic.my.HashAlgorithm;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.logic.my.MyJsonParse;
import com.xyt.ygcf.ui.my.CollectionActivitiy;
import com.xyt.ygcf.ui.my.ComplainListActivity;
import com.xyt.ygcf.ui.my.EvaluateActivity;
import com.xyt.ygcf.ui.my.GetBackPasswordActivity;
import com.xyt.ygcf.ui.my.PersonMessageActivity;
import com.xyt.ygcf.ui.my.RegisterActivity;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.BaseUtil;
import com.xyt.ygcf.util.FileUtils;
import com.xyt.ygcf.util.SpHelper;
import com.xyt.ygcf.widget.CircleImageView;

/**
 * 
 * @author yuyangming
 * 
 */
public class MyFragment extends BaseFragment {

	private static final int MAX_ERROR_COUNT = 5;

	
	private static final int LOGIN = 0;
	private static final int LOGIN_OUT = 1;

	private static final int REQUEST_CODE_REGISTER = 0x100;
	private static final int REQUEST_CODE_MESSAGE = 0x101;

	private EditText etName, etPassword, etVertify;

	private ImageView imgVertify;
	private CircleImageView imgUser;

	private CheckBox ckSavePassword, ckAutoLogin;

	private View loginLayout, loginMessageLayout, vertifyLayout;

	private TextView tvUserName;// userLocation;

	public String title = null;

	private IActivityComunicationFragment listner = null;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			imgUser.setUseDefaultStyle(false);
			bitmapUtils.display(imgUser, (String) msg.obj);
		};
	};

	public static MyFragment getInstance(String title) {
		MyFragment fragment = new MyFragment();
		fragment.title = title;
		return fragment;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.fragment_my, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mContext instanceof Main) {
			listner = (IActivityComunicationFragment) mContext;
		}
		initBitmapVariable();
		findView(getView());
		initViewData();
	}

	private void findView(View view) {
		ckSavePassword = (CheckBox) view.findViewById(R.id.fragment_my_save_checkbox);
		ckAutoLogin = (CheckBox) view.findViewById(R.id.fragment_my_auto_checkbox);
		etName = (EditText) view.findViewById(R.id.fragment_my_name);
		etPassword = (EditText) view.findViewById(R.id.fragment_my_password);
		etVertify = (EditText) view.findViewById(R.id.fragment_my_et_vertify);
		imgUser = (CircleImageView) view.findViewById(R.id.fragment_my_image_user);
		view.findViewById(R.id.fragment_my_login_btn).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_register).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_forget_password).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_message_person).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_collection).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_evaluate).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_complaint).setOnClickListener(this);
		view.findViewById(R.id.fragment_my_login_out).setOnClickListener(this);

		imgVertify = (ImageView) view.findViewById(R.id.fragment_my_tv_vertify);
		imgVertify.setOnClickListener(this);
		vertifyLayout = view.findViewById(R.id.fragment_my_layout_vertify);
		loginLayout = view.findViewById(R.id.fragment_my_no_logined);
		loginMessageLayout = view.findViewById(R.id.fragment_my_message_layout);

		tvUserName = (TextView) view.findViewById(R.id.fragment_my_user_name);
		// userLocation = (TextView)
		// view.findViewById(R.id.fragment_my_location);

		if (!TextUtils.isEmpty(title)) {
			((TextView) view.findViewById(R.id.desktop_userID)).setText(title);
		}
	}

	private void initViewData() {
		ckSavePassword.setText("记住密码");
		ckAutoLogin.setText("自动登录");
		SpHelper spHelper = SpHelper.init(mContext);
		if (LoginHelper.isLogin()) {
			setViewSuccessData();
			spHelper.setLoginOut(false);
		} else {
			etName.setText(spHelper.getUserCode());
			ckAutoLogin.setChecked(spHelper.getAutoLogin());
			loginLayout.setVisibility(View.VISIBLE);
			loginMessageLayout.setVisibility(View.GONE);

			errorCount = spHelper.getLoginErrorCount();
			if (errorCount >= MAX_ERROR_COUNT) {
				vertifyLayout.setVisibility(View.VISIBLE);
				requestVertifyCode();
			} else {
				boolean select = spHelper.getSavePassword();
				if (select) {
					etPassword.setText(spHelper.getUserPassword());
				}
				ckSavePassword.setChecked(select);
			}
		}
		spHelper = null;
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
			case R.id.fragment_my_login_btn:
				loginIfRight();
				break;
			case R.id.fragment_my_tv_vertify:
				requestVertifyCode();
				break;
			case R.id.fragment_my_register:
				startActivityForResult(new Intent(mContext, RegisterActivity.class), REQUEST_CODE_REGISTER);
				break;
			case R.id.fragment_my_message_person:
				startActivityForResult(new Intent(mContext, PersonMessageActivity.class), REQUEST_CODE_MESSAGE);
				break;
			case R.id.fragment_my_collection:
				startActivity(new Intent(mContext, CollectionActivitiy.class));
				// startActivity(new Intent(mContext, ComplainActivity.class));
				break;
			case R.id.fragment_my_evaluate:
				startActivity(new Intent(mContext, EvaluateActivity.class));
				break;
			case R.id.fragment_my_complaint:
				startActivity(new Intent(mContext, ComplainListActivity.class));
				break;
			case R.id.fragment_my_login_out:
				loginOut();
				break;
			case R.id.fragment_my_forget_password:
				startActivity(new Intent(mContext, GetBackPasswordActivity.class));
				break;
			default:
				break;
		}
	}

	private void loginIfRight() {
		final String nameString = etName.getText().toString().trim();
		final String passwordString = etPassword.getText().toString().trim();
		if (TextUtils.isEmpty(nameString)) {
			showToast("请输入用户名");
			return;
		}
		if (TextUtils.isEmpty(passwordString)
				|| (passwordString.length() < Constants.PASSWORD_MIN_LENGHT || passwordString.length() > Constants.PASSWORD_MAX_LENGHT)) {
			showToast("请输入合适长度的密码");
			return;
		}
		BaseUtil.hideSoftInput((Activity) mContext);
		login(nameString, passwordString);
	}

	private int errorCount = 0;

	private String lastPassword;

	private void login(String names, String passwords) {
		final String spPassword = SpHelper.init(mContext).getUserPassword();
		lastPassword = spPassword;
		final int spPasswordLength = spPassword.length();
		final int currentPasswordLength = passwords.length();
		final int maxPasswordLength = Constants.PASSWORD_MAX_LENGHT;
		if (LoginHelper.isLogin()) {
			String tempName = SpHelper.init(mContext).getUserCode();
			if (names.equals(tempName) && currentPasswordLength == maxPasswordLength
					&& spPasswordLength > maxPasswordLength
					&& spPassword.substring(0, currentPasswordLength).equals(passwords)) {
				loadSuccess();
				return;
			}
		}
		if (!isRequesting[LOGIN]) {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("code", names);
			if (errorCount >= MAX_ERROR_COUNT) {
				final String vertifyCode = etVertify.getText().toString();
				if (!TextUtils.isEmpty(vertifyCode) && vertifyCode.length() == 4) {
					params.addQueryStringParameter("verificationCode", vertifyCode);
					params.addQueryStringParameter("verifyCodeToken", String.valueOf(verifyCodeToken));
				} else {
					showToast("请输入验证码");
					return;
				}
			}
			showProgressDialog();
			boolean useSpPassword = (currentPasswordLength == maxPasswordLength)
					&& (currentPasswordLength < spPasswordLength) && (spPasswordLength > maxPasswordLength)
					&& (spPassword.substring(0, currentPasswordLength).equals(passwords));
			if (!useSpPassword) {
				try {
					passwords = HashAlgorithm.getHashText(passwords);
					lastPassword = passwords;
					isRequesting[LOGIN] = true;
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
			LoginHelper.login(requestCallBack, params, lastPassword, LOGIN);
		}
	}

	private long verifyCodeToken = 0L;

	private void requestVertifyCode() {
		verifyCodeToken = System.currentTimeMillis();
		bitmapUtils.display(imgVertify, UrlMy.VERTIFIY_CODE + verifyCodeToken, new BitmapLoadCallBack<ImageView>() {

			@Override
			public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				if (vertifyLayout.getVisibility() == View.GONE) {
					vertifyLayout.setVisibility(View.VISIBLE);
				}
				arg0.setImageBitmap(arg2);
			}

			@Override
			public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
				Toast.makeText(mContext, "获取验证码失败，请检查网络", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void loginOut() {
		if (!isRequesting[LOGIN_OUT] && LoginHelper.isLogin()) {
			isRequesting[LOGIN_OUT] = true;
			RequestParams params = new RequestParams();
			sendRequest(HttpMethod.GET, UrlMy.LOGIN_OUT, params, LOGIN_OUT, true);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_REGISTER:
					final String acount = data.getStringExtra("acount");
					etName.setText(acount);
					etPassword.setText("");
					SpHelper.init(mContext).setUserCode("");
					SpHelper.init(mContext).setUserPassword("");
					break;
				case REQUEST_CODE_MESSAGE:
					tvUserName.setText(LoginHelper.user.nickname);
					if (data != null && data.getBooleanExtra("avatr", false)) {
						final File file = FileUtils.getExternalAvatarFile(mContext);
						final String path = file.getAbsolutePath();
						sendDisplayAvatrMessage(path);
					}
					break;
				default:
					break;
			}
		}
	}

	private void setViewSuccessData() {
		tvUserName.setVisibility(View.VISIBLE);
		// userLocation.setVisibility(View.VISIBLE);
		loginLayout.setVisibility(View.GONE);
		loginMessageLayout.setVisibility(View.VISIBLE);
		tvUserName.setText(TextUtils.isEmpty(LoginHelper.user.nickname) ? LoginHelper.user.memberNo
				: LoginHelper.user.nickname);
		// userLocation.setText("定居地：" + "广州");
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
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_my_avar_login);
		bitmapUtils.display(imgUser, LoginHelper.user.avatr, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				imgUser.setUseDefaultStyle(false);
				imgUser.setImageBitmap(arg2);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				final File file = FileUtils.getExternalAvatarFile(mContext);
				if (file != null && file.exists()) {
					final String path = file.getAbsolutePath();
					sendDisplayAvatrMessage(path);
				} else {
					imgUser.setUseDefaultStyle(true);
					imgUser.setImageResource(R.drawable.ic_my_avar_login);
				}
			}
		});

	}

	private void loadSuccess() {
		vertifyLayout.setVisibility(View.GONE);
		errorCount = 0;
		SpHelper spHelper = SpHelper.init(mContext);
		spHelper.setLoginOut(false);
		spHelper.setUserCode(LoginHelper.user.memberNo);
		spHelper.setAutoLogin(ckAutoLogin.isChecked());
		spHelper.setSavePassword(ckSavePassword.isChecked());
		spHelper.setLoginErrorCount(errorCount);
		if (!ckSavePassword.isChecked()) {
			etPassword.setText("");
			SpHelper.init(mContext).setUserPassword("");
		} else {
			SpHelper.init(mContext).setUserPassword(lastPassword);
		}
		final FragmentActivity activity = (FragmentActivity) mContext;
		if (activity != null && !activity.isFinishing()) {
			activity.setResult(Activity.RESULT_OK);
			activity.finish();
		}
	}

	@Override
	protected void handleResponse(ResponseInfo<String> arg0, int which) {
		if (which == LOGIN) {
			Cookies.getInstance().setCookieStore(arg0.getCookieStore(), true);
		}
	}

	@Override
	public void handleJson(String json, int which) {
		try {
			SpHelper spHelper = SpHelper.init(mContext);
			if (which == LOGIN) {
				User user = MyJsonParse.parseLogin(json);
				if (user != null) {
					LoginHelper.user = user;
					loadSuccess();
					LoginHelper.setTimerServiceState(false);
				} else {
					handleParseJsonException(which);
				}
			} else if (which == LOGIN_OUT) {
				LoginHelper.user = null;
				spHelper.setAutoLogin(false);
				spHelper.setLoginOut(true);
				beenSetView = false;
				if (listner != null) {
					listner.framgentCallBack(0);
				}
			}
		} catch (JSONException e) {
			handleParseJsonException(which);
			e.printStackTrace();
		}
	}

	@Override
	public void handleError(String message, int which) {
		if (which == LOGIN) {
			Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
			errorCount++;
			if (errorCount >= MAX_ERROR_COUNT) {
				requestVertifyCode();
				SpHelper.init(mContext).setLoginErrorCount(errorCount);
			}
		} else {
			super.handleError(message, which);
		}
	}

	private boolean beenSetView = false;

	public void finishedLoginInLoginActivity() {
		if (beenSetView) {
			return;
		}
		beenSetView = true;
		setViewSuccessData();
	}
}
