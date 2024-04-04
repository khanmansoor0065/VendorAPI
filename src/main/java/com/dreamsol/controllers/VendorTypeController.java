package com.dreamsol.controllers;

import com.dreamsol.dto.*;
import com.dreamsol.services.VendorTypeService;

import com.dreamsol.services.imp.ExcelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("vendor-type")
@Tag(name = "VendorTypeController",description = "To perform operation on VendorType")
public class VendorTypeController {
	@Autowired
	private VendorTypeService vendorTypeService;

	@Autowired
	private ExcelService helperService;

	@Operation(
			summary = "POST operation on vendor Type",
			description = "It is used to save Vendor Type Object in database"
	)
	@PostMapping(value = "add")
	public ResponseEntity<VendorTypeDto> addVendorType(@Valid @RequestBody VendorTypeDto vendorTypeDto) {
			  return vendorTypeService.addVendorType(vendorTypeDto);
	}

	@Operation(
			summary = "PUT operation on vendor Type",
			description = "It is used to update Vendors Type Object in database"
	)
	@PutMapping(path = "update/{vendorTypeId}")
	public ResponseEntity<ApiResponse> updateVendorType(
			@RequestBody VendorTypeDto vendorTypeDto,
			@PathVariable Integer vendorTypeId) {
		return vendorTypeService.updateVendorType(vendorTypeDto,vendorTypeId);
	}

	@Operation(
			summary = "DELETE operation on vendor Type",
			description = "It is used to delete Vendors Type Object from database"
	)
	@DeleteMapping("delete/{vendorTypeId}")
	public ResponseEntity<ApiResponse> deleteVendorType(@PathVariable Integer vendorTypeId) {
		return this.vendorTypeService.deleteVendorType(vendorTypeId);
	}

	@Operation(
			summary = "GET operation on vendor Type",
			description = "It is used to retrieve Vendors Type Object from database"
	)
	@GetMapping("list")
	public ResponseEntity<VendorTypeResponse> getAllVendorType(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@RequestParam(value = "filter", required = false) String keyword) {
		return this.vendorTypeService.getAllVendorType(pageNumber, pageSize, sortBy, sortDir, keyword);
	}
	@PostMapping(value = "validate-excel-data")
	public ResponseEntity<ApiResponse> saveExcel(@RequestBody List<VendorTypeDto> vendorTypeDtoList) {
		return vendorTypeService.saveExelCorrectData(vendorTypeDtoList);
	}
}

