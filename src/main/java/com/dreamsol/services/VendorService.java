package com.dreamsol.services;

import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.VendorDto;
import com.dreamsol.dto.VendorResponse;
import com.dreamsol.dto.VendorResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VendorService 
{
   ResponseEntity<VendorResponseDto> addVendor(VendorDto vendorDto, String path, MultipartFile file);

   ResponseEntity<VendorResponseDto> updateVendor(VendorDto vendorDto,String path, MultipartFile file, Integer vendorId);

   ResponseEntity<VendorResponseDto> getVendorById(Integer vendorId);

   ResponseEntity<VendorResponse> getAllVendor(Integer pageNumber,Integer pageSize,String sortBy,String sortDir,String keyword);

   ResponseEntity<ApiResponse> deleteVendor(String path, Integer vendorId);

   ResponseEntity<List<VendorResponseDto>>  getDetailsByProduct(String productName);

   ResponseEntity<?> bulkData();

    ResponseEntity<ApiResponse> saveExelCorrectData(List<VendorDto> vendorDtoList);
}
