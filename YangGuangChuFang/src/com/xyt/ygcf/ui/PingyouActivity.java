package com.xyt.ygcf.ui;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.urls.UrlMy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

/**
 * 发表评优
 * 
 * @author wjj
 * 
 */
public class PingyouActivity extends BaseActivity {

	private Button btnTJ;
	private RadioButton rb11, rb12, rb13, rb21, rb22, rb23, rb31, rb32, rb33,
			rb41, rb42, rb43, rb51, rb52, rb53, rb61, rb62, rb63, rb71, rb72,
			rb73, rb81, rb82, rb83;

	private int v1, v2, v3, v4, v5, v6, v7, v8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pingyou);

		initTopView();

		initRadioGroups();


		btnTJ = (Button) findViewById(R.id.pingyou_tijiao);
		btnTJ.setOnClickListener(show);

	}

	private void initRadioGroups() {
		RadioGroup gp1 = (RadioGroup) this.findViewById(R.id.rg_py1);

		rb11 = (RadioButton) findViewById(R.id.rb11);
		rb12 = (RadioButton) findViewById(R.id.rb12);
		rb13 = (RadioButton) findViewById(R.id.rb13);

		rb21 = (RadioButton) findViewById(R.id.rb21);
		rb22 = (RadioButton) findViewById(R.id.rb22);
		rb23 = (RadioButton) findViewById(R.id.rb23);

		rb31 = (RadioButton) findViewById(R.id.rb31);
		rb32 = (RadioButton) findViewById(R.id.rb32);
		rb33 = (RadioButton) findViewById(R.id.rb33);

		rb41 = (RadioButton) findViewById(R.id.rb41);
		rb42 = (RadioButton) findViewById(R.id.rb42);
		rb43 = (RadioButton) findViewById(R.id.rb43);

		rb51 = (RadioButton) findViewById(R.id.rb51);
		rb52 = (RadioButton) findViewById(R.id.rb52);
		rb53 = (RadioButton) findViewById(R.id.rb53);

		rb61 = (RadioButton) findViewById(R.id.rb61);
		rb62 = (RadioButton) findViewById(R.id.rb62);
		rb63 = (RadioButton) findViewById(R.id.rb63);

		rb71 = (RadioButton) findViewById(R.id.rb71);
		rb72 = (RadioButton) findViewById(R.id.rb72);
		rb73 = (RadioButton) findViewById(R.id.rb73);

		rb81 = (RadioButton) findViewById(R.id.rb81);
		rb82 = (RadioButton) findViewById(R.id.rb82);
		rb83 = (RadioButton) findViewById(R.id.rb83);

	}

	private void initTopView() {
		setTitle("评优");
	}
	
	@Override
	public void handleJson(String json, int which) {
		super.handleJson(json, which);
		Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
		finish();
	}
		
	@Override
	public void handleError(String message, int which) {
		super.handleError(message, which);
		Toast.makeText(this, "提交失败", Toast.LENGTH_SHORT).show();
	}
	

	private OnClickListener show = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (rb11.isChecked()) {
				v1 = 1;
			} else if (rb12.isChecked()) {
				v1 = 2;
			} else if (rb13.isChecked()) {
				v1 = 3;
			}
			if (rb21.isChecked()) {
				v2 = 1;
			}
			if (rb22.isChecked()) {
				v2 = 2;
			}
			if (rb23.isChecked()) {
				v2 = 3;
			}
			if (rb31.isChecked()) {
				v3 = 1;
			}
			if (rb32.isChecked()) {
				v3 = 2;
			}
			if (rb33.isChecked()) {
				v3 = 3;
			}

			if (rb41.isChecked()) {
				v4 = 1;
			}
			if (rb42.isChecked()) {
				v4 = 2;
			}
			if (rb43.isChecked()) {
				v4 = 3;
			}

			if (rb51.isChecked()) {
				v5 = 1;
			}
			if (rb52.isChecked()) {
				v5 = 2;
			}
			if (rb53.isChecked()) {
				v5 = 3;
			}

			if (rb61.isChecked()) {
				v6 = 1;
			}
			if (rb62.isChecked()) {
				v6 = 2;
			}
			if (rb63.isChecked()) {
				v6 = 3;
			}

			if (rb71.isChecked()) {
				v7 = 1;
			}
			if (rb72.isChecked()) {
				v7 = 2;
			}
			if (rb73.isChecked()) {
				v7 = 3;
			}

			if (rb81.isChecked()) {
				v8 = 1;
			}
			if (rb82.isChecked()) {
				v8 = 2;
			}
			if (rb83.isChecked()) {
				v8 = 3;
			}

			String praiseDetails ="1=" + v1 + ",2=" + v2 + ",3=" + v3 + ",4=" + v4 + ",5="
					+ v5 + ",6=" + v6 + ",7=" + v7 + ",8=" + v8; 
			String id = getIntent().getStringExtra("id");
			RequestParams params  = new RequestParams();
			params.addQueryStringParameter("merchantId", id);
			params.addQueryStringParameter("praiseDetails", praiseDetails);
			sendRequest(UrlMy.RESTAURANT_HIGHEST_QUALITY, params, 0, true);
			
		}

	};
	


}
