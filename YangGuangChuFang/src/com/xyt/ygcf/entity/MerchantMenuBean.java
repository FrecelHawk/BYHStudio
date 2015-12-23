package com.xyt.ygcf.entity;

import android.graphics.Bitmap;

/**
 * 商家菜单Bean
 * 
 * @author lenovo hexiangyang
 * 
 */
public class MerchantMenuBean {
	/** 菜品id */
	public String id;
	/** 菜品icon */
	public String icon;
	/** 菜品名称 */
	public String name;
	/** 菜品评分 */
	public String grade;
	/** 菜品当前价格 */
	public String Price;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

}
