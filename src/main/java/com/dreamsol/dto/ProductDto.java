package com.dreamsol.dto;

public class ProductDto {
	private String productName;

	public ProductDto() {
	}

	public ProductDto(String productName) {
		super();
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
