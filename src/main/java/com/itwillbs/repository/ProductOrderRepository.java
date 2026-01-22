package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.ProductOrder;
import com.itwillbs.entity.enumtype.EscrowStatus;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
	// 1. 특정 채팅방에서 발생한 모든 주문 내역 조회
    List<ProductOrder> findByChatRoom_RoomId(Long roomId);

    // 2. 특정 채팅방에서 '결제 대기(PENDING)' 중인 주문이 있는지 확인 
    // (이미 요청된 건이 있는데 또 요청하는 것을 방지하기 위함)
    Optional<ProductOrder> findByChatRoom_RoomIdAndEscrowStatus(Long roomId, EscrowStatus status);

    // 3. 구매자의 전체 주문 내역 조회 (최신순)
    List<ProductOrder> findByBuyer_UserIdOrderByCreatedAtDesc(Long userId);

    // 4. 판매자의 전체 판매 내역 조회 (최신순)
    List<ProductOrder> findBySeller_UserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 5. 결제 실행 시 데이터 무결성을 위해 비관적 락(Pessimistic Lock)을 걸고 조회하고 싶을 때 사용
     * (여러 곳에서 동시에 결제 버튼을 누르는 등의 레이스 컨디션 방지)
     */
    @Query("SELECT o FROM ProductOrder o WHERE o.orderId = :orderId")
    Optional<ProductOrder> findByIdWithLock(@Param("orderId") Long orderId);
}
