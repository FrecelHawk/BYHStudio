package com.xyt.ygcf.entity;

/**
 * 首页推荐Bean
 * 
 * @author lenovo hexiangyang
 * 
 */
public class HomeRecommendBean {
	// id
	public String id;
	// 编号
	public String no;
	// 商品id
	public String productId;
	// 商品名称
	public String productName;
	// 商品缩略图
	public String productPhotourl;
	// 序号
	public String sequence;
	// 商家id
	public String merchantId;
	// 商家icon
	public String merchantLogo;
	// 商家名称
	public String merchantName;
	// 收藏数
	public String collectNumber;
	// 评论数
	public String commentNumber;

	public String getCollectNumber() {
		return collectNumber;
	}

	public void setCollectNumber(String collectNumber) {
		this.collectNumber = collectNumber;
	}

	public String getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(String commentNumber) {
		this.commentNumber = commentNumber;
	}

	public String getProductPhotourl() {
		return productPhotourl;
	}

	public void setProductPhotourl(String productPhotourl) {
		this.productPhotourl = productPhotourl;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantLogo() {
		return merchantLogo;
	}

	public void setMerchantLogo(String merchantLogo) {
		this.merchantLogo = merchantLogo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
