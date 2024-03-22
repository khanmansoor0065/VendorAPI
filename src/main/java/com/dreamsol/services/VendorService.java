package com.dreamsol.services;

import com.dreamsol.dto.VendorDto;
import com.dreamsol.dto.ExcelResponse;
import com.dreamsol.dto.VendorResponse;
import com.dreamsol.dto.VendorResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface VendorService 
{
   VendorResponseDto addVendor(VendorDto vendorDto, String path, MultipartFile file);
   
   VendorResponseDto updateVendor(VendorDto vendorDto,String path, MultipartFile file, Integer vendorId);
   
   VendorResponseDto getVendorById(Integer vendorId);
   
   VendorResponse getAllVendor(Integer pageNumber,Integer pageSize,String sortBy,String sortDir,String keyword);
   
   void deleteVendor(String path, Integer vendorId);
}
