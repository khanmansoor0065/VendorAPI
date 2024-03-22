package com.dreamsol.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InvalidData {
    private int rowNumber;
    private int columnIndex;
    private String errorMessage;
    private String invalidName;
    private long invalidMob;
    private String invalidEmail;
    private String invalidBrief;
    private VendorTypeDto vendorTypeDto;
    private Set<ProductDto> productDto;
}
