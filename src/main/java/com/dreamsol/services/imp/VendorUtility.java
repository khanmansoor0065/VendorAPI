package com.dreamsol.services.imp;

import com.dreamsol.dto.*;
import com.dreamsol.entities.Product;
import com.dreamsol.entities.Vendor;
import com.dreamsol.entities.VendorType;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class VendorUtility
{
    ModelMapper modelMapper=new ModelMapper();


    public Vendor dtoToVendor(VendorDto vendorDto) {
        Vendor vendor = this.modelMapper.map(vendorDto, Vendor.class);
        vendor.setVendorType(this.dtoToVendorType(vendorDto.getVendorTypeDto()));
        vendor.setProducts(this.dtoToProduct(vendorDto.getProductDto(),vendor));
        return vendor;
    }

    public VendorResponseDto vendorToDto(Vendor vendor) {
        VendorResponseDto vendorDto = this.modelMapper.map(vendor, VendorResponseDto.class);
        vendorDto.setVendorTypeDto(this.VendorTypeToDto(vendor.getVendorType()));
        vendorDto.setProductResponseDto(this.productsToDto(vendor.getProducts()));
        return vendorDto;
    }

    public VendorType dtoToVendorType(VendorTypeDto vendorTypeDto) {
        VendorType vendorType = this.modelMapper.map(vendorTypeDto, VendorType.class);
        return vendorType;
    }

    public VendorTypeDto VendorTypeToDto(VendorType vendorType) {
        VendorTypeDto vendorTypeDto = this.modelMapper.map(vendorType, VendorTypeDto.class);
        return vendorTypeDto;
    }

    public Set<Product> dtoToProduct(Set<ProductDto> productDtos, Vendor vendor)
    {
        Set<Product> products=productDtos.stream().map((productDto)->{
            Product prod=this.modelMapper.map(productDto, Product.class);
            prod.setVendor(vendor);
            return prod;
        }).collect(Collectors.toSet());
        return products;
    }

    public Set<ProductResponseDto> productsToDto(Set<Product> products)
    {
        Set<ProductResponseDto> productDto=products.stream().map((product)->{
            ProductResponseDto prodDto=this.modelMapper.map(product, ProductResponseDto.class);
            return prodDto;
        }).collect(Collectors.toSet());
        return productDto;
    }
}
