package com.dreamsol.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InvalidData {
    private String name;
    private long mobile;
    private String email;
    private String brief;
    private VendorTypeDto vendorTypeDto;
    private Set<ProductDto> productDto;
    private Set<String> messages;
}
