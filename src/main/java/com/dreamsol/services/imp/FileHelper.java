package com.dreamsol.services.imp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.dreamsol.dto.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class FileHelper {

	public static boolean checkExcelFormat(MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		} else {
			return false;
		}
	}

	public static ExcelResponse convertExcelToList(InputStream is) {
		ExcelResponse response = new ExcelResponse();
		List<VendorDto> correctData = new ArrayList<>();
		List<InvalidData> invalidData = new ArrayList<>();

		try {
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			DataFormatter formatter = new DataFormatter();

			Iterator<Row> iterator = sheet.iterator();
			int rowNumber = 0;

			while (iterator.hasNext()) {
				Row row = iterator.next();
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				int cid = 0;
				VendorDto vendorDto = new VendorDto();
				Set<ProductDto> listProductDto = new HashSet<>();
				boolean validRow = true;

				Iterator<Cell> cells = row.iterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					switch (cid) {
						case 0:
							String name = formatter.formatCellValue(cell);
							if (name == null || name.trim().isEmpty()) {
								validRow = false;
								invalidData.add(new InvalidData(rowNumber, cid, "Name cannot be empty"));
							}
							vendorDto.setName(name);
							break;
						case 1:
							if (cell.getCellTypeEnum() == CellType.NUMERIC) {
								long mob = (long) cell.getNumericCellValue();
								String mobString = String.valueOf(mob);
								if (mobString.length() != 10) {
									validRow = false;
									invalidData.add(new InvalidData(rowNumber, cid, "Mobile number must be 10 digits"));
								}
								vendorDto.setMob(mob);
							} else {
								validRow = false;
								invalidData.add(new InvalidData(rowNumber, cid, "Invalid cell type for mobile number"));
							}
							break;
						case 2:
							String email = formatter.formatCellValue(cell);
							if (!isValidEmail(email)) {
								validRow = false;
								invalidData.add(new InvalidData(rowNumber, cid, "Invalid email format"));
							}
							vendorDto.setEmail(email);
							break;
						case 3:
							String brief =formatter.formatCellValue(cell);
							if (brief == null || brief.trim().isEmpty()) {
								validRow = false;
								invalidData.add(new InvalidData(rowNumber, cid, "Brief cannot be empty"));
							}
							vendorDto.setBrief(brief);
							break;
						case 4:
							vendorDto.setVendorTypeDto(new VendorTypeDto(formatter.formatCellValue(cell)));
							break;
						case 5:
							String type = formatter.formatCellValue(cell);
							String[] typelist = type.split(",");
							for (String typeName : typelist) {
								ProductDto productdto = new ProductDto();
								productdto.setProductName(typeName);
								listProductDto.add(productdto);
							}
							vendorDto.setProductDto(listProductDto);
							break;
					}
					cid++;
				}
				if (validRow) {
					correctData.add(vendorDto);
				}
				rowNumber++;
			}
		} catch (IllegalArgumentException ex) {
			response.setErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setErrorMessage("An error occurred while processing the Excel file.");
		}

		response.setCorrectData(correctData);
		response.setInvalidData(invalidData);
		return response;
	}

	private static boolean isValidEmail(String email) {
		return email != null && email.contains("@");
	}
}


