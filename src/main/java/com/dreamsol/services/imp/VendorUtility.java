package com.dreamsol.services.imp;

import com.dreamsol.dto.*;
import com.dreamsol.entities.Product;
import com.dreamsol.entities.Vendor;
import com.dreamsol.entities.VendorType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class VendorUtility {

    public Vendor dtoToVendor(VendorDto vendorDto) {
        Vendor vendor = new Vendor();
        BeanUtils.copyProperties(vendorDto,vendor);
        vendor.setVendorType(dtoToVendorType(vendorDto.getVendorTypeDto()));
        vendor.setProducts(dtoToProduct(vendorDto.getProductDto(), vendor));
        return vendor;
    }

    public VendorResponseDto vendorToDto(Vendor vendor) {
        VendorResponseDto vendorDto = new VendorResponseDto();
        BeanUtils.copyProperties(vendor, vendorDto);
        vendorDto.setVendorTypeDto(vendorTypeToDto(vendor.getVendorType()));
        vendorDto.setProductResponseDto(productsToDto(vendor.getProducts()));
        return vendorDto;
    }

    public VendorType dtoToVendorType(VendorTypeDto vendorTypeDto) {
        VendorType vendorType = new VendorType();
        BeanUtils.copyProperties( vendorTypeDto,vendorType);
        return vendorType;
    }

    public VendorTypeDto vendorTypeToDto(VendorType vendorType) {
        VendorTypeDto vendorTypeDto = new VendorTypeDto();
        BeanUtils.copyProperties( vendorType,vendorTypeDto);
        return vendorTypeDto;
    }

    public Set<Product> dtoToProduct(Set<ProductDto> productDtos, Vendor vendor) {
        Set<Product> products = new HashSet<>();
        for (ProductDto productDto : productDtos) {
            Product prod = new Product();
            BeanUtils.copyProperties( productDto,prod);
            prod.setVendor(vendor);
            products.add(prod);
        }
        return products;
    }

    public Set<ProductResponseDto> productsToDto(Set<Product> products) {
        Set<ProductResponseDto> productDto = new HashSet<>();
        for (Product product : products) {
            ProductResponseDto prodDto = new ProductResponseDto();
            BeanUtils.copyProperties( product,prodDto);
            productDto.add(prodDto);
        }
        return productDto;
    }
    public List<Vendor> dtoToVendorList(List<VendorDto> vendorDtoList) {
        List<Vendor> vendorList = new ArrayList<>();
        for (VendorDto vendorDto : vendorDtoList) {
            vendorList.add(dtoToVendor(vendorDto));
        }
        return vendorList;
    }
    public List<VendorType> dtoToVendorTypeList(List<VendorTypeDto> vendorTypeDtoList) {
        List<VendorType> vendorTypeList = new ArrayList<>();
        for (VendorTypeDto vendorTypeDto : vendorTypeDtoList) {
            vendorTypeList.add(dtoToVendorType(vendorTypeDto));
        }
        return vendorTypeList;
    }
    public List<VendorResponseDto> vendorListToDtoList(List<Vendor> vendorList) {
        List<VendorResponseDto> vendorResponseList = new ArrayList<>();
        for (Vendor vendor : vendorList) {
            vendorResponseList.add(vendorToDto(vendor));
        }
        return vendorResponseList;
    }
    public ProductDto productToDto(Product product)
    {
        ProductDto productDto=new ProductDto();
        BeanUtils.copyProperties(product,productDto);
        return  productDto;
    }
    public InvalidData toInvalid(VendorDto vendor)
    {
        InvalidData invalidData = new InvalidData();
        BeanUtils.copyProperties(vendor, invalidData);
        invalidData.setVendorTypeDto(vendor.getVendorTypeDto());
        invalidData.setProductDto(vendor.getProductDto());
        invalidData.setMessages(ExcelService.validateEntityMsg(vendor,vendor.getClass()));
        return invalidData;
    }


    public InvalidVendorTypeData toInvalidVendorType(VendorTypeDto vendorTypeDto) {
        InvalidVendorTypeData invalid = new InvalidVendorTypeData();
        invalid.setTypeName(vendorTypeDto.getTypeName());
        invalid.setMessages(ExcelService.validateEntityMsg(vendorTypeDto, VendorTypeDto.class));
        return invalid;
    }
}
