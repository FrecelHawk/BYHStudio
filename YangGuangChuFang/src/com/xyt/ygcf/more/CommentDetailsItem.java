package com.xyt.ygcf.more;

import java.io.Serializable;

public class CommentDetailsItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public String id;
	public String imageView;// 头像
	public String commentName; // 名称
	public String commentTime;// 时间
	public String commentGrade;// 评分
	public String commentContent;// 评价内容
	public int commentPraise;  // 0,赞；1，不赞


}
