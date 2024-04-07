package com.dreamsol.dto;

import com.dreamsol.entities.Vendor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorTypeDto {
	@NotBlank(message = "Vendor Type must not be Empty")
	@Size(min = 4, message = "Name must be min of 3 characters!!")
	private String typeName;

}
