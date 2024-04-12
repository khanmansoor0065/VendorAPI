package com.dreamsol.services;

import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.VendorTypeDto;
import com.dreamsol.dto.VendorTypeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VendorTypeService
{

   ResponseEntity<ApiResponse> updateVendorType(VendorTypeDto vendorTypeDto,Integer vendorTypeId);


   ResponseEntity<VendorTypeResponse> getAllVendorType(Integer pageNumber,Integer pageSize,String sortBy,String sortDir,String keyword);

   ResponseEntity<ApiResponse> deleteVendorType(Integer vendorTypeId);

   ResponseEntity<VendorTypeDto> addVendorType(VendorTypeDto vendorTypeDto);

    ResponseEntity<ApiResponse> saveExelCorrectData(List<VendorTypeDto> vendorTypeDtoList);

    ResponseEntity<VendorTypeDto> getVendorTypeById(Integer vendorTypeId);
}
