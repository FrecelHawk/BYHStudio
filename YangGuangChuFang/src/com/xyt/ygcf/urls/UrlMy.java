package com.xyt.ygcf.urls;

public class UrlMy {
	/** IP地址 */
	// 内网服务器
//	 public static String HOST = "172.16.100.182:18080";
	// 外网服务器
//	 public static String HOST = "192.168.1.233:28080";
	 
	// 外网服务器，生产环境
//	public static String HOST = "192.168.1.241:8080";
//	public static String HOST = "192.168.1.241:28080";
	public static String HOST = "www.xeyess.com";

	//刘剑 
//	public static String HOST = "192.168.1.92:8080"; 
	//许继俊
//	public static String HOST = "172.16.100.145:8080";
	// 钟伟雄
//	public static String HOST = "172.16.100.121:8080";
	//郑乾业
//	public static String HOST = "172.16.100.105:8080";

	private UrlMy() {
		// 不要实例化这个类...
	}

	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";

	private final static String URL_SPLITTER = "/";
	/** 整合HTTP头文件 */
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;

	/** 获取cookies */
	public static final String COOKIES = URL_API_HOST + "ocr/api/init";

	/** 登录 */
	public static final String LOGIN = URL_API_HOST + "ocr/api/authx/mb/login";

	/** 获取验证码 */
	public static final String VERTIFIY_CODE = URL_API_HOST + "ocr/api/authx/getVerifyCode?verifyCodeToken=";

	/** 登出 */
	public static final String LOGIN_OUT = URL_API_HOST + "ocr/api/authx/logout";

	/** 注册 */
	public static final String REGISTER = URL_API_HOST + "ocr/api/in/vip/insertMember";

	/** 修改信息 */
	public static final String MODIFY_MESSAGE = URL_API_HOST + "ocr/api/in/vip/updateMember";

	/** 修改密码 */
	public static final String MODIFY_PASSWORD = URL_API_HOST + "ocr/api/in/vip/updatePassword";

	/** 我收藏的商家 */
	public static final String COLLECTION_RESTAURANT = URL_API_HOST + "ocr/api/in/ent/selectCollectionMerchant";

	/** 我收藏菜式 */
	public static final String COLLECTION_MENU = URL_API_HOST + "ocr/api/in/pro/selectCollectionProduct";

	/** 广告 */
	public final static String ADV = URL_API_HOST + "ocr/api/in/opt/selectAdvertising";

	/** 商品详情 */
	public final static String COMMODITY_DETAILS = URL_API_HOST + "ocr/api/in/pro/selectProduct";

	/** 商家详情 */
	public final static String MERCHANT_DETAILS = URL_API_HOST + "ocr/api/in/ent/selectMerchant";
	
	/** 查看视频设备 */
	public final static String VIDEO_DETAILS = URL_API_HOST + "ocr/api/in/ent/selectVideoFixture";
	
	/** 查看视频设备供应商 */
	public final static String VIDEO_SUPPLIER = URL_API_HOST + "ocr/api/in/ent/getSupplier";

	/** 地区 */
	public final static String REGION = URL_API_HOST + "ocr/api/in/sys/selectRegion";

	/** 搜索商家 */
	public final static String SEARCH_SHOP = URL_API_HOST + "ocr/api/in/ent/selectMerchantList";

	/** 收藏商家 */
	public final static String COLLECTION_SHOP_ID = URL_API_HOST + "ocr/api/in/ent/insertCollectionMerchant";

	/** 取消收藏商家 */
	public final static String DELETE_COLLECTION_RESTAURANT = URL_API_HOST + "ocr/api/in/ent/deleteCollectionMerchant";

	/** 搜索商品 */
	public final static String PRODUCTLIST = URL_API_HOST + "ocr/api/in/ent/selectMerchantList";

	/** 投诉 */
	public static final String ADD_COMPLAINT = URL_API_HOST + "ocr/api/in/opt/insertComplain";

	/** 我的投诉 */
	public static final String MY_COMPLAINT = URL_API_HOST + "ocr/api/in/opt/selectMyComplain";

	/** 投诉详情 */
	public static final String COMPLAINT_DETAIL = URL_API_HOST + "ocr/api/in/opt/selectComplain";

	/** 取消投诉 */
	public static final String DELETE_COMPLAINT = URL_API_HOST + "ocr/api/in/opt/deleteComplain";

	/** 美食分类 */
	public static final String FOODTYPE = URL_API_HOST + "ocr/api/in/opt/selectFoodType";

	/** 商家商品分类 */
	public static final String MERPROTYPE = URL_API_HOST + "ocr/api/in/ent/selectMerProType";

	/** 短信验证码 */
	public static final String SMS_VERIFICATION = URL_API_HOST + "gsm/api/ocr/smsVerification";

	/** 取消收藏商品 */
	public static final String DELETE_COLLECTION_MENU = URL_API_HOST + "ocr/api/in/pro/deleteCollectionProduct";
	/** 食材来源 */
	public static final String FOOD_SOURCE = URL_API_HOST + "ocr/api/in/ent/selectIngredient";

	/** 每日菜谱 */
	public static final String DAILY_RECIPE = URL_API_HOST + "ocr/api/in/pro/selectDailyRecipe";

	/** 食堂反馈 */
	public static final String REFECTORY_FEEDBACK = URL_API_HOST + "ocr/api/in/ent/insertSuggestion";
	
	/** 上传头像 */
	public static final String UPLOAD_AVATR = URL_API_HOST + "ocr/api/in/vip/uploadMemberPic";

	/** 意见反馈 */
	public static final String FEED_BACK = URL_API_HOST + "ocr/api/in/opt/insertFeedback";

	/** 查询商家评论 */
	public static final String INQUIRE_MER_EVALUATION = URL_API_HOST + "ocr/api/in/ent/selectMerEvaluation";

	/** 我评论的餐厅 */
	public static final String MY_EVALUATE_RESTAURANT = URL_API_HOST + "ocr/api/in/ent/selectMyMerEvaluation";

	/** 我评论的菜式 */
	public static final String MY_EVALUATE_MENU = URL_API_HOST + "ocr/api/in/pro/selectMyProEvaluation";

	/** 我的反馈 */
	public static final String MY_EVALUATE_FEEDBACK = URL_API_HOST + "ocr/api/in/ent/selectMySuggestion";

	/** 查询商品评论 */
	public static final String INQULRE_PRO_EVALUATION = URL_API_HOST + "ocr/api/in/pro/selectProEvaluation";

	/** 政府公告列表 */
	public static final String GOVERNMENT_NOTICE_LISTS = URL_API_HOST + "ocr/api/in/ann/selectAnnouncementList";

	/** 查看政府公告 */
	public static final String INQUIRE_GOVERNMENT_NOTICE = URL_API_HOST + "ocr/api/in/ann/selectAnnouncement";

	/** 收藏商品 */
	public static final String COLLECTION_PRODUCT = URL_API_HOST + "ocr/api/in/pro/insertCollectionProduct";

	/** 反馈详情 */
	public static final String EVALUATE_DETAIL = URL_API_HOST + "ocr/api/in/ent/selectSuggestionBack";

	/** 检查版本更新 */
	public static final String VERSION_CHECK = URL_API_HOST + "ocr/api/in/sys/versionCheck";
	
	/**餐厅评优*/
	public static final String RESTAURANT_HIGHEST_QUALITY = URL_API_HOST+"ocr/api/in/ent/insertPraiseDetailList";
	/** 餐厅评优 */

	/** 删除评价管理的餐厅 */
	public static final String DELETE_EVALUATE_RESTAURANT = URL_API_HOST + "ocr/api/in/ent/deleteMyMerEvaluation";

	/** 删除评价管理菜式 */
	public static final String DELETE_EVALUATE_MENU = URL_API_HOST + "ocr/api/in/pro/deleteMyProEvaluation";

	/** 删除评价管理反馈 */
	public static final String DELETE_EVALUATE_FEEDBACK = URL_API_HOST + "ocr/api/in/ent/deleteMySuggestion";
	
   /**商家评论*/
	public static final String SHOP_COMMENT = URL_API_HOST +"ocr/api/in/ent/insertMerchantEvaluation";
	
	/**查询反馈列表*/
	public static final String MER_FEED_BACK = URL_API_HOST +"ocr/api/in/ent/selectMerchantSuggestion";
	
	/**商品评论*/
	public static final String PRO_EVALUATION = URL_API_HOST + "ocr/api/in/pro/insertProductEvaluation";
	
	/** 心跳包*/
	public static final String KEEP_ALIVE_URL = URL_API_HOST + "ocr/api/refresh";
	
	/** 获取投诉主题*/
	public static final String COMLAIN_SUJECT = URL_API_HOST + "ocr/api/sys/getDicByClsCode";
	
	/** 搜索商品*/
	public static final String PRODUCT_LIST = URL_API_HOST + "ocr/api/in/pro/selectProductList";
	
	/** 查看字典数据*/
	public static final String DICTIONARY_DATA = URL_API_HOST + "ocr/api/in/sys/getDicByClsCode";
	
	/** 找回密码*/
	public static final String GETBACK_PASSWORD = URL_API_HOST + "ocr/api/in/vip/updateMemberPassword";
	
	/** 查看字典数据*/
	public static final String RECIPE_TYPE = URL_API_HOST + "ocr/api/in/pro/selectDailyRecipeType";
	
	/** 发送会员邮箱*/
	public static final String SEND_EMIAL = URL_API_HOST + "ocr/api/in/vip/sendMemberMail";
}
