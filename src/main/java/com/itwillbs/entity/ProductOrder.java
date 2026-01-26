package com.itwillbs.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.itwillbs.entity.enumtype.DealType;
import com.itwillbs.entity.enumtype.EscrowStatus;
import com.itwillbs.entity.enumtype.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자
@Table(name = "product_orders")
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "escrow_status", nullable = false)
    private EscrowStatus escrowStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type", nullable = false)
    private DealType dealType;

    @Column(name = "carrier_code", length = 100)
    private String carrierCode;

    @Column(name = "tracking_number", length = 255)
    private String trackingNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- 정적 팩토리 메서드 (Builder 대신 사용) ---
    /**
     * Re:pay 결제 요청 시 사용하는 생성 메서드
     */
    public static ProductOrder createRepayOrder(Product product, User buyer, User seller, 
                                               ChatRoom chatRoom, BigDecimal amount, DealType dealType) {
        ProductOrder order = new ProductOrder();
        order.product = product;
        order.buyer = buyer;
        order.seller = seller;
        order.chatRoom = chatRoom;
        order.paymentType = PaymentType.REPAY;
        order.totalAmount = amount;
        order.escrowStatus = EscrowStatus.PENDING; // 초기값 설정
        order.dealType = dealType;
        return order;
    }

    // --- 비즈니스 로직 (상태 변경) ---
    
    /**
     * 결제 완료 처리
     */
    public void markAsPaid() {
        if (this.escrowStatus != EscrowStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태인 주문만 결제가 가능합니다.");
        }
        this.escrowStatus = EscrowStatus.HELD;
    }

    /**
     * 운송장 번호 입력 (배송 시작)
     */
    public void updateDeliveryInfo(String carrierCode, String trackingNumber) {
        this.carrierCode = carrierCode;
        this.trackingNumber = trackingNumber;
    }
}