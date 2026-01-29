package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.dto.AdminOrderSummaryDTO;
import com.itwillbs.dto.OrderListResponseDTO;
import com.itwillbs.entity.ProductOrder;
import com.itwillbs.entity.enumtype.EscrowStatus;

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
    
    
    /**
     * 관리자용 거래 검색 쿼리
     * 필터: 거래상태(escrowStatus)
     * 검색: 상품명, 구매자명, 판매자명, 거래ID
     */
    @Query("SELECT new com.itwillbs.dto.OrderListResponseDTO(" +
    	       "o.orderId, p.productName, p.mainImageUrl, c.name, " +
    	       "b.username, s.username, o.totalAmount, o.createdAt, o.escrowStatus) " + // userName -> username
    	       "FROM ProductOrder o " +
    	       "JOIN o.product p " +
    	       "JOIN p.category c " +
    	       "JOIN o.buyer b " +
    	       "JOIN p.seller s " + 
    	       "WHERE (:status IS NULL OR o.escrowStatus = :status) " +
    	       "AND (:search IS NULL OR p.productName LIKE %:search% " +
    	       "OR b.username LIKE %:search% " + // 여기도 수정
    	       "OR s.username LIKE %:search%)") // 여기도 수정
    	Page<OrderListResponseDTO> searchOrders(
    	        @Param("status") com.itwillbs.entity.enumtype.EscrowStatus status, 
    	        @Param("search") String search, 
    	        Pageable pageable);
    
    
 // 전체 거래 수 조회
    long count();

    // 특정 상태(분쟁 접수 등)인 거래 수 조회 
    // 만약 분쟁 상태가 EscrowStatus에 없다면, 해당되는 상태 Enum으로 변경하세요.
    long countByEscrowStatus(EscrowStatus status);
    
    // 또는 쿼리로 직접 지정 (예: 분쟁 상태가 특정 조건일 때)
    @Query("SELECT COUNT(o) FROM ProductOrder o WHERE o.escrowStatus = :status")
    long countOrdersByStatus(@Param("status") EscrowStatus status);
}
