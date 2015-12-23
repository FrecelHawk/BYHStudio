package com.xyt.ygcf.entity;

/**
 * 商家菜谱Bean
 * 
 * @author lenovo hexiangyang
 * 
 */
public class MerchantMenuTypeBean {
	/** 类型id */
	public String typeId;
	/** 类型名称 */
	public String typeName;
	/** 类型数量 */
	public String typeCount;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(String typeCount) {
		this.typeCount = typeCount;
	}

	//0：餐厅  1：学校食堂 2：企业食堂 
	public int restaurant; 
	
	public MerchantMenuTypeBean() {
	}

	public MerchantMenuTypeBean(String typeName, int restaurant) {
		this.typeName = typeName;
		this.restaurant = restaurant;
	}

	public int getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(int restaurant) {
		this.restaurant = restaurant;
	}
	
}
