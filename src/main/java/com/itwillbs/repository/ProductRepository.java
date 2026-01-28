package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.Product;
import com.itwillbs.entity.enumtype.ProductSalesStatus;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByBuyer_UserId(Long userId, Pageable pageable);
    
 // 지역명 필터링 조회 (판매 중인 상품만)
    List<Product> findByRegionDisplayNameContainingAndSalesStatus(
        String regionName, 
        ProductSalesStatus salesStatus
    );
    
    @Query("SELECT p FROM Product p " +
            "JOIN p.seller s " + 
            "WHERE (p.productName LIKE %:kw% " +
            "OR s.nickname LIKE %:kw% " +
            "OR p.regionDisplayName LIKE %:kw%) " + // 지역명 조건 추가
            "AND p.salesStatus = 'ON_SALE'")
     Page<Product> findByKeyword(@Param("kw") String keyword, Pageable pageable);
}