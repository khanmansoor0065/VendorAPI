package com.dreamsol.controllers;

import java.io.IOException;
import java.util.List;

import com.dreamsol.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dreamsol.services.VendorService;

import com.dreamsol.services.imp.ExcelService;
import com.dreamsol.services.imp.ImageUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("vendor")
@Tag(name = "VendorController",description = "To perform operation on Vendor")
public class VendorController {
	@Autowired
	private VendorService vendorService;

	@Autowired
	private ImageUploadService imageUploadService;

	@Value("${project.image}")
	private String path;

	@Autowired
	private ExcelService helperService;

	@Operation(
			summary = "POST operation on vendor",
			description = "It is used to save Vendors Object in database"
	)
	@PostMapping(value = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VendorResponseDto> addVendor(@Valid @RequestPart("vendorDto") VendorDto vendorDto,
													   @RequestParam("file") MultipartFile file) {
		return vendorService.addVendor(vendorDto, path, file);
	}

	@Operation(
			summary = "PUT operation on vendor",
			description = "It is used to update Vendors Object in database"
	)
	@PutMapping(path = "update/{vendorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VendorResponseDto> updateVendor(
			@Valid
			@RequestPart("vendorDto") VendorDto vendorDto,
			@PathVariable Integer vendorId,
			@RequestParam("profileImage") MultipartFile file) {
		return vendorService.updateVendor(vendorDto, path, file, vendorId);
	}

	@Operation(
			summary = "DELETE operation on vendor",
			description = "It is used to delete Vendors Object from database"
	)
	@DeleteMapping("delete/{vendorId}")
	public ResponseEntity<ApiResponse> deleteVendor(@PathVariable Integer vendorId) {
		return this.vendorService.deleteVendor(path, vendorId);
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
		return this.vendorService.getAllVendor(pageNumber, pageSize, sortBy, sortDir, keyword);
	}

	@Operation(
			summary = "GET operation on vendor by using Vendor id",
			description = "It is used to retrieve Vendor Object from database using id"
	)
	@GetMapping("list/{vendorId}")
	public ResponseEntity<VendorResponseDto> getSingleVendor(@PathVariable Integer vendorId) {
		return this.vendorService.getVendorById(vendorId);
	}

	@GetMapping(value = "download/{fileName}")
	public void downloadFile(
			@PathVariable("fileName") String fileName, HttpServletResponse response)
			throws IOException {
		byte[] fileBytes = imageUploadService.getFileBytes(path, fileName);

		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);

		response.getOutputStream().write(fileBytes);
	}

	@Operation(
			summary = "POST operation to Upload Excel file",
			description = "It is used to store Excel data in Database"
	)
	@PostMapping(value = "excel-list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadUserExcelFile(@RequestParam("file") MultipartFile file,
												 @RequestParam(value = "name") String EntityName) {
			return helperService.validateExcelData(file, EntityName);
	}

	@Operation(
			summary = "GET operation to download data from the database in excel format",
			description = "It is used to download data from database"
	)
	@GetMapping("excel-download")
	public ResponseEntity<Resource> download(
			@RequestParam(required = false, defaultValue = "") String filter) throws IOException {
		return helperService.getActualData(filter);
	}

	@GetMapping("product{productName}")
	public ResponseEntity<List<VendorResponseDto>> getDetailsByProduct(@RequestParam(value = "productName") String productName) {
		return vendorService.getDetailsByProduct(productName);
	}

	@PostMapping("bulkData")
	public ResponseEntity<?> postBulkApi() {
		return this.vendorService.bulkData();
	}

	@Operation(
			summary = "POST operation on vendor to save excel correct list of vendors",
			description = "It is used to save excel correct list of vendors in database"
	)
	@PostMapping(value = "validate-excel-data")
	public ResponseEntity<ApiResponse> saveExcel(@RequestBody List<VendorDto> vendorDtoList) {
		return vendorService.saveExelCorrectData(vendorDtoList);
	}

	@GetMapping("excel-format-download")
	public ResponseEntity<Resource> downloadFormat(@RequestParam(value = "name") String EntityName) throws IOException {
		return helperService.getActualDataFormat(EntityName);
	}
}

