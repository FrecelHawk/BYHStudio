package com.xyt.ygcf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.ViewPagerAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 用户引导
 * 
 * @author lenovo hexiangyang
 * 
 */
public class GuideActivity extends Activity implements OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
		setContentView(R.layout.guide);
		initViews();
//		initDots();
	}

	/**
	 * 初始化页面
	 */
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		ImageView imageView1 = new ImageView(this);
		imageView1.setBackgroundResource(R.drawable.guide_one);
		ImageView imageView2 = new ImageView(this);
		imageView2.setBackgroundResource(R.drawable.guide_two);
		views.add(imageView1);
		views.add(imageView2);
		views.add(inflater.inflate(R.layout.what_new_four, null));
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	/**
	 * 初始化底部小点
	 */
//	private void initDots() {
//		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
//		// group.setBackgroundColor(Color.argb(200, 135, 135, 152));
//		dots = new ImageView[views.size()];
//		// 广告栏的小圆点图标
//		for (int i = 0; i < views.size(); i++) {
//			// 创建一个ImageView, 并设置宽高. 将该对象放入到数组中
//			LinearLayout layout = new LinearLayout(this);
//			layout.setPadding(5, 0, 0, 0);
//			imageView = new ImageView(this);
//			imageView.setLayoutParams(new LayoutParams(10, 10));
//			dots[i] = imageView;
//
//			// 初始值, 默认第0个选中
//			if (i == 0) {
//				dots[i].setBackgroundResource(R.drawable.feature_point_cur);
//			} else {
//				dots[i].setBackgroundResource(R.drawable.feature_point);
//			}
//			// 将小圆点放入到布局中
//
//			layout.addView(dots[i]);
//			group.addView(layout);
//		}
//	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		// setCurrentDot(arg0);
		// 获取当前显示的页面是哪个页面
		atomicInteger.getAndSet(arg0);
		// 重新设置原点布局集合
//		for (int i = 0; i < dots.length; i++) {
//			dots[arg0].setBackgroundResource(R.drawable.feature_point_cur);
//			if (arg0 != i) {
//				dots[i].setBackgroundResource(R.drawable.feature_point);
//			}
//		}
	}

}
