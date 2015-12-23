package com.baiyuhui.huangyan.network;

public class MyUrl {
	public static String HTTP = "http://";
	/** 内网服务器地址 */
	// public static String HOST = "192.168.1.114:3000";

	/** 外网服务器地址 */
	public static String HOST = "test.ai1000.com";

	/** 新闻 */
	public static final String NEWS = HTTP + HOST + "/api/news/getbytypepage";

	/** 新闻/详情 */
	public static final String NEWS_DETAILS = HTTP + HOST
			+ "/api/news/getone?id=";

	/** 视频 */
	public static final String VIDEO = HTTP + HOST + "/api/news/getbytypepage";

	/** 公益 */
	public static final String BENEFIT = HTTP + HOST
			+ "/api/news/getbytypepage";

	/** 故事 */
	public static final String STORY = HTTP + HOST + "/api/news/getbytypepage";

	/** 学堂 */
	public static final String SCHOOL = HTTP + HOST + "/api/news/getbytypepage";

	/** 关于我们 */
	public static final String PREP = HTTP + HOST + "/api/syscnf_get?id=100";

	/** 新手指南 */
	public static final String NEWBIE = HTTP + HOST + "/api/syscnf_get?id=103";

	/** 圈子获取全部版块 */
	public static final String CIRCLE = HTTP + HOST + "/api/sns/getforums";

	/** 圈子获取一条版块 */
	public static final String CIRCLE_ONE = HTTP + HOST
			+ "/api/sns/getforumone?id=";

	/** 圈子某一个版块下的帖子 */
	public static final String CIRCLE_CLASS = HTTP + HOST
			+ "/api/sns/getpostpage?page=0&forumid=";

	/** 圈子分类/帖子详情评论 */
	public static final String CIRCLE_CLASS_DETAILS = HTTP + HOST
			+ "/api/sns/getpost_replys";

	/** 商城商品 */
	public static final String FRAGMENT_STORE = HTTP + HOST
			+ "/api/mall/goods_gets_web?page=1";

	/** 商品详情 */
	public static final String GODDS_DETAILS = HTTP + HOST
			+ "/api/mall/goods_get_web";

	/** 获取购物车总数 */
	public static final String SHOP_CART_GET_NUM = HTTP + HOST
			+ "/api/mall/shopcart_getnum";

	/** 登录 */
	public static final String LOGIN = HTTP + HOST + "/api/user/login";

	/** 置顶帖子 */
	public static final String SET_POST_TOP  = HTTP + HOST + "/api/sns/setposttop";
	/** 获取帖子详情 */
	public static final String GET_POST_TOP  = HTTP + HOST + "/api/sns/getpostone";
	/** 上传发帖图标*/
	public static final String SOND_POST_IMG = HTTP + HOST + "/api/upimg";
	/** 发帖*/
	public static final String SOND_POST = HTTP + HOST + "/api/sns/addpost";

}
