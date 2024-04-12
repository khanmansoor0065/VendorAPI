package com.dreamsol.services.imp;

import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.VendorTypeDto;
import com.dreamsol.dto.VendorTypeResponse;
import com.dreamsol.entities.VendorType;
import com.dreamsol.exceptions.ResourceAlreadyExistsException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.VendorTypeRepo;
import com.dreamsol.services.VendorTypeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class VendorTypeServiceImp implements VendorTypeService {

	@Autowired
	private VendorTypeRepo vendorTypeRepo;

	@Autowired
	VendorUtility vendorUtility;

	VendorType savedVendorType;

	@Override
	public ResponseEntity<VendorTypeDto> addVendorType(VendorTypeDto vendorTypeDto) {
		VendorType vendorByTypeName = vendorTypeRepo.findByTypeName(vendorTypeDto.getTypeName());

		if (Objects.isNull(vendorByTypeName)) {
			VendorType vendorType = vendorUtility.dtoToVendorType(vendorTypeDto);
			savedVendorType = vendorTypeRepo.save(vendorType);
			try {
				return new ResponseEntity<>(vendorUtility.vendorTypeToDto(vendorType), HttpStatus.CREATED);
			} catch (ResourceAlreadyExistsException ex) {
				throw ex;
			}
		} else {
			throw new ResourceAlreadyExistsException("Vendor Type");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ApiResponse> updateVendorType(VendorTypeDto vendorTypeDto, Integer vendorTypeId) {
		try {
			VendorType vendorType = vendorTypeRepo.findById(vendorTypeId)
					.orElseThrow(() -> new ResourceNotFoundException("VendorType", "Id", vendorTypeId));

			vendorType.setTypeName(vendorTypeDto.getTypeName());
			vendorTypeRepo.save(vendorType);

			return new ResponseEntity<>(new ApiResponse("Vendor Type Updated Successfully", true), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("VendorType", "Id", vendorTypeId);
		}
	}


	@Override
	public ResponseEntity<VendorTypeResponse> getAllVendorType(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
														   String keyword) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<VendorType> pageVendorType;
		if (keyword != null && !keyword.isEmpty()) {
			pageVendorType = this.vendorTypeRepo.findByTypeNameContainingIgnoreCase(keyword, pageable);
		} else {
			pageVendorType = vendorTypeRepo.findAll(pageable);
		}

		List<VendorType> vendorTypes = pageVendorType.getContent();
		List<VendorTypeDto> vendorTypeDtos = vendorTypes.stream()
				.map(vendorUtility::vendorTypeToDto)
				.collect(Collectors.toList());

		VendorTypeResponse vendorTypeResponse = new VendorTypeResponse();
		vendorTypeResponse.setContent(vendorTypeDtos);
		vendorTypeResponse.setPageNumber(pageVendorType.getNumber());
		vendorTypeResponse.setPageSize(pageVendorType.getSize());
		vendorTypeResponse.setTotalElements(pageVendorType.getTotalElements());
		vendorTypeResponse.setTotalPages(pageVendorType.getTotalPages());
		vendorTypeResponse.setLastPage(pageVendorType.isLast());

		return ResponseEntity.ok(vendorTypeResponse);
	}

	@Override
	public ResponseEntity<ApiResponse> deleteVendorType(Integer vendorTypeId) {
		VendorType vendorType = this.vendorTypeRepo.findById(vendorTypeId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorTypeId));
		this.vendorTypeRepo.deleteById(vendorTypeId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Vendor Type deleted Successfully", true), HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ApiResponse> saveExelCorrectData(List<VendorTypeDto> vendorTypeDtoList)
	{
		List<VendorType> vendorTypeList = vendorUtility.dtoToVendorTypeList(vendorTypeDtoList);
		Set<VendorType> savedVendorTypeList = new HashSet<>();
		for(VendorType vendorType:vendorTypeList)
		{
			VendorType vendorTypeName = vendorTypeRepo.findByTypeName(vendorType.getTypeName());
			if (vendorTypeName != null)
			{
				vendorTypeName.setTypeName(vendorType.getTypeName());
				savedVendorTypeList.add(vendorTypeName);
			}else{
				savedVendorTypeList.add(vendorType);
			}
		}
		vendorTypeRepo.saveAll(savedVendorTypeList);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Correct Vendor Type List Saved Successfully", true), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VendorTypeDto> getVendorTypeById(Integer vendorTypeId) {
		VendorType vendorType = this.vendorTypeRepo.findById(vendorTypeId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor type", "Id", vendorTypeId));
		return ResponseEntity.ok(vendorUtility.vendorTypeToDto(vendorType));
	}
}

