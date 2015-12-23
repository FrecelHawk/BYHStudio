package com.xyt.ygcf;

import java.util.ArrayList;
import java.util.List;

import com.xyt.ygcf.entity.VideoItem;

/***
 * 必须是全局的常量才可以放
 * 
 * @author freesonfish
 * 
 */
public class Constants {

	/** 密码最短长度 */
	public static final int PASSWORD_MIN_LENGHT = 6;

	/** 密码最长长度 */
	public static final int PASSWORD_MAX_LENGHT = 20;

	/** 默认城市 */
	public static final String DEFAULTCITY = "DEFAULTCITY";
	/** 默认城市 id */
	public static final String DEFAULTCITY_ID = "DEFAULTCITY_ID";
	/** 学校食堂 */
	public static final String INDUSTRY_SCHOOL_MAKER = "SCH";
	/** 企业食堂 */
	public static final String INDUSTRY_COMPANY_MAKER = "ENT";
	/** 餐厅 */
	public static final String INDUSTRY_RESTAURANT_MAKER = "RST";

	/** 商品 */
	public static final String INDUSTRY_PRODUCT_MAKER = "PRO";

	/** 食堂 */
	public static final String INDUSTRY_CANTEEN = "SCH_ENT";

	public static final int TAB_MY_CODE = 3;
	
	/** 路径*/
	public static final String ROOT_PATH = "/data/data/com.xyt.ygcf/";
	
	public static List<VideoItem> videoListAll = new ArrayList<VideoItem>();
}
