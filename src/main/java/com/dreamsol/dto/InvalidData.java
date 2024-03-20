package com.dreamsol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidData {
    private int rowNumber;
    private int columnIndex;
    private String errorMessage;
//    private String invalidName;
//    private long invalidMob;
//    private String invalidEmail;
//    private String invalidBrief;
//    private VendorTypeDto vendorTypeDto;
//    private Set<ProductDto> productDto;


}
