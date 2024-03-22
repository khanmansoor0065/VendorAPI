package com.dreamsol.services.imp;

import com.dreamsol.dto.ExcelResponse;
import com.dreamsol.dto.VendorDto;
import com.dreamsol.entities.Vendor;
import com.dreamsol.repositories.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class HelperService {

	@Autowired
	private VendorRepo vendorRepo;

	VendorUtility vendorUtility=new VendorUtility();

	public ExcelResponse save(MultipartFile file) {
		ExcelResponse saveResponse = new ExcelResponse();
		try {
			ExcelResponse excelResponse = FileHelper.convertExcelToList(file.getInputStream());
			List<VendorDto> correctData = excelResponse.getCorrectData();
			for (VendorDto vendorDto : correctData) {
				Vendor vendor = vendorUtility.dtoToVendor(vendorDto);
				vendorRepo.save(vendor);
			}
			saveResponse.setInvalidData(excelResponse.getInvalidData());
			saveResponse.setCorrectData(excelResponse.getCorrectData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saveResponse;
	}

	public ByteArrayInputStream getActualData() throws IOException {
		List<Vendor> all=vendorRepo.findAll();
		ByteArrayInputStream byteArrayInputStream=FileHelper.dataToExcel(all);
		return  byteArrayInputStream;

	}

}
