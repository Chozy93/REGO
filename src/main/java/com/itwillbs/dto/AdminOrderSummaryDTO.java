package com.itwillbs.dto;

import java.time.LocalDateTime;

import com.itwillbs.entity.enumtype.EscrowStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminOrderSummaryDTO {
    private Long orderId;
    private String productName;   // Product와 조인해서 가져올 값
    private Long productId;
    private EscrowStatus escrowStatus;
    private LocalDateTime createdAt;
}
