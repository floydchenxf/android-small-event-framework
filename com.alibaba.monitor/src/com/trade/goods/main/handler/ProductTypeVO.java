package com.trade.goods.main.handler;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeVO {

	private Long id;

	/**
	 * 类型名称
	 */
	private String name;

	private Long companyId;

	/**
	 * top 产品
	 */
	private List<ProductVO> products = new ArrayList<ProductVO>();

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProductVO> getProducts() {
		return products;
	}

	public void addProduct(ProductVO productVO) {
		products.add(productVO);
	}

}
