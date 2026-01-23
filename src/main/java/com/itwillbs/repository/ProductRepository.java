package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Product;
import com.itwillbs.entity.enumtype.ProductSalesStatus;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByBuyer_UserId(Long userId, Pageable pageable);
    
 // 지역명 필터링 조회 (판매 중인 상품만)
    List<Product> findByRegionDisplayNameContainingAndSalesStatus(
        String regionName, 
        ProductSalesStatus salesStatus
    );
}