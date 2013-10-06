package com.trade.goods.main.handler;

public class ProductVO {

	/**
	 * 产品id
	 */
	private Long id;

	/**
	 * 产品名称
	 */
	private String productName;

	/**
	 * 产品图片
	 */
	private String productPic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductPic() {
		return productPic;
	}

	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}

}
