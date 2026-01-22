package com.itwillbs.dto;
import java.sql.Timestamp;

import com.itwillbs.entity.enumtype.PayTransactionType;
import com.itwillbs.entity.enumtype.TradeStatus;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class WalletTransactionDTO {
	private Long walletTransId;   // PK
    private Long walletId;        // FK (지갑 ID)
    private PayTransactionType paymentType;   // 'CHARGE'(충전), 'PAYMENT'(결제) 등
    private Long amount;          // 거래 금액 (+/-)
    private Long balanceSnapshot; // 거래 직후 잔액
    private Long relatedId;       // 연관 ID (계좌 ID 또는 주문 ID)
    private TradeStatus status;        // 'COMPLETED', 'FAILED' 등
    private Timestamp createdAt;  // 생성일시
}
