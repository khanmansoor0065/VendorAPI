package com.dreamsol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamsol.entities.VendorType;

@Repository
public interface VendorTypeRepo extends JpaRepository<VendorType, Integer>{

}
