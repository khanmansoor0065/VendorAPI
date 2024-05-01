package com.dreamsol.services.imp;

import com.dreamsol.dto.*;
import com.dreamsol.entities.Product;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.Vendor;
import com.dreamsol.entities.VendorType;
import com.dreamsol.exceptions.EmptyVendorListException;
import com.dreamsol.exceptions.ResourceAlreadyExistsException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.*;
import com.dreamsol.services.VendorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendorServiceImp implements VendorService {
	@Autowired
	private VendorRepo vendorRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private VendorUtility vendorUtility;

	@Autowired
	private VendorTypeRepo vendorTypeRepo;

	@Autowired
	private ImageUploadService imageUploadService;

	@Autowired
	private ExcelService helperService;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PermissionRepo permissionRepo;

	Vendor savedVendor;

	@Override
	public ResponseEntity<VendorResponseDto> addVendor(VendorDto vendorDto, String path, MultipartFile file) {
		Vendor vendorByEmail = vendorRepo.findByEmail(vendorDto.getEmail());
		Vendor vendorByMobile = vendorRepo.findByMob(vendorDto.getMob());
		//List<Role> roleList = roleRepo.findByRole(role.getRoles());

		if (Objects.isNull(vendorByEmail) && Objects.isNull(vendorByMobile)) {
			Vendor vendor = vendorUtility.dtoToVendor(vendorDto);
			savedVendor = vendorRepo.save(vendor);
			imageUploadService.uploadImage(path, file, savedVendor);
			try{
			return new ResponseEntity<>(vendorUtility.vendorToDto(vendor), HttpStatus.CREATED);
		} catch (ResourceAlreadyExistsException ex) {
			throw ex;
		}

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
	public ResponseEntity<VendorResponseDto> updateVendor(VendorDto vendorDto,String path,MultipartFile file, Integer vendorId) {
		Vendor vendor = vendorRepo.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorId));
		vendor.setName(vendorDto.getName());
		vendor.setMob(vendorDto.getMob());
		vendor.setEmail(vendorDto.getEmail());
		vendor.setBrief(vendorDto.getBrief());
		String fileName=file.getOriginalFilename();
		if(fileName!=null)
		{
			String oldFileName=vendor.getFile();
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

		return ResponseEntity.ok(vendorDto1);
	}

	@Override
	public ResponseEntity<VendorResponseDto> getVendorById(Integer vendorId) {
		Vendor vendor = this.vendorRepo.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorId));
		return ResponseEntity.ok(vendorUtility.vendorToDto(vendor));
	}

	@Override
	public ResponseEntity<VendorResponse> getAllVendor(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
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
			pageVendor = this.vendorRepo.findByNameLikeOrEmailLike("%" + keyword + "%","%" + keyword + "%", p);
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
		return ResponseEntity.ok(vendorResponse);
	}

	@Override
	public ResponseEntity<ApiResponse> deleteVendor(String path, Integer vendorId) {
		Vendor vendor = this.vendorRepo.findById(vendorId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor", "Id", vendorId));
		String fileName=vendor.getFile();
		if(fileName==null)
		{
			this.vendorRepo.delete(vendor);
		}
		else if(imageUploadService.deleteImage(path,fileName))
		{
			this.vendorRepo.delete(vendor);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse("Vendor deleted Successfully", true), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<VendorResponseDto>>  getDetailsByProduct(String productName) {
		List<Product> pList = productRepo.findByProductName(productName);
		List<VendorResponseDto> vendorList = new ArrayList<>();

		if (pList.isEmpty()) {
			throw new EmptyVendorListException("No Product Found");
		}

		for (Product product : pList) {
			Vendor vendor = product.getVendor();
			VendorResponseDto vendorDto = new VendorResponseDto();

			BeanUtils.copyProperties(vendor, vendorDto);

			VendorTypeDto vendorTypeDto = new VendorTypeDto();
			BeanUtils.copyProperties(vendor.getVendorType(), vendorTypeDto);
			vendorDto.setVendorTypeDto(vendorTypeDto);

			Set<ProductResponseDto> productResponseDtoSet = new HashSet<>();
			for (Product productItem : vendor.getProducts()) {
				ProductResponseDto productResponseDto = new ProductResponseDto();
				BeanUtils.copyProperties(productItem, productResponseDto);
				productResponseDtoSet.add(productResponseDto);
			}
			vendorDto.setProductResponseDto(productResponseDtoSet);

			vendorList.add(vendorDto);
		}
		return ResponseEntity.ok(vendorList);
	}
	public ResponseEntity<?> bulkData(){
		List<Vendor> vendorList=new ArrayList<Vendor>();
		long mob=6396928401L;
		for(int i=0;i<=300000;i++){
			Vendor vendor=new Vendor();
			String random= UUID.randomUUID().toString().substring(0,5);
			vendor.setName(random);
			vendor.setEmail(random+"@gmail.com");
			vendor.setMob(mob);
			vendor.setBrief(random);
			VendorType type=new VendorType();
			type.setTypeName(random);
			Set<Product> product=new HashSet<Product>();
			Product prd=new Product();
			prd.setVendor(vendor);
			prd.setProductName(random);
			product.add(prd);
			vendor.setVendorType(type);
			vendor.setProducts(product);
			vendorList.add(vendor);
			mob=mob+2;
		}
		this.vendorRepo.saveAll(vendorList);
		return ResponseEntity.ok().build();
	}
	@Override
	public ResponseEntity<ApiResponse> saveExelCorrectData(List<VendorDto> vendorDtoList) {
		List<Vendor> vendorList = vendorUtility.dtoToVendorList(vendorDtoList);
		Set<Vendor> savedVendorList = new HashSet<>();

		for (Vendor vendor : vendorList) {
			Vendor vendorByMobile = vendorRepo.findByMob(vendor.getMob());

			if (vendorByMobile != null) {
				vendorByMobile.setName(vendor.getName());
				vendorByMobile.setBrief(vendor.getBrief());
				vendorByMobile.setEmail(vendor.getEmail());
				savedVendorList.add(vendorByMobile);
			} else {
				savedVendorList.add(vendor);
			}
		}
		vendorRepo.saveAll(savedVendorList);
		return new ResponseEntity<>(new ApiResponse("Vendors List Saved successfully", true), HttpStatus.OK);
	}

}

