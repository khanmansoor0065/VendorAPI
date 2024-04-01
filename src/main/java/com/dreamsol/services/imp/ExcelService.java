package com.dreamsol.services.imp;

import com.dreamsol.dto.ExcelResponse;
import com.dreamsol.dto.VendorDto;
import com.dreamsol.entities.Vendor;
import com.dreamsol.exceptions.EmptyVendorListException;
import com.dreamsol.repositories.VendorRepo;
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
import java.util.List;

@Service
public class ExcelService {

	@Autowired
	private VendorRepo vendorRepo;

	VendorUtility vendorUtility=new VendorUtility();

	public ResponseEntity<?> save(MultipartFile file) {
		ExcelResponse saveResponse = new ExcelResponse();
		try {
			ExcelResponse excelResponse = ExcelHelper.convertExcelToList(file.getInputStream());
			List<VendorDto> correctData = excelResponse.getCorrectData();
			for (VendorDto vendorDto : correctData) {
				Vendor vendor = vendorUtility.dtoToVendor(vendorDto);
				//vendorRepo.save(vendor);
			}
			saveResponse.setInvalidData(excelResponse.getInvalidData());
			saveResponse.setCorrectData(excelResponse.getCorrectData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(saveResponse);
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


}
