package com.baiyuhui.huangyan.entity;

import java.util.List;

/**
 * 商品实体类
 * @author Administrator
 *
 */

public class Bean {
    public List<Goods> goods;
    public List<Info> infos;

    public static class Info {

	public String mymoney;
	public String myintegral;
	public String myicon;

    }

    public static class Goods {

	/**
	 * 商品唯一id
	 */
	public String goodstid;

	/**
	 * 图片路径
	 */
	public String goodsurl1;
	public String goodsurl2;
	public String goodsurl3;

	/**
	 * 商品图片
	 */

	public String goodsimg1;
	public String goodsimg2;
	public String goodsimg3;
	public String goodsimg4;
	public String goodsimg5;

	/**
	 * 商品图片logo
	 */
	public String goodslogo;
	/**
	 * 商品名字
	 */
	public String goodsname;
	/**
	 * 商品价格
	 */
	public double goodsmoney;
	/**
	 * 商品编码
	 * 
	 */
	public String goodscodding;

	/**
	 * 商品单位
	 */
	public String goodsunit;
	/**
	 * 商品描述
	 */
	public String goodsdetail;

    }

}
