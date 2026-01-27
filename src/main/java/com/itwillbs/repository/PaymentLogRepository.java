package com.itwillbs.repository;

import com.itwillbs.entity.PgPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLogRepository extends JpaRepository<PgPaymentLog, String> {
    // 필요 시 merchantUid나 orderId로 조회하는 메서드를 추가할 수 있습니다.
    // PgPaymentLog findByOrderId(Long orderId);
}