package com.xyt.ygcf.entity;

/**
 * 违规公示Bean
 * @author lenovo
 *
 */
public class VioaltionListBean {
	public String id;
	public String name;
	public String message;
	public String date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
