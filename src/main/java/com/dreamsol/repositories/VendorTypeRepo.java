package com.dreamsol.repositories;

import com.dreamsol.entities.VendorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorTypeRepo extends JpaRepository<VendorType, Integer>
{
    Page<VendorType> findByTypeNameContainingIgnoreCase(String typeName, Pageable pageable);

    VendorType findByTypeName(String typeName);

}
