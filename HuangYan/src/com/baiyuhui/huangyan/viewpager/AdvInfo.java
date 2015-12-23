package com.baiyuhui.huangyan.viewpager;

import java.io.Serializable;

public class AdvInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String adid;
	private String imagesrc;
	private String imageurl;
	public String getAdid() {
		return adid;
	}
	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getImagesrc() {
		return imagesrc;
	}
	public void setImagesrc(String imagesrc) {
		this.imagesrc = imagesrc;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public AdvInfo() {
		super();
	}


	

}
