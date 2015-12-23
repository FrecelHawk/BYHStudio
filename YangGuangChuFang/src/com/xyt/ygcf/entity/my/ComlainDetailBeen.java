package com.xyt.ygcf.entity.my;

public class ComlainDetailBeen {

	public String id;
	public String time;
	public String replyId;
	public String state;
	public String content;

	public ComlainDetailBeen() {

	}

	public ComlainDetailBeen(String id, String time, String replyId, String content, String state) {
		this.id = id;
		this.time = time;
		this.replyId = replyId;
		this.content = content;
		this.state = state;
	}
}
