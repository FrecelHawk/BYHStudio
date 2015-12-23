package com.baiyuhui.huangyan.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiyuhui.huangyan.R;
import com.baiyuhui.huangyan.activity.FindCirclePosted;
import com.baiyuhui.huangyan.activity.PersonDividend;
import com.baiyuhui.huangyan.activity.PersonMember;
import com.baiyuhui.huangyan.activity.PersonRecommend;
import com.baiyuhui.huangyan.entity.ImageBean;
import com.baiyuhui.huangyan.utils.BitmapUtil;
import com.baiyuhui.huangyan.utils.ImageTools;
import com.baiyuhui.huangyan.utils.ShareUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * ：个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonFragment extends Fragment implements OnClickListener {

	private View view;
	private LinearLayout personLayout;
	private TextView fenhong;
	private RelativeLayout recommend;
	private RelativeLayout invitation;
	private LinearLayout member;
	private ImageView photoImg;
	
	private LinearLayout ll_popup;
	private PopupWindow pop = null;

	public static final String DESCRIPTOR = "com.umeng.share";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
	
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	
	private static final int SCALE = 5;// 照片缩小比例

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_person, container, false);
		initViews();
		showPopupWindow();
		return view;
	}
	
	/**
	 * 
	 */
	private void initViews() {
		
		personLayout =  (LinearLayout) view.findViewById(R.id.person_layout);
		fenhong = (TextView) view.findViewById(R.id.fenhong);
		fenhong.setOnClickListener(this);

		recommend = (RelativeLayout)view.findViewById(R.id.recommend_lay);
		recommend.setOnClickListener(this);

		invitation = (RelativeLayout) view.findViewById(R.id.invitation_lay);
		invitation.setOnClickListener(this);

		member = (LinearLayout)view.findViewById(R.id.member);
		member.setOnClickListener(this);
		
		photoImg = (ImageView) view.findViewById(R.id.person_img);
		photoImg.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fenhong: // 分红明细
			startActivity(new Intent(getActivity(), PersonDividend.class));
			break;
		case R.id.recommend_lay: // 推荐的会员
			startActivity(new Intent(getActivity(), PersonRecommend.class));
			break;

		case R.id.invitation_lay: // 分享邀请码
			 new ShareUtil(getActivity(), mController, "", "", "");
			 mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
			 SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
			 SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
			 mController.openShare(getActivity(), false);
			break;

		case R.id.member: // 会员等级申请
			startActivity(new Intent(getActivity(), PersonMember.class));
			break;
		case R.id.person_img:
			ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
			pop.showAtLocation(personLayout, Gravity.BOTTOM, 0, 0);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
//				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				ImageBean takePhoto = new ImageBean();
				takePhoto.setBitmap(newBitmap);
				BitmapUtil.tempSelectBitmap.add(takePhoto);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
//				bitmap.recycle();
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getActivity().getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						
						ImageBean takePhoto2 = new ImageBean();
						takePhoto2.setBitmap(smallBitmap);
						BitmapUtil.tempSelectBitmap.add(takePhoto2);
						
						// 释放原始图片占用的内存，防止out of memory异常发生
//						photo.recycle();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}
	
	

	private void showPopupWindow(){
		pop = new PopupWindow(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows,
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
						.getExternalStorageDirectory(), "image.jpg"));
				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						imageUri);
				startActivityForResult(openCameraIntent,
						TAKE_PICTURE);
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
				
				Intent openAlbumIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				startActivityForResult(openAlbumIntent,
						CHOOSE_PICTURE);
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}
}
