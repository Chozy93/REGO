package com.itwillbs.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.itwillbs.entity.enumtype.PayTransactionType;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter // Setter 방식으로 데이터 주입
@NoArgsConstructor
@Table(name = "wallet_transactions")
public class WalletTransaction {
	
	// ----------- pk -------------
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;
	
	// 어떤 지갑에서 발생한 일인지 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private PayWallet payWallet;
    
    
    // 거래 금액 (+면 입금, -면 출금 개념이지만, 보통 절대값으로 적고 Type으로 구분함)
    @Column(nullable = false)
    private Long amount;
    
    
    // [중요] 거래 후 잔액 (스냅샷)
    // 나중에 지갑 금액이 꼬였을 때 이 값을 보고 추적합니다.
    @Column(name = "balance_snapshot", nullable = false)
    private Long balanceSnapshot;
    
 // 거래 유형 (충전/결제/환불/송금)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayTransactionType type;
    
 // 거래 내용 (예: "바로 결제", "신한은행 충전")
    @Column(name = "description", length = 100)
    private String description;

    // 거래 일시 (로그니까 생성일만 있으면 됨)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
	
}
