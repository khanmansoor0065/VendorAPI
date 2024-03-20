package com.dreamsol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponseDto {
	private String name;
	private long mob;
	private String email;
	private String brief;
	private String profileImage;
	private VendorTypeDto vendorTypeDto;
	private Set<ProductResponseDto> productResponseDto;

}
