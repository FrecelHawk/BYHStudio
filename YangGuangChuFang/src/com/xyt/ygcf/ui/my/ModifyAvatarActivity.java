package com.xyt.ygcf.ui.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.FileUtils;
import com.xyt.ygcf.widget.CircleImageView;

public class ModifyAvatarActivity extends BaseActivity {

	private static final int REQUEST_CODE_CAMERA = 0x100;
	private static final int REQUEST_CODE_CHOOSE = 0x101;
	private static final int REQUEST_CODE_CROP = 0x103;

	private GridView gridView;
	private CircleImageView imageView;

	private File mTempImageFile, mCropFile;

	private long previousId = 0L;

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_avatar);
		initBitmapVariable();
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_my_avar_login);
		findViews();
		setViewsData();
	}

	private void findViews() {
		findViewById(R.id.activity_modify_avatar_btn).setOnClickListener(this);
		imageView = (CircleImageView) findViewById(R.id.activity_modify_avatar_img);
		gridView = (GridView) findViewById(R.id.activity_modify_avatar_grid);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (previousId == id) {
					return;
				}
				previousId = id;
				setDrawableToFile((int) id);
			}
		});
	}

	private void setDrawableToFile(int drawableId) {
		mCropFile = FileUtils.getExternalAvatarCropFile(this);
		final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		try {
			FileOutputStream outputStream = new FileOutputStream(mCropFile);
			baos.writeTo(outputStream);
			outputStream.flush();
			outputStream.close();
			imageView.setUseDefaultStyle(true);
			imageView.setImageBitmap(bitmap);
			bitmapUtils.clearMemoryCache(mCropFile.getAbsolutePath());
			bitmapUtils.clearDiskCache(mCropFile.getAbsolutePath());
			setResult(Activity.RESULT_OK);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void setViewsData() {
		setTitle("修改头像");
		setAvatar();
		setResult(Activity.RESULT_CANCELED);
		// gridView.setAdapter(new AvatrAdapter(this));
	}

	private void setAvatar() {
		bitmapUtils.display(imageView, LoginHelper.user.avatr, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				imageView.setUseDefaultStyle(false);
				imageView.setImageBitmap(arg2);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				final File file = FileUtils.getExternalAvatarFile(ModifyAvatarActivity.this);
				if (file != null && file.exists()) {
					imageView.setUseDefaultStyle(false);
					bitmapUtils.display(imageView, file.getAbsolutePath());
				} else {
					imageView.setUseDefaultStyle(true);
					imageView.setImageResource(R.drawable.ic_my_avar_login);
				}
			}
		});
	}

	/**
	 * 缓存清除对话框
	 * 
	 * @param context
	 */
	public void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
		final View rootView = LayoutInflater.from(this).inflate(R.layout.view_upload_atatr, null);
		rootView.findViewById(R.id.view_upload_item_1).setOnClickListener(this);
		rootView.findViewById(R.id.view_upload_item_2).setOnClickListener(this);
		rootView.findViewById(R.id.view_upload_item_3).setOnClickListener(this);
		dialog.setContentView(rootView);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.activity_modify_avatar_btn:
				showChooseDialog();
				break;
			case R.id.view_upload_item_1:
				choosePhotoClicked();
				closeDialog();
				break;
			case R.id.view_upload_item_2:
				takePhotoClicked();
				closeDialog();
				break;
			case R.id.view_upload_item_3:
				closeDialog();
				break;
			default:
				break;
		}
	}

	private void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	/**
	 * 在弹出的对话框中,选择了拍照
	 */
	public void takePhotoClicked() {
		try {
			mTempImageFile = FileUtils.createTempImageFile(this);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "请检查存储是否正常", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempImageFile));
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}

	/**
	 * 对话框里选择从相册选取.
	 */
	public void choosePhotoClicked() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_CODE_CHOOSE);
	}

	private boolean cropImage(Uri uri) {
		mCropFile = FileUtils.getExternalAvatarCropFile(this);
		try {
			Uri afterCropFile = Uri.fromFile(mCropFile);
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", imageView.getWidth());
			intent.putExtra("outputY", imageView.getHeight());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, afterCropFile);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			startActivityForResult(intent, REQUEST_CODE_CROP);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		boolean success = false;
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_CAMERA:
					success = cropImage(Uri.fromFile(mTempImageFile));
					if (!success) {
						setAvatar(mTempImageFile.getAbsolutePath());
					}
					break;
				case REQUEST_CODE_CHOOSE:
					success = cropImage(data.getData());
					if (!success) {
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						String picturePath = cursor.getString(columnIndex);
						cursor.close();
						setAvatar(picturePath);
					}
					break;
				case REQUEST_CODE_CROP:
					setAvatar(mCropFile);
					break;
				default:
					break;
			}
		}
	}

	private void setAvatar(File file) {
		if (file != null && file.exists()) {
			setAvatar(file.getAbsolutePath());
			if (mTempImageFile != null && mTempImageFile.exists()) {
				mTempImageFile.delete();
				mTempImageFile = null;
			}
		}
	}

	private void setAvatar(String picturePath) {
		try {
			Bitmap bitmap = compressBitmap(picturePath);
			if (bitmap != null) {
				imageView.setUseDefaultStyle(false);
				imageView.setImageBitmap(bitmap);
				bitmap = null;
				RequestParams params = new RequestParams();
				params.addBodyParameter("file", mCropFile);
				sendRequest(HttpMethod.POST, UrlMy.UPLOAD_AVATR, params, 0, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩bitmap，图片按比例大小压缩方法
	 * 
	 * @param srcPath
	 * @return
	 * @throws IOException
	 */
	public Bitmap compressBitmap(String srcPath) throws IOException {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = imageView.getHeight();
		float ww = imageView.getWidth();
		int be = 1;
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) {
			be = 1;
		}
		newOpts.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		if (mCropFile != null) {
			FileOutputStream outputStream = new FileOutputStream(mCropFile);
			baos.writeTo(outputStream);
			outputStream.flush();
			outputStream.close();
		}
		baos.flush();
		baos.close();
		return bitmap;
	}

	@Override
	public void handleJson(String json, int which) {
		Toast.makeText(this, "上传头像成功", Toast.LENGTH_SHORT).show();
		setResult(Activity.RESULT_OK);
		File successFile = FileUtils.getExternalAvatarFile(this);
		mCropFile.renameTo(successFile);
		bitmapUtils.clearDiskCache(successFile.getAbsolutePath());
		bitmapUtils.clearMemoryCache(successFile.getAbsolutePath());
		bitmapUtils.clearDiskCache(LoginHelper.user.avatr);
		bitmapUtils.clearMemoryCache(LoginHelper.user.avatr);
		super.onBackPressed();
	}

	@Override
	public void handleError(String message, int which) {
		Toast.makeText(this, "上传头像出错，请重新上传", Toast.LENGTH_SHORT).show();
		setAvatar();
		deleteCropFile();
	}

	@Override
	public void handleNetError(String message, int which) {
		super.handleNetError(message, which);
		setAvatar();
		deleteCropFile();
	}

	@Override
	public void handleParseJsonException(int which) {
		setAvatar();
		deleteCropFile();
		Toast.makeText(this, "上传头像出错，请重新上传", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void handleServerBusy(String message, int which) {
		super.handleServerBusy(message, which);
		setAvatar();
		deleteCropFile();
	}

	private void deleteCropFile() {
		if (mCropFile != null && mCropFile.exists()) {
			mCropFile.delete();
		}
	}
}
