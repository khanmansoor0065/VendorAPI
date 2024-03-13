package com.dreamsol.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamsol.entities.Vendor;


public interface VendorRepo extends JpaRepository<Vendor,Integer>
{
//	@Query("select v from Vendor v where v.name like %:key%")
//	List<Vendor> findByName(@Param("key") String name);

	@Query("select v from Vendor v where v.name like %:key%")
	Page<Vendor> findByNameContainingIgnoreCase(@Param("key") String name, Pageable p);
	
	Vendor  findByMob(long mob);
	
	Vendor findByEmail(String email);

}
