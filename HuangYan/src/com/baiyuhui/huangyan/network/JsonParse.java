package com.baiyuhui.huangyan.network;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baiyuhui.huangyan.entity.BaseJsonBeen;
import com.baiyuhui.huangyan.entity.LoginBean;
import com.google.gson.JsonObject;

public class JsonParse {

	/**
	 * 解析json数据
	 * 
	 * @param json
	 * @throws JSONException
	 */
	/* 新闻解析 */
	public static List<BaseJsonBeen> parseNews(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String tit = jsonObject.optString("tit");
				String subtit = jsonObject.optString("subtit");
				String cdate = jsonObject.optString("cdate");
				String udate = jsonObject.optString("udate");
				String ico = jsonObject.optString("ico");
				int viewnum = jsonObject.optInt("viewnum");
				int idx = jsonObject.optInt("idx");
				String url = jsonObject.optString("url");
				int count = jsonObject.optInt("count");

				BaseJsonBeen jsonBeen = new BaseJsonBeen();
				jsonBeen.setId(id);
				jsonBeen.setTit(tit);
				jsonBeen.setSubtit(subtit);
				jsonBeen.setCdate(cdate);
				jsonBeen.setUdate(udate);
				jsonBeen.setIco(ico);
				jsonBeen.setViewnum(viewnum);
				jsonBeen.setIdx(idx);
				jsonBeen.setUrl(url);
				jsonBeen.setCount(count);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}

	/* 根据ID单独获取一条新闻解析 ---- 视频详情 */
	public static BaseJsonBeen parseAlone(String json) {

		BaseJsonBeen jsonBeen = new BaseJsonBeen();

		try {
			JSONObject jsonObject = new JSONObject(json);
			int id = jsonObject.optInt("id");
			String tit = jsonObject.optString("tit");
			String subtit = jsonObject.optString("subtit");
			String info = jsonObject.optString("info");
			int viewnum = jsonObject.optInt("viewnum");
			String ico = jsonObject.optString("ico");
			String cdate = jsonObject.optString("cdate");
			String content = jsonObject.optString("content");
			int idx = jsonObject.optInt("idx");
			int typelevelone = jsonObject.optInt("typelevelone");
			int typeleveltwo = jsonObject.optInt("typeleveltwo");
			String url = jsonObject.optString("url");
			int count = jsonObject.optInt("count");

			String strv0 = jsonObject.optString("strv0");

			jsonBeen.setId(id);
			jsonBeen.setTit(tit);
			jsonBeen.setSubtit(subtit);
			jsonBeen.setInfo(info);
			jsonBeen.setViewnum(viewnum);
			jsonBeen.setIco(ico);
			jsonBeen.setCdate(cdate);
			jsonBeen.setContent(content);
			jsonBeen.setIdx(idx);
			jsonBeen.setTypelevelone(typelevelone);
			jsonBeen.setTypeleveltwo(typeleveltwo);
			jsonBeen.setUrl(url);
			jsonBeen.setCount(count);

			jsonBeen.setStrv0(strv0);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonBeen;

	}

	/* 商城商品 */
	public static List<BaseJsonBeen> parseStore(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String img = jsonObject.optString("img");
				int count = jsonObject.optInt("count");
				String name = jsonObject.optString("name");
				String code = jsonObject.optString("code");
				String price = jsonObject.optString("price");
				String mallprice = jsonObject.optString("mallprice");
				String cheapprice = jsonObject.optString("cheapprice");

				BaseJsonBeen jsonBeen = new BaseJsonBeen();
				jsonBeen.setId(id);
				jsonBeen.setImg(img);
				jsonBeen.setCount(count);
				jsonBeen.setName(name);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}

	/* 圈子解析 */
	public static List<BaseJsonBeen> parseCircle(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String name = jsonObject.optString("name");
				String cdate = jsonObject.optString("cdate");
				String postnum = jsonObject.optString("postnum");
				String cuid = jsonObject.optString("cuid");
				String adminuids = jsonObject.optString("adminuids");
				int idx = jsonObject.optInt("idx");
				String ico = jsonObject.optString("ico");
				String info = jsonObject.optString("info");
				JSONArray array2 = jsonObject.getJSONArray("adminusers");
				for (int j = 0; j < array.length(); j++) {
					JSONObject object2 = array2.getJSONObject(i);
				}
				BaseJsonBeen jsonBeen = new BaseJsonBeen();
				jsonBeen.setId(id);
				jsonBeen.setName(name);
				jsonBeen.setCdate(cdate);
				jsonBeen.setIco(ico);
				jsonBeen.setIdx(idx);
				jsonBeen.setPostnum(postnum);
				jsonBeen.setCuid(cuid);
				jsonBeen.setAdminuids(adminuids);
				jsonBeen.setInfo(info);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}

	/*圈子版主详情---通过id得到特定版主的详情列表*/
	public static BaseJsonBeen parseCircleClassifySomePerson(String json) {

		BaseJsonBeen jsonBeen = new BaseJsonBeen();
		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			int id = jsonObject.optInt("id");
			String name = jsonObject.optString("name");
			String ico = jsonObject.optString("ico");
			String info = jsonObject.optString("info");
			String cdate = jsonObject.optString("cdate");				
			String cuid = jsonObject.optString("cuid");
			String postnum = jsonObject.optString("postnum");				
			int idx = jsonObject.optInt("idx");
			int adminuids = jsonObject.optInt("adminuids");
			
			JSONArray array = jsonObject.getJSONArray("adminusers");
			
			jsonBeen.setId(id);
			jsonBeen.setName(name);
			jsonBeen.setIco(ico);
			jsonBeen.setInfo(info);
			jsonBeen.setCdate(cdate);
			jsonBeen.setCuid(cuid);
			jsonBeen.setPostnum(postnum);
			jsonBeen.setIdx(idx);
			for (int i = 0; i < array.length(); i++) {
				

				BaseJsonBeen jsonBeenChild = new BaseJsonBeen();
				
				JSONObject Object = array.getJSONObject(i);
				String uid = Object.optString("uid");
				String uname = Object.optString("uname");
				String img = Object.optString("img");
				int sex = Object.optInt("sex");
				
				jsonBeenChild.setUid(uid);
				jsonBeenChild.setUname(uname);
				jsonBeenChild.setImg(img);
				jsonBeenChild.setSex(sex);
				

				listDatas.add(jsonBeenChild);
			}
			jsonBeen.setList(listDatas);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonBeen;

	}
	/* 圈子分类解析 ---分页获取某一个版块下的帖字列表 */
	public static List<BaseJsonBeen> parseCircleClassify(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String tit = jsonObject.optString("tit");
				String content = jsonObject.optString("content");
				String cdate = jsonObject.optString("cdate");
				String udate = jsonObject.optString("udate");
				String uid = jsonObject.optString("uid");
				int viewnum = jsonObject.optInt("viewnum");
				int replynum = jsonObject.optInt("replynum");
				int topnum = jsonObject.optInt("topnum");
				String info = jsonObject.optString("info");
				int count = jsonObject.optInt("count");
				BaseJsonBeen jsonBeen = new BaseJsonBeen();
				jsonBeen.setId(id);
				jsonBeen.setTit(tit);
				jsonBeen.setContent(content);
				jsonBeen.setCdate(cdate);
				jsonBeen.setUdate(udate);
				jsonBeen.setViewnum(viewnum);
				jsonBeen.setReplynum(replynum);
				jsonBeen.setTopnum(topnum);
				jsonBeen.setUid(uid);
				jsonBeen.setInfo(info);
				jsonBeen.setCount(count);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}

	/* 根据ID单独获取帖子详情 ---- 帖子详情 */
	public static List<BaseJsonBeen> parseCircleDetails(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String uid = jsonObject.optString("uid");
				String cdate = jsonObject.optString("cdate");
				String content = jsonObject.optString("content");
				int postid = jsonObject.optInt("postid");
				int pid = jsonObject.optInt("pid");
				String uname = jsonObject.optString("uname");
				String img = jsonObject.optString("img");
				int sex = jsonObject.optInt("sex");
				boolean hasmore_10_replys = jsonObject
						.optBoolean("hasmore_10_replys");

				BaseJsonBeen jsonBeen = new BaseJsonBeen();

				jsonBeen.setId(id);
				jsonBeen.setUid(uid);
				jsonBeen.setCdate(cdate);
				jsonBeen.setPostid(postid);
				jsonBeen.setContent(content);
				jsonBeen.setPid(pid);
				jsonBeen.setUname(uname);
				jsonBeen.setImg(img);
				jsonBeen.setSex(sex);
				jsonBeen.setHasmore_10_replys(hasmore_10_replys);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}

	/* 个人中心----物流通知 */
	public static List<BaseJsonBeen> parseInfromIogistics(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String uid = jsonObject.optString("uid");
				String cdate = jsonObject.optString("cdate");
				String content = jsonObject.optString("content");
				int postid = jsonObject.optInt("postid");
				int pid = jsonObject.optInt("pid");
				String uname = jsonObject.optString("uname");
				String img = jsonObject.optString("img");
				int sex = jsonObject.optInt("sex");
				boolean hasmore_10_replys = jsonObject
						.optBoolean("hasmore_10_replys");

				BaseJsonBeen jsonBeen = new BaseJsonBeen();

				jsonBeen.setId(id);
				jsonBeen.setUid(uid);
				jsonBeen.setCdate(cdate);
				jsonBeen.setPostid(postid);
				jsonBeen.setContent(content);
				jsonBeen.setPid(pid);
				jsonBeen.setUname(uname);
				jsonBeen.setImg(img);
				jsonBeen.setSex(sex);
				jsonBeen.setHasmore_10_replys(hasmore_10_replys);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}

	/* 个人中心----回复通知 */
	public static List<BaseJsonBeen> parseInfromRevert(String json) {

		List<BaseJsonBeen> listDatas = new ArrayList<BaseJsonBeen>();

		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				int id = jsonObject.optInt("id");
				String uid = jsonObject.optString("uid");
				String cdate = jsonObject.optString("cdate");
				String content = jsonObject.optString("content");
				int postid = jsonObject.optInt("postid");
				int pid = jsonObject.optInt("pid");
				String uname = jsonObject.optString("uname");
				String img = jsonObject.optString("img");
				int sex = jsonObject.optInt("sex");
				boolean hasmore_10_replys = jsonObject
						.optBoolean("hasmore_10_replys");

				BaseJsonBeen jsonBeen = new BaseJsonBeen();

				jsonBeen.setId(id);
				jsonBeen.setUid(uid);
				jsonBeen.setCdate(cdate);
				jsonBeen.setPostid(postid);
				jsonBeen.setContent(content);
				jsonBeen.setPid(pid);
				jsonBeen.setUname(uname);
				jsonBeen.setImg(img);
				jsonBeen.setSex(sex);
				jsonBeen.setHasmore_10_replys(hasmore_10_replys);

				listDatas.add(jsonBeen);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDatas;

	}
	/**
	 * 得到某一条帖子详情
	 * 
	 * @param json
	 * @return
	 */
	public static BaseJsonBeen parseSomeCircleDetail(String json) {
		BaseJsonBeen jsonBeen = new BaseJsonBeen();

		try {
			JSONObject jsonObject = new JSONObject(json);
			int id = jsonObject.optInt("id");
			String tit = jsonObject.optString("tit");
			String content = jsonObject.optString("content");
			String cdate = jsonObject.optString("cdate");
			String udate = jsonObject.optString("udate");
			String uid = jsonObject.optString("uid");
			int forumid = jsonObject.optInt("forumid");
			String uname = jsonObject.optString("uname");
			String img = jsonObject.optString("img");
			int sex = jsonObject.optInt("sex");
			
			jsonBeen.setId(id);
			jsonBeen.setTit(tit);
			jsonBeen.setContent(content);
			jsonBeen.setCdate(cdate);
			jsonBeen.setUdate(udate);
			jsonBeen.setUid(uid);
			
			jsonBeen.setUname(uname);
			jsonBeen.setImg(img);
			jsonBeen.setSex(sex);
			
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonBeen;

	}
	/**
	 * 解析商品详情
	 * 
	 * @param json
	 * @return
	 */
	public static BaseJsonBeen parseGoodsDetail(String json) {
		BaseJsonBeen jsonBeen = new BaseJsonBeen();

		try {
			JSONObject jsonObject = new JSONObject(json);
			int id = jsonObject.optInt("id");
			String name = jsonObject.optString("name");
			String img = jsonObject.optString("img");
			String code = jsonObject.optString("code");
			int attribute = jsonObject.optInt("attribute");

			jsonBeen.setId(id);
			jsonBeen.setName(name);
			jsonBeen.setImg(img);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonBeen;

	}
	
	/**
	 * 解析商品详情
	 * 
	 * @param json
	 * @return
	 */
	public static LoginBean parseLogin(String json) {
		LoginBean loginBean = new LoginBean();
		try {
			JSONObject jsonObject = new JSONObject(json);
			int err = jsonObject.optInt("err");
			String id = jsonObject.optString("uid");
			String name = jsonObject.optString("uname");
			String img = jsonObject.optString("img");
			String gender = jsonObject.optString("gender");

			loginBean.setErr(err);
			loginBean.setUid(id);
			loginBean.setUname(name);
			loginBean.setImg(img);
			loginBean.setGender(gender);
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return loginBean;

	}

	/**
	 * 解析帖子详情评论列表
	 * @param json
	 * @return
	 */
	public static List<BaseJsonBeen> parseCommentList(String json) {
		List<BaseJsonBeen> lists = new ArrayList<BaseJsonBeen>();
		BaseJsonBeen been = new BaseJsonBeen();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				
				
				JSONArray jsonArray = object2.getJSONArray("replys");
				List<BaseJsonBeen> list = new ArrayList<BaseJsonBeen>();
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject object3 = array.getJSONObject(i);
										
					BaseJsonBeen been1 = new BaseJsonBeen();		
					list.add(been1);
				}
				been.setList(list);
				lists.add(been);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lists;
	}
}

