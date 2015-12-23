package com.xyt.ygcf.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.http.RequestParams;
import com.xyt.ygcf.Variable;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.CityObjBean;
import com.xyt.ygcf.entity.FoodTypeBean;
import com.xyt.ygcf.entity.NearbyListItemBean;

public class SearchShopTool {
	/**
	 * 查询指定城市的区|县的数据
	 * @param regionId  城市编号
	 */
	public static RequestParams regionParams(String regionId){
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("regionId",regionId);
			params.addQueryStringParameter("type","3"); 
			params.addQueryStringParameter("level","3");
			return params;
			
	}
	
	
	/**
	 * 根据关键字搜索商家
	 * @param word  关键字 
	 * @param currentPage  当前页数
	 * @param handler  Handler对象
	 */
	public static RequestParams searchShopByWord(Context context,String word,String regionId,String industry){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("word",word);
		params.addQueryStringParameter("regionId",regionId);
		params.addQueryStringParameter("industry", industry);
		publicRequestParams(context,params);
		return params;
	}
	
	/**
	 * 根据 区(县)ID,美食ID，排序搜索商家
	 * @param regionId   区(县)ID
	 * @param foodTypeId  美食ID
	 * @param orderBy  排序
	 */
	public static RequestParams searchShopByCondition(Context context,String regionId,String foodTypeId,String orderBy,String industry){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("regionId",regionId);
		params.addQueryStringParameter("foodTypeId",foodTypeId);
		params.addQueryStringParameter("orderBy",orderBy);
		params.addQueryStringParameter("industry", industry);
		publicRequestParams(context,params);
		return params;
	}
	
	/**
	 * 根据城市ID来搜索商家（主要指某个市）
	 * @param regionId
	 * @return
	 */
	public static RequestParams searchShopByArea(Context context,String regionId,String industry,String orderBy){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("regionId",regionId);
		params.addQueryStringParameter("industry", industry);
		params.addQueryStringParameter("orderBy", orderBy);
		publicRequestParams(context,params);
		return params;
		
	}
	
	/**
	 * 搜索商家的公共参数
	 * @param params
	 * @param currentPage
	 */
	private static void publicRequestParams(Context context,RequestParams params){
		SpHelper spHelper =SpHelper.init(context);
		params.addQueryStringParameter("longitude", String.valueOf(spHelper.getCityLongitude()));
		params.addQueryStringParameter("latitude", String.valueOf(spHelper.getCityLatitude()));
		params.addQueryStringParameter("isPaging", "true");
		params.addQueryStringParameter("merPitureSize", Variable.itemBitmapSize);
	}
	
	
	
	/**
	 * 解析地区筛选
	 * @param json
	 * @throws JSONException 
	 */
	public static void parseRegion(String json,Handler handler,String cityID,int what) throws JSONException{
		JSONArray array = new JSONArray(json);
		if(array != null){
			List<CityObjBean>cityObjBeans = new ArrayList<CityObjBean>();
			CityObjBean objBean = new CityObjBean();
			objBean.name ="全城";
			objBean.id= cityID;
			cityObjBeans.add(objBean);
			int length =array.length();
			for (int i = 0; i <length ; i++) {
				JSONObject object = array.getJSONObject(i);
				objBean = new CityObjBean();
				objBean.id= object.optString("id");;
				objBean.name = object.optString("name");
				objBean.parentId = object.optString("parentId");
				cityObjBeans.add(objBean);
			}
			
			sendMessage(handler, what, cityObjBeans);
		}
	}
	/**
	 * 解析美食分类
	 * @param json
	 * @throws JSONException 
	 */
	public static void parserFoodType(String json,Handler handler,int what) throws JSONException {
		JSONArray array = new JSONArray(json);
		List<FoodTypeBean>foodTypeBeans = new ArrayList<FoodTypeBean>();
		FoodTypeBean typeBean = new FoodTypeBean();
		typeBean.setFoodId("");
		typeBean.setFoodName("全部");
		foodTypeBeans.add(typeBean);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				typeBean = new FoodTypeBean();
				typeBean.foodId = object.optString("id");
				typeBean.foodName = object.optString("name");
				foodTypeBeans.add(typeBean);
			}
			
			sendMessage(handler, what, foodTypeBeans);
	}
	
	/**
	 * 解析搜索商家的json数据
	 * @param json
	 * @throws JSONException
	 */
	public static void parserJson(String json,Handler handler,int what) throws JSONException{
//		Log.e("sch", json.toString());
		BaseListBeen<NearbyListItemBean> baseListBeens = new BaseListBeen<NearbyListItemBean>();
		JSONObject jsonObject = new JSONObject(json);
		baseListBeens.currentPage = jsonObject.optInt("currentPage");
		baseListBeens.totalPages = jsonObject.optInt("totalPages");
		JSONArray jsonArray = jsonObject.getJSONArray("beanList");
		int lenght = jsonArray.length();
		List<NearbyListItemBean> listDatas = new ArrayList<NearbyListItemBean>();
		
		for(int i =0; i<lenght;i++){
			NearbyListItemBean itemBean = new NearbyListItemBean();
			JSONObject item = (JSONObject) jsonArray.get(i);
			itemBean.logoUrl=item.getString("logo");
			itemBean.stopId=item.getString("id");
			itemBean.stopName=item.getString("shopName");
			itemBean.rate =item.getString("grade");
			
			itemBean.praiseScore =item.getInt("praiseScore");
			itemBean.longitude = item.getDouble("longitude");
			itemBean.latitude= item.getDouble("latitude");
			itemBean.theme = item.getString("foodType");
			itemBean.address = item.getString("address");
			itemBean.distance = item.getString("range");
			listDatas.add(itemBean);
		}
		
		baseListBeens.list = listDatas;
		sendMessage(handler,what, baseListBeens);
		baseListBeens = null;

		
	}
	
	private static void sendMessage(Handler handler,int what,Object obj){
		Message msg = Message.obtain();
		msg.what = what ;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	
	
}


