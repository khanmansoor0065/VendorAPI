package com.dreamsol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamsol.entities.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>
{

}
