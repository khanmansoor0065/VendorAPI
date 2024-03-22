package com.dreamsol.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dreamsol.exceptions.*;
import com.dreamsol.dto.ProductDto;
import com.dreamsol.dto.ProductResponseDto;
import com.dreamsol.dto.VendorDto;
import com.dreamsol.dto.ExcelResponse;
import com.dreamsol.dto.VendorResponse;
import com.dreamsol.dto.VendorResponseDto;
import com.dreamsol.dto.VendorTypeDto;
import com.dreamsol.entities.Product;
import com.dreamsol.entities.Vendor;
import com.dreamsol.entities.VendorType;
import com.dreamsol.repositories.VendorRepo;
import com.dreamsol.repositories.VendorTypeRepo;
import com.dreamsol.services.VendorService;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VendorServiceImp implements VendorService {
	@Autowired
	private VendorRepo vendorRepo;

	VendorUtility vendorUtility=new VendorUtility();

	@Autowired
	private VendorTypeRepo vendorTypeRepo;

	@Autowired
	private ImageUploadService imageUploadService;

	@Autowired
	private HelperService helperService;

	Vendor savedVendor;


	@Override
	public VendorResponseDto addVendor(VendorDto vendorDto, String path, MultipartFile file) {
		Vendor vendorByEmail = vendorRepo.findByEmail(vendorDto.getEmail());
		Vendor vendorByMobile = vendorRepo.findByMob(vendorDto.getMob());

		if (Objects.isNull(vendorByEmail) && Objects.isNull(vendorByMobile)) {
			Vendor vendor = vendorUtility.dtoToVendor(vendorDto);
			savedVendor = vendorRepo.save(vendor);
			imageUploadService.uploadImage(path, file, savedVendor);
			return vendorUtility.vendorToDto(vendor);

		} else {
			if (!Objects.isNull(vendorByEmail)) {
				throw new ResourceAlreadyExistsException("Email");
			} else {
				throw new ResourceAlreadyExistsException("Mobile No.");
			}
		}
	}

	@Override
	@Transactional
	public VendorResponseDto updateVendor(VendorDto vendorDto,String path,MultipartFile file, Integer vendorId) {
		Vendor vendor = vendorRepo.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorId));
		vendor.setName(vendorDto.getName());
		vendor.setMob(vendorDto.getMob());
		vendor.setEmail(vendorDto.getEmail());
		vendor.setBrief(vendorDto.getBrief());
		String fileName=file.getOriginalFilename();
		if(fileName!=null)
		{
			String oldFileName=vendor.getProfileImage();
			if(imageUploadService.deleteImage(path, oldFileName))
			{
				imageUploadService.uploadImage(path, file, vendor);
			}
		}
		int vendortype_id = vendor.getVendorType().getId();
		VendorType vendorType = this.vendorTypeRepo.findById(vendortype_id)
				.orElseThrow(() -> new ResourceNotFoundException("VedorType", "Id", vendortype_id));
		vendorTypeRepo.delete(vendorType);
		vendor.setVendorType(vendorUtility.dtoToVendorType(vendorDto.getVendorTypeDto()));
		Vendor updatedVendor = vendorRepo.save(vendor);
		VendorResponseDto vendorDto1 = vendorUtility.vendorToDto(updatedVendor);

		return vendorDto1;
	}

	@Override
	public VendorResponseDto getVendorById(Integer vendorId) {
		Vendor vendor = this.vendorRepo.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorId));
		return vendorUtility.vendorToDto(vendor);
	}

	@Override
	public VendorResponse getAllVendor(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
									   String keyword) {
		Sort sort = null;
		if (sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Vendor> pageVendor;
		if (keyword != null && !keyword.isEmpty()) {
			pageVendor = this.vendorRepo.findByNameContainingIgnoreCase("%" + keyword + "%", p);
		} else {
			pageVendor = this.vendorRepo.findAll(p);
		}
		List<Vendor> vendors = pageVendor.getContent();
		List<VendorResponseDto> vendorDtos = vendors.stream().map(vendor -> vendorUtility.vendorToDto(vendor))
				.collect(Collectors.toList());
		VendorResponse vendorResponse = new VendorResponse();
		vendorResponse.setContent(vendorDtos);
		vendorResponse.setPageNumber(pageVendor.getNumber());
		vendorResponse.setPageSize(pageVendor.getSize());
		vendorResponse.setTotalElements(pageVendor.getTotalElements());
		vendorResponse.setTotalPages(pageVendor.getTotalPages());
		vendorResponse.setLastPage(pageVendor.isLast());
		return vendorResponse;
	}

	@Override
	public void deleteVendor(String path,Integer vendorId) {
		Vendor vendor = this.vendorRepo.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorId));
		String fileName=vendor.getProfileImage();
		if(fileName==null)
		{
			this.vendorRepo.delete(vendor);
		}
		else if(imageUploadService.deleteImage(path,fileName))
		{
			this.vendorRepo.delete(vendor);
		}
	}
}

