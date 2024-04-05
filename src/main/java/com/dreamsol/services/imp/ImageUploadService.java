package com.dreamsol.services.imp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.entities.Vendor;
import com.dreamsol.repositories.VendorRepo;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Service
public class ImageUploadService 
{
	@Autowired
	private VendorRepo vendorRepo;

	public String uploadImage(String path, MultipartFile file, Vendor savedVendor) {
		String fileName = file.getOriginalFilename();
		String fileNameExtension = fileName.substring(fileName.lastIndexOf('.'));
		String[] arrayed = fileName.split("\\."); // Corrected
		fileName = arrayed[0];
		//String randomID = UUID.randomUUID().toString();
		String newFileName = fileName + fileNameExtension;
		String newFilePath = path + newFileName;
		File file2 = new File(path);
		if (!file2.exists()) {
			file2.mkdir();
		}
		try {
			Files.copy(file.getInputStream(), Paths.get(newFilePath));
			String link = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/vendor/download/")
					.path(newFileName).toUriString();
			savedVendor.setFile(link);
			vendorRepo.save(savedVendor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return the new file name
		return newFileName;
	}

	public boolean deleteImage(String path, String fileName) 
	{
		Path imagePath=Paths.get(path, fileName);
		try {
			Files.delete(imagePath);
			return true;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public byte[] getFileBytes(String path, String fileName) throws IOException, FileNotFoundException {
		String filePath = path + "/" + fileName;
		try (FileInputStream fileInputStream = new FileInputStream(filePath);
			 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
			}
			return byteArrayOutputStream.toByteArray();
		}
	}

 
}
