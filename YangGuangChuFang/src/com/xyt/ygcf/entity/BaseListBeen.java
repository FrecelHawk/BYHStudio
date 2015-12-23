package com.xyt.ygcf.entity;

import java.util.List;

/**
 * 
 * @author yuyangming
 *
 * @param <T>
 */
public class BaseListBeen<T> {
	
	/** 当前请求页 */
	public int currentPage = 1;
	/** 总共多少页 */
	public int totalPages = 1;

	/** 存列表item的list */
	public List<T> list;
}
