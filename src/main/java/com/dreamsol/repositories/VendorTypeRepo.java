package com.dreamsol.repositories;

import com.dreamsol.entities.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamsol.entities.VendorType;

import java.util.List;

@Repository
public interface VendorTypeRepo extends JpaRepository<VendorType, Integer>
{
    Page<VendorType> findByTypeNameContainingIgnoreCase(String typeName, Pageable pageable);

    VendorType findByTypeName(String typeName);

}
