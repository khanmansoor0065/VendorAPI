package com.dreamsol.services.imp;

import com.dreamsol.dto.ExcelResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class HelperService {

	public ExcelResponse save(MultipartFile file) {
		ExcelResponse saveResponse = new ExcelResponse();
		try {
			saveResponse.setCorrectData(FileHelper.convertExcelToList(file.getInputStream()).getCorrectData());
			saveResponse.setInvalidData(FileHelper.convertExcelToList(file.getInputStream()).getInvalidData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saveResponse;
	}
}
