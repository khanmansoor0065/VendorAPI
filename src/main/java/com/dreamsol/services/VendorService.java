package com.dreamsol.services;

import com.dreamsol.dto.VendorDto;
import com.dreamsol.dto.VendorResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VendorService 
{
   VendorDto addVendor(VendorDto vendorDto, String path, MultipartFile file);
   
   VendorDto updateVendor(VendorDto vendorDto,Integer vendorId);
   
   VendorDto getVendorById(Integer vendorId);
   
   VendorResponse getAllVendor(Integer pageNumber,Integer pageSize,String sortBy,String sortDir,String keyword);
   
   void deleteVendor(Integer vendorId);

}
