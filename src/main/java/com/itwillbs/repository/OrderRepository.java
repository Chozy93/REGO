package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
	// 기본 findById가 제공되지만, 
    // fetch join을 사용하면 쿼리 한 번으로 상품 정보까지 가져와서 성능이 좋아집니다.
    @Query("SELECT o FROM ProductOrder o JOIN FETCH o.product WHERE o.orderId = :orderId")
    Optional<ProductOrder> findByIdWithProduct(@Param("orderId") Long orderId);
}
