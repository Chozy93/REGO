package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
