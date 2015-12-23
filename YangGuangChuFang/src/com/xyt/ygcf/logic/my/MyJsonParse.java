package com.xyt.ygcf.logic.my;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.xyt.ygcf.Constants;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.my.AddressBeen;
import com.xyt.ygcf.entity.my.CollectionCanteenBeen;
import com.xyt.ygcf.entity.my.CollectionMenuBeen;
import com.xyt.ygcf.entity.my.CollectionRestaurantBeen;
import com.xyt.ygcf.entity.my.ComplainBeen;
import com.xyt.ygcf.entity.my.ComplainSubjectBean;
import com.xyt.ygcf.entity.my.EvaluateBeen;
import com.xyt.ygcf.entity.my.EvaluateFeedBackBeen;
import com.xyt.ygcf.entity.my.RSABeen;
import com.xyt.ygcf.entity.my.User;

public class MyJsonParse {

	public static RSABeen parseRSA(String json) throws JSONException {
		JSONObject object = new JSONObject(json);
		RSABeen been = null;
		if (object != null) {
			been = new RSABeen();
			been.exponent = object.optString("exponent");
			been.modulus = object.optString("modulus");
		}
		return been;
	}

	public static User parseLogin(String json) throws JSONException {
		JSONObject object = new JSONObject(json);
		User user = new User();
		user.token = object.optString("token");
		user.sessionId = object.optString("sessionId");
		JSONObject member = object.optJSONObject("member");
		if (member != null) {
			user.districtCode = member.optString("districtCode");
			user.nickname = member.optString("nickname");
			user.sex = member.optInt("sex", 0);
			user.birthDate = member.optString("birthDate");
			user.telephone = member.optString("telephone");
			user.gradeName = member.optString("gradeName");
			user.address = member.optString("address");
			user.name = member.optString("name");
			user.status = member.optString("status");
			user.memberNo = member.optString("memberNo");
			user.zip = member.optString("zip");
			user.mobile = member.optString("mobile");
			user.email = member.optString("email");
			user.avatr = member.optString("photoUrl");
		} else {
			return null;
		}
		return user;

	}

	/***
	 * 解析我收藏的商家
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static BaseListBeen<CollectionRestaurantBeen> parseCollectionRestaurantList(String json)
			throws JSONException {
		BaseListBeen<CollectionRestaurantBeen> baseListBeen = new BaseListBeen<CollectionRestaurantBeen>();
		List<CollectionRestaurantBeen> list = new ArrayList<CollectionRestaurantBeen>();
		JSONObject obj = new JSONObject(json);
		baseListBeen.currentPage = obj.optInt("currentPage");
		baseListBeen.totalPages = obj.optInt("totalPages");
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			for (int i = 0; i < lenght; ++i) {
				JSONObject object = array.getJSONObject(i);
				CollectionRestaurantBeen been = new CollectionRestaurantBeen();
				been.id = object.optString("id");
				been.photoUrl = object.optString("logo");
				been.restaurant = object.optString("shopName");
				been.score = object.optInt("grade", 0);
				been.type = object.optString("foodType");
				been.address = object.optString("address");
				been.distance = object.optString("range");
				list.add(been);
			}
		}
		baseListBeen.list = list;
		return baseListBeen;

	}

	/***
	 * 解析我收藏的商品
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static BaseListBeen<CollectionCanteenBeen> parseCollectionCanteenList(String json) throws JSONException {
		BaseListBeen<CollectionCanteenBeen> baseListBeen = new BaseListBeen<CollectionCanteenBeen>();
		List<CollectionCanteenBeen> list = new ArrayList<CollectionCanteenBeen>();
		JSONObject obj = new JSONObject(json);
		baseListBeen.currentPage = obj.optInt("currentPage");
		baseListBeen.totalPages = obj.optInt("totalPages");
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			for (int i = 0; i < lenght; ++i) {
				JSONObject object = array.getJSONObject(i);
				CollectionCanteenBeen been = new CollectionCanteenBeen();
				been.id = object.optString("id");
				been.name = object.optString("shopName");
				been.type = object.optString("industry");
				been.address = object.optString("address");
				been.distance = object.optString("range");
				been.photoUrl = object.optString("logo");
				list.add(been);
			}
		}
		baseListBeen.list = list;
		return baseListBeen;
	}

	/***
	 * 解析我收藏的菜式
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static BaseListBeen<CollectionMenuBeen> parseCollectionMenuList(String json) throws JSONException {
		BaseListBeen<CollectionMenuBeen> baseListBeen = new BaseListBeen<CollectionMenuBeen>();
		List<CollectionMenuBeen> list = new ArrayList<CollectionMenuBeen>();
		JSONObject obj = new JSONObject(json);
		baseListBeen.currentPage = obj.optInt("currentPage");
		baseListBeen.totalPages = obj.optInt("totalPages");
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			for (int i = 0; i < lenght; ++i) {
				JSONObject object = array.getJSONObject(i);
				CollectionMenuBeen been = new CollectionMenuBeen();
				been.id = object.optString("id");
				been.name = object.optString("name");
				final String recommendLevel = object.optString("recommendLevel");
				been.recommendLevel = TextUtils.isEmpty(recommendLevel) ? "0.0" : recommendLevel;
				been.price = "￥" + object.optString("actualPrice");
				been.industry = object.optString("industry",Constants.INDUSTRY_RESTAURANT_MAKER);
				been.shopName = object.optString("shopName");
				been.photoUrl = object.optString("photoUrl");
				list.add(been);
			}
		}
		baseListBeen.list = list;
		return baseListBeen;

	}

	/**
	 * 解析地址（已废弃）
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<AddressBeen> parseAddressList(String json) throws JSONException {
		List<AddressBeen> list = new ArrayList<AddressBeen>();
		JSONArray array = new JSONArray(json);
		if (array != null) {
			final int height = array.length();
			for (int i = 0; i < height; ++i) {
				JSONObject object = array.getJSONObject(i);
				AddressBeen been = new AddressBeen();
				been.code = object.optString("id");
				been.name = object.optString("name");
				list.add(been);
			}
		}
		Collections.sort(list, new Comparent());
		return list;
	}

	/***
	 * 解析我的投诉
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static BaseListBeen<ComplainBeen> parseComplainList(String json) throws JSONException {
		BaseListBeen<ComplainBeen> baseListBeen = new BaseListBeen<ComplainBeen>();
		List<ComplainBeen> list = new ArrayList<ComplainBeen>();
		JSONObject obj = new JSONObject(json);
		baseListBeen.currentPage = obj.optInt("currentPage");
		baseListBeen.totalPages = obj.optInt("totalPages");
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			for (int i = 0; i < lenght; ++i) {
				JSONObject object = array.getJSONObject(i);
				ComplainBeen been = new ComplainBeen();
				been.id = object.optString("id");
				been.shop = object.optString("shopName");
				been.state = object.optString("status");
				been.content = object.optString("content");
				been.order = object.optString("no");
				list.add(been);
			}
		}
		baseListBeen.list = list;
		return baseListBeen;

	}

	/***
	 * 解析我的评价管理
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static BaseListBeen<EvaluateBeen> parseEvaluateList(String json, final int type) throws JSONException {
		BaseListBeen<EvaluateBeen> baseListBeen = new BaseListBeen<EvaluateBeen>();
		List<EvaluateBeen> list = new ArrayList<EvaluateBeen>();
		JSONObject obj = new JSONObject(json);
		baseListBeen.currentPage = obj.optInt("currentPage");
		baseListBeen.totalPages = obj.optInt("totalPages");
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			if (type == 0) {
				for (int i = 0; i < lenght; ++i) {
					JSONObject object = array.getJSONObject(i);
					EvaluateBeen been = new EvaluateBeen();
					been.id = object.optString("id");
					been.name = object.optString("shopName");
					final String recommendLevel = object.optString("recommendLevel");
					been.recommendLevel = TextUtils.isEmpty(recommendLevel) ? "0.0" : recommendLevel;
					been.commentContent = object.optString("content");
					been.commentTime = object.optString("CreateDt");
					been.photoUrl = object.optString("logo");
					// been.commentContent = object.optString("content");
					// been.order = object.optString("no");
					list.add(been);
				}
			} else {
				for (int i = 0; i < lenght; ++i) {
					JSONObject object = array.getJSONObject(i);
					EvaluateBeen been = new EvaluateBeen();
					been.id = object.optString("id");
					been.name = object.optString("name");
					final String score = object.optString("recommendLevel");
					been.recommendLevel = TextUtils.isEmpty(score) ? "0.0" : score;
					been.commentContent = object.optString("content");
					been.commentTime = object.optString("CreateDt");
					been.photoUrl = object.optString("photoUrl");
					// been.commentContent = object.optString("content");
					// been.order = object.optString("no");
					list.add(been);
				}
			}
		}
		baseListBeen.list = list;
		return baseListBeen;

	}

	/***
	 * 解析我的评价反馈
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static BaseListBeen<EvaluateFeedBackBeen> parseEvaluateFeedbackList(String json) throws JSONException {
		BaseListBeen<EvaluateFeedBackBeen> baseListBeen = new BaseListBeen<EvaluateFeedBackBeen>();
		List<EvaluateFeedBackBeen> list = new ArrayList<EvaluateFeedBackBeen>();
		JSONObject obj = new JSONObject(json);
		baseListBeen.currentPage = obj.optInt("currentPage");
		baseListBeen.totalPages = obj.optInt("totalPages");
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			for (int i = 0; i < lenght; ++i) {
				JSONObject object = array.getJSONObject(i);
				EvaluateFeedBackBeen been = new EvaluateFeedBackBeen();
				been.id = object.optString("id");
				been.name = object.optString("merchantName");
				been.content = object.optString("content");
				been.type = object.optString("industryName");
				been.photoUrl = object.optString("domainLogo");
				// been.commentContent = object.optString("content");
				// been.order = object.optString("no");
				list.add(been);
			}
		}
		baseListBeen.list = list;
		return baseListBeen;

	}

	/***
	 * 解析投诉主题
	 * 
	 * @param json
	 *            json字符串
	 * @return 返回BaseListBeen
	 * @throws JSONException
	 */
	public static void parseComlainSubjectList(List<ComplainSubjectBean> list, String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.optJSONArray("beanList");
		if (array != null) {
			final int lenght = array.length();
			for (int i = 0; i < lenght; ++i) {
				JSONObject object = array.getJSONObject(i);
				ComplainSubjectBean been = new ComplainSubjectBean();
				been.name = object.optString("name");
				been.code = object.optString("code");
				list.add(been);
			}
		}
	}
}
