package com.xyt.ygcf.more;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodSourceBean implements Serializable {
	public String foodName;
	public String quality;
	public String foodFrom;
	public String foodTime;
	public String unitName;

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getFoodFrom() {
		return foodFrom;
	}

	public void setFoodFrom(String foodFrom) {
		this.foodFrom = foodFrom;
	}

	public String getFoodTime() {
		return foodTime;
	}

	public void setFoodTime(String foodTime) {
		this.foodTime = foodTime;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
