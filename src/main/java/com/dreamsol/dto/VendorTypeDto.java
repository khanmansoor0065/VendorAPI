package com.dreamsol.dto;

import jakarta.validation.constraints.NotBlank;

public class VendorTypeDto {
	@NotBlank(message = "Vendor Type must not be Empty")
	private String typeName;

	public VendorTypeDto() {
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
