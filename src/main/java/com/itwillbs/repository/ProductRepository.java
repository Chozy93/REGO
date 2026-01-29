package com.itwillbs.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.dto.MonthlyCountDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.enumtype.ProductSalesStatus;

public interface ProductRepository extends JpaRepository<Product, Long> {



    long countByCreatedAtBefore(LocalDateTime time);

    List<Product> findTop3ByOrderByCreatedAtDesc();

    Page<Product> findByBuyer_UserId(Long userId, Pageable pageable);
    
 // 지역명 필터링 조회 (판매 중인 상품만)
    List<Product> findByRegionDisplayNameContainingAndSalesStatus(
        String regionName, 
        ProductSalesStatus salesStatus
    );
    

    // 검색기능
    @Query("SELECT p FROM Product p " +
            "JOIN p.seller s " + 
            "WHERE (p.productName LIKE %:kw% " +
            "OR s.nickname LIKE %:kw% " +
            "OR p.regionDisplayName LIKE %:kw%) " + // 지역명 조건 추가
            "AND p.salesStatus = 'ON_SALE'")
     Page<Product> findByKeyword(@Param("kw") String keyword, Pageable pageable);
    
    /**
     * 상품 등록 추이: 월별 등록된 상품 수 집계 (올해 데이터 기준)
     */
    @Query("SELECT new com.itwillbs.dto.MonthlyCountDTO(MONTH(p.createdAt), COUNT(p)) " +
           "FROM Product p " +
           "WHERE YEAR(p.createdAt) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(p.createdAt) " +
           "ORDER BY MONTH(p.createdAt) ASC")
    List<MonthlyCountDTO> getMonthlyProductCounts();
    
    
    /**
     * 거래 완료 추이: 월별 판매 완료된(SOLD_OUT) 상품 수 집계 (올해 데이터 기준)
     * 거래 완료 시점은 updatedAt을 기준으로 합니다.
     */
 // 거래 완료 추이: Enum을 파라미터로 받음
    @Query("SELECT new com.itwillbs.dto.MonthlyCountDTO(MONTH(p.updatedAt), COUNT(p)) " +
           "FROM Product p " +
           "WHERE p.salesStatus = :status " +  // 파라미터 바인딩 사용
           "AND YEAR(p.updatedAt) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(p.updatedAt) " +
           "ORDER BY MONTH(p.updatedAt) ASC")
    List<MonthlyCountDTO> getMonthlyOrderCounts(@Param("status") ProductSalesStatus status);
    


}


