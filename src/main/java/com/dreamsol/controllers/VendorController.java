package com.dreamsol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.VendorDto;
import com.dreamsol.dto.VendorResponse;
import com.dreamsol.services.VendorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("vendor")
@Tag(name = "VendorController",description = "To perform operation on Vendor")
public class VendorController 
{
	@Autowired
    private VendorService vendorService;
	
	@Value("${project.image}")
	private String path;
		
	@Operation(
			summary = "POST operation on vendor",
			description = "It is used to save Vendors Object in database"
			)
	@PostMapping(value = "add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VendorDto> addVendor(@Valid @RequestPart("vendorDto") VendorDto vendorDto,
			 @RequestParam("profileImage") MultipartFile file)
	{
		VendorDto addedVendorDto=this.vendorService.addVendor(vendorDto,path,file);
		return new ResponseEntity<>(addedVendorDto,HttpStatus.CREATED);
	}
	
	@Operation(
			summary = "PUT operation on vendor",
			description = "It is used to update Vendors Object in database"
			)
	@PutMapping("update/{vendorId}")
	public ResponseEntity<VendorDto> updateVendor(@Valid  @RequestBody VendorDto vendorDto,@PathVariable Integer vendorId)
	{
	    VendorDto updatedVendor=this.vendorService.updateVendor(vendorDto, vendorId);
	    return ResponseEntity.ok(updatedVendor);
	}

	@Operation(
			summary = "DELETE operation on vendor",
			description = "It is used to delete Vendors Object from database"
			)
	@DeleteMapping("delete/{vendorId}")
	public ResponseEntity<ApiResponse> deleteVendor(@PathVariable Integer vendorId)
	{
		this.vendorService.deleteVendor(vendorId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Vendor deleted Successfully",true),HttpStatus.OK);
	}

	@Operation(
			summary = "GET operation on vendor",
			description = "It is used to retrieve Vendors Object from database"
			)
	@GetMapping("list")
	public ResponseEntity<VendorResponse> getAllVendor(
	        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
	        @RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize,
	        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
	        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
	        @RequestParam(value = "filter", required = false) String keyword) {
	    return ResponseEntity.ok(this.vendorService.getAllVendor(pageNumber, pageSize, sortBy, sortDir, keyword));
	}
	
	@Operation(
			summary = "GET operation on vendor by using Vendor id",
			description = "It is used to retrieve Vendor Object from database using id"
			)
	@GetMapping("list/{vendorId}")
	public ResponseEntity<VendorDto> getSinglevendor(@PathVariable Integer vendorId)
	{
		return ResponseEntity.ok(this.vendorService.getVendorById(vendorId));
	}

}
