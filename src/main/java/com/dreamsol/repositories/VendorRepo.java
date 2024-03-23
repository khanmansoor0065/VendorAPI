package com.dreamsol.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamsol.entities.Vendor;

import java.util.List;


public interface VendorRepo extends JpaRepository<Vendor,Integer>
{
	Page<Vendor> findByNameLikeOrEmailLike(String name,String email, Pageable p);

	List<Vendor> findByNameLikeOrEmailLike(String name,String email);

	//List<Vendor> findByMob(long mobile);
	
	Vendor  findByMob(long mob);
	
	Vendor findByEmail(String email);

}
