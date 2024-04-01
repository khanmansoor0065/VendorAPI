package com.dreamsol.services;

import com.dreamsol.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VendorTypeService
{

   ResponseEntity<ApiResponse> updateVendorType(VendorTypeDto vendorTypeDto,Integer vendorTypeId);


   ResponseEntity<VendorTypeResponse> getAllVendorType(Integer pageNumber,Integer pageSize,String sortBy,String sortDir,String keyword);

   ResponseEntity<ApiResponse> deleteVendorType(Integer vendorTypeId);

   ResponseEntity<VendorTypeDto> addVendorType(VendorTypeDto vendorTypeDto);

}
