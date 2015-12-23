package com.xyt.ygcf.ui;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.entity.my.User;
import com.xyt.ygcf.logic.my.LoginHelper;
import com.xyt.ygcf.urls.UrlMy;
import com.xyt.ygcf.util.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

/**
 * 发表评论
 * 
 * @author wjj
 * 
 */
public class CommentActivity extends BaseActivity  {

	private RatingBar ratBar;
	private Button  btnTJ;
	private EditText et_con;

	// 1：商家  2：商品
	private int eatery;
	//商品或商家id
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		getIntentData();
		initTopView();
    }
	
	private void getIntentData(){
		eatery = getIntent().getIntExtra("eatery", 1);
		id = getIntent().getStringExtra("id");
	}

	private void initTopView() {
		ratBar = (RatingBar) findViewById(R.id.indicator_ratingbar1);
		et_con = (EditText) findViewById(R.id.commemt_con);
		btnTJ = (Button) findViewById(R.id.comment_tijiao);
		btnTJ.setOnClickListener(this);
		setTitle("发表评论");
	}

	@Override
	public void onClick(View v) {
		submitComment();
	}
	
	private void submitComment(){
		 int recommendLevel=(int) ratBar.getRating();
		if( recommendLevel ==0){
			Toast.makeText(this, "您尚未对商家作出评级", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String commentContent = et_con.getText().toString();
		if(TextUtils.isEmpty(commentContent)){
			Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(eatery == 1){
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("merchantId", id);
			params.addQueryStringParameter("isAnonymous", "true");
			params.addQueryStringParameter("content", commentContent);
			params.addQueryStringParameter("recommendLevel",String.valueOf(recommendLevel));
			params.addQueryStringParameter("nickname", LoginHelper.user.nickname);
			sendRequest(UrlMy.SHOP_COMMENT, params, 0, true);
		}else {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("productId", id);
			params.addQueryStringParameter("isAnonymous", "1");
			params.addQueryStringParameter("content", commentContent);
			params.addQueryStringParameter("recommendLevel",String.valueOf(recommendLevel));
			params.addQueryStringParameter("nickname", LoginHelper.user.nickname);
			sendRequest(UrlMy.PRO_EVALUATION, params, 1, true);
		}
	}
	
	

	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		if(which == 0){
			Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
		}else if(which == 1){
			Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
		}
		finish();
	}
		
	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
		Toast.makeText(this, "提交失败", Toast.LENGTH_SHORT).show();
	}
	
		

		

	

	

	

}
