package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
