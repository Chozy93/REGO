package com.itwillbs.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentRequestDto {
	private Long productId;
    private Long sellerId;
    private Long buyerId;
    private Long roomId;
    private BigDecimal amount;

}
