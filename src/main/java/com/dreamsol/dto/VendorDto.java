package com.dreamsol.dto;

import java.util.Set;

import jakarta.validation.constraints.*;

public class VendorDto {
	@NotEmpty
	@Size(min = 4, message = "Name must be min of 3 characters!!")
	private String name;
	@Min(value=6000000000L,message="Mobile number must start with [6-9] and have 10 digits")
    @Max(value = 9999999999L,message = "Mobile number must start with [6-9]")
	private long mob;
	@Email(message = "Email address is not Valid!!")
	private String email;
	@NotEmpty
	@Size(max = 200, message = "Description has maximum 200 characters!!")
	private String brief;
	private String profileImage;
	private VendorTypeDto vendorTypeDto;
	private Set<ProductDto> productDto;

	public VendorDto() {
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMob() {
		return mob;
	}

	public void setMob(long mob) {
		this.mob = mob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public VendorTypeDto getVendorTypeDto() {
		return vendorTypeDto;
	}

	public void setVendorTypeDto(VendorTypeDto vendorTypeDto) {
		this.vendorTypeDto = vendorTypeDto;
	}

	public Set<ProductDto> getProductDto() {
		return productDto;
	}

	public void setProductDto(Set<ProductDto> productDto) {
		this.productDto = productDto;
	}

}
