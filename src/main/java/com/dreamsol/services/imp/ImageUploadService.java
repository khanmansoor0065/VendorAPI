package com.dreamsol.services.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.entities.Vendor;
import com.dreamsol.repositories.VendorRepo;


@Service
public class ImageUploadService 
{
	@Autowired
	private VendorRepo vendorRepo;
	
	public String uploadImage(String path,MultipartFile file,Vendor savedVendor)
	{
		String fileName=file.getOriginalFilename();
		String fileNameExtension=fileName.substring(fileName.lastIndexOf('.'));
		String randomID=UUID.randomUUID().toString();
		String newFileName=randomID+fileNameExtension;
		String newFilePath=path+newFileName;
		File file2=new File(path);
		if(!file2.exists())  
		{
			file2.mkdir();
		}
		try
		{
			Files.copy(file.getInputStream(),Paths.get(newFilePath));
			savedVendor.setProfileImage(newFileName);
			vendorRepo.save(savedVendor);
		}catch(IOException e) {
			e.printStackTrace();
		}
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

	public InputStream getResource(String path, String fileName) throws FileNotFoundException
	{
		String fullPath=path+File.separator+fileName;
		InputStream inputstream=new FileInputStream(fullPath);
		return inputstream;
	}

 
}
