package com.dreamsol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelResponse {
	private List<VendorDto> correctData;
	private List<VendorDto> invalidData;
	private String errorMessage;

}
