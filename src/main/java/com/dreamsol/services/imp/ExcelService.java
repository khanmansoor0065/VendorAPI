package com.dreamsol.services.imp;

import com.dreamsol.dto.*;
import com.dreamsol.entities.Vendor;
import com.dreamsol.exceptions.EmptyVendorListException;
import com.dreamsol.repositories.VendorRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

import static com.dreamsol.services.imp.ExcelHelper.convertExcelToList;



@Service
public class ExcelService {

	@Autowired
	private VendorRepo vendorRepo;

	@Autowired
	private VendorUtility vendorUtility;

	public static Validator validator;

	public ResponseEntity<?> validateExcelData(MultipartFile file, String name) throws IllegalArgumentException{
		try {
			if (!ExcelHelper.checkExcelFormat(file)) {
				throw new IllegalArgumentException("Invalid File! "+name);
			}

			Class<?> dtoClass = getDtoClass(name);

			List<Object> validData = new ArrayList<>();
			List<Object> invalidData = new ArrayList<>();
			List<?> excelData = (List<?>) convertExcelToList(file.getInputStream(), name);

			excelData.forEach(excelEntity -> {
				boolean isValid = validateEntity(excelEntity, dtoClass);
				Object dto = isValid ? convertToValidDto(excelEntity,name) : convertToInvalidDto(excelEntity,name);
				(isValid ? validData : invalidData).add(dto);
			});

			Map<String, Object> response = new HashMap<>();
			response.put("ValidData", validData);
			response.put("InvalidData", invalidData);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Enter a valid entity name "+name);
		} catch (Exception e) {
			throw new IllegalArgumentException("Enter a valid entity name "+name);
		}
	}

	private Class<?> getDtoClass(String entityName) {
		switch (entityName.toLowerCase()) {
			case "vendor":
				return VendorDto.class;
			case "vendortype":
				return VendorTypeDto.class;
			case "product":
				return ProductDto.class;
			default:
				throw new IllegalArgumentException("Enter a valid entity name "+entityName);
		}
	}

	public static <T> boolean validateEntity(T dto, Class<?> dtoClass) {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		return violations.isEmpty();
	}
	public static <T> Set<String> validateEntityMsg(T dto, Class<?> dtoClass) {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		Set<String>msges= violations.stream().map(violation-> violation.getMessage()).collect(Collectors.toSet());
		return msges;
	}

	public Object convertToValidDto(Object entity, String entityName) {
		switch (entityName.toLowerCase()) {
			case "vendor":
				return entity;
			case "vendortype":
				return entity;
			case "product":
				return entity;
			default:
				throw new IllegalArgumentException("Enter a valid entity name "+entityName);
		}
	}

	public Object convertToInvalidDto(Object entity, String entityName) {
		switch (entityName.toLowerCase()) {
			case "vendor":
				return vendorUtility.toInvalid((VendorDto) entity);
			case "vendortype":
				return vendorUtility.toInvalidVendorType((VendorTypeDto) entity);
			case "product":
				return entity;
			default:
				throw new IllegalArgumentException("Entity is not valid  "+entityName);
		}
	}



	public ResponseEntity<Resource> getActualData(String filter) throws IOException {
		List<Vendor> filteredVendors;

		if (filter != null && !filter.isEmpty()) {
			filteredVendors = this.vendorRepo.findByNameLikeOrEmailLike("%" + filter + "%","%" + filter + "%");
			if(filteredVendors.isEmpty())
			{
				throw new EmptyVendorListException("No vendors found for the provided filter: " + filter);
			}
		} else {
			filteredVendors = this.vendorRepo.findAllVendorsWithProductsAndVendorType();
		}

		ByteArrayInputStream byteArrayInputStream = ExcelHelper.dataToExcel(filteredVendors);
		try{
		InputStreamResource file = new InputStreamResource(byteArrayInputStream);
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
	public ResponseEntity<Resource> getActualDataFormat(String entityName) throws RuntimeException {
		String[] vendorHeaders = {
				"Name",
				"Mobile",
				"Email",
				"Brief",
				"Vendor Type",
				"Product"
		};

		String[] vendorTypeHeaders = {
				"Type Name"
		};

		ByteArrayInputStream excelHeader;

		if (entityName.equalsIgnoreCase("vendor")) {
			excelHeader = ExcelHelper.createExcelHeader(vendorHeaders);
		} else if (entityName.equalsIgnoreCase("vendorType")) {
			excelHeader = ExcelHelper.createExcelHeader(vendorTypeHeaders);
		} else {

			throw new IllegalArgumentException("Invalid entityName "+entityName);
		}

		try {
			InputStreamResource file = new InputStreamResource(excelHeader);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + entityName + ".xlsx")
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(file);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

}
