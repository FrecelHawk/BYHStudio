package com.xyt.ygcf.ui.my;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.base.BaseActivity;

public class RegisterProtocolActivity extends BaseActivity {

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			TextView textView = (TextView) findViewById(R.id.activity_register_protocol_tv);
			textView.setText(result);
			result = null;
			dissmissProgressDialog();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_protocol);
		setTitle("用户注册协议");
		showProgressDialog();
		new ReadingThread().start();
	}

	private String result = "";

	private class ReadingThread extends Thread {

		@Override
		public void run() {
			getTextFromAssets("register_protocol.txt");
			handler.sendEmptyMessage(0);
		}
	}

	private String getTextFromAssets(String fileName) {
		try {
			InputStream in = getResources().getAssets().open(fileName);
			int lenght = in.available();
			byte[] buffer = new byte[lenght];
			in.read(buffer);
			result = new String(buffer,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
