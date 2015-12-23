package com.xyt.ygcf.ui.search;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	public final String TAG = "VideoActivity";
	private final String VIDEO1 = "test1.3gp";
	private final String VIDEO2 = "test2.3gp";
	private final String VIDEO3 = "test3.3gp";
	private final String VIDEO4 = "test4.3gp";

	private String video[] = { VIDEO1, VIDEO2, VIDEO3, VIDEO4 };
	private int i = 0;

	public boolean storeVideoFile() {
		try {
			InputStream is = getResources().getAssets().open(video[i]);

			// 注意,这里用 MODE_WORLD_READABLE 是因为播放Video的是MediaPlayer进程,不是本进程
			// 为了让, MediaPlayer进程能读取此文件,所以设置为: MODE_WORLD_READABLE
			FileOutputStream os = openFileOutput(video[i], MODE_WORLD_READABLE);

			byte[] buffer = new byte[1024];
			while (is.read(buffer) > -1) {
				os.write(buffer);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Random r = new Random();

		i = r.nextInt(5);

		if (!storeVideoFile())
			return;

		VideoView videoView = new VideoView(this);

		setContentView(videoView);

		// videoView.setMediaController(new MediaController(this));
		// videoView.setVideoURI(Uri
		// .parse("http://192.168.1.241:28080/ocr/data/20140715.mp4"));
		videoView
				.setVideoPath(getFilesDir().getAbsolutePath() + "/" + video[i]);

		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// 是否循环播放
				mp.setLooping(false);

			}
		});

		videoView.start();

		videoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// 播放结束后
						finish();
					}
				});
	}

}
