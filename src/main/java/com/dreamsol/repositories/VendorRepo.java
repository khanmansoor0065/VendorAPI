package com.dreamsol.repositories;

import com.dreamsol.entities.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface VendorRepo extends JpaRepository<Vendor,Integer>
{
	Page<Vendor> findByNameLikeOrEmailLike(String name,String email, Pageable p);

	@Query("SELECT DISTINCT v FROM Vendor v JOIN FETCH v.products p JOIN FETCH v.vendorType vt WHERE v.name LIKE %:name% OR v.email LIKE %:email%")
	List<Vendor> findByNameLikeOrEmailLike(String name, String email);
	
	Vendor  findByMob(long mob);
	
	Vendor findByEmail(String email);

	@Query("SELECT DISTINCT v FROM Vendor v JOIN FETCH v.products p JOIN FETCH v.vendorType vt")
	List<Vendor> findAllVendorsWithProductsAndVendorType();

}
