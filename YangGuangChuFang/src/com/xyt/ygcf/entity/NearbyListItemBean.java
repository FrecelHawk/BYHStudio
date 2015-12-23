package com.xyt.ygcf.entity;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

import com.xyt.ygcf.entity.my.CollectionCanteenBeen;
import com.xyt.ygcf.entity.my.MyBaseListBeen;

import android.R.integer;
import android.graphics.Bitmap;

/**
 * 周边模块-列表的Bean类(待续)
 * 
 */
public class NearbyListItemBean implements Serializable{

	public String logoUrl; //icon
	public String stopId; // 商店或食堂的标识
	public String stopName; // 商店或食堂
	public String theme; // 主题或性质
	public String rate; // 评级
	public String phone; // 电话
	public String distance; // 距离
	public double longitude;// 经度
	public double latitude;// 纬度
	public String address; //地址
	public int praiseScore;  //评优值

}
