package com.dreamsol.controllers;

import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.VendorTypeDto;
import com.dreamsol.dto.VendorTypeResponse;
import com.dreamsol.services.VendorTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("vendor-type")
@Tag(name = "VendorTypeController",description = "To perform operation on VendorType")
@SecurityRequirement(name="bearerAuth")
public class VendorTypeController {

	@Autowired
	private VendorTypeService vendorTypeService;

//	@Autowired
//	private ExcelService helperService;


	@PostMapping(value = "add")
	public ResponseEntity<VendorTypeDto> addVendorType(@Valid @RequestBody VendorTypeDto vendorTypeDto) {
			  return vendorTypeService.addVendorType(vendorTypeDto);
	}


	@PutMapping(path = "update/{vendorTypeId}")
	public ResponseEntity<ApiResponse> updateVendorType(
			@RequestBody VendorTypeDto vendorTypeDto,
			@PathVariable Integer vendorTypeId) {
		return vendorTypeService.updateVendorType(vendorTypeDto,vendorTypeId);
	}


	@DeleteMapping("delete/{vendorTypeId}")
	public ResponseEntity<ApiResponse> deleteVendorType(@PathVariable Integer vendorTypeId) {
		return this.vendorTypeService.deleteVendorType(vendorTypeId);
	}


	@GetMapping("list")
	public ResponseEntity<VendorTypeResponse> getAllVendorType(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@RequestParam(value = "filter", required = false) String keyword) {
		return this.vendorTypeService.getAllVendorType(pageNumber, pageSize, sortBy, sortDir, keyword);
	}
	@PostMapping(value = "save-excel-data")
	public ResponseEntity<ApiResponse> saveExcel(@RequestBody List<VendorTypeDto> vendorTypeDtoList) {
		return vendorTypeService.saveExelCorrectData(vendorTypeDtoList);
	}

	@GetMapping("get-ById/{vendorTypeId}")
	public ResponseEntity<VendorTypeDto> getSingleVendorType(@PathVariable Integer vendorTypeId) {
		return this.vendorTypeService.getVendorTypeById(vendorTypeId);
	}
}

