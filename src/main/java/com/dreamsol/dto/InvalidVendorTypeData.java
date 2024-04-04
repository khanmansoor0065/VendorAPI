package com.dreamsol.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InvalidVendorTypeData
{
    private String typeName;
    private Set<String> messages;
}
