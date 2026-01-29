package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.dto.AdminOrderSummaryDTO;
import com.itwillbs.entity.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
	// 기본 findById가 제공되지만, 
    // fetch join을 사용하면 쿼리 한 번으로 상품 정보까지 가져와서 성능이 좋아집니다.
    @Query("SELECT o FROM ProductOrder o JOIN FETCH o.product WHERE o.orderId = :orderId")
    Optional<ProductOrder> findByIdWithProduct(@Param("orderId") Long orderId);
    
    /**
     * 상품 ID와 구매자 ID를 조건으로 주문 내역을 조회합니다.
     * 필드 경로: product(객체) -> productId(필드) / buyer(객체) -> userId(필드)
     */
    Optional<ProductOrder> findByProductProductIdAndBuyerUserId(Long productId, Long buyerId);
    
    // admin페이지 대시보드에서 order내역 조회
    @Query("SELECT new com.itwillbs.dto.AdminOrderSummaryDTO(o.orderId, p.productName, p.productId, o.escrowStatus, o.createdAt) " +
            "FROM ProductOrder o JOIN o.product p " +
            "ORDER BY o.createdAt DESC")
     List<AdminOrderSummaryDTO> findRecentOrders(Pageable pageable);
    
}
