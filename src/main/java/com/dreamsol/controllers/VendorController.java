package com.dreamsol.controllers;

import java.io.*;

import com.dreamsol.dto.*;
import com.dreamsol.exceptions.EmptyVendorListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.dreamsol.exceptions.ResourceAlreadyExistsException;
import com.dreamsol.services.VendorService;
import com.dreamsol.services.imp.FileHelper;
import com.dreamsol.services.imp.HelperService;
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
	private HelperService helperService;

	@Operation(
			summary = "POST operation on vendor",
			description = "It is used to save Vendors Object in database"
	)
	@PostMapping(value = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VendorResponseDto> addVendor(@Valid @RequestPart("vendorDto") VendorDto vendorDto,
													   @RequestParam("profileImage") MultipartFile file) {
		try {
			VendorResponseDto addedVendorDto = this.vendorService.addVendor(vendorDto, path, file);
			return new ResponseEntity<>(addedVendorDto, HttpStatus.CREATED);
		} catch (ResourceAlreadyExistsException ex) {
			throw ex;
		}
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
		VendorResponseDto updatedVendor = this.vendorService.updateVendor(vendorDto, path, file, vendorId);
		return ResponseEntity.ok(updatedVendor);
	}

	@Operation(
			summary = "DELETE operation on vendor",
			description = "It is used to delete Vendors Object from database"
	)
	@DeleteMapping("delete/{vendorId}")
	public ResponseEntity<ApiResponse> deleteVendor(@PathVariable Integer vendorId) {
		this.vendorService.deleteVendor(path, vendorId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Vendor deleted Successfully", true), HttpStatus.OK);
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
	public ResponseEntity<VendorResponseDto> getSingleVendor(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(this.vendorService.getVendorById(vendorId));
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
	@PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadUserExcelFile(@RequestParam("file") MultipartFile file) {
		if (FileHelper.checkExcelFormat(file)) {
			ExcelResponse excelResponse = helperService.save(file);
			return ResponseEntity.ok(excelResponse);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an Excel file only");
	}
	@Operation(
			summary = "GET operation to download data from the database in excel format",
			description = "It is used to download data from database"
	)
	@GetMapping("excel-download")
	public ResponseEntity<Resource> download(
			@RequestParam(required = false,defaultValue = "") String filter) throws IOException{
		try {
			ByteArrayInputStream actualData = helperService.getActualData(filter);
			InputStreamResource file = new InputStreamResource(actualData);
			ResponseEntity<Resource> body = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "vendor.xlsx")
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(file);
			return body;
		}catch (EmptyVendorListException ex)
		{
			throw ex;
		}
	}
}

