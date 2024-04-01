package com.dreamsol.services.imp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.dreamsol.dto.*;
import com.dreamsol.entities.Product;
import com.dreamsol.entities.Vendor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {

	public static String[] HEADERS = {
			"Name",
			"Mobile",
			"Email",
			"Brief",
			"Vendor Type",
			"Product"
	};

	public static String SHEET_NAME = "vendor_data";


	public static boolean checkExcelFormat(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
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

				InvalidData invalidDataObj = new InvalidData();

				Iterator<Cell> cells = row.iterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					switch (cid) {
						case 0:
							String name = formatter.formatCellValue(cell);
							if (name == null || name.trim().isEmpty()) {
								validRow = false;
								invalidDataObj.setRowNumber(rowNumber);
								invalidDataObj.setColumnIndex(cid);
								invalidDataObj.setErrorMessage("Name cannot be empty");
								//invalidDataObj.setInvalidName(name);
							}
							invalidDataObj.setInvalidName(name);
							vendorDto.setName(name);
							break;
						case 1:
							if (cell.getCellTypeEnum() == CellType.NUMERIC) {
								long mob = (long) cell.getNumericCellValue();
								String mobString = String.valueOf(mob);
								if (mobString.length() != 10) {
									validRow = false;
									invalidDataObj.setRowNumber(rowNumber);
									invalidDataObj.setColumnIndex(cid);
									invalidDataObj.setErrorMessage("Mobile number must be 10 digits");
									//	invalidDataObj.setInvalidMob(mob);
								}
								invalidDataObj.setInvalidMob(mob);
								vendorDto.setMob(mob);
							} else {
								validRow = false;
								invalidDataObj.setRowNumber(rowNumber);
								invalidDataObj.setColumnIndex(cid);
								invalidDataObj.setErrorMessage("Invalid cell type for mobile number");
							}
							break;
						case 2:
							String email = formatter.formatCellValue(cell);
							if (!isValidEmail(email)) {
								validRow = false;
								invalidDataObj.setRowNumber(rowNumber);
								invalidDataObj.setColumnIndex(cid);
								invalidDataObj.setErrorMessage("Invalid email format");
								//invalidDataObj.setInvalidEmail(email);
							}
							invalidDataObj.setInvalidEmail(email);
							vendorDto.setEmail(email);
							break;
						case 3:
							String brief = formatter.formatCellValue(cell);
							if (brief == null || brief.trim().isEmpty()) {
								validRow = false;
								invalidDataObj.setRowNumber(rowNumber);
								invalidDataObj.setColumnIndex(cid);
								invalidDataObj.setErrorMessage("Brief cannot be empty");
								//invalidDataObj.setInvalidBrief(brief);
							}
							invalidDataObj.setInvalidBrief(brief);
							vendorDto.setBrief(brief);
							break;
						case 4:
							invalidDataObj.setVendorTypeDto(new VendorTypeDto(formatter.formatCellValue(cell)));
							vendorDto.setVendorTypeDto(new VendorTypeDto(formatter.formatCellValue(cell)));
							break;
						case 5:
							String type = formatter.formatCellValue(cell);
							String[] typeList = type.split(",");
							for (String typeName : typeList) {
								ProductDto productdto = new ProductDto();
								productdto.setProductName(typeName);
								listProductDto.add(productdto);
							}
							invalidDataObj.setProductDto(listProductDto);
							vendorDto.setProductDto(listProductDto);
							break;
					}
					cid++;
				}
				if (!validRow) {
					invalidData.add(invalidDataObj);
				} else {
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


	public static ByteArrayInputStream dataToExcel(List<Vendor> list) {

		Workbook workbook = new SXSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			Sheet sheet = workbook.createSheet(SHEET_NAME);

//			String[] x = {"Mandatory", "Mandatory", "Mandatory", "Optional", "Mandatory", "Mandatory"};
//			Row row0 = sheet.createRow(0);
//
//			Font boldFont = workbook.createFont();
//			boldFont.setBold(true);
//
//			CellStyle boldStyle = workbook.createCellStyle();
//			boldStyle.setFont(boldFont);
//
//			for (int i = 0; i < x.length; i++) {
//				Cell cell = row0.createCell(i);
//				cell.setCellValue(x[i]);
//				cell.setCellStyle(boldStyle);
//			}

			Font boldFont = workbook.createFont();
			boldFont.setBold(true);

			CellStyle boldStyle = workbook.createCellStyle();
			boldStyle.setFont(boldFont);


			Row row1 = sheet.createRow(0);
			for (int i = 0; i < HEADERS.length; i++) {
				Cell cell = row1.createCell(i);
				cell.setCellValue(HEADERS[i]);
				cell.setCellStyle(boldStyle);
			}

			int rowIndex = 1;
			for (Vendor v : list) {
				Row dataRow = sheet.createRow(rowIndex);
				rowIndex++;
				dataRow.createCell(0).setCellValue(v.getName());
				dataRow.createCell(1).setCellValue(v.getMob());
				dataRow.createCell(2).setCellValue(v.getEmail());
				dataRow.createCell(3).setCellValue(v.getBrief());
				dataRow.createCell(4).setCellValue(v.getVendorType().getTypeName());
				StringBuilder products = new StringBuilder();
				for (Product product : v.getProducts()) {
					products.append(product.getProductName()).append(", ");
				}
				if (products.length() > 0) {
					products.setLength(products.length() - 2);
				}
				dataRow.createCell(5).setCellValue(products.toString());
			}
			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());


		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}