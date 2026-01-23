package com.itwillbs.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pg_payment_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PgPaymentLog {

    @Id
    @Column(name = "imp_uid", length = 100)
    private String impUid; // 아임포트 결제 고유 번호 (PK)

    @Column(name = "merchant_uid", nullable = false, length = 100)
    private String merchantUid; // 가맹점 주문 번호

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 실제 주문 번호 (FK 대상)

    @Column(name = "user_id", nullable = false)
    private Long userId; // 결제자 ID

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // 결제 금액

    @Column(name = "pg_provider", nullable = false, length = 50)
    private String pgProvider; // PG사 정보

    @Column(name = "pay_method", nullable = false, length = 50)
    private String payMethod; // 결제 수단

    @Column(nullable = false, length = 20)
    private String status; // 결제 상태 (paid, cancelled 등)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}