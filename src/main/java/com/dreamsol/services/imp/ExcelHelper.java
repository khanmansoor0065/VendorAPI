package com.dreamsol.services.imp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import com.dreamsol.dto.*;
import com.dreamsol.entities.Product;
import com.dreamsol.entities.Vendor;
import com.dreamsol.exceptions.EmptyVendorListException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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

	private static final Map<String, Class<?>> entityDtoMap = new HashMap<>();

	static {
		entityDtoMap.put("vendor", VendorDto.class);
		entityDtoMap.put("vendorType", VendorTypeDto.class);
		entityDtoMap.put("product", ProductDto.class);
	}

	public static List<Object> convertExcelToList(InputStream is, String entityName) {
		List<Object> dataList = new ArrayList<>();

		Class<?> dtoClass = entityDtoMap.get(entityName);
		if (dtoClass == null) {
			throw new EmptyVendorListException("No DTO class found for entity: " + entityName);
        }

		try {
			Workbook workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			int rowNumber = 0;

			for (Row row : sheet) {
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				int cid = 0;
				Object dtoInstance = dtoClass.getDeclaredConstructor().newInstance();

				for (Cell cell : row) {
					if (cid < dtoClass.getDeclaredFields().length) {
						Field field = dtoClass.getDeclaredFields()[cid];
						field.setAccessible(true);
						try {
							if (cell.getCellTypeEnum() == CellType.NUMERIC) {
								if (field.getType() == Long.class || field.getType() == long.class) {
									field.set(dtoInstance, (long) cell.getNumericCellValue());
								} else if (field.getType() == String.class) {
									field.set(dtoInstance, String.valueOf(cell.getNumericCellValue()));
								}
							} else if (cell.getCellTypeEnum() == CellType.STRING) {
								if (field.getType() == Long.class || field.getType() == long.class) {
									field.set(dtoInstance, Long.parseLong(cell.getStringCellValue()));
								} else if (field.getType() == String.class) {
									field.set(dtoInstance, cell.getStringCellValue());
								} else if (field.getType() == VendorTypeDto.class) {
									VendorTypeDto vendorTypeDto = new VendorTypeDto();
									vendorTypeDto.setTypeName(cell.getStringCellValue());
									field.set(dtoInstance, vendorTypeDto);
								} else if (field.getType() == Set.class && field.getGenericType() instanceof ParameterizedType) {
									ParameterizedType setType = (ParameterizedType) field.getGenericType();
									Class<?> setTypeClass = (Class<?>) setType.getActualTypeArguments()[0];
									if (setTypeClass == ProductDto.class) {
										String val = cell.getStringCellValue();
										String[] vals = val.split(",");
										Set<ProductDto> productDtos = new HashSet<>();
										for (String str : vals) {
											ProductDto productDto = new ProductDto();
											productDto.setProductName(str);
											productDtos.add(productDto);
										}
										field.set(dtoInstance, productDtos);
									}
								}
							}
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					cid++;
				}
				dataList.add(dtoInstance);
				rowNumber++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return dataList;
	}

	public static ByteArrayInputStream dataToExcel(List<Vendor> list) {

		Workbook workbook = new SXSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			Sheet sheet = workbook.createSheet(SHEET_NAME);

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
	public static ByteArrayInputStream createExcelHeader(String[] headers) {

		Workbook workbook = new SXSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			Font boldFont = workbook.createFont();
			boldFont.setBold(true);

			CellStyle boldStyle = workbook.createCellStyle();
			boldStyle.setFont(boldFont);


			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(boldStyle);
			}
			Row mandatoryRow=sheet.createRow(1);
			for(int i=0;i<headers.length;i++)
			{
				Cell cell = mandatoryRow.createCell(i);
				cell.setCellValue("Mandatory");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}